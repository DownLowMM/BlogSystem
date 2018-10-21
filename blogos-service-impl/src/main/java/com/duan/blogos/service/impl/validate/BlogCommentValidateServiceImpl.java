package com.duan.blogos.service.impl.validate;

import com.alibaba.dubbo.config.annotation.Service;
import com.duan.blogos.service.service.validate.BlogCommentValidateService;
/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
@Service
public class BlogCommentValidateServiceImpl implements BlogCommentValidateService {

    public boolean checkCommentContent(String content) {
        // UPDATE: 2018/2/3 更新
        return true;
    }
}
