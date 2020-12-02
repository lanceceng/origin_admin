package com.cat.origin.modules.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cat.origin.common.result.MsgResult;
import com.cat.origin.common.utils.ConstUtils;
import com.cat.origin.modules.dto.LoginUser;
import com.cat.origin.modules.entity.PermissionEntity;
import com.cat.origin.modules.mapper.PermissionDao;
import com.cat.origin.modules.service.PermissionService;
import com.cat.origin.utils.UserUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Api(tags = "权限")
@Controller
@RequestMapping("/admin/sys/menu")
public class PermissionController {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        LoginUser currUser =  UserUtil.getLoginUser();
        model.addAttribute("params", params);
        model.addAttribute("menuList", null);
        return "admin/sys/menu/menuList";
    }

    @RequestMapping("/list")
    @ResponseBody
    List<PermissionEntity> list() {
        LoginUser currUser =  UserUtil.getLoginUser();
        QueryWrapper<PermissionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.orderByAsc("order_no");
//                .orderByDesc("create_time");
        //查询数据
        return permissionService.list(wrapper);
    }

    @GetMapping("/edit")
    String edit(Model model, Long id) {
        if (id != null) {
            PermissionEntity sysMenu = permissionService.getById(id);
            model.addAttribute("menu", sysMenu);
            model.addAttribute("parentMenu", new PermissionEntity());

            if (sysMenu != null) {
                Long parentId = sysMenu.getParentId();
                if (parentId != null) {
                    PermissionEntity parentMenu = permissionService.getById(parentId);
                    if (parentMenu != null) {
                        model.addAttribute("parentMenu", parentMenu);
                    }
                }
            }
        }

        return "admin/sys/menu/menuEdit";
    }

    @GetMapping("/add")
    String add(Model model, Long parentId) {
        if (parentId != null) {
            PermissionEntity sysMenu = permissionService.getById(parentId);
            model.addAttribute("parentMenu", sysMenu);
        } else {
            model.addAttribute("parentMenu", new PermissionEntity());
        }

        // 获取排序号，最末节点排序号+30
        Integer maxOrderNo = permissionService.getMaxOrderNo(parentId);
        if (maxOrderNo != null) {
            model.addAttribute("maxOrderNo", maxOrderNo + 30);
        } else {
            model.addAttribute("maxOrderNo", 30);
        }

        return "admin/sys/menu/menuAdd";
    }

    @PostMapping("/save")
    @ResponseBody
    MsgResult save(PermissionEntity menu) {
        try {
            if (menu.getId() == null) {
//                menu.setCreateBy(ShiroUtils.getUserId());
//                menu.setCreateDate(new Date());
                permissionService.save(menu);
            } else {
                PermissionEntity oldMenu = permissionService.getById(menu.getId());
                oldMenu.setName(menu.getName());
                oldMenu.setParentId(menu.getParentId());
                oldMenu.setIcon(menu.getIcon());
                oldMenu.setType(menu.getType());
                oldMenu.setHref(menu.getHref());
                oldMenu.setPermission(menu.getPermission());
                oldMenu.setStatus(menu.getStatus());
                oldMenu.setOrderNo(menu.getOrderNo());
//                oldMenu.setUpdateBy(ShiroUtils.getUserId());
//                oldMenu.setUpdateDate(new Date());

                permissionService.updateById(oldMenu);
            }
            return MsgResult.ok();
        } catch (Exception e) {
            logger.error("保存菜单失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }

    }

    @PostMapping("/saveSort")
    @ResponseBody
    MsgResult saveSort(Long[] ids, Integer[] orderNos) {
        try {
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    PermissionEntity menu = new PermissionEntity();
                    menu.setId(ids[i]);
                    menu.setOrderNo(orderNos[i]);
                    permissionService.updateById(menu);
                }
            }
            return MsgResult.ok();
        } catch (Exception e) {
            logger.error("保存菜单失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        if (permissionService.deleteMenu(id) > 0) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 菜单树形数据结构
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        QueryWrapper<PermissionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.eq("type", ConstUtils.MENU_TYPE_MENU);
//        wrapper.orderByAsc("sort").orderByDesc("create_time");
        List<PermissionEntity> list = permissionService.list(wrapper);
        if (list != null && list.size() > 0) {
            for (PermissionEntity e : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                mapList.add(map);
            }
        }

        return mapList;
    }
}
