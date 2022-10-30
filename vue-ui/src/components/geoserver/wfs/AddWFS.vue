<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>WFS</el-breadcrumb-item>
            <el-breadcrumb-item>WFS添加数据</el-breadcrumb-item>
        </el-breadcrumb>
        <!--卡片区域-->
        <el-card>
            <button id="drawTool" @click="drawClick">添加</button>
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
                wfsURL: 'http://localhost:28081/geoserver/wfs?',
                layer: 'port'
            },
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
            let that = this
            //测试数据 添加WFS数据  添加所有
            var wfsSource = new Vector({
                format: new GeoJSON(),
                url: function (extent) {
                    return (
                        that.geoserverData.wfsURL + 'service=WFS&version=1.1.0&request=GetFeature&typeName=' +
                        that.geoserverData.wsName + ":" +
                        that.geoserverData.layer + '&' + 'outputFormat=application/json&srsname=EPSG:4326&bbox=' +
                        extent.join(',') + ',EPSG:4326');
                },
                strategy: loadingstrategy.bbox
            });

            let wfsLayer = new VectorLayer({
                title: 'add Layer',
                source: wfsSource,
                style: function (feature) {
                    return new Style({
                        geometry: feature.getGeometry(),
                        image: new Circle({
                            radius: 5,
                            fill: new Fill({
                                color: "red"
                            })
                        })
                    })
                },
            })

            var layers = [
              getTdtLayer("vec_w"),
              getTdtLayer("cva_w"), wfsLayer
            ];

            var map = new Map({
                target: 'map',
                layers: layers,
                view: new View({
                    projection: "EPSG:4326",
                    center: [114, 31],
                    zoom: 4
                })
            });

            var draw = new Draw({
                source: new Vector({
                    features: []
                }),
                type: 'Point',
                freehand: false,//自由手绘,
                stopClick: true
            });
            map.addInteraction(draw);
            draw.setActive(false);

            draw.on("drawend", function (e) {
                draw.setActive(false);
                var feature = e.feature;
                that.addFeature(feature);
                wfsLayer.getSource().changed();
            });
            this.draw = draw
        },
        drawClick() {
            this.draw.setActive(true)
        },
        async addFeature(feature) {
            // 1、构造Feature
            let ft = feature.clone()
            let properties = ft.getProperties()
            properties.address = "测试要素添加"
            properties.name = "添加"
            properties.porttype = "wnm添加"
            properties.province = "wnm添加"
            let coordinates = ft.getGeometry().getCoordinates()
            properties.lat = coordinates[0]
            properties.lng = coordinates[1]
            ft.setProperties(properties)

            let geom = ft.getGeometry()
            // 避免出现报错PointOutsideEnvelopeException: 1 outside of (-90.0,90
            // 进行经纬度调换
            geom.applyTransform((flatCoordinates, flatCoordinates2, stride) => {
                for (var j = 0; j < flatCoordinates.length; j += stride) {
                    var y = flatCoordinates[j]
                    var x = flatCoordinates[j + 1]
                    flatCoordinates[j] = x
                    flatCoordinates[j + 1] = y
                }
            })
            ft.setGeometryName('geom') // postgis字段为geom、shp字段名为the_geom
            ft.setGeometry(geom)
            // 2、更新到后台
            let WFSTSerializer = new WFS();
            var featObject = WFSTSerializer.writeTransaction([ft],
                null, null, {
                featureNS: this.geoserverData.uri,
                featurePrefix: this.geoserverData.wsName,//工作空间名称
                featureType: this.geoserverData.layer,//图层名称
                srsName: 'EPSG:4326'
            })
            var serializer = new XMLSerializer()
            var featString = serializer.serializeToString(featObject);
            const { data: res } = await this.$http.post(this.geoserverData.wfsURL,
                featString,
                {
                    headers: {
                        //根据接扣文档需要加的请求头
                        "Content-Type": "application/xml"
                    }
                })
            if (res.indexOf("Exception") != -1) {
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
