<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>WFS</el-breadcrumb-item>
            <el-breadcrumb-item>加载WFS</el-breadcrumb-item>
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
import TileLayer from 'ol/layer/tile';
import VectorLayer from 'ol/layer/vector';
import OSM from "ol/source/osm";
import Vector from 'ol/source/vector';
import WFS from 'ol/format/wfs';
import GeoJSON from 'ol/format/geojson';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Fill from 'ol/style/fill';
import {getTdtLayer} from "../../../utils/searchUtils";
export default {
    data() {
        return {}
    },
    created() {
    },
    mounted() {
        this.initMap()
    },
    methods: {
        async initMap() {
            // 服务配置，命名空间、图层、服务地址等
            let geoserverData = {
                wsName: 'topp',
                uri: 'http://www.openplans.org/topp',
                wfsURL: process.env.VUE_APP_GEOSERVER + 'geoserver/wfs?',
                layer: 'states'
            }

            let layers = [
                new TileLayer({
                    source: new OSM()
                })
            ];

            let map = new Map({
                target: 'map',
                layers: [
                  getTdtLayer("vec_w"),
                  getTdtLayer("cva_w")
                ],
                view: new View({
                    projection: "EPSG:4326",
                    center: [267, 38],
                    zoom: 4
                })
            });

            // 通过wfs加载数据
            let data = {
                srcName: 'EPSG:4326',
                featureNS: geoserverData.uri,
                featurePrefix: geoserverData.wsName,
                featureTypes: [geoserverData.layer],
                outputFormat: 'application/json'
            }
            let request = new WFS().writeGetFeature(data);
            const { data: res } = await this.$http.post(geoserverData.wfsURL,
                new XMLSerializer().serializeToString(request),
                {
                    headers: {
                        //根据接扣文档需要加的请求头
                        "Content-Type": "application/xml"
                    }
                })
            if (!res) return
            let features = new GeoJSON().readFeatures(res)
            let source = new Vector();
            source.addFeatures(features)
            let wfsLayer = new VectorLayer({
                title: 'add Layer',
                source: source,
                style: new Style({
                    stroke: new Stroke({
                        color: 'blue',
                        lineDash: [4],
                        width: 3,
                    }),
                    fill: new Fill({
                        color: 'rgba(0,0,255,0.47)',
                    }),
                })
            })
            map.addLayer(wfsLayer)
            alert("加载成功！")
        }
    }
}
</script>

<style lang="less" scoped>

</style>
