package com.cat.origin.modules.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cat.origin.modules.dto.LoginUser;
import com.cat.origin.modules.dto.Token;
import com.cat.origin.modules.entity.TokenEntity;
import com.cat.origin.modules.mapper.TokenDao;
import com.cat.origin.modules.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * token存到数据库的实现类
 * 
 * @author cat
 *
 */
@Service
public class TokenServiceDbImpl extends ServiceImpl<TokenDao, TokenEntity> implements TokenService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	/**
	 * token过期秒数
	 */
	@Value("${token.expire.seconds}")
	private Long expireSeconds;
	
	@Autowired(required = false)
	private TokenDao tokenDao;

//	@Autowired
//	private SysLogService logService;

	/**
	 * 私钥
	 */
	@Value("${token.jwtSecret}")
	private String jwtSecret;

	private static Key KEY = null;

	private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

	@Override
	public Token saveToken(LoginUser loginUser) {
		loginUser.setToken(UUID.randomUUID().toString());
		loginUser.setLoginTime(System.currentTimeMillis());
		loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);

		TokenEntity model = new TokenEntity();
		model.setId(loginUser.getToken());
		model.setCreateTime(new Date());
		model.setUpdateTime(new Date());
		model.setExpireTime(new Date(loginUser.getExpireTime()));
		model.setVal(JSONObject.toJSONString(loginUser));

		tokenDao.save(model);
		// 登陆日志
//		logService.save(loginUser.getId(), "登陆", true, null);

		String jwtToken = createJWTToken(loginUser);

		return new Token(jwtToken, loginUser.getLoginTime());
	}

	/**
	 * 生成jwt
	 * 
	 * @param loginUser
	 * @return
	 */
	private String createJWTToken(LoginUser loginUser) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(LOGIN_USER_KEY, loginUser.getToken());// 放入一个随机字符串，通过该串可找到登陆用户

		String jwtToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance())
				.compact();

		return jwtToken;
	}

	@Override
	public void refresh(LoginUser loginUser) {
		loginUser.setLoginTime(System.currentTimeMillis());
		loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);

		TokenEntity model = tokenDao.getById(loginUser.getToken());
		model.setUpdateTime(new Date());
		model.setExpireTime(new Date(loginUser.getExpireTime()));
		model.setVal(JSONObject.toJSONString(loginUser));

		tokenDao.update(model);
	}

	@Override
	public LoginUser getLoginUser(String jwtToken) {
		String uuid = getUUIDFromJWT(jwtToken);
		if (uuid != null) {
			TokenEntity model = tokenDao.getById(uuid);
			return toLoginUser(model);
		}

		return null;
	}

	@Override
	public boolean deleteToken(String jwtToken) {
		String uuid = getUUIDFromJWT(jwtToken);
		if (uuid != null) {
			TokenEntity model = tokenDao.getById(uuid);
			LoginUser loginUser = toLoginUser(model);
			if (loginUser != null) {
				tokenDao.delete(uuid);
//				logService.save(loginUser.getId(), "退出", true, null);

				return true;
			}
		}

		return false;
	}

	private LoginUser toLoginUser(TokenEntity model) {
		if (model == null) {
			return null;
		}

		// 校验是否已过期
		if (model.getExpireTime().getTime() > System.currentTimeMillis()) {
			return JSONObject.parseObject(model.getVal(), LoginUser.class);
		}

		return null;
	}

	private Key getKeyInstance() {
		if (KEY == null) {
			synchronized (TokenServiceDbImpl.class) {
				if (KEY == null) {// 双重锁
					byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
					KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
				}
			}
		}

		return KEY;
	}

	private String getUUIDFromJWT(String jwt) {
		if ("null".equals(jwt) || StringUtils.isBlank(jwt)) {
			return null;
		}

		Map<String, Object> jwtClaims = null;
		try {
			jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwt).getBody();
			return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
		} catch (ExpiredJwtException e) {
			log.error("{}已过期", jwt);
		} catch (Exception e) {
			log.error("{}", e);
		}

		return null;
	}

}
