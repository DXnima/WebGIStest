/*
 Navicat Premium Data Transfer

 Source Server         : PostgreSQL
 Source Server Type    : PostgreSQL
 Source Server Version : 120004
 Source Host           : localhost:5432
 Source Catalog        : webgistest
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120004
 File Encoding         : 65001

 Date: 01/06/2021 22:55:20
*/


-- ----------------------------
-- Table structure for gx_city
-- ----------------------------
DROP TABLE IF EXISTS "public"."gx_city";
CREATE TABLE "public"."gx_city" (
  "id" int4 NOT NULL DEFAULT nextval('gx_city_id_seq'::regclass),
  "name" varchar(20) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of gx_city
-- ----------------------------
INSERT INTO "public"."gx_city" VALUES (45, '广西');
INSERT INTO "public"."gx_city" VALUES (13, '河北');
INSERT INTO "public"."gx_city" VALUES (35, '福建');
INSERT INTO "public"."gx_city" VALUES (64, '宁夏');
INSERT INTO "public"."gx_city" VALUES (62, '甘肃');
INSERT INTO "public"."gx_city" VALUES (50, '重庆');
INSERT INTO "public"."gx_city" VALUES (14, '山西');
INSERT INTO "public"."gx_city" VALUES (53, '云南');
INSERT INTO "public"."gx_city" VALUES (54, '西藏');
INSERT INTO "public"."gx_city" VALUES (65, '新疆');
INSERT INTO "public"."gx_city" VALUES (33, '浙江');
INSERT INTO "public"."gx_city" VALUES (15, '内蒙古');
INSERT INTO "public"."gx_city" VALUES (42, '湖北');
INSERT INTO "public"."gx_city" VALUES (41, '河南');
INSERT INTO "public"."gx_city" VALUES (11, '北京');
INSERT INTO "public"."gx_city" VALUES (51, '四川');
INSERT INTO "public"."gx_city" VALUES (43, '湖南');
INSERT INTO "public"."gx_city" VALUES (63, '青海');
INSERT INTO "public"."gx_city" VALUES (61, '陕西');
INSERT INTO "public"."gx_city" VALUES (31, '上海');
INSERT INTO "public"."gx_city" VALUES (37, '山东');
INSERT INTO "public"."gx_city" VALUES (32, '江苏');
INSERT INTO "public"."gx_city" VALUES (52, '贵州');
INSERT INTO "public"."gx_city" VALUES (34, '安徽');
INSERT INTO "public"."gx_city" VALUES (23, '黑龙江');
INSERT INTO "public"."gx_city" VALUES (46, '海南');
INSERT INTO "public"."gx_city" VALUES (36, '江西');
INSERT INTO "public"."gx_city" VALUES (12, '天津');
INSERT INTO "public"."gx_city" VALUES (44, '广东');
INSERT INTO "public"."gx_city" VALUES (22, '吉林');
INSERT INTO "public"."gx_city" VALUES (21, '辽宁');
INSERT INTO "public"."gx_city" VALUES (71, '台湾');
INSERT INTO "public"."gx_city" VALUES (81, '香港');
INSERT INTO "public"."gx_city" VALUES (91, '澳门');

-- ----------------------------
-- Primary Key structure for table gx_city
-- ----------------------------
ALTER TABLE "public"."gx_city" ADD CONSTRAINT "pkey_gx_city" PRIMARY KEY ("id");
