package com.duan.blogos.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 2018/9/24.
 *
 * @author DuanJiaNing
 */
@Data
public class BlogLabelRelaDTO implements Serializable {

    private Long id;

    private Long blogId;

    private Long labelId;

    private Date insertTime;

}
