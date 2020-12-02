package com.cat.origin.modules.service;

import com.cat.origin.modules.dto.LoginUser;
import com.cat.origin.modules.dto.Token;
import com.cat.origin.modules.entity.TokenEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
public interface TokenService extends IService<TokenEntity> {

    Token saveToken(LoginUser loginUser);

    void refresh(LoginUser loginUser);

    LoginUser getLoginUser(String token);

    boolean deleteToken(String token);
}
