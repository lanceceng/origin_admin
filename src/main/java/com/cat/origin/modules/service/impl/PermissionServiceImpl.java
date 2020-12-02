package com.cat.origin.modules.service.impl;

import com.cat.origin.common.utils.ConstUtils;
import com.cat.origin.modules.entity.PermissionEntity;
import com.cat.origin.modules.mapper.PermissionDao;
import com.cat.origin.modules.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, PermissionEntity> implements PermissionService {

    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Override
    public Set<String> listUserPermissions(Long userId) {
        if (userId == null) {
            return null;
        }

        Set<String> permissionList = null;
        if (permissionList == null){
            List<String> perms;
            if (1L == userId) {
                perms = baseMapper.listAllPermissions();
            } else {
                perms = baseMapper.listUserPermissions(userId);
            }

            if (perms != null && perms.size() > 0) {
                permissionList = new HashSet<>();
                for (String perm : perms) {
                    if (StringUtils.isNotBlank(perm)) {
                        permissionList.addAll(Arrays.asList(perm.trim().split(",")));
                    }
                }
//                CacheUtils.putSysCache("perms_" + userId, permissionList);
            }
        }

        return permissionList;
    }

    @Override
    public List<PermissionEntity> listUserMenu(Long userId) {
        if (userId == null) {
            return null;
        }
        List<PermissionEntity> menuList = null;

        if (menuList == null){
            if (1L == userId) {
                menuList = baseMapper.listAllMenu();
            } else {
                menuList = baseMapper.getUserMenuList(userId);
            }

            if (menuList != null && menuList.size() > 0) {
//                CacheUtils.putSysCache("menus_" + userId, menuList);
            }
        }

        return menuList;
    }

    @Override
    public List<PermissionEntity> listRoleMenu(Long roleId) {
        return baseMapper.listRoleMenu(roleId);
    }

    @Override
    public int deleteMenu(Long parentId) {
        List<Long> idArr = getAllChildIds(parentId);
        int count = 0;
        if (idArr != null) {
            //循环删除所有本身及子节点
            for (Long id : idArr) {
                PermissionEntity menu = new PermissionEntity();
                menu.setId(id);
                menu.setDelFlag(ConstUtils.DEL_FLAG_DELETE);
                count += baseMapper.updateById(menu);
            }
        }
        return count;
    }

    @Override
    public String selectAllChildIds(Long parentId) {
        return baseMapper.selectAllChildIds(parentId);
    }

    @Override
    public Integer getMaxOrderNo(Long parentId) {
        return baseMapper.getMaxOrderNo(parentId);
    }

    private static final List<Long> ids = new ArrayList<>();

    private List<Long> getAllChildIds(Long parentId){
        ids.clear();
        List<PermissionEntity> list = baseMapper.listAllMenuAndPerm();
        forChild(list, parentId);
        ids.add(parentId);
        return ids;
    }

    private void forChild(List<PermissionEntity> list, Long parentId){
        for(PermissionEntity entity: list){
            if(entity.getParentId() == parentId){
                ids.add(entity.getId());
                forChild(list, entity.getId());
            }
        }
    }
}
