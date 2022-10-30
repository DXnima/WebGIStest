<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>WebGIS系统模板</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div class="base">
        <nav class="layout layout-nav">
          <span class="nav-title">XXXXXX地理信息系统</span>
        </nav>
        <div class="layout layout-main">
          <div class="main-layer"></div>
          <div class="main-map">
            <ul class="map-tools">
              <li>全图</li>
              <li>放大</li>
              <li>缩小</li>
              <li>打印</li>
            </ul>
            <ul class="map-timeline">
              <li day-label="周日" class="active"></li>
              <li day-label="周一"></li>
              <li day-label="周二"></li>
              <li day-label="周三"></li>
              <li day-label="周四"></li>
              <li day-label="周五"></li>
              <li day-label="周六"></li>
            </ul>
          </div>
        </div>
        <footer class="layout layout-footer">
          @Copy Right LZUGIS
        </footer>
      </div>
    </el-card>
  </div>
</template>
<script>
export default {
  data() {
    return {}
  },
  created() {
  },
  methods: {}
}
</script>

<style lang="less" scoped>
.base {
  width: 100%;
  height: 1080px;
  overflow: hidden;
  padding: 0;
  margin: 0;
  font-size: 12px;
}

@keyframes load {
  from {
    -webkit-transform: rotate(0deg);
  }

  to {
    -webkit-transform: rotate(360deg);
  }
}

.load-modal {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.45);
  z-index: 99;
}

.load-modal .load-loading {
  position: absolute;
  left: calc(50% - 35px);
  top: calc(50% - 35px);
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: 10px solid #fff;
  border-right: 10px solid #c6270e;
  animation: load 1s linear infinite;
}

.layout {
  overflow: hidden;
  padding: 0;
  margin: 0;
  width: 100%;
}

.layout-nav,
.layout-footer {
  background-color: #c6270e;
  color: white;
  height: 50px;
  line-height: 50px;
}

.layout-nav .nav-title {
  padding-left: 10px;
  font-size: 20px;
}

.layout-main {
  height: calc(100% - 80px);
}

.layout-main div {
  float: left;
  box-shadow: 1px 1px 2px #ccc;
  height: 100%;
}

.layout-main div.main-layer {
  width: 220px;
}

.layout-main div.main-map {
  width: calc(100% - 220px);
  position: relative;
}

.layout-main div.main-map .map-tools {
  position: absolute;
  right: 10px;
  top: 10px;
  z-index: 9;
  margin: 0;
  padding: 0;
  list-style: none;
  overflow: hidden;
  background-color: white;
  box-shadow: 1px 1px 2px #ccc;
}

.layout-main div.main-map .map-tools li {
  float: left;
  padding: 6px 10px;
}

.layout-main div.main-map .map-tools li:not(:first-child) {
  border-left: 1px solid #ccc;
}

.layout-main div.main-map .map-tools li:hover {
  cursor: pointer;
  background-color: #cccccc;
}

.map-timeline {
  position: absolute;
  bottom: 10px;
  right: 10px;
  box-shadow: 1px 1px 2px #ccc;
  background-color: white;
  z-index: 9;
  width: 450px;
  height: 50px;
  padding: 0 10px;
  margin: 0;
  list-style: none;
  text-align: center;
}

.map-timeline li {
  display: inline-block;
  width: 60px;
  position: relative;
  height: 50px;
}

.map-timeline li.active:after {
  color: #c6270e;
  font-weight: bold;
}

.map-timeline li:before {
  position: absolute;
  top: 32px;
  left: calc(50% - 6px);
  z-index: 99;
  width: 6px;
  height: 6px;
  content: " ";
  background-color: #c6270e;
  border-radius: 50%;
  border: 3px solid #fff;
}

.map-timeline li:after {
  position: absolute;
  top: 14px;
  left: 0;
  content: attr(day-label);
  z-index: 99;
  width: 100%;
  text-align: center;
}

.map-timeline:after {
  content: " ";
  position: absolute;
  top: 35px;
  left: 10px;
  width: 450px;
  height: 5px;
  background-color: #c6270e;
  border-radius: 10px;
}

.layout-footer {
  height: 30px;
  line-height: 30px;
  text-align: center;
}
</style>
