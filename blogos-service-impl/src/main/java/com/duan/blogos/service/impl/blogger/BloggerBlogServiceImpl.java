package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.config.preference.FileProperties;
import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.blog.BlogCategoryDao;
import com.duan.blogos.service.dao.blog.BlogStatisticsDao;
import com.duan.blogos.service.dao.blogger.BloggerPictureDao;
import com.duan.blogos.service.dto.blog.BlogDTO;
import com.duan.blogos.service.dto.blog.BlogTitleIdDTO;
import com.duan.blogos.service.entity.blog.Blog;
import com.duan.blogos.service.entity.blog.BlogStatistics;
import com.duan.blogos.service.enums.BlogFormatEnum;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.manager.DataFillingManager;
import com.duan.blogos.service.manager.ImageManager;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerBlogService;
import com.duan.blogos.service.service.blogger.BloggerCategoryService;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.FileUtils;
import com.duan.common.util.MultipartFile;
import com.duan.common.util.StringUtils;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static com.duan.blogos.service.enums.BlogFormatEnum.MD;
import static com.duan.blogos.service.enums.BlogStatusEnum.PUBLIC;

/**
 * Created on 2017/12/19.
 * 博主检索博文
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerBlogServiceImpl implements BloggerBlogService {

    @Autowired
    private BlogStatisticsDao statisticsDao;

    @Autowired
    private WebsiteProperties websiteProperties;

    @Autowired
    private FileProperties fileProperties;

    @Autowired
    private DataFillingManager dataFillingManager;

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private BlogCategoryDao categoryDao;

    @Autowired
    private BloggerPictureDao pictureDao;

    @Autowired
    private BloggerCategoryService categoryService;

    @Override
    public Long insertBlog(Long bloggerId, Long[] categories, Long[] labels,
                           BlogStatusEnum status, String title, String content, String contentMd,
                           String summary, String[] keyWords, boolean analysisImg) {

        // 1 插入数据到bolg表
        String ch = dbProperties.getStringFiledSplitCharacterForNumber();
        String chs = dbProperties.getStringFiledSplitCharacterForString();
        Blog blog = new Blog();
        blog.setBloggerId(bloggerId);
        blog.setCategoryIds(StringUtils.longArrayToString(categories, ch));
        blog.setLabelIds(StringUtils.longArrayToString(labels, ch));
        blog.setState(status.getCode());
        blog.setTitle(title);
        blog.setContent(content);
        blog.setContentMd(contentMd);
        blog.setSummary(summary);
        blog.setKeyWords(StringUtils.arrayToString(keyWords, chs));
        blog.setWordCount(content.length());

        int effect = blogDao.insert(blog);
        if (effect <= 0) return null;

        Long blogId = blog.getId();

        // 2 插入数据到blog_statistics表（生成博文信息记录）
        BlogStatistics statistics = new BlogStatistics();
        statistics.setBlogId(blogId);
        effect = statisticsDao.insert(statistics);
        if (effect <= 0)
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, new SQLException());

        if (analysisImg) {
            // 3 解析本地图片引用并使自增
            Long[] imids = parseContentForImageIds(content, bloggerId);
            // UPDATE: 2018/1/19 更新 自增并没有实际作用
            if (!CollectionUtils.isEmpty(imids)) {
                // 修改图片可见性，引用次数
                Arrays.stream(imids).forEach(id -> imageManager.imageInsertHandle(bloggerId, id));
            }
        }

        // 4 lucene创建索引
        try {
            luceneIndexManager.add(blog);
        } catch (IOException e) {
            e.printStackTrace();
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }

        return blogId;
    }

    // 解析博文中引用的相册图片
    private Long[] parseContentForImageIds(String content, Long bloggerId) {
        //http://localhost:8080/image/1/type=public/523?default=5
        //http://localhost:8080/image/1/type=private/1
        String regex = "http://" + websiteProperties.getAddr() + "/image/" + bloggerId + "/.*?/(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        List<String> res = new ArrayList<>();
        while (matcher.find()) {
            String str = matcher.group();
            int index = str.lastIndexOf("/");
            res.add(str.substring(index + 1));
        }

        long[] arr = res.stream()
                .mapToLong(Integer::valueOf)
                .distinct()
                .toArray();

        Long[] ls = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ls[i] = arr[i];
        }

        return ls;
    }

    @Override
    public boolean updateBlog(Long bloggerId, Long blogId, Long[] newCategories, Long[] newLabels, BlogStatusEnum newStatus,
                              String newTitle, String newContent, String newContentMd, String newSummary, String[] newKeyWords) {

        // 1 更新博文中引用的本地图片（取消引用的useCount--，新增的useCount++）
        Blog oldBlog = blogDao.getBlogById(blogId);
       /* if (newContent != null) {
            if (!oldBlog.getContent().equals(newContent)) {

                final Long[] oldIids = parseContentForImageIds(oldBlog.getContent(), bloggerId); // 1 2 3 4
                final Long[] newIids = parseContentForImageIds(newContent, bloggerId); // 1 3 4 6

                // 求交集 1 3 4
                int[] array = IntStream.of(oldIids).filter(value -> {
                    for (Long id : newIids) if (id == value) return true;
                    return false;
                }).toArray();

                // -- 2
                int[] allM = new int[oldIids.length + array.length];
                System.arraycopy(oldIids, 0, allM, 0, oldIids.length);
                System.arraycopy(array, 0, allM, oldIids.length, array.length);
                IntStream.of(allM).distinct().forEach(pictureDao::updateUseCountMinus);

                // ++ 6
                int[] allP = new int[newIids.length + array.length];
                System.arraycopy(newIids, 0, allP, 0, newIids.length);
                System.arraycopy(array, 0, allP, newIids.length, array.length);
                IntStream.of(allP).distinct().forEach(id -> {
                    pictureDao.updateUseCountPlus(id);

                    // 将用到的图片修改为public（有必要的话）
                    try {
                        imageManager.moveImageAndUpdateDbIfNecessary(bloggerId, id, BloggerPictureCategoryEnum.PUBLIC);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            }
        }*/

        // 2 更新博文
        String ch = dbProperties.getStringFiledSplitCharacterForNumber();
        String chs = dbProperties.getStringFiledSplitCharacterForString();
        Blog blog = new Blog();
        blog.setId(blogId);
        if (newCategories != null) blog.setCategoryIds(StringUtils.longArrayToString(newCategories, ch));
        if (newLabels != null) blog.setLabelIds(StringUtils.longArrayToString(newLabels, ch));
        // 博文未通过审核时不能修改状态
        if (newStatus != null && !oldBlog.getState().equals(BlogStatusEnum.VERIFY.getCode()))
            blog.setState(newStatus.getCode());
        if (newTitle != null) blog.setTitle(newTitle);
        if (newContent != null) blog.setContent(newContent);
        if (newSummary != null) blog.setSummary(newSummary);
        if (newContentMd != null) blog.setContentMd(newContentMd);
        if (newKeyWords != null) blog.setKeyWords(StringUtils.arrayToString(newKeyWords, chs));
        int effect = blogDao.update(blog);
        if (effect <= 0)
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, new SQLException());

        // 3 更新lucene
        try {
            luceneIndexManager.update(blog);
        } catch (IOException e) {
            e.printStackTrace();
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }

        return true;
    }

    @Override
    public boolean deleteBlog(Long bloggerId, Long blogId) {

        Blog blog = blogDao.getBlogById(blogId);
        if (blog == null) return false;

        // 1 删除博文记录
        int effect = blogDao.delete(blogId);
        if (effect <= 0) return false;

        // 2 删除统计信息
        int effectS = statisticsDao.deleteByUnique(blogId);
        // MAYBUG 断点调试时effectS始终为0，但最终事务提交时记录却会正确删除，？？？ 因而注释下面的判断
        //if (effectS <= 0) throw new UnknownException(blog");

        // 3 图片引用useCount--
        Long[] ids = parseContentForImageIds(blog.getContent(), bloggerId);
        if (!CollectionUtils.isEmpty(ids))
            Arrays.stream(ids).forEach(pictureDao::updateUseCountMinus);

        // 4 删除lucene索引
        luceneIndexManager.delete(blogId);

        return true;
    }

    @Override
    public boolean deleteBlogPatch(Long bloggerId, Long[] blogIds) {

        for (Long id : blogIds) {
            if (!deleteBlog(bloggerId, id))
                throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, new SQLException());
        }

        return true;
    }

    @Override
    public boolean getBlogForCheckExist(Long blogId) {
        return !(blogDao.getBlogIdById(blogId) == null);
    }

    @Override
    public ResultModel<BlogDTO> getBlog(Long bloggerId, Long blogId) {

        Blog blog = blogDao.getBlogById(blogId);

        String ch = dbProperties.getStringFiledSplitCharacterForNumber();
        String chs = dbProperties.getStringFiledSplitCharacterForString();
        String whs = websiteProperties.getUrlConditionSplitCharacter();

        if (blog != null && blog.getBloggerId().equals(bloggerId)) {

            String cids = blog.getCategoryIds();
            String lids = blog.getLabelIds();
            String keyWords = blog.getKeyWords();

            if (!StringUtils.isEmpty(cids))
                blog.setCategoryIds(cids.replace(ch, whs));

            if (!StringUtils.isEmpty(lids))
                blog.setLabelIds(lids.replace(ch, whs));

            if (!StringUtils.isBlank(keyWords))
                blog.setKeyWords(keyWords.replace(chs, whs));

            return new ResultModel<>(dataFillingManager.blog2DTO(blog));

        }

        throw ResultUtil.failException(CodeMessage.BLOG_UNKNOWN_BLOG, new SQLException());
    }

    @Override
    public Long getBlogId(Long bloggerId, String blogName) {
        return blogDao.getBlogIdByUniqueKey(bloggerId, blogName);
    }

    @Override
    public List<BlogTitleIdDTO> insertBlogPatch(MultipartFile file, Long bloggerId) {

        FileUtils.mkdirsIfNotExist(fileProperties.getPatchImportBlogTempPath());

        // 保存到临时文件
        String fullPath = fileProperties.getPatchImportBlogTempPath() +
                File.separator +
                "temp-" +
                bloggerId +
                "-" +
                System.currentTimeMillis() +
                "-"
//                file.getOriginalFilename()
                ;
        // TODO

//        FileUtils.saveFileTo(file, fullPath);

        // 解析博文
        List<BlogTitleIdDTO> result = new ArrayList<>();
        final Parser parser = Parser.builder().build();
        final HtmlRenderer renderer = HtmlRenderer.builder().build();

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(fullPath, Charset.forName("GBK"));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            Long cateId = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                /*
                entry.getName() = 随笔/
                entry.getName() = 随笔/无标题文章.md
                entry.getName() = 项目/
                entry.getName() = 项目/-我的音乐（Musicoco）--本地音乐播放器开发总结.md
                entry.getName() = 项目/BLOG---个人博文系统开发总结-一：概览.md
                entry.getName() = 项目/BLOG---个人博文系统开发总结-三：批量博文导入功能.md
                entry.getName() = 项目/BLOG---个人博文系统开发总结-二：使用Lucene完成博文检索功能.md
                entry.getName() = 项目/Musicoco-用户指南.md
                entry.getName() = java-集合-4---HashSet.md
                entry.getName() = java-集合-5---LinkedHashMap.md
                entry.getName() = 算法/
                entry.getName() = 算法/n-阶贝塞尔曲线计算公式实现.md
                entry.getName() = 算法/时间复杂度及其计算.md
                entry.getName() = 算法/网易2017春招笔试真题编程题集合——2-优雅的点.md
                entry.getName() = 算法/网易2017春招笔试真题编程题集合——4-消除重复元素.md
                entry.getName() = 算法/网易2017春招笔试真题编程题集合——9-涂棋盘.md
                */

                if (entry.isDirectory()) {

                    if (name.indexOf("/") != name.length() - 1) {
                        continue; // 只取第一级目录作为类别
                    }

                    String dirName = name.substring(0, name.length() - 1);

                    Long id = categoryDao.getCategoryIdByTitle(bloggerId, dirName);
                    if (id != null) {
                        cateId = id;
                    } else {
                        cateId = categoryService.insertBlogCategory(bloggerId, null, dirName, "");
                    }

                    continue;
                }

                // 跟目录下
                if (!name.contains("/")) {
                    cateId = null;
                }

                BufferedInputStream stream = new BufferedInputStream(zipFile.getInputStream(entry));
                InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));

                BlogTitleIdDTO node = analysisAndInsertMdFile(parser, renderer, entry, reader, bloggerId, cateId);

                if (node != null) {
                    result.add(node);
                }

            }

        } catch (IOException e) {
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        } finally {
            if (zipFile != null) try {
                zipFile.close();

                // 删除临时文件
                FileUtils.deleteFileIfExist(fullPath);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return result;
    }

    @Override
    public String getAllBlogForDownload(Long bloggerId, BlogFormatEnum format) {
        List<Blog> blogs = blogDao.listAllByFormat(bloggerId, format.getCode());
        if (CollectionUtils.isEmpty(blogs)) return null;

        FileUtils.mkdirsIfNotExist(fileProperties.getPatchDownloadBlogTempPath());

        String zipFilePath = fileProperties.getPatchDownloadBlogTempPath() +
                File.separator +
                System.currentTimeMillis() +
                "-" +
                "total-of-" +
                blogs.size() +
                "-blogs.zip";

        File zipFile = new File(zipFilePath);
        List<String> tempBlogFile = new ArrayList<>();

        // 压缩博文
        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            for (Blog blog : blogs) {
                String path = addBlogToZip(blog, zipOut, format);
                if (path != null) {
                    tempBlogFile.add(path);
                }
            }
        } catch (IOException e) {
            throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        } finally {
            if (zipOut != null) {
                try {
                    zipOut.close();
                } catch (IOException e) {
                    throw ResultUtil.failException(CodeMessage.COMMON_UNKNOWN_ERROR, e);
                }
            }
        }

        // 统一删除临时博文文件
        tempBlogFile.forEach(FileUtils::deleteFileIfExist);

        return zipFile.getAbsolutePath();
    }

    private String addBlogToZip(Blog blog, ZipOutputStream zipOut, BlogFormatEnum format) throws IOException {

        // 新建博文文件
        String title = blog.getTitle();
        String content = format == MD ? blog.getContentMd() : blog.getContent();

        String bp = fileProperties.getPatchDownloadBlogTempPath() +
                File.separator +
                title +
                (format == MD ? ".md" : ".html");

        FileOutputStream fo = new FileOutputStream(bp);
        OutputStreamWriter writer = new OutputStreamWriter(fo, "UTF-8");
        writer.write(content);
        writer.close();

        // 添加到 zip 压缩文件
        File entryFile = new File(bp);
        zipOut.putNextEntry(new ZipEntry(entryFile.getName()));

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(entryFile));
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bis.read(buffer)) > 0) {
            zipOut.write(buffer, 0, len);
        }

        bis.close();
        zipOut.closeEntry();

        return bp;
    }

    // 解析 md 文件读取字符流，新增记录到数据库
    // cateId 仅 md 文件在根目录下是才为 -1
    private BlogTitleIdDTO analysisAndInsertMdFile(Parser parser, HtmlRenderer renderer, ZipEntry entry,
                                                   InputStreamReader reader, Long bloggerId, Long cateId) throws IOException {

        String name = entry.getName();

        if (!name.endsWith(".md")) return null;

        StringBuilder b = new StringBuilder((int) entry.getSize());
        int len = 0;
        char[] buff = new char[1024];
        while ((len = reader.read(buff)) > 0) {
            b.append(buff, 0, len);
        }

        // reader.close();
        // zip 文件关闭由 370行：zipFile.close() 统一关闭

        // 内容
        String mdContent = b.toString();

        // 对应的 html 内容
        Document document = parser.parse(mdContent);
        String htmlContent = renderer.render(document);

        // 摘要
        String firReg = htmlContent.replaceAll("<.*?>", ""); // 避免 subString 使有遗留的 html 标签，前端显示时会出错
        String tmpStr = firReg.length() > 500 ? firReg.substring(0, 500) : firReg;
        String aftReg = tmpStr.replaceAll("\\n", "");
        String summary = aftReg.length() > 200 ? aftReg.substring(0, 200) : aftReg;

        // UPDATE: 2018/4/4 更新 图片引用

        // 文件名作为标题
        String title = cateId == -1 ? name.replace(".md", "") :
                name.substring(name.lastIndexOf("/") + 1).replace(".md", "");

        Long id = insertBlog(bloggerId,
                new Long[]{cateId},
                null,
                PUBLIC,
                title,
                htmlContent,
                mdContent,
                summary,
                null,
                false);
        if (id == null) return null;

        BlogTitleIdDTO node = new BlogTitleIdDTO();
        node.setTitle(title);
        node.setId(id);

        return node;
    }

}
