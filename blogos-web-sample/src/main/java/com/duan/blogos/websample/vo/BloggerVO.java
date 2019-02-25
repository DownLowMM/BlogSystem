package com.duan.blogos.websample.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2019/2/25.
 *
 * @author DuanJiaNing
 */
@Data
public class BloggerVO implements Serializable {

    private Long id;

    private String name;

    private String nameBase64;

}
