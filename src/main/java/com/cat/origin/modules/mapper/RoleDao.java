package com.cat.origin.modules.mapper;

import com.cat.origin.modules.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统管理-角色表  Mapper 接口
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
public interface RoleDao extends BaseMapper<RoleEntity> {

    /**
     * 查询用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleEntity> selectUserRoles(Long userId);
}
