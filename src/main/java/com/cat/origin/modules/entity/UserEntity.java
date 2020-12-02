package com.cat.origin.modules.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统管理-用户基础信息表
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public UserEntity(){}

    public UserEntity(Long id, String username){
        this.id = id;
        this.username = username;
    }

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    @TableField("username")
    private String username;

    /**
     * 登录密码
     */
    @TableField("password")
    private String password;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 性别 0:男 1:女
     */
    @TableField("sex")
    private String sex;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 手机号码
     */
    @TableField("telephone")
    private String telephone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;


    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 生日
     */
    @TableField("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField("create_id")
    private Long createId;

    @TableField("update_id")
    private Long updateId;

    public interface Status {
        int DISABLED = 0;
        int VALID = 1;
        int LOCKED = 2;
    }
}
