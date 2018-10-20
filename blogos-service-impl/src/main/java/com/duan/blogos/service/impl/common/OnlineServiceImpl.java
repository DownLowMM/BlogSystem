package com.duan.blogos.service.impl.common;

import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.common.OnlineService;
import com.duan.blogos.service.util.DataConverter;
import com.duan.blogos.service.util.ExceptionUtil;
import com.duan.blogos.service.util.TokenUtil;
import com.duan.blogos.service.vo.LoginVO;
import com.duan.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/9/14.
 *
 * @author DuanJiaNing
 */
@Service
public class OnlineServiceImpl implements OnlineService {

    private static final String ONLINE_PREFIX = "online:uid:";

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${app.name}")
    private String appName;

    @Value("${redis.key.expire}")
    private long expireTime;

    @Value("${redis.key.token.expire-day}")
    private int tokenExpireDay;

    @Autowired
    private BloggerAccountDao accountDao;

    @Override
    public Long getLoginBloggerId(String token) {

        try {
            return TokenUtil.decode(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultModel login(LoginVO vo) {

        BloggerAccount acc = accountDao.getAccountByName(vo.getUsername());

        // 用户不存在
        if (acc == null) {
            throw ExceptionUtil.get(CodeMessage.BLOGGER_UNKNOWN_BLOGGER);
        }

        // 密码错误
        try {
            if (!acc.getPassword().equals(new BigInteger(StringUtils.toSha(vo.getPassword())).toString())) {
                throw ExceptionUtil.get(CodeMessage.BLOGGER_PASSWORD_INCORRECT);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw ExceptionUtil.get(CodeMessage.BLOGGER_PASSWORD_INCORRECT);
        }


        Long uid = acc.getId();
        ValueOperations valueOperations = redisTemplate.opsForValue();

        // tokenKey
        String tokenKey = TokenUtil.getTokenKey(uid);
        String token = TokenUtil.getToken(uid);
        valueOperations.set(tokenKey, token, tokenExpireDay, TimeUnit.DAYS);

        // online record
        valueOperations.set(appName + ":" + ONLINE_PREFIX + uid, String.valueOf(uid), expireTime, TimeUnit.MINUTES);

        return ResultModel.success(DataConverter.PO2DTO.getLoginResultDTO(acc, token));
    }

    @Override
    public ResultModel logout(Long uid) {

        // tokenKey
        String tokenKey = TokenUtil.getTokenKey(uid);
        redisTemplate.delete(tokenKey);

        // online record
        redisTemplate.delete(appName + ":" + ONLINE_PREFIX + uid);

        return ResultModel.success();
    }

}
