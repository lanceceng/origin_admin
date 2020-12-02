package com.cat.origin.modules.service.impl;

import com.cat.origin.modules.entity.RoleUserEntity;
import com.cat.origin.modules.mapper.UserRoleDao;
import com.cat.origin.modules.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统管理 - 用户角色关联表  服务实现类
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, RoleUserEntity> implements UserRoleService {

}
