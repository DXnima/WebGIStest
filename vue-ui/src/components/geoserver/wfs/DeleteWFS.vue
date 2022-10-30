<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>WFS</el-breadcrumb-item>
            <el-breadcrumb-item>WFS删除数据</el-breadcrumb-item>
        </el-breadcrumb>
        <!--卡片区域-->
        <el-card>
            <div id="map"></div>
        </el-card>
    </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import VectorLayer from 'ol/layer/vector';
import Vector from 'ol/source/vector';
import GeoJSON from 'ol/format/geojson';
import WFS from 'ol/format/wfs';
import Select from 'ol/interaction/select';
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
            }
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
                                color: "blue"
                            })
                        })
                    })
                }
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

            let select = new Select({
                layers: [wfsLayer],
                style: new Style({
                    image: new Circle({
                        radius: 8,
                        fill: new Fill({
                            color: "red"
                        })
                    })
                })
            });

            select.on("select", function (e) {
                var features = e.selected;
                if (features.length > 0) {
                    var feature = features[0];
                    that.deleteFeature(feature);
                    wfsLayer.getSource().changed();
                }
            });

            map.addInteraction(select);

            map.on('pointermove', function (evt) {
                if (map.hasFeatureAtPixel(evt.pixel)) {
                    map.getTargetElement().style.cursor = "pointer"
                } else {
                    map.getTargetElement().style.cursor = "default"
                }
            });
        },
        async deleteFeature(feature) {
            // 1、构造Feature
            let ft = feature.clone()
            ft.setId(feature.getId());
            // 2、更新到后台
            let WFSTSerializer = new WFS()
            var featObject = WFSTSerializer.writeTransaction(null,
                null, [ft], {
                featureNS: this.geoserverData.uri,
                featurePrefix: this.geoserverData.wsName,//工作空间名称
                featureType: this.geoserverData.layer,//图层名称
                srsName: 'EPSG:4326'
            });
            var serializer = new XMLSerializer()
            var featString = serializer.serializeToString(featObject)
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
                alert("删除失败！" + res)
            }
            else {
                alert("删除成功！")
                window.location.reload();
            }
        }
    }
}
</script>

<style lang="less" scoped>

</style>
