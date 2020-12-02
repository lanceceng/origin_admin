package com.cat.origin.modules.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cat.origin.common.result.MsgResult;
import com.cat.origin.common.result.PageResult;
import com.cat.origin.common.utils.ConstUtils;
import com.cat.origin.modules.entity.PermissionEntity;
import com.cat.origin.modules.entity.RoleEntity;
import com.cat.origin.modules.service.PermissionService;
import com.cat.origin.modules.service.RoleService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统管理-角色表  前端控制器
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Api(tags = "角色")
@Controller
@RequestMapping("/admin/sys/role")
public class RoleController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("roleList", null);
        return "admin/sys/role/roleList";
    }

    @RequestMapping("/list")
    @ResponseBody
    PageResult list(Integer pageNo, Integer pageSize) {
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.orderByDesc("create_time");
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNo == null) {
            pageNo = 1;
        } else {
            pageNo = pageNo / pageSize + 1;//页面上传过来的是offset，需要转成页码
        }
        //分页查询数据
        IPage<RoleEntity> rolePage = new Page<>(pageNo, pageSize);
        rolePage = roleService.page(rolePage, wrapper);
        return new PageResult(rolePage.getRecords(), rolePage.getTotal());
    }

    @GetMapping("/edit")
    String edit(Model model, Long id) {
        if (id != null) {
            RoleEntity sysRole = roleService.getById(id);
            model.addAttribute("role", sysRole);

            List<PermissionEntity> hasMenus = permissionService.listRoleMenu(id);
            if (hasMenus != null && hasMenus.size() > 0) {
                String selectedIds = "";
                for (PermissionEntity menu : hasMenus) {
                    selectedIds += menu.getId() + ",";
                }
                model.addAttribute("selectedIds", selectedIds);
            }
        }

        model.addAttribute("menuList", getMenuMapList());

        return "admin/sys/role/roleEdit";
    }

    @GetMapping("/add")
    String add(Model model) {
        model.addAttribute("menuList", getMenuMapList());
        return "admin/sys/role/roleAdd";
    }

    /**
     * 获取所有菜单列表，转换成ztree树结构
     */
    private List<Map<String, Object>> getMenuMapList() {
        QueryWrapper<PermissionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.eq("status", ConstUtils.YES);
//        wrapper.orderByAsc("order_no").orderByDesc("create_time");
        List<PermissionEntity> allMenus = permissionService.list(wrapper);

        if (allMenus != null && allMenus.size() > 0) {
            List<Map<String, Object>> mapList = Lists.newArrayList();
            for (PermissionEntity menu : allMenus) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", menu.getId());
                map.put("pId", menu.getParentId());
                map.put("name", menu.getName());
                mapList.add(map);
            }

            return mapList;
        }

        return null;
    }

    @PostMapping("/save")
    @ResponseBody
    MsgResult save(RoleEntity role, String menuIds) {
        try {
            if (role.getId() == null) {
                role.setCreateId(9L);
                role.setCreateTime(new Date());
            }
            role.setUpdateId(9L);
            role.setUpdateTime(new Date());
            if (roleService.saveOrUpdateRole(role, menuIds)) {
                return MsgResult.ok();
            }
        } catch (Exception e) {
            logger.error("保存角色信息失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }

        return MsgResult.error();
    }

    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        RoleEntity role = new RoleEntity();
        role.setId(id);
        role.setDelFlag(ConstUtils.DEL_FLAG_DELETE);
        if (roleService.updateById(role)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 验证角色名是否有效
     * @param oldName 老的角色名
     * @param name 新的角色名
     */
    @ResponseBody
    @RequestMapping(value = "checkLoginNameAjax")
    public String checkRoleName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return "true";
        } else if (name != null) {
            QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
            wrapper.eq("name", name);
            if (roleService.count(wrapper) == 0) {
                return "true";
            }
        }
        return "false";
    }
}
