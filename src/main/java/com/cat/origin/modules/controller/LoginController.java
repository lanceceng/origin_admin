package com.cat.origin.modules.controller;


import com.cat.origin.modules.dto.LoginUser;
import com.cat.origin.modules.entity.PermissionEntity;
import com.cat.origin.modules.entity.UserEntity;
import com.cat.origin.modules.service.PermissionService;
import com.cat.origin.utils.CookieUtils;
import com.cat.origin.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 系统管理 - 日志表 前端控制器
 * </p>
 *
 * @author cat
 * @since 2020-11-15
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionService permissionService;

//    @Autowired
//    public LoginController(ISysMenuService sysMenuService) {
//        this.sysMenuService = sysMenuService;
//    }

    @GetMapping({"/", "", "/index"})
    String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginUser currUser =  UserUtil.getLoginUser();
//            SysUser sysUser = ShiroUtils.getUser();
//            if (sysUser == null) {
//                return "admin/login";
//            }


            System.out.println(currUser);
            Long userId = 9L;
//            //刷新首页/重新登录清除缓存
//            CacheUtils.removeSysCache("menus_" + userId);
//            CacheUtils.removeSysCache("perms_" + userId);

            HttpSession session = request.getSession();
            session.setAttribute("s_sysUser", new UserEntity(9L, "admin"));
            List<PermissionEntity> menuList = permissionService.listUserMenu(userId);
            session.setAttribute("menuList", menuList);

            CookieUtils.setCookie(response, "menu_p", "");
            CookieUtils.setCookie(response, "menu_c", "");
        } catch (Exception e) {
            logger.error("加载首页数据失败:{}", e.getMessage());
        }

        return "admin/index";
    }

    /*@GetMapping("/quit")
    String quit() {
        ShiroUtils.logout();
        return "redirect:login";
    }*/

    @GetMapping("/login")
    String login() {
        return "admin/login";
    }

    @GetMapping("/main")
    String main() {
        return "admin/main";
    }

    @GetMapping("/403")
    String error403() {
        return "admin/403";
    }
}
