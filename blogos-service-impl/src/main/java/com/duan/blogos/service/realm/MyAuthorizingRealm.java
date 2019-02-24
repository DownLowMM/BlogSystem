package com.duan.blogos.service.realm;

import com.duan.blogos.service.dao.BloggerAccountDao;
import com.duan.blogos.service.entity.BloggerAccount;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on 2017/12/11.
 * shiro realm
 *
 * @author DuanJiaNing
 */
public class MyAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private BloggerAccountDao accountDao;

    /**
     * 权限验证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 首先执行登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户账号
        String username = token.getPrincipal().toString();
        BloggerAccount account = accountDao.getAccountByName(username);
        if (account != null) {

            //将查询到的用户账号和密码存放到 authenticationInfo用于后面的权限判断。第三个参数随便放一个就行了。
            return new SimpleAuthenticationInfo(account.getUsername(), account.getPassword(), "");

        } else {
            return null;
        }
    }
}
