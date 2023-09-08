<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间分析</el-breadcrumb-item>
            <el-breadcrumb-item>网络分析</el-breadcrumb-item>
        </el-breadcrumb>
        <!--卡片区域-->
        <el-card>
            <div id="map"></div>
            <h3>请在地图上点击起点和终点 <a href="https://zhuanlan.zhihu.com/p/82225790">参考链接</a></h3>
            <button id="but" @click="clickPgr">导航</button>
        </el-card>
    </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import Feature from 'ol/feature';
import Point from 'ol/geom/point';
import TileLayer from 'ol/layer/tile';
import VectorLayer from 'ol/layer/vector';
import TileWMS from 'ol/source/tilewms';
import Vector from 'ol/source/vector';
import GeoJSON from 'ol/format/geojson';
import Style from 'ol/style/style';
import Icon from 'ol/style/icon';
import Stroke from 'ol/style/stroke';
import loadingstrategy from 'ol/loadingstrategy';
import {getTdtLayer} from "../../utils/searchUtils";
export default {
    data() {
        return {
            url: process.env.VUE_APP_GEOSERVER,
            // 路网图层
            lyr: 'webgistest:shenzhen_roads',
            // 导航图层
            pgr_lyr: 'webgistest:pgr_shenzhen_roads',
            map: null,
            wfs: null,
            //图片图层
            iconLayer: [null, null],
            coord: [],
            number: 0,
            img1: require('../../assets/image/1.png'),
            img2: require('../../assets/image/2.png')
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
            let roadLayer = new TileLayer({
                source: new TileWMS({
                    url: that.url + 'geoserver/wms',
                    params: { 'LAYERS': that.lyr, 'TILED': true },
                    serverType: 'geoserver'
                })
            })
            let map = new Map({
                target: "map",
                layers: [
                  getTdtLayer("vec_w"),
                  getTdtLayer("cva_w"),
                  roadLayer
                ],
                view: new View({
                    center: [12702592.2, 2590070.3],
                    projection: 'EPSG:3857',
                    zoom: 12
                })
            });
            let coord = [[12677354.9, 2578172.3], [12677441.2, 2577908.3]];
            this.number = 1;
            map.on('singleclick', function (e) {
                switch (that.number % 3) {
                    case 1:
                        coord[0] = e.coordinate;
                        that.addIcon(e.coordinate, that.img1);
                        break;
                    case 2:
                        coord[1] = e.coordinate;
                        that.addIcon(e.coordinate, that.img2);
                        break;
                    case 0:
                        that.addIcon(e.coordinate, that.img1);
                        map.removeLayer(that.wfs);
                        break;
                }
                that.number++;
            });
            this.map = map
            this.coord = coord
        },
        wfsLayer(coord) {
            const that = this
            if (this.wfs)
                this.map.removeLayer(this.wfs);
            let wfs = new VectorLayer({
                title: 'add Layer',
                source: new Vector({
                    format: new GeoJSON(),
                    url: function (extent) {
                        return (
                            that.url + 'geoserver/wfs?service=WFS&version=1.1.0&request=GetFeature&typeName=' + that.pgr_lyr + '&'
                            + 'viewparams=x1:' + coord[0][0] + ';y1:' + coord[0][1] + ';x2:' + coord[1][0] + ';y2:' + coord[1][1] + '&'
                            + 'outputFormat=application/json&srsname=EPSG:3857&bbox=' +
                            extent.join(',') + ',EPSG:3857');
                    },
                    strategy: loadingstrategy.bbox
                }),
                style: new Style({
                    stroke: new Stroke({
                        color: 'rgb(255,111,0)',
                        width: 6
                    })
                })
            });
            this.wfs = wfs
            return wfs;
        },
        //点击导航事件
        clickPgr() {
            this.map.addLayer(this.wfsLayer(this.coord));
        },
        //图层上添加图标
        addIcon(coord, url) {
            //矢量标注的数据源
            let vectorSource = new Vector({
                features: [new Feature({ geometry: new Point(coord) })]
            });
            //矢量标注图层
            let icon = new VectorLayer({
                source: vectorSource,
                style: new Style({
                    image: new Icon({
                        src: url //图标的url
                    })
                })
            });
            console.log(this.number % 3)
            switch (this.number % 3) {
                case 1:
                    this.iconLayer[0] = icon;
                    this.map.addLayer(icon);
                    break;
                case 2:
                    this.iconLayer[1] = icon;
                    this.map.addLayer(icon);
                    break;
                case 0:
                    this.map.removeLayer(this.iconLayer[0]);
                    this.map.removeLayer(this.iconLayer[1]);
                    break;
            }
        }
    }
}
</script>

<style>
h3 {
    position: absolute;
    left: 50%;
    top: 120px;
}

#but {
    position: absolute;
    left: 300px;
    top: 130px;
    width: 80px;
    height: 40px;
}
</style>
