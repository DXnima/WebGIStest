<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>WFS</el-breadcrumb-item>
            <el-breadcrumb-item>WFS综合查询</el-breadcrumb-item>
        </el-breadcrumb>
        <!--卡片区域-->
        <el-card>
            <div class="div_auto">
                <el-autocomplete :fetch-suggestions="querySearchAsync" placeholder="请输入搜索内容" value-key="name"
                    style="width: 250px" @select="handleSelect" @change="handleInput" v-model="input"></el-autocomplete>
            </div>
            <div class="div_button" style="left: 580px">
                <el-select v-model="type" placeholder="请选择查询的图层">
                    <el-option v-for="item in selectLayer" :key="item.value" :label="item.name" :value="item.name">
                    </el-option>
                </el-select>
            </div>
            <div class="div_button" style="right: 50px">
                <el-form ref="form" :model="nearForm" label-width="80px">
                    <el-form-item label="输入圆心">
                        <el-input v-model="nearForm.lnglat" placeholder="请输入经纬度"></el-input>
                    </el-form-item>
                    <el-form-item label="输入半径">
                        <el-input v-model="nearForm.radius"></el-input>
                    </el-form-item>
                    <el-form-item label="地图显示">
                        <el-switch v-model="nearForm.show"></el-switch>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="searchNearBy">周边搜索</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div id="map"></div>
        </el-card>
    </div>
</template>
<script>
import { getSearch, getSearchData, addGeoJSON, spaceFilter } from "@/utils/searchUtils";
import Map from 'ol/map';
import View from 'ol/view';
import Circle from 'ol/geom/circle';
import Polygon from 'ol/geom/polygon';
import TileLayer from 'ol/layer/tile';
import TileWMS from 'ol/source/tilewms';
import Projection from 'ol/proj/projection';
import control from 'ol/control';
import {getTdtLayer} from "../../../utils/searchUtils";
export default {
    data() {
        return {
            // 服务配置，命名空间、图层、服务地址等
            url: process.env.VUE_APP_GEOSERVER,//BaseURL
            map: null,//地图对象
            input: '',
            restaurants: [],//搜素下拉数据存储
            type: '查询所有信息',//选择查询类型
            selectLayer: [
                { name: '查询地名信息(poi)', value: 'poi' },
                { name: '查询地标信息(landmarks)', value: 'landmarks' },
                { name: '查询道路信息(roads)', value: 'roads' },
                { name: '查询所有信息', value: '' }
            ],
            nearForm: {
                lnglat: '-73.98,40.76',//圆心
                radius: 500,//半径
                show: true
            },
        }
    },
    created() {
    },
    mounted() {
        this.initMap()
    },
    methods: {
        initMap() {
            let that = this;
            let view = new View({
                zoom: 13,
                projection: 'EPSG:4326',
                center: [-73.98, 40.79],
            });
            //加载图层
            let tileWms = new TileLayer({
                source: new TileWMS({
                    url: this.url + 'geoserver/wms',
                    params: {
                        'FORMAT': 'image/png',
                        tiled: true,
                        LAYERS: ['tiger:poly_landmarks', 'tiger:tiger_roads', 'tiger:poi'],
                        tilesOrigin: -74.047185 + "," + 40.679648
                    }
                })
            });
            this.map = new Map({
                controls: control.defaults({
                    attribution: false
                }).extend([]),
                target: "map",
                layers: [
                  getTdtLayer("vec_w"),
                  getTdtLayer("cva_w"), tileWms
                ],
                view: view
            });
            this.map.on("singleclick", function (evt) {
                //获取经纬度
                that.nearForm.lnglat = evt.coordinate.toString();
            });
        },
        querySearchAsync(queryString, cb) {
            console.log(queryString);
            let that = this
            //关键字搜索
            getSearch(this.url, this.type, queryString, 5).then(data => {
                console.log('关键字搜索结果', data)
                that.restaurants = []
                //整理GeoJSON数据
                that.restaurants = getSearchData(data);
                cb(that.restaurants)
            })
        },
        //点击指定结果定位指定位置
        handleSelect(item) {
            addGeoJSON(this.map, item.id);
        },
        //输入事件
        handleInput(str) {
            this.querySearchAsync(str)
        },
        //根据中心点经纬度、半径以及关键字进行周边查询
        searchNearBy() {
            //绘制圆
            //设置单位为M
            let projection = new Projection({
                code: 'EPSG:4326',
                units: 'degrees',
                global: true
            });
            let meters = projection.getMetersPerUnit();
            let circle = new Circle(this.nearForm.lnglat.split(',').map(Number), this.nearForm.radius / meters);
            let geometry = new Polygon.fromCircle(circle);
            console.log(geometry);
            spaceFilter(this.map, this.url, this.type, 10, geometry).then((data) => {
                console.log('空间查询结果', data)
                if (this.nearForm.show) addGeoJSON(this.map, '');
            })
        }
    }
}
</script>
<style>
.div_auto {
    position: absolute;
    z-index: 1;
    left: 300px;
}

.div_button {
    position: absolute;
    z-index: 999;
}

.el-form-item__label {
    width: 90px;
    color: #2c2c2c;
}
</style>
