package com.duan.blogos.service.impl.common;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.common.SmsService;
import com.duan.blogos.service.util.ExceptionUtil;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created on 2018/9/15.
 *
 * @author DuanJiaNing
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Value("jisu.appkey")
    private String appkey;

    @Value("smssend-url")
    private String smssendUrl;

    @Override
    public ResultModel sendSmsTo(String content, String phone) {

        String url;
        try {
            url = smssendUrl + "?mobile=" + phone + "&content=" + URLEncoder.encode(content, "utf-8") + "&appkey="
                    + appkey;
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }

        OkHttpClient client = new OkHttpClient();
        Request okRequest = new Request.Builder()
                .post(new FormBody.Builder().build())
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(okRequest).execute();
        } catch (IOException e) {
            throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
        }

        if (response != null) {
            try {
                JSONObject obj = JSONObject.parseObject(response.body().string());
                String status = obj.getString("status");
                if (Integer.valueOf(status) == 0) {
                    return ResultModel.success();
                } else {
                    // FIXME 极速短信API“签名不存在”错误
                    return ResultModel.fail(obj.getString("msg"), ResultModel.FAIL);
                }

            } catch (IOException e) {
                throw ExceptionUtil.get(CodeMessage.COMMON_UNKNOWN_ERROR, e);
            }

        }

        throw ExceptionUtil.get(CodeMessage.COMMON_OPERATE_FAIL);
    }

}
