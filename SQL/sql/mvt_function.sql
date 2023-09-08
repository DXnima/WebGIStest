create or replace function TileBBox (z int, x int, y int, srid int = 3857)
    returns geometry
    language plpgsql immutable as
$func$
declare
max numeric := 20037508.34;
    res numeric := (max*2)/(2^z);
    bbox geometry;
begin
    bbox := ST_MakeEnvelope(
        -max + (x * res),
        max - (y * res),
        -max + (x * res) + res,
        max - (y * res) - res,
        3857
    );
    if srid = 3857 then
        return bbox;
else
        return ST_Transform(bbox, srid);
end if;
end;
$func$;

-- 获取Geometry的边界minX minY maxX maxY
DROP FUNCTION IF EXISTS MVT_BBoxXY;
CREATE OR REPLACE FUNCTION MVT_BBoxXY(z int, g geometry, srid int = 3857)
    RETURNS int[]
AS
$BODY$
DECLARE
max numeric := 20037508.34;
    res numeric := 2^(z-1);
    poi1 geometry; poi2 geometry;
    minx numeric; miny numeric;
	maxx numeric; maxy numeric;
BEGIN
    IF srid != 3857 THEN
        g := ST_Transform(g, 3857);
END IF;
    poi1 := ST_Point(ST_XMin(g), ST_YMin(g));
    poi2 := ST_Point(ST_XMax(g), ST_YMax(g));
    minx = (1 + ST_X(poi1)/max)*res;
	maxy = (1 - ST_Y(poi1)/max)*res;
	maxx = (1 + ST_X(poi2)/max)*res;
	miny = (1 - ST_Y(poi2)/max)*res;
	IF minx < 0 THEN minx = 0; END IF;
	IF miny < 0 THEN miny = 0; END IF;
	IF maxx < 0 THEN maxx = 0; END IF;
	IF maxy < 0 THEN maxy = 0; END IF;
RETURN array[trunc(minx,0),trunc(miny,0),trunc(maxx,0),trunc(maxy,0)];
END
$BODY$
LANGUAGE plpgsql IMMUTABLE;

-- 将切片存入表mvt_test中
DROP TABLE IF EXISTS mvt_test;
CREATE TABLE mvt_test
(
    z integer not null,
    x integer not null,
    y integer not null,
    mvt bytea,
    --bbox geometry(Polygon,3857),
    CONSTRAINT mvt_test_z_x_y_pk PRIMARY KEY (z, x, y)
);
CREATE index if NOT EXISTS mvt_test_z_x_y_pk ON mvt_test (z, x, y);

-- 创建需要矢量切片的规则
-- 本文是将layer_university、capital、province三个表的数据进行矢量切片
DROP FUNCTION IF EXISTS MVT_Rule;
CREATE FUNCTION MVT_Rule(z integer, x integer, y integer) RETURNS bytea AS
    $$
DECLARE
bbox geometry := TileBBox( z,x,y );
BEGIN
RETURN (SELECT
                ( SELECT ST_AsMVT ( P, 'university', 4096, 'geom' ) AS "mvt" FROM
                    ( SELECT ST_AsMVTGeom ( ST_AsText ( geom ), bbox, 4096, 64, TRUE ) geom,name FROM layer_university WHERE geom && bbox) AS P ) ||
                ( SELECT ST_AsMVT ( P, 'capital', 4096, 'geom' ) AS "mvt" FROM
                    ( SELECT ST_AsMVTGeom ( ST_AsText ( geom ), bbox, 4096, 64, TRUE ) geom,name FROM capital WHERE geom && bbox) AS P ) ||
                ( SELECT ST_AsMVT ( P, 'china', 4096, 'geom' ) AS "mvt" FROM
                    ( SELECT ST_AsMVTGeom ( ST_AsText ( geom ), bbox, 4096, 64, TRUE ) geom,province,type FROM province WHERE geom && bbox) AS P )
                AS mvt);
END;
$$
LANGUAGE'plpgsql';

-- 矢量切片存储到表
DROP FUNCTION IF EXISTS MVT_Title;
CREATE FUNCTION MVT_Title (zoom int,x1 int,y1 int)
    RETURNS bytea
AS
    $$
declare
title bytea;
BEGIN
  IF EXISTS(SELECT * FROM mvt_test WHERE x=x1 AND y=y1 AND z = zoom) THEN
     title = (SELECT mvt FROM mvt_test WHERE x=x1 AND y=y1 AND z=zoom);
ELSE
	 title = (SELECT MVT_Rule(zoom,x1,y1));
INSERT INTO mvt_test (z,x,y,mvt) VALUES (zoom,x1,y1,title);
END IF;
RETURN title;
END;
$$
LANGUAGE'plpgsql';

-- 缓存瓦片mvt_test
DROP FUNCTION IF EXISTS MVT_Test_Cache;
CREATE OR REPLACE FUNCTION MVT_Test_Cache(start_zoom int, end_zoom int, g geometry, srid int = 3857)
RETURNS VOID
AS
$BODY$
DECLARE
bbox numeric[];
    title bytea;
BEGIN
FOR cache_z in start_zoom..end_zoom  loop
        bbox := MVT_BBoxXY(cache_z, g, srid);
FOR cache_x in bbox[1]..bbox[3]  loop
            FOR cache_y in bbox[2]..bbox[4]  loop
                title = (SELECT MVT_Title(cache_z, cache_x, cache_y));
                IF EXISTS(SELECT * FROM mvt_test WHERE x = cache_x AND y = cache_y AND z = cache_z) THEN
UPDATE mvt_test SET mvt = title WHERE x = cache_x AND y = cache_y AND z = cache_z;
ELSE
                    INSERT INTO mvt_test (z, x, y, mvt) VALUES (cache_z, cache_x, cache_y, title);
END IF;
END loop;
END loop;
END loop;
END
$BODY$
LANGUAGE plpgsql VOLATILE;

-- 测试瓦片
-- MVT_Title(zoom,x,y)
-- SELECT MVT_Title(0,0,0);
-- SELECT MVT_Title(1,1,0);

-- 测试缓存
-- 缓存等级zoom:0-2
-- 缓存范围bbox:[73.5011421000001,6.32342077500007,135.08851148,53.5609010500001]
-- SELECT MVT_Test_Cache(0,6, ST_MakeEnvelope(73.5011421000001,6.32342077500007,135.08851148,53.5609010500001, 4326))