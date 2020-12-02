package com.cat.origin.modules.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cat.origin.common.result.MsgResult;
import com.cat.origin.common.result.PageResult;
import com.cat.origin.common.utils.ConstUtils;
import com.cat.origin.common.utils.StringUtils;
import com.cat.origin.exception.BusinessException;
import com.cat.origin.modules.entity.RoleEntity;
import com.cat.origin.modules.entity.UserEntity;
import com.cat.origin.modules.service.RoleService;
import com.cat.origin.modules.service.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 系统管理-用户基础信息表 前端控制器
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Api(tags = "用户")
@Controller
@RequestMapping("/admin/sys/user")
public class UserController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("userList", null);
        return "admin/sys/user/userList";
    }

    @RequestMapping("/list")
    @ResponseBody
    PageResult list(@RequestParam Map<String, String> params, Integer pageNo, Integer pageSize) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        if (StringUtils.isNotBlank(params.get("username"))) {
            wrapper.like("username", params.get("username"));
        }
        if (StringUtils.isNotBlank(params.get("nickname"))) {
            wrapper.like("nickname", params.get("nickname"));
        }
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
        IPage<UserEntity> userPage = new Page<>(pageNo, pageSize);
        userPage = userService.page(userPage, wrapper);
        return new PageResult(userPage.getRecords(), userPage.getTotal());
    }

    @GetMapping("/edit")
    String edit(Model model, Long id) {
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.eq("status", ConstUtils.YES);
        wrapper.orderByDesc("create_time");
        List<RoleEntity> allRoles = roleService.list(wrapper);
        model.addAttribute("allRoles", allRoles);

        if (id != null) {
            UserEntity sysUser = userService.getById(id);
            model.addAttribute("user", sysUser);

            List<RoleEntity> hasRoles = roleService.selectUserRoles(id);
            model.addAttribute("hasRoles", hasRoles);
            if (allRoles != null && allRoles.size() > 0 && hasRoles != null && hasRoles.size() > 0) {
                for (RoleEntity role : allRoles) {
                    for (RoleEntity hasRole : hasRoles) {
                        if (hasRole.getId().equals(role.getId())) {
                            role.setExtend("checked");
                            break;
                        }
                    }
                }
            }
        }


        return "admin/sys/user/userEdit";
    }

    @GetMapping("/info")
    String info(Model model) {
        UserEntity user = userService.getById(9L);
        model.addAttribute("user", user);

        return "admin/sys/user/infoEdit";
    }

    @GetMapping("/add")
    String add(Model model) {
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.eq("status", ConstUtils.YES);
        wrapper.orderByDesc("create_time");
        List<RoleEntity> allRoles = roleService.list(wrapper);
        model.addAttribute("allRoles", allRoles);
        return "admin/sys/user/userAdd";
    }

    @PostMapping("/save")
    @ResponseBody
    MsgResult save(UserEntity user, String roleIds) {
        user.setDelFlag(0);
        try {
            if (user.getId() == null) {
                user.setCreateId(9L);
                user.setCreateTime(new Date());
            }
            user.setUpdateId(9L);
            user.setUpdateTime(new Date());
            if (userService.saveOrUpdateUser(user, roleIds)) {
                return MsgResult.ok();
            }
        } catch (BusinessException e) {
            logger.error("保存用户信息失败：{}", e.getMessage());
            return MsgResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error("保存用户信息失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }

        return MsgResult.error();
    }

    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        if (1 == id) {
            return MsgResult.error("不允许删除管理员");
        }
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setDelFlag(ConstUtils.DEL_FLAG_DELETE);
        if (userService.updateById(user)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    @PostMapping("/resetPwd")
    @ResponseBody
    MsgResult resetPwd(Long id) {
        if (1 == id) {
            return MsgResult.error("不允许重置管理员密码");
        }
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setPassword(passwordEncoder.encode("1"));
        if (userService.updateById(user)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 验证登录名是否有效
     * @param oldLoginName 老的登录名
     * @param loginName 新的登录名
     */
    @ResponseBody
    @RequestMapping(value = "checkLoginNameAjax")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName !=null && userService.getByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    @GetMapping("/pwd")
    String pwd(Model model) {
        return "admin/sys/user/pwdUpdate";
    }

    @PostMapping("/updatePwd")
    @ResponseBody
    MsgResult updatePwd(String oldPassword, String newPassword, String reNewPassword) {
        if (StringUtils.isBlank(oldPassword)) {
            return MsgResult.error("旧密码不能为空");
        }
        if (StringUtils.isBlank(newPassword)) {
            return MsgResult.error("新密码不能为空");
        }
        if (!newPassword.equals(reNewPassword)) {
            return MsgResult.error("确认密码与新密码不一致");
        }
        UserEntity user = userService.getById(9);
        if (user == null) {
            return MsgResult.error("登录已超时");
        }
        // 密码错误
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return MsgResult.error("旧密码不正确");
        }

        //更新密码
        UserEntity updateUser = new UserEntity();
        updateUser.setId(user.getId());
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        if (userService.updateById(updateUser)) {
            return MsgResult.ok();
        }

        return MsgResult.error();
    }
}
