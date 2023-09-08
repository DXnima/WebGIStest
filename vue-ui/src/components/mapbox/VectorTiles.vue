<template>
    <div>
        <!--面包屑导航区域-->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>GeoServer</el-breadcrumb-item>
            <el-breadcrumb-item>加载PostGIS矢量瓦片</el-breadcrumb-item>
        </el-breadcrumb>
        <!--卡片区域-->
        <el-card>
            <div id='map' style='width: 100%; height: 1080px;'>
                <div id="zoom"></div>
            </div>
        </el-card>
    </div>
</template>
<script>
const mapboxgl = require('mapbox-gl');  //引入组件
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
            mapboxgl.accessToken = 'pk.eyJ1IjoiZHhuaW1hIiwiYSI6ImNrbzA3YXM3YjBiM20yb21zd21hNmZ2YmkifQ.E-CP-z0iDN9RcMywNydpfA';
            let url = process.env.VUE_APP_BASE_API + 'mvt_test/{z}/{x}/{y}.pbf'
            let map = new mapboxgl.Map({
                container: 'map', // container id
                style: {
                    "version": 8,
                    "name": "Empty Style",
                    "metadata": { "maputnik:renderer": "mbgljs" },
                    "sources": {
                        "mvt_test": {
                            "type": "vector",
                            "tiles": [url],
                            "minZoom": 0,
                            "maxZoom": 5
                        }
                    },
                    "sprite": "",
                    "glyphs": "https://orangemug.github.io/font-glyphs/glyphs/{fontstack}/{range}.pbf",
                    "layers": [
                      {
                        "id": "china",
                        "type": "fill",
                        "source": "mvt_test",
                        "source-layer": "china",
                        "paint": {"fill-color": "rgba(195, 195, 195, 1)"}
                      },
                      {
                        "id": "university",
                        "type": "circle",
                        "source": "mvt_test",
                        "source-layer": "university",
                        "paint": {
                          "circle-color": "rgba(255, 54, 54, 1)",
                          "circle-stroke-color": "rgba(255, 32, 32, 1)"
                        }
                      },
                      {
                        "id": "capital",
                        "type": "circle",
                        "source": "mvt_test",
                        "source-layer": "capital",
                        "paint": {"circle-stroke-color": "rgba(247, 125, 125, 1)"}
                      }
                    ],
                    "id": "waigzs8za"
                },
                zoom: 2,
                center: [108.438, 34.431]
            });
            //当鼠标滚动时，触发事件，获取当前地图缩放级别
            map.on("wheel", function () {
                let range = map.getZoom();
                document.getElementById("zoom").innerHTML = "zoom:" + parseInt(range);
                console.log(range);
            });
        }
    }
}
</script>

<style>
#zoom {
    position: absolute;
    z-index: 999;
    background: blanchedalmond;
    font-size: 24px;
}
</style>
