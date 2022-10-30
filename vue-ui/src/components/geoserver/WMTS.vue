<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>加载WMTS</el-breadcrumb-item>
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
import WMTSGrid from 'ol/tilegrid/wmts';
import WMTS from 'ol/source/wmts';
import Projection from 'ol/proj/projection';
import control from 'ol/control';
import {getTdtLayer} from "../../utils/searchUtils";
export default {
    data() {
        return {
            projection: null
        }
    },
    created() {
    },
    mounted() {
        this.initMap()
    },
    methods: {
        initMap() {
          var projection = new Projection({
            code: 'EPSG:4326',
            units: 'degrees',
            axisOrientation: 'neu'
          });
          var view = new View({
            zoom: 2,
            center: [-97, 38],
            projection: projection
          });

          var map = new Map({
            controls: control.defaults({
              attribution: false
            }).extend([]),
            target: "map",
            layers: [
              getTdtLayer("vec_w"),
              getTdtLayer("cva_w"),
              this.getTileLayer('topp:states')
            ],
            view: view
          });
          this.projection = projection
        },
        getTileLayer(lyr) {
            var gridsetName = 'EPSG:4326';
            var gridNames = ['EPSG:4326:0', 'EPSG:4326:1', 'EPSG:4326:2', 'EPSG:4326:3', 'EPSG:4326:4', 'EPSG:4326:5', 'EPSG:4326:6', 'EPSG:4326:7', 'EPSG:4326:8', 'EPSG:4326:9', 'EPSG:4326:10', 'EPSG:4326:11', 'EPSG:4326:12', 'EPSG:4326:13', 'EPSG:4326:14', 'EPSG:4326:15', 'EPSG:4326:16', 'EPSG:4326:17', 'EPSG:4326:18'];
            var baseUrl = 'http://localhost:28081/geoserver/gwc/service/wmts';
            var format = 'image/png';
            var layerName = lyr;
            var resolutions = [0.703125, 0.3515625, 0.17578125, 0.087890625, 0.0439453125, 0.02197265625, 0.010986328125, 0.0054931640625, 0.00274658203125, 0.001373291015625, 6.866455078125E-4, 3.4332275390625E-4, 1.71661376953125E-4, 8.58306884765625E-5, 4.291534423828125E-5, 2.1457672119140625E-5, 1.0728836059570312E-5, 5.364418029785156E-6, 2.682209014892578E-6];
            var baseParams = ['VERSION', 'LAYER', 'STYLE', 'TILEMATRIX', 'TILEMATRIXSET', 'SERVICE', 'FORMAT'];
            var params = {
                'VERSION': '1.0.0',
                'LAYER': layerName,
                'TILEMATRIX': gridNames,
                'TILEMATRIXSET': gridsetName,
                'SERVICE': 'WMTS',
                'FORMAT': format
            };

            var url = baseUrl + '?';
            for (var param in params) {
                if (baseParams.indexOf(param.toUpperCase()) < 0) {
                    url = url + param + '=' + params[param] + '&';
                }
            }
            url = url.slice(0, -1);

            var source = new WMTS({
                url: url,
                layer: params['LAYER'],
                matrixSet: params['TILEMATRIXSET'],
                format: params['FORMAT'],
                projection: this.projection,
                tileGrid: new WMTSGrid({
                    tileSize: [256, 256],
                    extent: [-180.0, -90.0, 180.0, 90.0],
                    origin: [-180.0, 90.0],
                    resolutions: resolutions,
                    matrixIds: params['TILEMATRIX']
                }),
                style: params['STYLE'],
                wrapX: true
            });
            var layer = new TileLayer({
                source: source
            });
            return layer;
        }
    }
}
</script>

<style lang="less" scoped>

</style>
