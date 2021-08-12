/*
 Navicat Premium Data Transfer

 Source Server         : PostgreSQL
 Source Server Type    : PostgreSQL
 Source Server Version : 120004
 Source Host           : localhost:5432
 Source Catalog        : meiyibao
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120004
 File Encoding         : 65001

 Date: 05/07/2021 11:16:02
*/


-- ----------------------------
-- Table structure for port
-- ----------------------------
DROP TABLE IF EXISTS "public"."port";
CREATE TABLE "public"."port" (
  "gid" int4 NOT NULL DEFAULT nextval('port_gid_seq'::regclass),
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
INSERT INTO "public"."port" VALUES (1, '沿海', '浙江省宁波市北仑区宁波舟山港', '浙江省', 122.050553, '宁波舟山港', 29.964108, '0101000000BD6DA6423C835E40DDCF29C8CFF63D40');
INSERT INTO "public"."port" VALUES (2, '沿海', '上海市浦东新区江东路上海港', '上海市', 121.783312, '上海港', 31.555718, '01010000001EA4A7C821725E406CCCEB88438E3F40');
INSERT INTO "public"."port" VALUES (3, '沿海', '天津市滨海新区天津港', '天津市', 117.787691, '天津港', 39.007073, '0101000000A016838769725D40A1D79FC4E7804340');
INSERT INTO "public"."port" VALUES (4, '沿海', '河北省唐山市乐亭县海港开发区京唐港', '河北省', 119.002643, '京唐港', 39.195162, '01010000000EA48B4D2BC05D4003B68311FB984340');
INSERT INTO "public"."port" VALUES (5, '沿海', '河北省唐山市曹妃甸区发扬道曹妃甸港', '河北省', 118.495718, '曹妃甸港', 38.992849, '01010000007782FDD7B99F5D40E36E10AD157F4340');
INSERT INTO "public"."port" VALUES (6, '沿海', '山东省日照市东港区日照港', '山东省', 119.53124, '日照港', 35.318648, '0101000000EE940ED6FFE15D40C9772975C9A84140');
INSERT INTO "public"."port" VALUES (7, '沿海', '山东省烟台市芝罘区环海路2号烟台港', '山东省', 121.394868, '烟台港', 37.557879, '01010000002A8F6E8445595E4006103E9468C74240');
INSERT INTO "public"."port" VALUES (8, '沿海', '河北省沧州市黄骅市黄骅港', '河北省', 117.858784, '黄骅港', 38.344723, '0101000000FF942A51F6765D40EA961DE21F2C4340');
INSERT INTO "public"."port" VALUES (9, '内河', '江苏省南通市崇川区人民西路南通港', '江苏省', 120.853196, '南通港', 31.996507, '0101000000FE4465C39A365E402E3C2F151BFF3F40');
INSERT INTO "public"."port" VALUES (10, '沿海', '河北省秦皇岛市海港区河北大街东段1号秦皇岛港', '河北省', 119.61552, '秦皇岛港', 39.914881, '01010000002C82FFAD64E75D40AB5D13D21AF54340');
INSERT INTO "public"."port" VALUES (11, '内河', '重庆市渝中区滨江路重庆港', '重庆市', 106.595646, '重庆港', 29.552525, '0101000000917F66101FA65A40EF384547728D3D40');
INSERT INTO "public"."port" VALUES (12, '内河', '湖南省岳阳市云溪区沿江路岳阳港', '湖南省', 113.136392, '岳阳港', 29.437121, '0101000000E8DB82A5BA485C4013656F29E76F3D40');
INSERT INTO "public"."port" VALUES (13, '内河', '江苏省镇江市京口区镇江港', '江苏省', 119.468679, '镇江港', 32.248939, '01010000009B5434D6FEDD5D4077D9AF3BDD1F4040');
INSERT INTO "public"."port" VALUES (14, '内河', '江苏省无锡市江阴市江阴港', '江苏省', 120.287782, '江阴港', 31.947123, '0101000000299831056B125E40174A26A776F23F40');
INSERT INTO "public"."port" VALUES (15, '沿海', '广东省珠海市金湾区珠海港', '广东省', 113.540329, '珠海港', 22.223196, '0101000000250516C094625C401899805F23393640');
INSERT INTO "public"."port" VALUES (16, '内河', '江西省九江市柴桑区港城大道九江港', '江西省', 116.009529, '九江港', 29.739915, '010100000045D7851F9C005D40E1D1C6116BBD3D40');
INSERT INTO "public"."port" VALUES (17, '内河', '湖北省荆州市沙市区临江路荆州港', '湖北省', 112.242315, '荆州港', 30.302764, '01010000002315C616820F5C40F96706F1814D3E40');
INSERT INTO "public"."port" VALUES (18, '内河', '湖北省武汉市江岸区沿江大道武汉港', '湖北省', 114.300033, '武汉港', 30.58013, '010100000021AE9CBD33935C40B16D516683943E40');
INSERT INTO "public"."port" VALUES (19, '内河', '湖北省黄石市黄石港区交通路1号黄石港', '湖北省', 115.078064, '黄石港', 30.227438, '0101000000ADBF2500FFC45C401EDE7360393A3E40');

-- ----------------------------
-- Indexes structure for table port
-- ----------------------------
CREATE INDEX "port_geom_idx" ON "public"."port" USING gist (
  "geom" "public"."gist_geometry_ops_2d"
);

-- ----------------------------
-- Primary Key structure for table port
-- ----------------------------
ALTER TABLE "public"."port" ADD CONSTRAINT "port_pkey" PRIMARY KEY ("gid");
