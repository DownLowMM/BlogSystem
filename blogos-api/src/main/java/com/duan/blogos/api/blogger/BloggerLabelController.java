package com.duan.blogos.api.blogger;

import com.duan.blogos.service.dto.blog.BlogLabelDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerLabelService;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created on 2018/1/12.
 * 博文标签API，标签的使用不限定博主，即只要标签存在，任何博主都可以使用
 * <p>
 * 1 查看所有标签
 * 2 查看指定标签
 * 3 修改标签
 * 4 删除标签
 * 5 增加标签
 * 6 获取指定博主创建的标签
 *
 * @author DuanJiaNing
 */
@RestController
@RequestMapping("/blogger/{bloggerId}/label")
public class BloggerLabelController extends BaseBloggerController {

    @Autowired
    private BloggerLabelService bloggerLabelService;


    /**
     * 获取指定博主创建的标签
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel<List<BlogLabelDTO>> list(HttpServletRequest request,
                                                @PathVariable Long bloggerId,
                                                @RequestParam(value = "offset", required = false) Integer offset,
                                                @RequestParam(value = "rows", required = false) Integer rows) {
        handleAccountCheck(bloggerId);

        ResultModel<List<BlogLabelDTO>> result = bloggerLabelService.listLabelByBlogger(bloggerId,
                offset == null ? 0 : offset, rows == null ? -1 : rows);
        if (result == null) handlerEmptyResult();

        return result;
    }

    /**
     * 新增标签
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel add(HttpServletRequest request,
                           @PathVariable Long bloggerId,
                           @RequestParam("title") String title) {

        handleTitleCheck(title, request);

        Long id = bloggerLabelService.insertLabel(bloggerId, title);
        if (id < 0) handlerOperateFail();

        return new ResultModel<>(id);
    }

    /**
     * 修改标签
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.PUT)
    public ResultModel update(HttpServletRequest request,
                              @PathVariable Long bloggerId,
                              @PathVariable Long labelId,
                              @RequestParam("title") String newTitle) {
        handleTitleCheck(newTitle, request);

        boolean result = bloggerLabelService.updateLabel(labelId, bloggerId, newTitle);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

    /**
     * 删除标签
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.DELETE)
    public ResultModel delete(HttpServletRequest request,
                              @PathVariable("labelId") Long labelId,
                              @PathVariable Long bloggerId) {
        handleAccountCheck(bloggerId);
        boolean result = bloggerLabelService.deleteLabel(bloggerId, labelId);
        if (!result) handlerOperateFail();

        return new ResultModel<>("");
    }

    // 检查标题合法性
    private void handleTitleCheck(String title, HttpServletRequest request) {
        if (StringUtils.isEmpty(title))
            throw ResultUtil.failException(CodeMessage.COMMON_PARAMETER_ILLEGAL);
    }

}
