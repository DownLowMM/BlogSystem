package com.duan.blogos.config.interceptor;


import com.alibaba.fastjson.JSON;
import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.util.TokenUtil;
import com.duan.blogos.util.CodeMessage;
import com.duan.blogos.util.CurrentUserThreadLocal;
import com.duan.blogos.util.SpringUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2018/5/30.
 *
 * @author DuanJiaNing
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private RedisTemplate redisTemplate = (RedisTemplate) SpringUtil.getBean("redisTemplate");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!canHandle(handler)) {
            return true;
        }

        String token = request.getHeader("token");
        if (token == null) {
            token = request.getParameter("token");
        }

        // 判断请求是否有 token，非法 token
        if (token == null || illegalToken(token)) {
            ResultModel resultModel = new ResultModel("token required", CodeMessage.TOKEN_REQUIRED.getCode());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(resultModel));

            return false;
        }

        // token 非法
        if (expiredToken(token)) {
            ResultModel resultModel = new ResultModel("invalid token", CodeMessage.INVALID_TOKEN.getCode());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(resultModel));

            return false;
        }

        return true;
    }

    /**
     * 非法 token
     * 无法解密的 token 即为非法 token
     */
    private boolean illegalToken(String token) {

        try {
            TokenUtil.decode(token);
        } catch (Exception e) {
            return true;
        }

        return false;
    }


    /**
     * token 已过期
     */
    private boolean expiredToken(String token) {

        try {
            Long uid = TokenUtil.decode(token);
            String key = TokenUtil.getTokenKey(uid);

            Object object = redisTemplate.opsForValue().get(key);
            if (object == null) {
                // 过期
                return true;
            }

            CurrentUserThreadLocal.setCurrentUid(uid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 判断是否需要处理
     *
     * @return 不用处理返回 ture
     */
    private boolean canHandle(Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> clasz = handlerMethod.getBeanType();
        if (clasz.isAnnotationPresent(TokenNotRequired.class)) {
            return false;
        }

        TokenNotRequired checkToken = handlerMethod.getMethod().getAnnotation(TokenNotRequired.class);
        if (checkToken != null) {
            return false;
        }

        return true;
    }


}
