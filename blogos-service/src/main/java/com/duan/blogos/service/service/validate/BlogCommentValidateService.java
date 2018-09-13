package com.duan.blogos.service.service.validate;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public interface BlogCommentValidateService {

    /**
     * 审核评论内容
     *
     * @param content 评论内容
     * @return 审核通过为true
     */
    boolean checkCommentContent(String content);
}
