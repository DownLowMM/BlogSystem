package com.duan.blogos.service.vo;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created on 2018/10/6.
 *
 * @author DuanJiaNing
 */
@Data
public class FileVO implements Serializable {

    private InputStream inputStream;

    private String originalFilename;

    private String contentType;

}
