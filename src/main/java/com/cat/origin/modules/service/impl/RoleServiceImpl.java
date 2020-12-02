package com.cat.origin.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cat.origin.common.utils.StringUtils;
import com.cat.origin.modules.dto.RoleDto;
import com.cat.origin.modules.entity.RoleEntity;
import com.cat.origin.modules.entity.RolePermissionEntity;
import com.cat.origin.modules.mapper.RoleDao;
import com.cat.origin.modules.mapper.RolePermissionDao;
import com.cat.origin.modules.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 系统管理-角色表  服务实现类
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    private static final Logger log = LoggerFactory.getLogger("adminLogger");
    
    @Autowired(required = false)
    public RolePermissionDao rolePermissionDao;

    @Override
    public List<RoleEntity> selectUserRoles(Long userId) {
        return baseMapper.selectUserRoles(userId);
    }

    @Override
    @Transactional
    public boolean saveOrUpdateRole(RoleEntity role, String menuIds) throws Exception {
        if (role == null) {
            return false;
        }
        if (role.getId() == null) {
            //新增角色
            baseMapper.insert(role);
            Long roleId = role.getId();
            if (roleId != null && StringUtils.isNotBlank(menuIds)) {
                String[] menuIdArr = menuIds.split(",");
                for (String menuId : menuIdArr) {
                    if (StringUtils.isNumeric(menuId)) {
                        RolePermissionEntity sysRoleMenu = new RolePermissionEntity();
                        sysRoleMenu.setRoleId(roleId);
                        sysRoleMenu.setPermissionId(Long.parseLong(menuId));
                        rolePermissionDao.insert(sysRoleMenu);
                    }
                }
            }
        } else {
            baseMapper.updateById(role);
            Long roleId = role.getId();
            //删除已关联的菜单
            UpdateWrapper<RolePermissionEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("role_id", roleId);
            rolePermissionDao.delete(wrapper);
            //关联新的菜单

            if (StringUtils.isNotBlank(menuIds)) {
                String[] menuIdArr = menuIds.split(",");
                for (String menuId : menuIdArr) {
                    if (StringUtils.isNumeric(menuId)) {
                        RolePermissionEntity sysRoleMenu = new RolePermissionEntity();
                        sysRoleMenu.setRoleId(roleId);
                        sysRoleMenu.setPermissionId(Long.parseLong(menuId));
                        rolePermissionDao.insert(sysRoleMenu);
                    }
                }
            }
        }

        return true;
    }
}
