package com.cat.origin.modules.service;

import com.cat.origin.modules.dto.RoleDto;
import com.cat.origin.modules.entity.RoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统管理-角色表  服务类
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
public interface RoleService extends IService<RoleEntity> {

    /**
     * 查询用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleEntity> selectUserRoles(Long userId);

    /**
     * 保存角色
     * @param role 角色
     * @param menuIds 菜单id列表
     */
    boolean saveOrUpdateRole(RoleEntity role, String menuIds) throws Exception;
}
