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

-- 将切片存入表mvt_china中
DROP TABLE IF EXISTS mvt;
CREATE TABLE mvt
(
    z integer not null,
    x integer not null,
    y integer not null,
    mvt bytea,
    --bbox geometry(Polygon,3857),
    CONSTRAINT mvt_z_x_y_pk PRIMARY KEY (z, x, y)
);
CREATE index if NOT EXISTS mvt_z_x_y_idx ON mvt (z, x, y);

-- 矢量切片
DROP FUNCTION IF EXISTS mvt;
CREATE FUNCTION mvt(z integer, x integer, y integer) RETURNS bytea AS
    $$
DECLARE
bbox geometry := TileBBox( z,x,y );
BEGIN
RETURN (SELECT
                ( SELECT ST_AsMVT ( P, 'university', 4096, 'geom' ) AS "mvt" FROM
                    ( SELECT ST_AsMVTGeom ( ST_Transform ( ST_GeometryFromText ( ST_AsText ( geom ), 4326 ), 3857 ), bbox, 4096, 64, TRUE ) geom,name FROM layer_university WHERE geom && bbox) AS P ) ||
                ( SELECT ST_AsMVT ( P, 'capital', 4096, 'geom' ) AS "mvt" FROM
                    ( SELECT ST_AsMVTGeom ( ST_Transform ( ST_GeometryFromText ( ST_AsText ( geom ), 4326 ), 3857 ), bbox, 4096, 64, TRUE ) geom,name FROM capital WHERE geom && bbox) AS P ) ||
                ( SELECT ST_AsMVT ( P, 'china', 4096, 'geom' ) AS "mvt" FROM
                    ( SELECT ST_AsMVTGeom ( ST_Transform ( ST_GeometryFromText ( ST_AsText ( geom ), 4326 ), 3857 ), bbox, 4096, 64, TRUE ) geom,province,type FROM province WHERE geom && bbox) AS P )
                AS mvt);
END;
$$
LANGUAGE'plpgsql';

-- 矢量切片存储到表
DROP FUNCTION IF EXISTS mvt_title;
CREATE FUNCTION mvt_title (zoom int,x1 int,y1 int)
    RETURNS bytea
AS
    $$
declare
title bytea;
BEGIN
  IF EXISTS(SELECT * FROM mvt WHERE x=x1 AND y=y1 AND z = zoom) THEN
     title = (SELECT mvt FROM mvt WHERE x=x1 AND y=y1 AND z=zoom);
ELSE
	 title = (SELECT mvt(zoom,x1,y1));
INSERT INTO mvt (z,x,y,mvt) VALUES (zoom,x1,y1,title);
END IF;
RETURN title;
END;
$$
LANGUAGE'plpgsql';