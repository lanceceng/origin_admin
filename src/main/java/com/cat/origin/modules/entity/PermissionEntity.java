package com.cat.origin.modules.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.List;

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
@TableName("sys_permission")
public class PermissionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 权限名称
     */
    @TableField("name")
    private String name;

    /**
     * 样式
     */
    @TableField("css")
    private String css;

    /**
     * 样式
     */
    @TableField("icon")
    private String icon;

    /**
     * 路由
     */
    @TableField("href")
    private String href;

    /**
     * 1：菜单 2：按钮
     */
    @TableField("type")
    private Long type;

    /**
     * 权限位置
     */
    @TableField("permission")
    private String permission;

    /**
     * 排序序号
     */
    @TableField("order_no")
    private Integer orderNo;

    /**
     * 状态 0-隐藏 1-显示
     */
    @TableField("status")
    private Integer status;

    @TableField(exist = false)
    private List<PermissionEntity> childList;

    @TableField("del_flag")
    private Integer delFlag;
}
