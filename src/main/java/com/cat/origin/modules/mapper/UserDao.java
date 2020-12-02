package com.cat.origin.modules.mapper;

import com.cat.origin.modules.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统管理-用户基础信息表 Mapper 接口
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 根据登录名查询用户信息
     * @param loginName 登录名
     * @return 用户信息
     */
    UserEntity getByLoginName(String loginName);

    @Select("select * from sys_user t where t.username = #{username}")
    UserEntity getUser(String username);
}
