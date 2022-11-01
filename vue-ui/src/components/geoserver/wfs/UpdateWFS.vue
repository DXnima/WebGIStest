<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>WFS</el-breadcrumb-item>
            <el-breadcrumb-item>WFS更新数据</el-breadcrumb-item>
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
            let that = this
            //测试数据 添加WFS数据  添加所有
            let wfsSource = new Vector({
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
                },
            })

            let layers = [
              getTdtLayer("vec_w"),
              getTdtLayer("cva_w"), wfsLayer
            ];

            let map = new Map({
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
                            color: "#f00"
                        })
                    })
                })
            });

            let edit = new Modify({
                features: select.getFeatures()
            });

            map.addInteraction(select);
            map.addInteraction(edit);
            edit.setActive(false);
            let feature
            select.on("select", function (e) {
                let features = e.selected;
                if (features.length > 0) {
                    feature = features[0];
                    wfsLayer.getSource().changed();
                }
            });

            edit.on("modifyend", function (e) {
                // 把修改完成的feature暂存起来
                let features = e.features;
                let feature = features.item(0);
                that.updateFeature(feature);
            });

            map.on('click', function (evt) {
                if (!map.hasFeatureAtPixel(evt.pixel)) {
                    edit.setActive(false);
                } else {
                    edit.setActive(true);
                }
            });
            map.on('pointermove', function (evt) {
                if (map.hasFeatureAtPixel(evt.pixel)) {
                    map.getTargetElement().style.cursor = "pointer"
                } else {
                    map.getTargetElement().style.cursor = "default"
                }
            });
        },
        async updateFeature(feature) {
            // 1、构造Feature
            let ft = feature.clone()
            // 更新操作必须要有id
            let id = feature.getId()
            ft.setId(id)
            let properties = ft.getProperties()
            properties.address = "测试要素修改"

            let coordinates = ft.getGeometry().getCoordinates()
            properties.lng = coordinates[0]
            properties.lat = coordinates[1]

            ft.setProperties(properties)

            //经纬度换位置
            ft.getGeometry().applyTransform((flatCoordinates, flatCoordinates2, stride) => {
                for (let j = 0; j < flatCoordinates.length; j += stride) {
                    let y = flatCoordinates[j]
                    let x = flatCoordinates[j + 1]
                    flatCoordinates[j] = x
                    flatCoordinates[j + 1] = y
                }
            })

            ft.setGeometryName('geom')// postgis字段为geom、shp字段名为the_geom

            // 2、更新到后台
            let WFSTSerializer = new WFS()
            let featObject = WFSTSerializer.writeTransaction(null,
                [ft], null, {
                featureType: this.geoserverData.layer,
                featureNS: this.geoserverData.uri,
                srsName: 'EPSG:4326'
            })
            let serializer = new XMLSerializer()
            let featString = serializer.serializeToString(featObject)
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
