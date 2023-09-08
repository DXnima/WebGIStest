-- https://postgis.net/docs/ST_AsGeoJSON.html
-- 多表合并geojson
WITH lineJSON AS (
    SELECT
        jsonb_build_object (
                'type', 'Feature',
                'geometry', ST_AsGeoJSON(ST_Transform(geom, 4326)) :: jsonb - 'crs',
                'properties', to_jsonb ( T.* ) - 'id' - 'ID' - 'geom' - 'the_geom'
            ) AS json
    FROM
        "YS_LINE" AS T
),
     pointJSON AS (
         SELECT
             jsonb_build_object (
                     'type', 'Feature',
                     'geometry', ST_AsGeoJSON(ST_Transform(geom, 4326)) :: jsonb - 'crs',
                     'properties', to_jsonb ( T.* ) - 'id' - 'ID' - 'geom' - 'the_geom'
                 ) AS json
         FROM
             "YS_POINT" AS T
     ),
     unionALL AS(
         SELECT * FROM lineJSON AS l
         UNION
         SELECT * FROM pointJSON as p
     )
SELECT json_build_object(
               'type', 'FeatureCollection',
               'features', json_agg(json)
           ) as geojson
FROM unionALL