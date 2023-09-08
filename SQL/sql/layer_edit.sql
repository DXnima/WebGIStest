/*
 Navicat Premium Data Transfer

 Source Server         : PostgresSQL
 Source Server Type    : PostgreSQL
 Source Server Version : 120011
 Source Host           : localhost:5432
 Source Catalog        : webgistest
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120011
 File Encoding         : 65001

 Date: 06/11/2022 19:31:14
*/


-- ----------------------------
-- Table structure for layer_edit
-- ----------------------------
DROP TABLE IF EXISTS "public"."layer_edit";
CREATE TABLE "public"."layer_edit" (
  "id" int4 NOT NULL,
  "name" varchar(64) COLLATE "pg_catalog"."default",
  "geom" "public"."geometry"
)
;

-- ----------------------------
-- Records of layer_edit
-- ----------------------------
INSERT INTO "public"."layer_edit" VALUES (3, '面', '0103000020110F000001000000050000006C636DF1C6646641240BA779FB9A5241123C24A4DE236741FE0D90D0FAA74B41DE6EACCD26366941A6ECB09375B74E41F246CE3BD8256841FA54F98C356B52416C636DF1C6646641240BA779FB9A5241');
INSERT INTO "public"."layer_edit" VALUES (1, '点', '0101000020110F0000FB572E15679363417E32F0C6E3DB5141');
INSERT INTO "public"."layer_edit" VALUES (2, '线', '0102000020110F000003000000A9EE07C40C5C64410210E700924C5141240EDC012DC363418AA9A90637224B417DDFB2E9E27766410C846BB8B36A4941');

-- ----------------------------
-- Primary Key structure for table layer_edit
-- ----------------------------
CREATE SEQUENCE IF NOT EXISTS layer_edit_id_seq;
ALTER TABLE "public"."layer_edit" ALTER COLUMN "id" SET DEFAULT nextval('layer_edit_id_seq');
ALTER TABLE "public"."layer_edit" ADD CONSTRAINT "pkey_layer_edit" PRIMARY KEY ("id");
