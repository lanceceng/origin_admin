package com.cat.origin.modules.service;

import com.cat.origin.exception.BusinessException;
import com.cat.origin.modules.dto.UserDto;
import com.cat.origin.modules.entity.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统管理-用户基础信息表 服务类
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 根据登录名查询用户信息
     * @param loginName 登录名
     * @return 用户信息对象
     */
    UserEntity getByLoginName(String loginName);

    /**
     * 保存用户信息
     * @param user 用户信息
     * @param roleIds 分配的角色列表
     */
    boolean saveOrUpdateUser(UserEntity user, String roleIds) throws BusinessException;

    UserEntity getUser(String username);
}
