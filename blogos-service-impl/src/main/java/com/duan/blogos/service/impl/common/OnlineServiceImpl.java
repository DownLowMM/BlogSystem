package com.duan.blogos.service.impl.common;

import com.duan.blogos.service.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.exception.CodeMessage;
import com.duan.blogos.service.exception.ResultUtil;
import com.duan.blogos.service.restful.ResultModel;
import com.duan.blogos.service.service.blogger.BloggerAccountService;
import com.duan.blogos.service.service.common.OnlineService;
import com.duan.blogos.service.util.TokenUtil;
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
    private BloggerAccountService accountService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${app.name}")
    private String appName;

    @Value("${redis.key.expire}")
    private long expireTime;

    @Value("${redis.key.token.expire-day}")
    private int tokenExpireDay;

    @Override
    public long getLoginBloggerId(String token) {

        try {
            return TokenUtil.decode(token);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public ResultModel login(BloggerAccountDTO account) {

        BloggerAccountDTO acc = accountService.getAccount(account.getUsername());

        // 用户不存在
        if (acc == null) {
            throw ResultUtil.failException(CodeMessage.BLOGGER_UNKNOWN_BLOGGER);
        }

        // 密码错误
        try {
            if (!acc.getPassword().equals(new BigInteger(StringUtils.toSha(account.getUsername())).toString())) {
                throw ResultUtil.failException(CodeMessage.BLOGGER_PASSWORD_INCORRECT);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw ResultUtil.failException(CodeMessage.BLOGGER_PASSWORD_INCORRECT);
        }


        Long uid = Long.valueOf(acc.getId());
        ValueOperations valueOperations = redisTemplate.opsForValue();

        // tokenKey
        String tokenKey = TokenUtil.getTokenKey(uid);
        valueOperations.set(tokenKey, uid, tokenExpireDay, TimeUnit.DAYS);

        // online record
        valueOperations.set(appName + ":" + ONLINE_PREFIX + uid, uid, expireTime, TimeUnit.MINUTES);

        return ResultModel.success();
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
