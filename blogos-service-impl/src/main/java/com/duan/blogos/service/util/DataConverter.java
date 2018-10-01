package com.duan.blogos.service.util;

import com.duan.blogos.service.dto.LoginResultDTO;
import com.duan.blogos.service.entity.blogger.BloggerAccount;

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

}
