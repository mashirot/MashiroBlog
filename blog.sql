/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.2.101:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 22/08/2023 05:23:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
                          `id` bigint NOT NULL,
                          `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `password` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `profile` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `admin_username_uq`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
                            `id` bigint NOT NULL,
                            `author_id` bigint NOT NULL,
                            `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `comment_count` int NOT NULL DEFAULT 0,
                            `is_delete` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:正常 1:删除',
                            `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `article_author_id_index`(`author_id` ASC) USING BTREE,
                            INDEX `article_author_id_is_delete_create_time_index`(`author_id` ASC, `is_delete` ASC, `create_time` ASC) USING BTREE,
                            INDEX `article_author_id_is_delete_index`(`author_id` ASC, `is_delete` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category`  (
                                     `id` bigint NOT NULL,
                                     `article_id` bigint NOT NULL,
                                     `category_id` bigint NOT NULL,
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE INDEX `article_category_article_id_category_id_uindex`(`article_id` ASC, `category_id` ASC) USING BTREE,
                                     UNIQUE INDEX `article_category_category_id_article_id_uindex`(`category_id` ASC, `article_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
                                `id` bigint NOT NULL,
                                `article_id` bigint NOT NULL,
                                `tag_id` bigint NOT NULL,
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE INDEX `article_tag_article_id_tag_id_uindex`(`article_id` ASC, `tag_id` ASC) USING BTREE,
                                UNIQUE INDEX `article_tag_tag_id_article_id_uindex`(`tag_id` ASC, `article_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
                             `id` bigint NOT NULL,
                             `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `category_uq`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
                            `id` bigint NOT NULL,
                            `article_id` bigint NOT NULL,
                            `sender_nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `sender_email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `receiver_nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                            `reply_comment_id` bigint NULL DEFAULT NULL,
                            `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `sender_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `status` tinyint UNSIGNED NOT NULL COMMENT '0:正常 1:待审核',
                            `is_secret` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:普通 1:私密',
                            `is_delete` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:普通 1:删除',
                            `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `comment_article_id_index`(`article_id` ASC) USING BTREE,
                            INDEX `comment_article_id_status_is_delete_index`(`article_id` ASC, `status` ASC, `is_delete` ASC) USING BTREE COMMENT '指定文章, 状态, 是否删除',
                            INDEX `comment_article_id_status_is_secret_is_delete_index`(`article_id` ASC, `status` ASC, `is_secret` ASC, `is_delete` ASC) USING BTREE COMMENT '指定文章, 状态, 私密, 是否删除',
                            INDEX `comment_is_delete_index`(`is_delete` ASC) USING BTREE COMMENT '所以已删除评论索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
                        `id` bigint NOT NULL,
                        `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE INDEX `tag_name_uq`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for sys_info
-- ----------------------------
DROP VIEW IF EXISTS `sys_info`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `sys_info` AS select `t1`.`nickname` AS `nickname`,`t1`.`email` AS `email`,`t1`.`profile` AS `profile`,`t2`.`count` AS `count`,`t3`.`count` AS `count`,`t4`.`count` AS `count`,`t5`.`count` AS `count`,`t6`.`count` AS `count`,`t7`.`run_day` AS `run_day` from (((((((select `admin`.`nickname` AS `nickname`,`admin`.`email` AS `email`,`admin`.`profile` AS `profile` from `admin`) `t1` join (select count(0) AS `count` from `article` where (`article`.`is_delete` = 0)) `t2`) join (select count(0) AS `count` from `comment` where ((`comment`.`is_delete` = 0) and (`comment`.`status` = 0))) `t3`) join (select count(0) AS `count` from `comment` where ((`comment`.`is_delete` = 0) and (`comment`.`status` = 1))) `t4`) join (select count(0) AS `count` from `category`) `t5`) join (select count(0) AS `count` from `tag`) `t6`) join (select (to_days(now()) - to_days(`admin`.`create_time`)) AS `run_day` from `admin`) `t7`);

SET FOREIGN_KEY_CHECKS = 1;
