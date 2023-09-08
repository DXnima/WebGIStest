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

 Date: 06/11/2022 19:31:42
*/


-- ----------------------------
-- Table structure for port
-- ----------------------------
DROP TABLE IF EXISTS "public"."port";
CREATE TABLE "public"."port" (
  "gid" int4 NOT NULL,
  "porttype" varchar(254) COLLATE "pg_catalog"."default",
  "address" varchar(254) COLLATE "pg_catalog"."default",
  "province" varchar(254) COLLATE "pg_catalog"."default",
  "lng" float8,
  "name" varchar(254) COLLATE "pg_catalog"."default",
  "lat" float8,
  "geom" "public"."geometry"
)
;

-- ----------------------------
-- Records of port
-- ----------------------------
INSERT INTO "public"."port" VALUES (1, '沿海', '浙江省宁波市北仑区宁波舟山港', '浙江省', 122.050553, '宁波舟山港', 29.964108, '0101000020110F00001EE426AD15EA6941A5CB348CDCB14A41');
INSERT INTO "public"."port" VALUES (2, '沿海', '上海市浦东新区江东路上海港', '上海市', 121.783312, '上海港', 31.555718, '0101000020110F0000163AED088FDB6941898741FB95444C41');
INSERT INTO "public"."port" VALUES (3, '沿海', '天津市滨海新区天津港', '天津市', 117.787691, '天津港', 39.007073, '0101000020110F0000162E15396002694115314431FF035241');
INSERT INTO "public"."port" VALUES (4, '沿海', '河北省唐山市乐亭县海港开发区京唐港', '河北省', 119.002643, '京唐港', 39.195162, '0101000020110F0000E5E5E5336A44694197123362581E5241');
INSERT INTO "public"."port" VALUES (5, '沿海', '河北省唐山市曹妃甸区发扬道曹妃甸港', '河北省', 118.495718, '曹妃甸港', 38.992849, '0101000020110F0000816CA55FDC286941ED3784D301025241');
INSERT INTO "public"."port" VALUES (6, '沿海', '山东省日照市东港区日照港', '山东省', 119.53124, '日照港', 35.318648, '0101000020110F0000147BA99825616941E9CF2639A90C5041');
INSERT INTO "public"."port" VALUES (7, '沿海', '山东省烟台市芝罘区环海路2号烟台港', '山东省', 121.394868, '烟台港', 37.557879, '0101000020110F0000566C80DC71C66941711205F6483B5141');
INSERT INTO "public"."port" VALUES (8, '沿海', '河北省沧州市黄骅市黄骅港', '河北省', 117.858784, '黄骅港', 38.344723, '0101000020110F0000CBAB407A3D06694146283AAEC3A75141');
INSERT INTO "public"."port" VALUES (9, '内河', '江苏省南通市崇川区人民西路南通港', '江苏省', 120.853196, '南通港', 31.996507, '0101000020110F00005CA7A98700A969417D6A0B1052B54C41');
INSERT INTO "public"."port" VALUES (10, '沿海', '河北省秦皇岛市海港区河北大街东段1号秦皇岛港', '河北省', 119.61552, '秦皇岛港', 39.914881, '0101000020110F0000923CE058BA6569416FFBE32DD3835241');
INSERT INTO "public"."port" VALUES (11, '内河', '重庆市渝中区滨江路重庆港', '重庆市', 106.595646, '重庆港', 29.552525, '0101000020110F0000B26E12A107A26641AA9AF4E7C74A4A41');
INSERT INTO "public"."port" VALUES (12, '内河', '湖南省岳阳市云溪区沿江路岳阳港', '湖南省', 113.136392, '岳阳港', 29.437121, '0101000020110F00008E2B86B18D0568415AA41C21F42D4A41');
INSERT INTO "public"."port" VALUES (13, '内河', '江苏省镇江市京口区镇江港', '江苏省', 119.468679, '镇江港', 32.248939, '0101000020110F00005E826210BF5D694101C7F72320F64C41');
INSERT INTO "public"."port" VALUES (14, '内河', '江苏省无锡市江阴市江阴港', '江苏省', 120.287782, '江阴港', 31.947123, '0101000020110F0000913082D4448A6941226572D9A9A84C41');
INSERT INTO "public"."port" VALUES (15, '沿海', '广东省珠海市金湾区珠海港', '广东省', 113.540329, '珠海港', 22.223196, '0101000020110F0000C6217B73821B684141C2EEE0B35D4341');
INSERT INTO "public"."port" VALUES (16, '内河', '江西省九江市柴桑区港城大道九江港', '江西省', 116.009529, '九江港', 29.739915, '0101000020110F0000201B4136B9A16841016CCEFAA8794A41');
INSERT INTO "public"."port" VALUES (17, '内河', '湖北省荆州市沙市区临江路荆州港', '湖北省', 112.242315, '荆州港', 30.302764, '0101000020110F0000DD813DABF4D4674198D42980FF064B41');
INSERT INTO "public"."port" VALUES (18, '内河', '湖北省武汉市江岸区沿江大道武汉港', '湖北省', 114.300033, '武汉港', 30.58013, '0101000020110F0000263014AFCD44684192E01F1FF24C4B41');
INSERT INTO "public"."port" VALUES (19, '内河', '湖北省黄石市黄石港区交通路1号黄石港', '湖北省', 115.078064, '黄石港', 30.227438, '0101000020110F000011F38CEF176F6841B157DB4009F44A41');

-- ----------------------------
-- Indexes structure for table port
-- ----------------------------
CREATE INDEX "port_geom_idx" ON "public"."port" USING gist (
  "geom" "public"."gist_geometry_ops_2d"
);

-- ----------------------------
-- Primary Key structure for table port
-- ----------------------------
CREATE SEQUENCE IF NOT EXISTS port_gid_seq;
ALTER TABLE "public"."port" ALTER COLUMN "gid" SET DEFAULT nextval('port_gid_seq');
ALTER TABLE "public"."port" ADD CONSTRAINT "port_pkey" PRIMARY KEY ("gid");
