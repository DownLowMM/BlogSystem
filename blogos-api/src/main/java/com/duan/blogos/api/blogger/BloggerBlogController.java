package com.duan.blogos.api.blogger;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.annonation.Uid;
import com.duan.blogos.service.common.BlogSortRule;
import com.duan.blogos.service.common.Order;
import com.duan.blogos.service.dto.blog.BlogDTO;
import com.duan.blogos.service.dto.blog.BlogTitleIdDTO;
import com.duan.blogos.service.dto.blogger.BlogListItemDTO;
import com.duan.blogos.service.enums.BlogFormatEnum;
import com.duan.blogos.service.enums.BlogStatusEnum;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerBlogService;
import com.duan.common.spring.verify.Rule;
import com.duan.common.spring.verify.annoation.parameter.ArgVerify;
import com.duan.common.util.CollectionUtils;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.duan.blogos.service.common.Rule.VIEW_COUNT;

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
@RequestMapping("/blogger/blog")
public class BloggerBlogController extends BaseBloggerController {

    @Autowired
    private BloggerBlogService bloggerBlogService;

    /**
     * 新增博文
     */
    @PostMapping
    public ResultModel add(
            @Uid Long bloggerId,
            @RequestParam(value = "cids", required = false) String categoryIds,
            @RequestParam(value = "lids", required = false) String labelIds,
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam("title") String title,
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam("content") String content,
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam("contentMd") String contentMd,
            @ArgVerify(rule = Rule.NOT_BLANK)
            @RequestParam("summary") String summary,
            @RequestParam(value = "keywords", required = false) String keyWords) {

        // 检查不能为null的参数是否为null
        if (StringUtils.isBlank(title) || StringUtils.isBlank(content) || StringUtils.isBlank(summary))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

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
        if (id == null) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 检索博文
     */
    @GetMapping
    @TokenNotRequired
    public ResultModel<List<BlogListItemDTO>> list(
            @RequestParam Long bloggerId,
            @RequestParam(value = "cids", required = false) String categoryIds,
            @RequestParam(value = "lids", required = false) String labelIds,
            @RequestParam(value = "kword", required = false) String keyWord,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "status", required = false) Integer status) {

        //检查排序规则
        String sor = sort == null ? VIEW_COUNT.name() : sort.toUpperCase();
        String ord = order == null ? Order.DESC.name() : order.toUpperCase();
        handleSortRuleCheck(sor, ord);

        String sp = ",";
        Long[] cids = StringUtils.longStringDistinctToArray(categoryIds, sp);
        Long[] lids = StringUtils.longStringDistinctToArray(labelIds, sp);
        //检查博文类别和标签
        handleCategoryAndLabelCheck(bloggerId, cids, lids);

        BlogStatusEnum stat = null;
        if (status != null) stat = BlogStatusEnum.valueOf(status);
        if (stat == null) stat = BlogStatusEnum.PUBLIC; // status 传参错误

        //执行数据查询
        BlogSortRule rule = new BlogSortRule(com.duan.blogos.service.common.Rule.valueOf(sor), Order.valueOf(ord));

        ResultModel<List<BlogListItemDTO>> listResultModel = bloggerBlogService.listFilterAll(cids, lids, keyWord, bloggerId,
                offset == null ? 0 : offset, rows == null ? -1 : rows, rule, stat);
        if (listResultModel == null) handlerEmptyResult();

        return listResultModel;
    }

    /**
     * 获取指定博文
     */
    @GetMapping("/{blogId}")
    @TokenNotRequired
    public ResultModel<BlogDTO> get(
            @RequestParam Long bloggerId,
            @PathVariable Long blogId) {

        ResultModel<BlogDTO> blog = bloggerBlogService.getBlog(bloggerId, blogId);
        if (blog == null) handlerEmptyResult();

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
            @RequestParam(value = "title", required = false) String newTitle,
            @RequestParam(value = "content", required = false) String newContent,
            @RequestParam(value = "contentMd", required = false) String newContentMd,
            @RequestParam(value = "summary", required = false) String newSummary,
            @RequestParam(value = "cids", required = false) String newCategoryIds,
            @RequestParam(value = "lids", required = false) String newLabelIds,
            @RequestParam(value = "kword", required = false) String newKeyWord,
            @RequestParam(value = "status", required = false) Integer newStatus) {

        // 所有参数都为null，则不更新。
        if (Stream.of(newTitle, newContent, newSummary, newCategoryIds, newLabelIds, newKeyWord, newStatus)
                .filter(Objects::nonNull).count() <= 0)
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        // 检查修改到的博文状态是否允许
        if (newStatus != null && !blogValidateService.isBlogStatusAllow(newStatus))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        handleBlogExistAndCreatorCheck(bloggerId, blogId);

        // 将 Unicode 解码
        newContent = StringUtils.unicodeToString(newContent);
        newContentMd = StringUtils.unicodeToString(newContentMd);

        handleBlogContentCheck(newTitle, newContent, newContentMd, newSummary, newKeyWord);

        String sp = ",";
        Long[] cids = newCategoryIds == null ? null : StringUtils.longStringDistinctToArray(newCategoryIds, sp);
        Long[] lids = newLabelIds == null ? null : StringUtils.longStringDistinctToArray(newLabelIds, sp);

        //检查博文类别和标签
        handleCategoryAndLabelCheck(bloggerId, cids, lids);

        String[] kw = newKeyWord == null ? null : StringUtils.stringArrayToArray(newKeyWord, sp);
        BlogStatusEnum stat = newStatus == null ? null : BlogStatusEnum.valueOf(newStatus);

        //执行更新
        if (!bloggerBlogService.updateBlog(bloggerId, blogId, cids, lids, stat, newTitle, newContent, newContentMd, newSummary, kw))
            handlerOperateFail();

        return new ResultModel<>("");
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
            handlerOperateFail();

        return new ResultModel<>("");
    }

    /**
     * 批量删除博文
     */
    @DeleteMapping("/patch")
    public ResultModel deletePatch(
            @Uid Long bloggerId,
            @RequestParam("ids") String ids) {

        Long[] blogIds = StringUtils.longStringDistinctToArray(ids, ",");
        if (CollectionUtils.isEmpty(blogIds))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        for (Long id : blogIds) {
            handleBlogExistAndCreatorCheck(bloggerId, id);
        }

        if (!bloggerBlogService.deleteBlogPatch(bloggerId, blogIds))
            handlerOperateFail();

        return new ResultModel<>("");
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
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);

        com.duan.common.util.MultipartFile fi = null;// TODO 转化
        List<BlogTitleIdDTO> blogsTitles = bloggerBlogService.insertBlogPatch(fi, bloggerId);
        if (CollectionUtils.isEmpty(blogsTitles))
            handlerOperateFail();

        return new ResultModel<>(blogsTitles);
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
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
        }

        String zipFilePath = bloggerBlogService.getAllBlogForDownload(bloggerId, format);
        if (StringUtils.isEmpty(zipFilePath)) handlerOperateFail();

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
            if (!zipFile.exists()) handlerOperateFail();

            response.setContentType("application/x-zip-compressed");
            FileInputStream in = new FileInputStream(zipFile);
            byte[] buff = new byte[in.available()];
            in.read(buff);

            os.write(buff);
            os.flush();

            in.close();
            os.close();

        } catch (IOException e) {
            handlerOperateFail(e);
        }

    }

    // 检查博文是否存在，且博文是否属于指定博主
    private void handleBlogExistAndCreatorCheck(Long bloggerId, Long blogId) {
        if (!blogValidateService.isCreatorOfBlog(bloggerId, blogId))
            throw ResultUtil.failException(CodeMessage.BLOG_UNKNOWN_BLOG);
    }

    // 检查类别和标签
    private void handleCategoryAndLabelCheck(Long bloggerId, Long[] cids, Long[] lids) {

        if (!CollectionUtils.isEmpty(cids)) {
            for (Long id : cids) {
                if (!bloggerValidateService.checkBloggerBlogCategoryExist(bloggerId, id))
                    throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }
        }

        if (!CollectionUtils.isEmpty(lids)) {
            for (Long id : lids) {
                if (!blogValidateService.checkLabelsExist(id))
                    throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
            }
        }

    }

    //博文内容审核
    private void handleBlogContentCheck(String title, String content, String contentMd, String summary,
                                        String keyWords) {
        if (!blogValidateService.verifyBlog(title, content, contentMd, summary, keyWords))
            throw ResultUtil.failException(CodeMessage.BLOG_ILLEGAL);

    }

    // 检查排序规则
    private void handleSortRuleCheck(String sort, String order) {

        if (sort != null && !com.duan.blogos.service.common.Rule.contains(sort)) {
            throw ResultUtil.failException(CodeMessage.BLOG_BLOG_SORT_RULE_UNDEFINED);
        }

        if (order != null && !Order.contains(order)) {
            throw ResultUtil.failException(CodeMessage.BLOG_BLOG_SORT_ORDER_UNDEFINED);
        }
    }

}
