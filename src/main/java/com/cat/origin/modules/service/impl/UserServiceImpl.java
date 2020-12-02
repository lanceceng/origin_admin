package com.cat.origin.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cat.origin.common.utils.StringUtils;
import com.cat.origin.exception.BusinessException;
import com.cat.origin.modules.dto.UserDto;
import com.cat.origin.modules.entity.RoleUserEntity;
import com.cat.origin.modules.entity.UserEntity;
import com.cat.origin.modules.mapper.RolePermissionDao;
import com.cat.origin.modules.mapper.UserDao;
import com.cat.origin.modules.mapper.UserRoleDao;
import com.cat.origin.modules.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 系统管理-用户基础信息表 服务实现类
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Autowired(required = false)
    public UserRoleDao userRoleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserEntity getByLoginName(String loginName) {
        if (StringUtils.isNotBlank(loginName)) {
            return baseMapper.getByLoginName(loginName);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean saveOrUpdateUser(UserEntity user, String roleIds) throws BusinessException {
        if (user == null) {
            return false;
        }
        //新增
        if (user.getId() == null) {
            if (getByLoginName(user.getUsername()) != null) {
                throw new BusinessException("登录名不能重复");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            baseMapper.insert(user);

            //保存关联的角色
            if (user.getId() != null && StringUtils.isNotBlank(roleIds)) {
                String[] roleIdArr = roleIds.split(",");
                for (String roleId : roleIdArr) {
                    if (StringUtils.isNumeric(roleId)) {
                        RoleUserEntity sysUserRole = new RoleUserEntity();
                        sysUserRole.setUserId(user.getId());
                        sysUserRole.setRoleId(Long.parseLong(roleId));

                        userRoleDao.insert(sysUserRole);
                    }
                }
            }
        } else {//修改，注意：此处不修改为null的字段，防止修改密码
            baseMapper.updateById(user);

            //先删除已关联的角色
            UpdateWrapper<RoleUserEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_id", user.getId());
            userRoleDao.delete(wrapper);

            //保存新关联的角色
            if (StringUtils.isNotBlank(roleIds)) {
                String[] roleIdArr = roleIds.split(",");
                for (String roleId : roleIdArr) {
                    if (StringUtils.isNumeric(roleId)) {
                        RoleUserEntity sysUserRole = new RoleUserEntity();
                        sysUserRole.setUserId(user.getId());
                        sysUserRole.setRoleId(Long.parseLong(roleId));

                        userRoleDao.insert(sysUserRole);
                    }
                }
            }
        }

//        CacheUtils.removeSysCache("perms_" + user.getId());
        return true;
    }

    @Override
    public UserEntity getUser(String username) {
        return baseMapper.getUser(username);
    }
}
