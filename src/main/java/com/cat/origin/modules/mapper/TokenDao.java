package com.cat.origin.modules.mapper;

import com.cat.origin.modules.entity.TokenEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
public interface TokenDao extends BaseMapper<TokenEntity> {

    @Insert("insert into t_token(id, val, expireTime, createTime, updateTime) values (#{id}, #{val}, #{expireTime}, #{createTime}, #{updateTime})")
    int save(TokenEntity model);

    @Select("select * from t_token t where t.id = #{id}")
    TokenEntity getById(String id);

    @Update("update t_token t set t.val = #{val}, t.expireTime = #{expireTime}, t.updateTime = #{updateTime} where t.id = #{id}")
    int update(TokenEntity model);

    @Delete("delete from t_token where id = #{id}")
    int delete(String id);
}
