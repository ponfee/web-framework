-- ----------------------------------------------------------------管理员
-- 密码：admin123
INSERT INTO `t_user` (`id`, `username`, `password`, `nickname`, `mobile_phone`, `status`, `is_deleted`, `create_by`, `update_by`) VALUES ('1', 'admin', '$2a$02$3uoCD_x-v8Dki_P2NDom6A$4C_dITGOhikwqA2H8cE4rJlRuVb3v8Rj', '管理员', '13418467597', '1', '0', '1', '1');
-- 密码: khpsw123
INSERT INTO `t_user` (`id`, `username`, `password`, `nickname`, `mobile_phone`, `status`, `is_deleted`, `create_by`, `update_by`) VALUES ('2', 'administrator', '$2a$02$4ANcOlUb5fusSFYkWHfJnA$FPrOYQTCQFBHEXPEupsMyVPR8tk16OO0', '管理员', '13418467597', '1', '0', '1', '1');


-- 新增角色
INSERT INTO `t_role` (`id`, `role_code`, `role_name`, `status`, `create_by`, `update_by`) VALUES ('1', 'MANAGER', '管理员', '1', '1', '1');


-- 给用户配置角色
INSERT INTO `t_user_role` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `t_user_role` (`user_id`, `role_id`) VALUES ('2', '1');


-- 新增权限
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('000000', NULL, '全部权限', NULL, '1', '/**', '1', '1', '1', '1');


-- 给角色配置权限
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('1', '000000');


-- ----------------------------------------------------------------普通用户
-- 密码：test123
INSERT INTO `t_user` (`id`, `username`, `password`, `nickname`, `mobile_phone`, `status`, `is_deleted`, `create_by`, `update_by`) VALUES ('3',  'test', '$2a$02$kkqqim-dh6DkboyAjlbAbA$8TXxHsMWVeZMav4UHWSvyGEPqrb_Sw3z', '测试账号', '13418467597', '1', '0', '1', '1');
INSERT INTO `t_role` (`id`, `role_code`, `role_name`, `status`, `create_by`, `update_by`) VALUES ('2', 'GENERAL', '普通用户', '1', '1', '1');
INSERT INTO `t_user_role` (`user_id`, `role_id`) VALUES ('3', '2');


-- ----------------------------------------------------------------后台管理
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('100000', NULL,     '用户中心',     NULL, '1', '/page/usercenter.html', '0', '1', '1', '1');
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('100001', '100000', '用户信息修改', NULL, '2', '/user/ops/modifyinfo',  '1', '1', '1', '1');
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('100002', '100000', '姓名修改',     NULL, '2', '/user/ops/modifyname',  '2', '1', '1', '1');
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('100003', '100000', '手机号修改',   NULL, '2', '/user/ops/modifyphone', '3', '1', '1', '1');
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('100004', '100000', '密码修改',     NULL, '2', '/user/ops/modifypwd',   '4', '1', '1', '1');
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('100005', '100000', '查看我的角色', NULL, '2', '/user/ops/myrole',      '6', '1', '1', '1');
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('100006', '100000', '查看我的信息', NULL, '2', '/user/ops/myinfo',      '8', '1', '1', '1');
INSERT INTO `t_permit` (`permit_id`, `parent_id`, `permit_name`, `permit_icon`, `permit_type`, `permit_url`, `orders`, `status`, `create_by`, `update_by`) VALUES ('100007', '100000', '查看我的权限', NULL, '2', '/user/ops/mypermits',   '9', '1', '1', '1');
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('2', '100000');
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('2', '100001');
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('2', '100002');
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('2', '100003');
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('2', '100004');
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('2', '100005');
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('2', '100006');
INSERT INTO `t_role_permit` (`role_id`, `permit_id`) VALUES ('2', '100007');
