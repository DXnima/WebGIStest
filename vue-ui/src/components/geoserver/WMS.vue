<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>加载WMS</el-breadcrumb-item>
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
import ImageLayer from 'ol/layer/image';
import ImageWMS from 'ol/source/imagewms';
import control from 'ol/control';
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
        initMap() {
            let view = new View({
                zoom: 4,
                center: [267, 38],
                projection: "EPSG:4326"
            });
            let wms = new ImageLayer({
                source: new ImageWMS({
                    ratio: 1,
                    url: 'http://localhost:28081/geoserver/wms',
                    params: {
                        'FORMAT': 'image/png',
                        'VERSION': '1.1.1',
                        LAYERS: 'topp:states'
                    }
                })
            });
            let map = new Map({
                controls: control.defaults({
                    attribution: false
                }).extend([]),
                target: "map",
                layers: [
                    wms
                ],
                view: view
            });
        }
    }
}
</script>

<style lang="less" scoped>

</style>
