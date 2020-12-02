package com.cat.origin.modules.service.impl;

import com.cat.origin.modules.dto.LoginUser;
import com.cat.origin.modules.dto.Token;
import com.cat.origin.modules.entity.TokenEntity;
import com.cat.origin.modules.mapper.TokenDao;
import com.cat.origin.modules.service.TokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Service
public class TokenServiceImpl extends ServiceImpl<TokenDao, TokenEntity> implements TokenService {

    /**
     * token过期秒数
     */
    @Value("${token.expire.seconds}")
    private Long expireSeconds;

    @Autowired
    private RedisTemplate<String, LoginUser> redisTemplate;

//    @Autowired
//    private SysLogService logService;

    @Override
    public Token saveToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();

        loginUser.setToken(token);
        cacheLoginUser(loginUser);
        // 登陆日志
//        logService.save(loginUser.getId(), "登陆", true, null);

        return new Token(token, loginUser.getLoginTime());
    }

    private void cacheLoginUser(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
        // 缓存
        redisTemplate.boundValueOps(getTokenKey(loginUser.getToken())).set(loginUser, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 更新缓存的用户信息
     */
    @Override
    public void refresh(LoginUser loginUser) {
        cacheLoginUser(loginUser);
    }

    @Override
    public LoginUser getLoginUser(String token) {
        return redisTemplate.boundValueOps(getTokenKey(token)).get();
    }

    @Override
    public boolean deleteToken(String token) {
        String key = getTokenKey(token);
        LoginUser loginUser = redisTemplate.opsForValue().get(key);
        if (loginUser != null) {
            redisTemplate.delete(key);
            // 退出日志
//            logService.save(loginUser.getId(), "退出", true, null);

            return true;
        }

        return false;
    }

    private String getTokenKey(String token) {
        return "tokens:" + token;
    }
}
