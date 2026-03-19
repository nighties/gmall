-- 美团小程序数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS gmall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gmall;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码 (加密)',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像 URL',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `gender` tinyint(4) DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
  `points` int(11) DEFAULT 0 COMMENT '积分',
  `level` int(11) DEFAULT 1 COMMENT '会员等级',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品 ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '价格 (分)',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `stock` int(11) DEFAULT 0 COMMENT '库存',
  `sales` int(11) DEFAULT 0 COMMENT '销量',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类 ID',
  `shop_id` bigint(20) DEFAULT NULL COMMENT '商家 ID',
  `images` text COMMENT '商品图片 (JSON 数组)',
  `specs` text COMMENT '商品规格 (JSON)',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态 0-下架 1-上架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 分类表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类 ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父分类 ID',
  `level` int(11) DEFAULT 1 COMMENT '层级',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 购物车表
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物车 ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品 ID',
  `spec_id` bigint(20) DEFAULT NULL COMMENT '规格 ID',
  `quantity` int(11) NOT NULL DEFAULT 1 COMMENT '数量',
  `checked` tinyint(4) DEFAULT 1 COMMENT '是否选中 0-否 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单 ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
  `freight_amount` decimal(10,2) DEFAULT 0 COMMENT '运费',
  `discount_amount` decimal(10,2) DEFAULT 0 COMMENT '优惠金额',
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '优惠券 ID',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '订单状态 0-待付款 1-待发货 2-待收货 3-已完成 4-已取消',
  `remark` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单商品表
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单商品 ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单 ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品 ID',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `quantity` int(11) NOT NULL COMMENT '商品数量',
  `spec_info` varchar(255) DEFAULT NULL COMMENT '规格信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- 收货地址表
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL COMMENT '地址 ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `province` varchar(50) DEFAULT NULL COMMENT '省',
  `city` varchar(50) DEFAULT NULL COMMENT '市',
  `district` varchar(50) DEFAULT NULL COMMENT '区',
  `detail` varchar(255) NOT NULL COMMENT '详细地址',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `is_default` tinyint(4) DEFAULT 0 COMMENT '是否默认 0-否 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 优惠券表
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon` (
  `id` bigint(20) NOT NULL COMMENT '优惠券 ID(使用雪花算法生成)',
  `name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `type` tinyint(4) NOT NULL COMMENT '类型 1-满减 2-折扣',
  `amount` decimal(10,2) NOT NULL COMMENT '金额 (满减时有效)',
  `discount` decimal(3,2) DEFAULT NULL COMMENT '折扣 (折扣时有效)',
  `min_purchase` decimal(10,2) DEFAULT NULL COMMENT '最低消费',
  `max_discount` decimal(10,2) DEFAULT NULL COMMENT '最高抵扣 (折扣时有效)',
  `total_count` int(11) DEFAULT 0 COMMENT '发放总数',
  `remain_count` int(11) DEFAULT 0 COMMENT '剩余数量',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券表
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon` (
  `id` bigint(20) NOT NULL COMMENT '用户优惠券 ID(使用雪花算法生成)',
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `coupon_id` bigint(20) NOT NULL COMMENT '优惠券 ID',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0-未使用 1-已使用 2-已过期',
  `order_id` bigint(20) DEFAULT NULL COMMENT '使用的订单 ID',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- 商品收藏表
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏 ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品 ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品收藏表';

-- 商品评价表
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价 ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品 ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单 ID',
  `score` int(11) NOT NULL COMMENT '评分 1-5',
  `content` text COMMENT '评价内容',
  `images` text COMMENT '评价图片 (JSON 数组)',
  `reply` text COMMENT '商家回复',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态 0-隐藏 1-显示',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评价表';

-- 商家表
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `id` bigint(20) NOT NULL COMMENT '商家 ID',
  `name` varchar(100) NOT NULL COMMENT '商家名称',
  `logo` varchar(255) DEFAULT NULL COMMENT '商家 Logo',
  `description` text COMMENT '商家描述',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) DEFAULT NULL COMMENT '商家地址',
  `latitude` decimal(10,8) DEFAULT NULL COMMENT '纬度',
  `longitude` decimal(11,8) DEFAULT NULL COMMENT '经度',
  `business_hours` varchar(50) DEFAULT NULL COMMENT '营业时间',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态 0-休息 1-营业中',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家表';

-- 用户积分明细表
DROP TABLE IF EXISTS `user_point_history`;
CREATE TABLE `user_point_history` (
  `id` bigint(20) NOT NULL COMMENT '积分明细 ID(使用雪花算法生成)',
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `points` int(11) NOT NULL COMMENT '积分变化数量',
  `change_type` tinyint(4) NOT NULL COMMENT '变更类型 1-签到 2-购物 3-评价 4-退货 5-其他',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_change_type` (`change_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户积分明细表';

-- 插入测试数据
-- 分类数据
INSERT INTO `category` (`name`, `parent_id`, `level`, `sort`) VALUES
('美食', 0, 1, 1),
('外卖', 0, 1, 2),
('酒店', 0, 1, 3),
('汉堡', 1, 2, 1),
('奶茶', 1, 2, 2),
('pizza', 1, 2, 3);

-- 商家数据
INSERT INTO `shop` (`name`, `logo`, `description`, `phone`, `address`, `business_hours`, `status`) VALUES
('麦当劳', 'https://placeholder.com/shop1.png', '全球知名快餐连锁', '400-123-4567', '北京市朝阳区', '09:00-22:00', 1),
('肯德基', 'https://placeholder.com/shop2.png', '炸鸡汉堡专家', '400-987-6543', '北京市海淀区', '09:00-22:00', 1),
('星巴克', 'https://placeholder.com/shop3.png', '高品质咖啡', '400-555-6666', '北京市东城区', '08:00-23:00', 1);

-- 用户数据 (密码为 123456 的加密)
INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `points`, `level`) VALUES
('test', '$2a$10$XoLvF5C2dz9.7JHK8sN.TOxl9yYp4CqQKQ8MmqPHAKQYYKZKQJIZe', '测试用户', '13800138000', 100, 1);
