package com.cat.origin.modules.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * <p>
 * 树形选择控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-11
 */
@Controller
@RequestMapping("/admin/treeData")
public class TreeDataController {

    @RequestMapping("singleSelect")
    String singleSelect(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        return "admin/treedata/singleSelect";
    }

    @RequestMapping("iconSelect")
    String iconSelect(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        return "admin/treedata/iconSelect";
    }
}
