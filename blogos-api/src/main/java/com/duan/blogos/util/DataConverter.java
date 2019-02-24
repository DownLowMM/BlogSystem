package com.duan.blogos.util;

import com.duan.blogos.service.common.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created on 2018/10/1.
 *
 * @author DuanJiaNing
 */
public class DataConverter {

    public static class VO {

        public static FileVO multipartFile2VO(MultipartFile file) throws IOException {
            FileVO vo = new FileVO();
            vo.setContentType(file.getContentType());
            vo.setInputStream(file.getInputStream());
            vo.setOriginalFilename(file.getOriginalFilename());

            return vo;
        }
    }

}
