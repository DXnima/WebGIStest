<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>WFS</el-breadcrumb-item>
            <el-breadcrumb-item>WFS查询数据</el-breadcrumb-item>
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
import filter from 'ol/format/filter';
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
            wfsFilter: null
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
                title: 'add WFS',
                source: wfsSource,
                style: new Style({
                    image: new Circle({
                        radius: 5,
                        fill: new Fill({
                            color: "#389BCD",
                            opacity: 0.5,
                        }),
                    }),
                })
            })

            let wfsFilter = new VectorLayer({
                source: new Vector(),
                style: new Style({
                    image: new Circle({
                        radius: 5,
                        fill: new Fill({
                            color: "#d90000",
                            opacity: 0.5,
                        }),
                    }),
                })
            });

            let layers = [
              getTdtLayer("vec_w"),
              getTdtLayer("cva_w"), wfsLayer, wfsFilter
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
            this.wfsFilter = wfsFilter
            this.findData()
        },
        // 查询过滤图层
        findData() {
            const that = this
            let data = {
                srcName: 'EPSG:4326',
                featureNS: this.geoserverData.uri,
                featurePrefix: this.geoserverData.wsName,
                featureTypes: [this.geoserverData.layer],
                outputFormat: 'application/json',
                // 查询方式就和写 SQL 一样
                filter: filter.and(
                    filter.like('porttype', '沿海'),
                    filter.equalTo('name', '珠海港')
                )
            }
            let request = new WFS().writeGetFeature(data)
            fetch(that.geoserverData.wfsURL, {
                method: 'POST',
                body: new XMLSerializer().serializeToString(request),
            }).then(function (response) {
                return response.json()
            }).then(function (json) {
                console.log(JSON.stringify(json, null, "\t"))
                let features = new GeoJSON({
                    geometryName: 'geom',
                }).readFeatures(json)
                if (that.wfsFilter) {
                    that.wfsFilter.getSource().addFeatures(features)
                }
            })
        }
    }
}
</script>

<style lang="less" scoped>

</style>
