
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `type` varchar(16) COLLATE utf8_bin NOT NULL,
                          `k` varchar(16) COLLATE utf8_bin NOT NULL,
                          `val` varchar(64) COLLATE utf8_bin NOT NULL,
                          `createTime` datetime NOT NULL,
                          `updateTime` datetime NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `type` (`type`,`k`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
                         `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                         `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口名称',
                         `module` varchar(50) DEFAULT NULL COMMENT '模块名',
                         `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口地址',
                         `ip` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '访问人IP',
                         `user_id` int(11) DEFAULT '0' COMMENT '访问人ID 0:未登录用户操作',
                         `status` int(2) DEFAULT '1' COMMENT '访问状态',
                         `execute_time` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口执行时间',
                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                         `create_id` int(10) DEFAULT NULL,
                         `update_id` int(10) DEFAULT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1699 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统管理 - 日志表';


DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `parent_id` int(11) NOT NULL COMMENT '父级id',
                                `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '权限名称',
                                `css` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '样式',
                                `href` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '菜单链接',
                                `type` tinyint(1) NOT NULL COMMENT '类型 1-菜单 2-权限/按钮',
                                `permission` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '权限位置',
                                `order_no` int(11) NOT NULL COMMENT '排序',
                                `icon` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '图标',
                                `status` tinyint(1) DEFAULT NULL COMMENT '状态 0-隐藏 1-显示',
                                `del_flag` tinyint(1) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


INSERT INTO `sys_permission` VALUES ('1', '0', '用户管理', 'fa-users', '', '1', '', '1', 'fa-users', '1', '1');
INSERT INTO `sys_permission` VALUES ('2', '7', '用户', 'fa-user', '/admin/sys/user/', '1', '', '2', 'fa-user', '1', '0');
INSERT INTO `sys_permission` VALUES ('3', '2', '查询', '', '', '2', 'sys:user:query', '100', null, '1', '0');
INSERT INTO `sys_permission` VALUES ('4', '2', '新增', '', '', '2', 'sys:user:add', '100', null, '1', '0');
INSERT INTO `sys_permission` VALUES ('6', '0', '修改密码', 'fa-pencil-square-o', 'pages/user/changePassword.html', '1', 'sys:user:password', '4', null, '1', '1');
INSERT INTO `sys_permission` VALUES ('7', '0', '系统设置', 'fa-gears', '', '1', '', '5', 'fa-gears', '1', '0');
INSERT INTO `sys_permission` VALUES ('8', '7', '菜单', 'fa-cog', '/admin/sys/menu/', '1', '', '6', 'fa-th-list', '1', '0');
INSERT INTO `sys_permission` VALUES ('9', '8', '查询', '', '', '2', 'sys:menu:query', '100', null, '1', '0');
INSERT INTO `sys_permission` VALUES ('10', '8', '新增', '', '', '2', 'sys:menu:add', '100', null, '1', '0');
INSERT INTO `sys_permission` VALUES ('11', '8', '删除', '', '', '2', 'sys:menu:del', '100', null, '1', '0');
INSERT INTO `sys_permission` VALUES ('12', '7', '角色', 'fa-user-secret', '/admin/sys/role/', '1', '', '7', 'fa-mortar-board', '1', '0');
INSERT INTO `sys_permission` VALUES ('13', '12', '查询', '', '', '2', 'sys:role:query', '100', null, '1', '0');
INSERT INTO `sys_permission` VALUES ('14', '12', '新增', '', '', '2', 'sys:role:add', '100', null, '1', '0');
INSERT INTO `sys_permission` VALUES ('15', '12', '删除', '', '', '2', 'sys:role:del', '100', null, '1', '0');
INSERT INTO `sys_permission` VALUES ('42', '2', '删除', '', '', '2', 'sys:user:del', '100', null, '1', '0');


DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                          `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                          `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色编码',
                          `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '角色名称',
                          `status` tinyint(1) DEFAULT NULL,
                          `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                          `create_id` int(10) DEFAULT NULL,
                          `update_id` int(10) DEFAULT NULL,
                          `del_flag` tinyint(1) DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理-角色表 ';


INSERT INTO `sys_role` VALUES ('10', null, '管理员', '1', '123', '2020-11-15 23:15:00', '2020-11-17 00:08:48', null, null, '0');
INSERT INTO `sys_role` VALUES ('11', null, '测试', '1', '', '2020-11-15 23:20:31', '2020-11-29 17:25:28', null, '9', '0');


DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
                                     `role_id` int(11) DEFAULT NULL COMMENT '角色id',
                                     `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
                                     KEY `role_id` (`role_id`) USING BTREE,
                                     KEY `permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `sys_role_permission` VALUES ('10', '0');
INSERT INTO `sys_role_permission` VALUES ('10', '1');
INSERT INTO `sys_role_permission` VALUES ('10', '2');
INSERT INTO `sys_role_permission` VALUES ('10', '3');
INSERT INTO `sys_role_permission` VALUES ('10', '4');
INSERT INTO `sys_role_permission` VALUES ('10', '42');
INSERT INTO `sys_role_permission` VALUES ('10', '6');
INSERT INTO `sys_role_permission` VALUES ('10', '7');
INSERT INTO `sys_role_permission` VALUES ('10', '8');
INSERT INTO `sys_role_permission` VALUES ('10', '9');
INSERT INTO `sys_role_permission` VALUES ('10', '10');
INSERT INTO `sys_role_permission` VALUES ('10', '11');
INSERT INTO `sys_role_permission` VALUES ('10', '12');
INSERT INTO `sys_role_permission` VALUES ('10', '13');
INSERT INTO `sys_role_permission` VALUES ('10', '14');
INSERT INTO `sys_role_permission` VALUES ('10', '15');
INSERT INTO `sys_role_permission` VALUES ('10', '16');
INSERT INTO `sys_role_permission` VALUES ('10', '17');
INSERT INTO `sys_role_permission` VALUES ('10', '18');
INSERT INTO `sys_role_permission` VALUES ('10', '19');
INSERT INTO `sys_role_permission` VALUES ('10', '20');
INSERT INTO `sys_role_permission` VALUES ('10', '21');
INSERT INTO `sys_role_permission` VALUES ('10', '22');
INSERT INTO `sys_role_permission` VALUES ('10', '23');
INSERT INTO `sys_role_permission` VALUES ('10', '24');
INSERT INTO `sys_role_permission` VALUES ('10', '25');
INSERT INTO `sys_role_permission` VALUES ('10', '26');
INSERT INTO `sys_role_permission` VALUES ('10', '27');
INSERT INTO `sys_role_permission` VALUES ('10', '28');
INSERT INTO `sys_role_permission` VALUES ('10', '29');
INSERT INTO `sys_role_permission` VALUES ('10', '30');
INSERT INTO `sys_role_permission` VALUES ('10', '31');
INSERT INTO `sys_role_permission` VALUES ('10', '32');
INSERT INTO `sys_role_permission` VALUES ('10', '33');
INSERT INTO `sys_role_permission` VALUES ('10', '34');
INSERT INTO `sys_role_permission` VALUES ('10', '35');
INSERT INTO `sys_role_permission` VALUES ('10', '36');
INSERT INTO `sys_role_permission` VALUES ('10', '37');
INSERT INTO `sys_role_permission` VALUES ('10', '38');
INSERT INTO `sys_role_permission` VALUES ('10', '39');
INSERT INTO `sys_role_permission` VALUES ('10', '40');
INSERT INTO `sys_role_permission` VALUES ('11', '7');
INSERT INTO `sys_role_permission` VALUES ('11', '2');
INSERT INTO `sys_role_permission` VALUES ('11', '3');
INSERT INTO `sys_role_permission` VALUES ('11', '4');
INSERT INTO `sys_role_permission` VALUES ('11', '42');


DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
                               `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `role_id` int(10) DEFAULT NULL COMMENT '角色ID',
                               `user_id` int(10) DEFAULT NULL COMMENT '用户ID',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理 - 用户角色关联表 ';


INSERT INTO `sys_role_user` VALUES ('33', '10', '9');
INSERT INTO `sys_role_user` VALUES ('36', '11', '13');
INSERT INTO `sys_role_user` VALUES ('37', '10', '13');
INSERT INTO `sys_role_user` VALUES ('45', '11', '14');


DROP TABLE IF EXISTS `sys_token`;
CREATE TABLE `sys_token` (
                           `id` varchar(36) COLLATE utf8_bin NOT NULL COMMENT 'token',
                           `val` text COLLATE utf8_bin NOT NULL COMMENT 'LoginUser的json串',
                           `expireTime` datetime NOT NULL,
                           `createTime` datetime NOT NULL,
                           `updateTime` datetime NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                          `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                          `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
                          `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录密码',
                          `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
                          `sex` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别 0:男 1:女',
                          `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号码',
                          `telephone` varchar(11) DEFAULT NULL,
                          `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
                          `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
                          `birthday` datetime DEFAULT NULL,
                          `status` int(1) DEFAULT NULL COMMENT '状态',
                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                          `create_id` int(10) DEFAULT NULL,
                          `update_id` int(10) DEFAULT NULL,
                          `del_flag` tinyint(1) DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统管理-用户基础信息表';


INSERT INTO `sys_user` VALUES ('9', 'admin', '$2a$10$k07zWhPyB/fE.UWsC4VeceuBh5Z0lE7t77cFiI0BFhVB6cnrbC.Ou', 'admin', null, '', '', '123@qq.ccom', null, '2020-11-16 00:00:00', '1', '2020-11-15 23:53:05', '2020-11-22 12:14:58', null, '9', '0');
INSERT INTO `sys_user` VALUES ('14', 'admin123', '$2a$10$PvOplFv8IN2XHKMusS6CSuA3Pex5Tn9OmGmd6FXMDuB/YoC4fRIA2', 'admin123', null, '', null, '123@qq.ccom', null, null, '1', '2020-11-22 12:17:15', '2020-11-23 21:44:10', '9', '9', '0');
