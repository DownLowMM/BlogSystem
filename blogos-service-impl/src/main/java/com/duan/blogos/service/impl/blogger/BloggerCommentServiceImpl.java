package com.duan.blogos.service.impl.blogger;

import com.duan.blogos.service.dao.blog.BlogCommentDao;
import com.duan.blogos.service.dao.blog.BlogStatisticsDao;
import com.duan.blogos.service.entity.blog.BlogComment;
import com.duan.blogos.service.service.blogger.BloggerCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2018/3/13.
 *
 * @author DuanJiaNing
 */
@Service
public class BloggerCommentServiceImpl implements BloggerCommentService {

    @Autowired
    private BlogStatisticsDao statisticsDao;

    @Autowired
    private BlogCommentDao commentDao;

    @Override
    public Long insertComment(Long blogId, Long spokesmanId, Long listenerId, int state, String content) {

        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setContent(content);
        comment.setListenerId(listenerId);
        comment.setSpokesmanId(spokesmanId);
        comment.setState(state);
        commentDao.insert(comment);

        //博文评论次数加一
        statisticsDao.updateCommentCountPlus(blogId);
        return comment.getId();
    }

    @Override
    public boolean deleteComment(Long commentId, Long bloggerId) {

        BlogComment comment = commentDao.getCommentById(commentId);

        // 只有发布评论的人才能删除评论
        if (comment == null || !comment.getSpokesmanId().equals(bloggerId)) {
            return false;
        }

        int effect = commentDao.delete(commentId);

        if (effect <= 0) return false;
        else statisticsDao.updateCommentCountMinus(comment.getBlogId());

        return true;
    }

}
