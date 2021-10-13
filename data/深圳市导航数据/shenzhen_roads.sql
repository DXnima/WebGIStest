-- 首先为shenzhen_roads表添加四个字段（后面涉及到对应字段时再详细解释）：
-- 
-- source —— 用于保存路径起始顶点的id
-- target —— 用于保存路径终止顶点的id
-- cost —— 用于保存路径正向的成本（或者代价）
-- reverse_cost —— 用于保存路径反向的成本（或者代价）
ALTER TABLE shenzhen_roads
ADD COLUMN source INTEGER,
ADD COLUMN target INTEGER,
ADD COLUMN cost DOUBLE PRECISION,
ADD COLUMN reverse_cost DOUBLE PRECISION;

--正向路径的成本
UPDATE shenzhen_roads
SET cost = ST_Length(geom), reverse_cost = -1
WHERE oneway = 'F';

--反向路径的成本
UPDATE shenzhen_roads
SET reverse_cost = ST_Length(geom), cost = -1
WHERE oneway = 'T';

-- 双向路径的成本
UPDATE shenzhen_roads
SET cost = ST_Length(geom), reverse_cost = ST_Length(geom)
WHERE oneway = 'B';

-- 创建路网拓扑需要调用pgr_createTopology函数
-- 六个参数分别表示：
-- 路网表
-- 路径之间的容差，两条路径的距离大于这个容差值，就表示它们不相交，否则就是相交。
-- 路网表中包含空间信息的列
-- 路网表的主码列
-- 保存路径起始顶点的id的列
-- 保存路径终止顶点的id的列
SELECT pgr_createTopology(
	'shenzhen_roads', 
	0.001,
	'geom',
	'gid',
	'source',
	'target'
); 

-- 从锦绣民俗中华村步行到世界之窗
SELECT * FROM pgr_dijkstra(
	'SELECT gid AS id,
		source, target,
		cost, reverse_cost
	FROM shenzhen_roads',
	10564, 12089,
	directed := FALSE
);

-- 起点不同，但终点一样
SELECT * FROM pgr_dijkstra(
	'SELECT gid AS id,
		source, target,
		cost, reverse_cost
	FROM shenzhen_roads',
	ARRAY[10564, 13019], 12089,
	directed := FALSE
);

-- 起点相同，终点不同
SELECT * FROM pgr_dijkstra(
	'SELECT gid AS id,
		source, target,
		cost/1.3 AS cost, reverse_cost/1.3 AS reverse_cost
	FROM shenzhen_roads',
	12089, ARRAY[10564, 13019],
	directed := FALSE
);

-- 起点不同，终点不同
SELECT * FROM pgr_dijkstra(
	'SELECT gid AS id,
		source, target,
		cost/1.3/60 AS cost, reverse_cost/1.3/60 AS reverse_cost
	FROM shenzhen_roads',
	ARRAY[12089, 13019], ARRAY[10564, 7304],
	directed := FALSE
);

-- wrk_dijkstra的函数具有如下特点：
-- 可以在车辆道路的全部区域进行搜索。
-- 将数据库视图作为参数。
-- 结果包含道路名、路径的几何信息（二进制格式）、路径各个路段的大地方位角（单位十进制度）等等。
CREATE OR REPLACE FUNCTION wrk_dijkstra(
    IN edges_subset regclass,	-- 视图作为参数
    IN source BIGINT,
    IN target BIGINT,
    OUT seq INTEGER,
    OUT gid BIGINT,
    OUT name TEXT,
    OUT cost FLOAT,
    OUT azimuth FLOAT,
    OUT route_readable TEXT,
    OUT route_geom geometry
) RETURNS SETOF record AS 
$BODY$
    WITH
    dijkstra AS (
        SELECT * FROM pgr_dijkstra(
            -- 使用参数化的视图
            'SELECT gid AS id, * FROM ' || $1,
            $2, $3)
    ),
    get_geom AS (
        SELECT dijkstra.*, ways.name,
            CASE
                WHEN dijkstra.node = ways.source THEN geom
                ELSE ST_Reverse(geom)
            END AS route_geom
        FROM dijkstra JOIN shenzhen_roads AS ways ON (edge = gid)
        ORDER BY seq)
    SELECT
        seq,
        edge,
        name,
        cost,
        degrees(ST_azimuth(ST_StartPoint(route_geom), ST_EndPoint(route_geom))) AS azimuth,
        ST_AsText(route_geom),
        route_geom
    FROM get_geom
    ORDER BY seq;
$BODY$
LANGUAGE 'sql';

-- 创建函数wrk_fromAtoB
CREATE OR REPLACE FUNCTION wrk_fromAtoB(
	IN edges_subset regclass,
	IN x1 NUMERIC, IN y1 numeric,
	IN x2 NUMERIC, IN y2 NUMERIC,
	OUT seq INTEGER,
	OUT gid BIGINT,
	OUT name TEXT,
	OUT costs FLOAT,
	OUT azimuth FLOAT,
	OUT geom GEOMETRY
)
RETURNS SETOF record AS
$BODY$
DECLARE 
	final_query TEXT;
BEGIN
	final_query := 
		FORMAT($$
			WITH
			vertices AS (
				SELECT * FROM shenzhen_roads_vertices_pgr
				WHERE id IN (
					SELECT source FROM %1$I
					UNION
					SELECT target FROM %1$I
				)
			),
			dijkstra AS (
				SELECT * 
				FROM wrk_dijkstra(
					'%1$I',
					-- source
					(SELECT id FROM vertices
                        ORDER BY the_geom <-> ST_SetSRID(ST_Point(%2$s, %3$s), 3857) LIMIT 1),
					-- target
					(SELECT id FROM vertices
                        ORDER BY the_geom <-> ST_SetSRID(ST_Point(%4$s, %5$s), 3857) LIMIT 1)
				)
			)
			SELECT 
			   seq,
			   dijkstra.gid,
			   dijkstra.name,
			   dijkstra.cost / 1000.0 AS costs,
			   azimuth,
			   route_geom AS geom
			FROM dijkstra 
			JOIN shenzhen_roads ways USING(gid);$$,
		edges_subset, x1,y1,x2,y2
		);
	RAISE notice '%', final_query; -- 执行该函数时显示函数的逻辑代码信息
	RETURN QUERY EXECUTE final_query;
END;
$BODY$
LANGUAGE 'plpgsql';

SELECT * FROM wrk_fromAtoB('shenzhen_roads',12677354.9,2578172.3,12677441.2, 2577908.3);