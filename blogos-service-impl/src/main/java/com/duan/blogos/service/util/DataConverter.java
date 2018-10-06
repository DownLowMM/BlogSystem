package com.duan.blogos.service.util;

import com.duan.blogos.service.dto.LoginResultDTO;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created on 2018/10/1.
 *
 * @author DuanJiaNing
 */
public class DataConverter {

    public static class PO2DTO {
        public static LoginResultDTO getLoginResultDTO(BloggerAccount po, String token) {
            LoginResultDTO dto = new LoginResultDTO();
            dto.setId(po.getId());
            dto.setRegisterDate(po.getRegisterDate());
            dto.setUsername(po.getUsername());
            dto.setToken(token);

            return dto;
        }
    }

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
