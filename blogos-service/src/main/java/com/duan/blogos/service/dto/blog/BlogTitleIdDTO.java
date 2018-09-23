package com.duan.blogos.service.dto.blog;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2018/4/4.
 *
 * @author DuanJiaNing
 */
@Data
public class BlogTitleIdDTO implements Serializable {

    private String title;

    private Long id;

}
