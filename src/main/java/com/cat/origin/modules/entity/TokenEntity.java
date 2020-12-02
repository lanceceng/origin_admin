package com.cat.origin.modules.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_token")
public class TokenEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * token
     */
    @TableId("id")
    private String id;

    /**
     * LoginUser的json串
     */
    @TableField("val")
    private String val;

    @TableField("expireTime")
    private Date expireTime;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateTime")
    private Date updateTime;


}
