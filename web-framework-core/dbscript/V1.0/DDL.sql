DROP TABLE IF EXISTS `t_user`;
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(32) NOT NULL COMMENT '用户名（登录账号）',
  `password` varchar(100) NOT NULL COMMENT '用户密码',
  `nickname` varchar(60) DEFAULT NULL COMMENT '昵称',
  `mobile_phone` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `status` tinyint(1) UNSIGNED NOT NULL COMMENT '状态：0禁用；1启用；',
  `is_deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否已被删除：1是；0否；',
  `create_by` bigint(20) UNSIGNED NOT NULL COMMENT '创建人',
  `create_tm` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) UNSIGNED NOT NULL COMMENT '最近更新人',
  `update_tm` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `version` int(10) UNSIGNED NOT NULL DEFAULT '1' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) USING BTREE,
  KEY `idx_updatetm` (`update_tm`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';


DROP TABLE IF EXISTS `t_role`;
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_code` varchar(30) NOT NULL COMMENT '角色代码：英文数字下划线组成',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `status` tinyint(1) UNSIGNED NOT NULL COMMENT '状态：0无效；1有效；',
  `create_by` bigint(20) UNSIGNED NOT NULL COMMENT '创建人',
  `create_tm` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) UNSIGNED NOT NULL COMMENT '最近更新人',
  `update_tm` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `version` int(10) UNSIGNED NOT NULL DEFAULT '1' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`) USING BTREE,
  KEY `idx_rolename` (`role_name`),
  KEY `idx_updatetm` (`update_tm`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色表';


DROP TABLE IF EXISTS `t_permit`;
CREATE TABLE IF NOT EXISTS `t_permit` (
  `permit_id` varchar(50) NOT NULL COMMENT '权限编号，英文数字下划线组成',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父权限编号：没有父权限时为空',
  `permit_name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `permit_icon` varchar(512) DEFAULT NULL COMMENT '权限图标',
  `permit_type` tinyint(3) UNSIGNED NOT NULL COMMENT '类型：1菜单目录；2页面按钮；',
  `permit_url` varchar(512) DEFAULT NULL COMMENT '权限url，相对根路径的地址',
  `orders` tinyint(3) UNSIGNED NOT NULL COMMENT '排序序号',
  `status` tinyint(1) UNSIGNED NOT NULL COMMENT '状态：0禁用；1启用；',
  `create_by` bigint(20) UNSIGNED NOT NULL COMMENT '创建人',
  `create_tm` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) UNSIGNED NOT NULL COMMENT '最近更新人',
  `update_tm` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `version` int(10) UNSIGNED NOT NULL DEFAULT '1' COMMENT '版本号',
  PRIMARY KEY `pk_permit_id` (`permit_id`) USING BTREE,
  KEY `idx_parentid` (`parent_id`) USING BTREE,
  KEY `idx_updatetm` (`update_tm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

/*DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE IF NOT EXISTS `t_resource` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rcode` varchar(50) NOT NULL COMMENT '资源编号，英文数字下划线组成',
  `pcode` varchar(50) DEFAULT NULL COMMENT '父资源编号：没有父资源时为空',
  `name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `icon` varchar(512) DEFAULT NULL COMMENT '资源图标',
  `type` tinyint(3) UNSIGNED NOT NULL COMMENT '类型：1菜单目录；2页面按钮；',
  `url` varchar(512) DEFAULT NULL COMMENT '资源url，相对根路径的地址',
  `orders` tinyint(3) UNSIGNED NOT NULL COMMENT '排序序号',
  `status` tinyint(1) UNSIGNED NOT NULL COMMENT '状态：0禁用；1启用；',
  `create_by` bigint(20) UNSIGNED NOT NULL COMMENT '创建人',
  `create_tm` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) UNSIGNED NOT NULL COMMENT '最近更新人',
  `update_tm` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `version` int(10) UNSIGNED NOT NULL DEFAULT '1' COMMENT '版本号',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rcode` (`rcode`) USING BTREE,
  KEY `idx_pcode` (`pcode`) USING BTREE,
  KEY `idx_updatetm` (`update_tm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源表';*/


DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `idx_roleid` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';


DROP TABLE IF EXISTS `t_role_permit`;
CREATE TABLE IF NOT EXISTS `t_role_permit` (
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色ID',
  `permit_id` varchar(50) NOT NULL COMMENT '权限编码',
  PRIMARY KEY (`role_id`,`permit_id`),
  KEY `idx_permitid` (`permit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';




-- -----------------------------------------------------
DROP TABLE IF EXISTS `t_register_contention`;
CREATE TABLE IF NOT EXISTS `t_register_contention` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `attr` varchar(10) NOT NULL COMMENT 'attr',
  `ckey` varchar(20) NOT NULL COMMENT 'key',
  `cval` varchar(20) NOT NULL COMMENT 'value',
  PRIMARY KEY (`id`), 
  UNIQUE KEY `uk_attr_ckey` (`attr`,`ckey`) USING BTREE,
  UNIQUE KEY `uk_attr_cval` (`attr`,`cval`) USING BTREE 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源注册抢占表';

DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment` (
  `id`             bigint(20)   unsigned     NOT NULL AUTO_INCREMENT  COMMENT '自增主键ID',
  `user_id`        bigint(20)   unsigned     NOT NULL                 COMMENT '用户ID',
  `order_id`       varchar(30)               NOT NULL                 COMMENT '支付ID',
  `order_no`       varchar(60)                         DEFAULT NULL   COMMENT '支付业务单号',
  `order_amt`      bigint(20)   unsigned     NOT NULL                 COMMENT '订单金额（最小单位分）',
  `biz_type`       smallint(5)  unsigned     NOT NULL                 COMMENT '业务类型：1大屏；',
  `status`         smallint(5)  unsigned     NOT NULL  DEFAULT '0'    COMMENT '支付状态：0待买家付款；1支付成功；2支付失败；3待卖家收款；4交易关闭；',
  `channel_type`   varchar(30)                         DEFAULT NULL   COMMENT '支付渠道类型',
  `sfp_order_id`   varchar(60)                         DEFAULT NULL   COMMENT '顺手付单号',
  `third_order_no` varchar(60)                         DEFAULT NULL   COMMENT '第三方单号',
  `bank_id`        varchar(60)                         DEFAULT NULL   COMMENT '银行流水号',
  `source`         varchar(20)                         DEFAULT NULL   COMMENT '来源：PC；H5；APP；',
  `client_ip`      varchar(50)                         DEFAULT NULL   COMMENT '客户端IP',
  `extra_data`     varchar(255)                        DEFAULT NULL   COMMENT '附加数据',
  `remark`         varchar(255)                        DEFAULT NULL   COMMENT '备注',
  `retry_count`    smallint(5)  unsigned     NOT NULL  DEFAULT '0'    COMMENT '重试次数（手动支付查询）',
  `trade_tm`       datetime                            DEFAULT NULL   COMMENT '支付时间',
  `create_tm`      datetime                  NOT NULL                 COMMENT '创建时间',
  `update_tm`      datetime                  NOT NULL                 COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orderid` (`order_id`),
  KEY `idx_userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '支付表';

DROP TABLE IF EXISTS `t_refund`;
CREATE TABLE `t_refund` (
  `id`             bigint(20)   unsigned      NOT NULL AUTO_INCREMENT  COMMENT '自增主键ID',
  `user_id`        bigint(20)   unsigned      NOT NULL                 COMMENT '用户ID',
  `refund_id`      varchar(30)                NOT NULL                 COMMENT '退款ID',
  `order_id`       varchar(30)                NOT NULL                 COMMENT '支付ID',
  `refund_no`      varchar(60)                          DEFAULT NULL   COMMENT '退款业务单号',
  `refund_amt`     bigint(20)   unsigned      NOT NULL                 COMMENT '退款金额（最小单位分）',
  `refund_rate`    decimal(5,2) unsigned      NOT NULL  DEFAULT 100    COMMENT '退款费率（百分数）',
  `handle_fee`     bigint(20)   unsigned      NOT NULL  DEFAULT 0      COMMENT '退款手续费',
  `refund_des`     varchar(255)                         DEFAULT NULL   COMMENT '退款描述',
  `status`         smallint(5)  unsigned      NOT NULL  DEFAULT 0      COMMENT '退款状态：0退款处理中；1退款完成；2退款失败；',
  `sfp_refund_id`  varchar(60)                          DEFAULT NULL   COMMENT '顺手付退款单号',
  `bank_refund_id` varchar(60)                          DEFAULT NULL   COMMENT '第三方退款单号',
  `remark`         varchar(255)                         DEFAULT NULL   COMMENT '备注',
  `trade_tm`       datetime                             DEFAULT NULL   COMMENT '交易时间',
  `retry_count`    smallint(5)  unsigned      NOT NULL  DEFAULT 0      COMMENT '重试次数（手动退款查询）',
  `create_tm`      datetime                   NOT NULL                 COMMENT '创建时间',
  `update_tm`      datetime                   NOT NULL                 COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refundid` (`refund_id`),
  KEY `idx_orderid` (`order_id`),
  KEY `idx_userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '退款表';














DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(255) NOT NULL COMMENT '子编码',
  `pode` varchar(255) DEFAULT NULL COMMENT '父编码',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `orders` int(6) UNSIGNED DEFAULT 0 COMMENT '排序',
  `create_tm` datetime DEFAULT NULL COMMENT '创建时间',
  `update_tm` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) UNSIGNED DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) UNSIGNED DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`), 
  UNIQUE KEY `uk_code` (`code`) USING BTREE 
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '字典表';


DROP TABLE IF EXISTS `t_ops_log`;
CREATE TABLE `t_ops_log` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `ops_user` varchar(60) DEFAULT NULL COMMENT '操作人',
  `ops_type` int(8) UNSIGNED DEFAULT 0 COMMENT '操作类型',
  `url` varchar(1024) DEFAULT NULL COMMENT '访问地址',
  `client_ip` varchar(127) DEFAULT NULL COMMENT '客户端IP',
  `log_tm` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_logtm` (`log_tm`)
) ENGINE = InnoDB COMMENT = '操作日志';

