package com.duan.blogos.api.blog;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.api.BaseController;
import com.duan.blogos.service.BlogFilterService;
import com.duan.blogos.service.blogger.BloggerBlogService;
import com.duan.blogos.service.common.dto.blog.BlogDTO;
import com.duan.blogos.service.common.dto.blog.BlogListItemDTO;
import com.duan.blogos.service.common.dto.blog.BlogTitleIdDTO;
import com.duan.blogos.service.common.enums.*;
import com.duan.blogos.service.common.restful.PageResult;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.common.vo.FileVO;
import com.duan.blogos.util.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.duan.blogos.service.common.enums.Rule.VIEW_COUNT;

/**
 * Created on 2018/1/15.
 * 博主博文api
 * <p>
 * 1 新增博文
 * 2 获取博文
 * 3 获取指定博文
 * 4 修改博文
 * 5 删除博文
 * 6 批量删除博文
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blog")
public class BlogController extends BaseController {

    @Reference
    private BloggerBlogService bloggerBlogService;

    @Reference
    private BlogFilterService blogFilterService;

    /**
     * 新增博文
     */
    @PostMapping
    public ResultModel add(
            @Uid Long bloggerId,
            @RequestParam(value = "cids", required = false) String categoryIds,
            @RequestParam(value = "lids", required = false) String labelIds,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String contentMd,
            @RequestParam String summary,
            @RequestParam(required = false) String keyWords) {

        // 将 Unicode 解码
        content = StringUtils.unicodeToString(content);
        contentMd = StringUtils.unicodeToString(contentMd);

        handleBlogContentCheck(title, content, contentMd, summary, keyWords);

        String sp = ",";
        Long[] cids = StringUtils.longStringDistinctToArray(categoryIds, sp);
        Long[] lids = StringUtils.longStringDistinctToArray(labelIds, sp);

        //检查博文类别和标签
        handleCategoryAndLabelCheck(bloggerId, cids, lids);

        String[] kw = StringUtils.stringArrayToArray(keyWords, sp);
        // UPDATE: 2018/1/16 更新 博文审核 图片引用
        Long id = bloggerBlogService.insertBlog(bloggerId, cids, lids, BlogStatusEnum.PUBLIC, title, content, contentMd,
                summary, kw, false);
        if (id == null)
            return handlerOperateFail();

        return ResultModel.success(id);
    }

    /**
     * 检索博文
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<PageResult<BlogListItemDTO>> list(
            @RequestParam(required = false) Long bloggerId,
            @RequestParam(required = false, value = "cids") String categoryIds,
            @RequestParam(required = false, value = "lids") String labelIds,
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) Integer status) {

        //检查排序规则
        String sor = sort == null ? VIEW_COUNT.name() : sort.toUpperCase();
        String ord = order == null ? Order.DESC.name() : order.toUpperCase();
        handleSortRuleCheck(sor, ord);

        BlogStatusEnum stat = null;
        if (status != null) stat = BlogStatusEnum.valueOf(status);
        if (stat == null) stat = BlogStatusEnum.PUBLIC; // status 传参错误

        //执行数据查询
        BlogSortRule rule = new BlogSortRule(Rule.valueOf(sor), Order.valueOf(ord));

        String sp = ",";
        Long[] cids = StringUtils.longStringDistinctToArray(categoryIds, sp);
        Long[] lids = StringUtils.longStringDistinctToArray(labelIds, sp);
        //检查博文类别和标签
        handleCategoryAndLabelCheck(bloggerId, cids, lids);

        return blogFilterService.listFilterAll(Util.isArrayEmpty(cids) ? null : Arrays.asList(cids),
                Util.isArrayEmpty(lids) ? null : Arrays.asList(lids), keyWord, bloggerId,
                pageNum, pageSize, rule, stat);

    }

    /**
     * 获取指定博文
     */
    @GetMapping("/{blogId}")
    @TokenNotRequired
    public ResultModel<BlogDTO> get(@PathVariable Long blogId) {

        ResultModel<BlogDTO> blog = bloggerBlogService.getBlog(blogId);
        if (blog == null)
            return handlerEmptyResult();

        // 编码为 Unicode
        BlogDTO bg = blog.getData();
        bg.setContent(StringUtils.stringToUnicode(bg.getContent()));
        bg.setContentMd(StringUtils.stringToUnicode(bg.getContentMd()));

        return blog;
    }

    /**
     * 更新博文
     */
    @PutMapping("/{blogId}")
    public ResultModel update(
            @Uid Long bloggerId,
            @PathVariable Long blogId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String contentMd,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String categoryIds,
            @RequestParam(required = false) String labelIds,
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) Integer status) {

        // 所有参数都为null，则不更新。
        if (Stream.of(title, content, summary, categoryIds, labelIds, keyWord, status)
                .filter(Objects::nonNull).count() <= 0)
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        // 检查修改到的博文状态是否允许
        if (status != null && !blogValidateService.isBlogStatusAllow(status))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        handleBlogExistAndCreatorCheck(bloggerId, blogId);

        // 将 Unicode 解码
        content = StringUtils.unicodeToString(content);
        contentMd = StringUtils.unicodeToString(contentMd);

        handleBlogContentCheck(title, content, contentMd, summary, keyWord);

        String sp = ",";
        Long[] cids = categoryIds == null ? null : StringUtils.longStringDistinctToArray(categoryIds, sp);
        Long[] lids = labelIds == null ? null : StringUtils.longStringDistinctToArray(labelIds, sp);

        //检查博文类别和标签
        handleCategoryAndLabelCheck(bloggerId, cids, lids);

        String[] kw = keyWord == null ? null : StringUtils.stringArrayToArray(keyWord, sp);
        BlogStatusEnum stat = status == null ? null : BlogStatusEnum.valueOf(status);

        //执行更新
        if (!bloggerBlogService.updateBlog(bloggerId, blogId, cids, lids, stat, title, content, contentMd, summary, kw))
            return handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 删除博文
     */
    @DeleteMapping("/{blogId}")
    public ResultModel delete(
            @Uid Long bloggerId,
            @PathVariable Long blogId) {

        handleBlogExistAndCreatorCheck(bloggerId, blogId);

        if (!bloggerBlogService.deleteBlog(bloggerId, blogId))
            return handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 批量删除博文
     */
    @DeleteMapping("/patch")
    public ResultModel deletePatch(
            @Uid Long bloggerId,
            @RequestParam String ids) {

        Long[] blogIds = StringUtils.longStringDistinctToArray(ids, ",");
        if (Util.isArrayEmpty(blogIds))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        for (Long id : blogIds) {
            handleBlogExistAndCreatorCheck(bloggerId, id);
        }

        if (!bloggerBlogService.deleteBlogPatch(bloggerId, blogIds))
            return handlerOperateFail();

        return ResultModel.success();
    }

    /**
     * 批量导入博文
     * <p>
     * 返回成功导入博文的博文名和id
     */
    @PostMapping("/patch")
    public ResultModel<List<BlogTitleIdDTO>> patchImportBlog(
            @Uid Long bloggerId,
            @RequestParam("zipFile") MultipartFile file) {

        // 检查是否为 zip 文件
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".zip"))
            throw ExceptionUtil.get(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        FileVO fileVO = null;
        try {
            fileVO = DataConverter.VO.multipartFile2VO(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }

        List<BlogTitleIdDTO> blogsTitles = bloggerBlogService.insertBlogPatch(fileVO, bloggerId);
        if (CollectionUtils.isEmpty(blogsTitles))
            return handlerOperateFail();

        return ResultModel.success(blogsTitles);
    }

    /**
     * 下载博文
     */
    @GetMapping("/download-type={type}")
    public void download(HttpServletResponse response,
                         @Uid Long bloggerId,
                         @PathVariable String type) {

        // 检查请求的文件类别
        BlogFormatEnum format = BlogFormatEnum.get(type);
        if (format == null) {
            return;
        }

        String zipFilePath = bloggerBlogService.getAllBlogForDownload(bloggerId, format);
        if (StringUtils.isEmpty(zipFilePath))
            return;

        // 输出文件流
        outFile(zipFilePath, response);

        // 删除临时 zip 文件
        File file = new File(zipFilePath);
        file.delete();

    }

    // 输出文件流
    private void outFile(String zipFilePath, HttpServletResponse response) {

        try (ServletOutputStream os = response.getOutputStream()) {
            File zipFile = new File(zipFilePath);
            if (!zipFile.exists()) return;

            response.setContentType("application/x-zip-compressed");
            FileInputStream in = new FileInputStream(zipFile);
            byte[] buff = new byte[in.available()];
            in.read(buff);

            os.write(buff);
            os.flush();

            in.close();
            os.close();

        } catch (IOException e) {
            //
        }

    }

}
