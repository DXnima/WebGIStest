import Feature from 'ol/feature';
import VectorLayer from 'ol/layer/vector';
import TileLayer from "ol/layer/tile";
import XYZ from "ol/source/xyz";
import Vector from 'ol/source/vector';
import GeoJSON from 'ol/format/geojson';
import WFS from 'ol/format/wfs';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Circle from 'ol/style/circle';
import Fill from 'ol/style/fill';
import Filter from 'ol/format/filter';
import Extent from 'ol/extent';

let SPACE_NAME = "tiger";//geoserver工作空间
let SRS_NAME = "EPSG:4326";//坐标系
//全局存储高亮图层
let searchLayer = null
//全局存储空间查询要素
let filterLayer = null
//存储返回的GeoJSON数据
let GeoJSONData = null

/**
 * poi:地名信息，landmarks:地标，roads:道路
 * @param i
 * @returns {string}
 * @constructor
 */
const TYPE = (i) => {
    return {
        poi: 'poi',
        landmarks: 'poly_landmarks',
        roads: 'tiger_roads'
    }[i] || 'poi,poly_landmarks,tiger_roads';
}

/**
 * 不同图层的空间字段，和主键排序字段
 * @param i
 * @param j
 * @returns {*}
 * @constructor
 */
const GEOTYPE = (i, j) => {
    return {
        poi: ['the_geom', 'NAME', 'POI'],
        poly_landmarks: ['the_geom', 'LANAME', '地标'],
        tiger_roads: ['the_geom', 'NAME', '道路']
    }[i][j];
}

const FIELD = (i) => {
    return {
        NAME: 'NAME',
        LANAME: 'LANAME'
    }[i] || false;
}

const FIELD_NAME = (i) => {
    return {
        NAME: 'name',
        LANAME: 'name'
    }[i] || false;
}

/**
 * 多个url异步请求
 * @param urls url数组
 * @param max 最大异步数量
 * @param callback 回调函数
 */
const getRequestAll = function (urls, max, callback) {
    let fetchArr = [],  // 存储并发max的promise数组
        i = 0;
    function toFetch() {
        if (i === urls.length) {   // 所有的都处理完了， 返回一个resolve
            return Promise.resolve();
        }
        let one = fetch(urls[i++]).then(response => response.json())
            .then(data => {
                fetchArr.splice(fetchArr.indexOf(one), 1);
                return data
            })
            .catch(() => {
                fetchArr.splice(fetchArr.indexOf(one), 1);
                return {
                    crs: { properties: { name: "urn:ogc:def:crs:EPSG::4326" }, type: "name" },
                    features: [],
                    numberMatched: 0,
                    numberReturned: 0,
                    timeStamp: formatDate(new Date().getTime()),
                    totalFeatures: 0,
                    type: "FeatureCollection"
                }
            })
        // 取出第i个url， 放入fetch里面 , 每取一次i++
        fetchArr.push(one);  //将当前的promise存入并发数组中
        let p = Promise.resolve();
        if (fetchArr.length >= max) {
            // 当并行数量达到最大后， 用race比较 第一个完成的， 然后再调用一下函数自身。
            p = Promise.race(fetchArr);
        }
        return p.then(() => toFetch());
    }
    // arr循环完后， 现在fetchArr里面剩下最后max个promise对象， 使用all等待所有的都完成之后执行callback
    toFetch().then(() => Promise.all(fetchArr)).then((a) => {
        callback(a);
    })
}

/**
 * 多个url异步请求POST
 * @param url BaseURL
 * @param body 传入请求数据 与arr一一对应
 * @param max 最大异步数量
 * @param callback 回调函数
 */
const postRequestAll = function (url, body, max, callback) {
    let fetchArr = [],  // 存储并发max的promise数组
        i = 0;

    function toFetch() {
        if (i === body.length) {   // 所有的都处理完了， 返回一个resolve
            return Promise.resolve();
        }
        let one = fetch(url, {
            method: 'POST',
            body: body[i++],
        }).then(response => response.json())
            .then(data => {
                fetchArr.splice(fetchArr.indexOf(one), 1);
                return data
            })
            .catch(() => {
                fetchArr.splice(fetchArr.indexOf(one), 1);
                return {
                    crs: { properties: { name: "urn:ogc:def:crs:EPSG::4326" }, type: "name" },
                    features: [],
                    numberMatched: 0,
                    numberReturned: 0,
                    timeStamp: formatDate(new Date().getTime()),
                    totalFeatures: 0,
                    type: "FeatureCollection"
                }
            })
        // 取出第i个url， 放入fetch里面 , 每取一次i++
        fetchArr.push(one);  //将当前的promise存入并发数组中
        let p = Promise.resolve();
        if (fetchArr.length >= max) {
            // 当并行数量达到最大后， 用race比较 第一个完成的， 然后再调用一下函数自身。
            p = Promise.race(fetchArr);
        }
        return p.then(() => toFetch());
    }
    // arr循环完后， 现在fetchArr里面剩下最后max个promise对象， 使用all等待所有的都完成之后执行callback
    toFetch().then(() => Promise.all(fetchArr)).then((a) => {
        callback(a);
    })
}

/**
 * 时间戳转换方法    date:时间戳数字
 */
const formatDate = function (d) {
    let date = new Date(d);
    let YY = date.getFullYear() + '-';
    let MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    let DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
    let hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    let mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    let ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    return YY + MM + DD + " " + hh + mm + ss;
}

/**
 * 拼接sql语句
 * @param featureTypes 要素字段
 * @param keyword 查询关键字
 * @returns {*[
 *     {
 *         typeName:'buidling',
           cql:`( name ILIKE '%搜索关键字%')`
 *     }
 * ]}
 */

const getLikeSQL = (featureTypes, keyword) => {
    let sql = []
    featureTypes.forEach(featureType => {
        let like = '';
        featureType.properties.forEach(propertie => {
            if (FIELD(propertie.name)) {
                like += ' ' + propertie.name + ` ILIKE '%${keyword}%' OR`
            }
        })
        like = like.substring(0, like.lastIndexOf('OR'));
        sql.push({
            typeName: featureType.typeName,
            cql: `( ${like} )`
        })
    })
    return sql;
}

/**
 * 获取查询拼接链接
 * @param url baseURL
 * @param type 查询类型
 * @param keyword 查询关键字
 * @returns {Promise<unknown>}
 [
 {
          typeName:'buidling',
           cql:`( name ILIKE '%搜索关键字%')`
      },...
 ]
 */
const getDescribeToSql = (url, type, keyword) => {
    type = TYPE(type);
    return new Promise((resolve) => {
        fetch(url + `geoserver/wfs?service=WFS&version=1.1.0&outputFormat=application/json&request=DescribeFeatureType&typeName=${type}`
        ).then(function (response) {
            return response.json();
        }).then(function (json) {
            resolve(getLikeSQL(json.featureTypes, keyword))
        }).catch((e) => {
            console.error(e)
            return resolve([])
        });
    })
}

/**
 * 返回查询链接集合
 * @param url baseURL
 * @param type 查询类型
 * @param keyword 查询关键字
 * @param pageSize 查询数量
 * @returns {Promise<unknown>}
 * [url1,url2,url3,.....]
 */

const getSearchURLs = (url, type, keyword, pageSize) => {
    let baseURL = url + `geoserver/wfs?service=WFS&version=1.1.0&request=GetFeature&outputFormat=application/json&sortBy=&srsName=${SRS_NAME}&`
    let urls = [];
    return new Promise((resolve) => {
        getDescribeToSql(url, type, keyword).then(r => {
            if (r.length > 0) {
                r.forEach(item => {
                    let typeName = item.typeName;
                    let cql = item.cql;
                    urls.push(encodeURI(baseURL + `maxFeatures=${pageSize}&typeName=${SPACE_NAME}:${typeName}&cql_filter=${cql}`))
                })
                return urls
            }
            return urls
        }).then(data => {
            return resolve(data)
        }).catch((e) => {
            console.error(e)
            return resolve([])
        })
    })
}

/**
 * 根据url集合进行请求获取查询结果
 * @param url baseURL
 * @param type 查询类型
 * @param keyword 查询关键字
 * @param pageSize 查询数量
 */
const getSearch = (url, type, keyword, pageSize) => {
    return new Promise((resolve) => {
        getSearchURLs(url, type, keyword, pageSize).then(re => {
            getRequestAll(re, re.length + 1, (data) => {
                let features = []
                data.forEach(feature => {
                    if (feature !== undefined) {
                        feature.features.forEach(item => {
                            features.push(item)
                        })
                    }
                })
                GeoJSONData = {
                    crs: { properties: { name: "urn:ogc:def:crs:EPSG::4326" }, type: "name" },
                    features: features,
                    numberMatched: pageSize,
                    numberReturned: features.length,
                    timeStamp: formatDate(new Date().getTime()),
                    totalFeatures: features.length,
                    type: 'FeatureCollection'
                }
                return resolve(GeoJSONData)
            })
        }).catch((err) => {
            console.error("查询失败:", err)
        })
    })
}

/**
 * 返回过滤后XML
 * @param layerName 图层名称
 * @param pageSize  每页数据量
 * @param filter 过滤条件
 * @returns {string}
 */
const getFeatureRequest = (layerName, pageSize, filter) => {
    let options = {
        srsName: SRS_NAME,
        featureNS: 'http://www.census.gov',
        featurePrefix: SPACE_NAME,
        featureTypes: [layerName],
        maxFeatures: pageSize,
        outputFormat: 'application/json',
        filter: filter
    }
    let featureRequest = new WFS().writeGetFeature(options);
    let request = new XMLSerializer().serializeToString(featureRequest);
    //XML中添加排序规则
    //let sortBy = `<SortBy xmlns="http://www.opengis.net/ogc"><SortProperty><PropertyName>${GEOTYPE(layerName, 1)}</PropertyName><SortOrder>0</SortOrder></SortProperty></SortBy>`;
    //在<Filter之前插入sortBy字符串
    //request = request.slice(0, request.indexOf('<Filter')) + sortBy + request.slice(request.indexOf('<Filter'));
    return request;
}

/**
 * post请求
 * @param url BaseURL
 * @param body 提交数据
 * @param pageSize 单页数量
 * @param resolve Promise.resolve
 */
const postWFS = (url, body, pageSize, resolve) => {
    postRequestAll(url + 'geoserver/wfs', body, body.length + 1, (data) => {
        let features = []
        data.forEach(feature => {
            if (feature !== undefined) {
                feature.features.forEach(item => {
                    features.push(item)
                })
            }
        })
        GeoJSONData = {
            crs: { properties: { name: "urn:ogc:def:crs:EPSG::4326" }, type: "name" },
            features: features,
            numberMatched: pageSize,
            numberReturned: features.length,
            timeStamp: formatDate(new Date().getTime()),
            totalFeatures: features.length,
            type: 'FeatureCollection'
        }
        return resolve(GeoJSONData)
    })
}

/**
 * space filter 空间查询
 * @param map 地图对象
 * @param url baseURL
 * @param type 查询类型
 * @param pageSize  分页数量
 * @param polygon 多边形geometry类型
 * @returns {Promise<unknown>}
 */
const spaceFilter = (map, url, type, pageSize, polygon) => {
    map.removeLayer(filterLayer);
    let source = new Vector();
    let style = ('getStyle' in polygon) ? polygon.getStyle() : new Style({
        stroke: new Stroke({
            color: 'rgba(144,233,253,0.60)',
            width: 1,
        }),
        fill: new Fill({
            color: 'rgba(128,225,255,0.10)',
        }),
    });
    polygon = ('getGeometry' in polygon) ? polygon.getGeometry() : polygon;
    source.addFeature(new Feature({ geometry: polygon, }));
    filterLayer = new VectorLayer({
        source: source,
        style: style
    });
    map.addLayer(filterLayer);

    //坐标系为EPSG:4326,需要转换经纬度，不然查询结果为空
    polygon.applyTransform((flatCoordinates, flatCoordinates2, stride) => {
        for (let j = 0; j < flatCoordinates.length; j += stride) {
            let y = flatCoordinates[j]
            let x = flatCoordinates[j + 1]
            flatCoordinates[j] = x
            flatCoordinates[j + 1] = y
        }
    });
    let layerNames = TYPE(type).split(',') || [TYPE(type)];
    let body = [];
    layerNames.forEach(layerName => {
        let filter = Filter.intersects(GEOTYPE(layerName, 0), polygon, SRS_NAME)
        body.push(getFeatureRequest(layerName, pageSize, filter));
    });
    return new Promise((resolve) => {
        postWFS(url, body, pageSize, resolve);
    })
}

/**
 * 整理返回的GeoJSON数据
 * @param re GeoJSON数据
 * @returns {*[]}
 [{
     id: "meiyibao_station.fid-4c06493f_17c55e0b8df_10e3",
     name: "查询结果"
 },....]
 */
const getSearchData = (re) => {
    let data = [];
    re.features.forEach(item => {
        let keys = Object.keys(item.properties);
        let name = '', type = item.id.substring(0, item.id.lastIndexOf('.'));
        let property = {
            id: item.id
        }
        keys.forEach(key => {
            if (FIELD(key) && FIELD_NAME(key)) {
                if (FIELD_NAME(key) === 'name') {
                    name += item.properties[FIELD(key)] + '/'
                } else {
                    property[FIELD_NAME(key)] = item.properties[FIELD(key)]
                }
            }
        })
        //删除最后一个/字符
        name = name.substring(0, name.lastIndexOf('/'));
        property['name'] = GEOTYPE(type, 2) + ':' + name;
        data.push(property)
    })
    return data;
}

//创建样式
const styleFunction = function (feature) {
    const image = new Circle({
        radius: 4,
        fill: new Fill({
            color: 'rgb(255,0,0)',
        }),
        stroke: new Stroke({ color: 'rgb(31,37,154)', width: 1 }),
    });
    const styles = {
        'Point': new Style({
            image: image,
        }),
        'LineString': new Style({
            stroke: new Stroke({
                color: 'rgb(255,89,0)',
                width: 3,
            }),
        }),
        'MultiLineString': new Style({
            stroke: new Stroke({
                color: 'rgb(255,89,0)',
                width: 3,
            }),
        }),
        'MultiPoint': new Style({
            image: image,
        }),
        'MultiPolygon': new Style({
            stroke: new Stroke({
                color: 'yellow',
                width: 1,
            }),
            fill: new Fill({
                color: 'rgba(255,255,0,0.29)',
            }),
        }),
        'Polygon': new Style({
            stroke: new Stroke({
                color: 'blue',
                lineDash: [4],
                width: 3,
            }),
            fill: new Fill({
                color: 'rgba(0,0,255,0.47)',
            }),
        }),
        'GeometryCollection': new Style({
            stroke: new Stroke({
                color: 'rgb(208,0,255)',
                width: 2,
            }),
            fill: new Fill({
                color: 'rgb(208,0,255)',
            }),
            image: new Circle({
                radius: 10,
                fill: null,
                stroke: new Stroke({
                    color: 'rgb(208,0,255)',
                }),
            }),
        }),
        'Circle': new Style({
            stroke: new Stroke({
                color: 'rgba(255,0,0,0.2)',
                width: 2,
            }),
            fill: new Fill({
                color: 'rgba(255,0,0,0.2)',
            }),
        }),
    };
    return styles[feature.getGeometry().getType()];
};

/**
 * 添加GeoJSON到地图
 * @param map ol map对象
 * @param features geojson
 * @param featureId 指定id
 */
const addGeoJSON = (map, featureId) => {
    let features = GeoJSONData;
    map.removeLayer(searchLayer);
    if (features === null) return null;
    const source = new Vector();
    if (featureId === undefined || featureId === null || featureId === '') {
        //读取数据表中geojson创建为feature对象
        let feature = new GeoJSON().readFeatures(features);
        source.addFeatures(feature);
    } else {
        //循环data 加载要素到地图
        features.features.forEach(item => {
            if (item.id === featureId) {
                //读取数据表中wkt创建为feature对象
                let feature = new GeoJSON().readFeature(item);
                source.addFeature(feature);
            }
        });
    }
    if (source.getFeatures().length === 0) return null;
    searchLayer = new VectorLayer({
        source: source,
        style: styleFunction
    });
    //添加geojson图层到地图
    map.addLayer(searchLayer);
    //通过extent缩放至范围
    let extent = source.getExtent();
    let r = map.getView().getResolutionForExtent(extent, map.getSize());
    //设置地图显示缩放等级
    map.getView().setResolution(r);
    //设置地图中心点
    map.getView().setCenter(Extent.getCenter(extent));
}

/**
 * 加载天地图
 * @param lyr
 * @returns {_ol_layer_Tile_}
 */
const getTdtLayer = (lyr) => {
    // let url = "http://t0.tianditu.gov.cn/DataServer?T="+lyr+"&X={x}&Y={y}&L={z}";
    let urls = [];
    for (let i = 0; i < 8; i++) {
        urls.push("http://t" + i + ".tianditu.gov.cn/DataServer?T=" + lyr + "&X={x}&Y={y}&L={z}&tk=16c5722fed64bcdbb390cc21a5548cf9");
    }
    return new TileLayer({
        source: new XYZ({
            urls: urls
        })
    });
}

export {
    getRequestAll,
    postRequestAll,
    formatDate,
    getLikeSQL,
    getDescribeToSql,
    getSearchURLs,
    getSearch,
    getFeatureRequest,
    postWFS,
    spaceFilter,
    getSearchData,
    styleFunction,
    addGeoJSON,
    getTdtLayer
}
