<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>WFS</el-breadcrumb-item>
            <el-breadcrumb-item>WFS综合操作</el-breadcrumb-item>
        </el-breadcrumb>
        <!--卡片区域-->
        <el-card>
            <button id="insert" @click="insertClick">添加</button>
            <button id="update" @click="updateClick">修改</button>
            <button id="delete" @click="deleteClick">删除</button>
            <button id="end" @click="endClick">结束操作</button>
            <div id="map"></div>
        </el-card>
    </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import Point from 'ol/geom/point';
import VectorLayer from 'ol/layer/vector';
import Vector from 'ol/source/vector';
import GeoJSON from 'ol/format/geojson';
import WFS from 'ol/format/wfs';
import Draw from 'ol/interaction/draw';
import Select from 'ol/interaction/select';
import Modify from 'ol/interaction/modify';
import Style from 'ol/style/style';
import Circle from 'ol/style/circle';
import Fill from 'ol/style/fill';
import loadingstrategy from 'ol/loadingstrategy';
import {getTdtLayer} from "../../../utils/searchUtils";
export default {
    data() {
        return {
            // 服务配置，命名空间、图层、服务地址等
            geoserverData: {
                wsName: 'webgistest',
                uri: 'http://www.openplans.org/webgistest',
                wfsURL: process.env.VUE_APP_GEOSERVER + 'geoserver/wfs?',
                layer: 'port'
            },
            select: null,
            edit: null,
            draw: null
        }
    },
    created() {
    },
    mounted() {
        this.initMap()
    },
    methods: {
        initMap() {
            const that = this
            //测试数据 添加WFS数据  添加所有
            let wfsSource = new Vector({
                format: new GeoJSON(),
                url: function (extent) {
                    return (
                        that.geoserverData.wfsURL + 'service=WFS&version=1.1.0&request=GetFeature&typeName=' +
                        that.geoserverData.wsName + ":" +
                        that.geoserverData.layer + '&' + 'outputFormat=application/json&srsname=EPSG:3857&bbox=' +
                        extent.join(',') + ',EPSG:3857');
                },
                strategy: loadingstrategy.bbox
            });

            let wfsLayer = new VectorLayer({
                title: 'add Layer',
                source: wfsSource
            })

            let layers = [
              getTdtLayer("vec_w"),
              getTdtLayer("cva_w"), wfsLayer
            ];

            let map = new Map({
                target: 'map',
                layers: layers,
                view: new View({
                  projection: "EPSG:3857",
                  center: [12233037.3, 4861921.87],
                    zoom: 4
                })
            });

            map.on('pointermove', function (evt) {
                if (map.hasFeatureAtPixel(evt.pixel)) {
                    map.getTargetElement().style.cursor = "pointer"
                } else {
                    map.getTargetElement().style.cursor = "default"
                }
            });

            let select = new Select({
                layers: [wfsLayer],
                style: new Style({
                    image: new Circle({
                        radius: 8,
                        fill: new Fill({
                            color: "#f00"
                        })
                    })
                })
            });

            let edit = new Modify({
                features: select.getFeatures()
            });

            let draw = new Draw({
                source: new Vector({
                    features: []
                }),
                type: 'Point',
                freehand: false,//自由手绘,
                stopClick: true
            });

            map.addInteraction(select);
            map.addInteraction(edit);
            map.addInteraction(draw);
            edit.setActive(false);
            draw.setActive(false);
            select.setActive(false);

            draw.on("drawend", function (e) {
                let feature = e.feature;
                that.sendWFS(feature, "insert");
            });
            select.on("select", function (e) {
                let features = e.selected;
                if (features.length > 0) {
                    let feature = features[0];
                    if (!edit.getActive()) {
                        that.sendWFS(feature, "delete");
                    }
                }
            });

            edit.on("modifyend", function (e) {
                // 把修改完成的feature暂存起来
                let features = e.features;
                let feature = features.item(0);
                that.sendWFS(feature, "update");
            });
            this.select = select
            this.edit = edit
            this.draw = draw
        },
        insertClick() {
            this.select.setActive(false)
            this.edit.setActive(false)
            this.draw.setActive(true);
        },
        updateClick() {
            this.draw.setActive(false)
            this.edit.setActive(true);
            this.select.setActive(true);
        },
        deleteClick() {
            this.draw.setActive(false)
            this.edit.setActive(false)
            this.select.setActive(true);
        },
        endClick() {
            this.draw.setActive(false)
            this.select.setActive(false)
            this.edit.setActive(false)
        },
        async sendWFS(feature, type) {
            //发送WFS 请求 ，type 分别为insert update delect
            // 1、构造Feature
            let ft = feature.clone()
            // 更新操作必须要有id
            let id = feature.getId()
            let properties = ft.getProperties()
            let featObject;
            let WFSTSerializer = new WFS();
            let _this = this.geoserverData;
            let options = {
                featureNS: _this.uri,
                featurePrefix: _this.wsName, //工作空间名称
                featureType: _this.layer, //图层名称
                srsName: 'EPSG:3857'
            };
            if (type === "insert") {
                properties.address = "测试要素添加"
                properties.name = "添加"
                properties.porttype = "wnm添加"
                properties.province = "wnm添加"
                let coordinates = ft.getGeometry().getCoordinates()
                properties.lng = coordinates[0]
                properties.lat = coordinates[1]
                ft.setProperties(properties)

                let geom = ft.getGeometry()
                geom.applyTransform((flatCoordinates, flatCoordinates2, stride) => {
                    for (let j = 0; j < flatCoordinates.length; j += stride) {
                        let y = flatCoordinates[j]
                        let x = flatCoordinates[j + 1]
                        flatCoordinates[j] = x
                        flatCoordinates[j + 1] = y
                    }
                })
                ft.setGeometryName('geom') // postgis字段为geom、shp字段名为the_geom
                ft.setGeometry(geom)

                featObject = WFSTSerializer.writeTransaction([ft], null, null, options);
            } else if (type === "update") {
                ft.setId(id)
                properties.address = "测试要素修改"
                properties.name = "修改"

                let coordinates = ft.getGeometry().getCoordinates()
                properties.lng = coordinates[0]
                properties.lat = coordinates[1]
                ft.setProperties(properties)

                ft.getGeometry().applyTransform((flatCoordinates, flatCoordinates2, stride) => {
                    for (let j = 0; j < flatCoordinates.length; j += stride) {
                        let y = flatCoordinates[j]
                        let x = flatCoordinates[j + 1]
                        flatCoordinates[j] = x
                        flatCoordinates[j + 1] = y
                    }
                })
                ft.setGeometryName('geom')// postgis字段为geom、shp字段名为the_geom

                featObject = WFSTSerializer.writeTransaction(null, [ft], null, options);
            } else if (type === "delete") {
                ft.setId(id)
                featObject = WFSTSerializer.writeTransaction(null, null, [ft], options);
            }
            let serializer = new XMLSerializer();
            let featString = serializer.serializeToString(featObject);
            const { data: res } = await this.$http.post(this.geoserverData.wfsURL,
                featString,
                {
                    headers: {
                        //根据接扣文档需要加的请求头
                        "Content-Type": "application/xml"
                    }
                })
            if (res.indexOf("Exception") !== -1) {
                console.log(res);
                alert("添加失败！" + res)
            }
            else {
                alert("添加成功！")
                window.location.reload();
            }
        }
    }
}
</script>

<style lang="less" scoped>

</style>
