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

 Date: 01/06/2021 22:55:33
*/


-- ----------------------------
-- Table structure for layer_edit
-- ----------------------------
DROP TABLE IF EXISTS "public"."layer_edit";
CREATE TABLE "public"."layer_edit" (
  "id" int4 NOT NULL DEFAULT nextval('layer_edit_id_seq'::regclass),
  "name" varchar(64) COLLATE "pg_catalog"."default",
  "geom" "public"."geometry"
)
;

-- ----------------------------
-- Records of layer_edit
-- ----------------------------
INSERT INTO "public"."layer_edit" VALUES (19, '点', '010100000000000000A0935740109AE5AB1A0D4240');
INSERT INTO "public"."layer_edit" VALUES (21, '点2', '010100000000000000E0E75940D88ECCDC81294040');
INSERT INTO "public"."layer_edit" VALUES (20, '线', '0102000000030000000000000020695C405C403CB3549F44400000000080DC5B40B05654DD059B40400000000060775940FE7E55FA8C8B4240');
INSERT INTO "public"."layer_edit" VALUES (22, 'nima DX', '0101000000FB572E15679363417E32F0C6E3DB5141');
INSERT INTO "public"."layer_edit" VALUES (23, 'nima DX', '010200000003000000A9EE07C40C5C64410210E700924C5141240EDC012DC363418AA9A90637224B417DDFB2E9E27766410C846BB8B36A4941');
INSERT INTO "public"."layer_edit" VALUES (24, '面', '010300000001000000050000006C636DF1C6646641240BA779FB9A5241123C24A4DE236741FE0D90D0FAA74B41DE6EACCD26366941A6ECB09375B74E41F246CE3BD8256841FA54F98C356B52416C636DF1C6646641240BA779FB9A5241');

-- ----------------------------
-- Primary Key structure for table layer_edit
-- ----------------------------
ALTER TABLE "public"."layer_edit" ADD CONSTRAINT "pkey_layer_edit" PRIMARY KEY ("id");
