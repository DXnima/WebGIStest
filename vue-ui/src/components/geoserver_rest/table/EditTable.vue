<template>
  <div>
    <div id="map" style="max-height: 640px;"></div>
    <el-card>
      <!--输入框和添加按钮-->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable @clear="getQueryData" @input="inputChange">
            <el-button slot="append" icon="el-icon-search" @click="getFindData"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addFeatures">添加</el-button>
          <el-button type="primary" @click="removeCheck">批量删除</el-button>
        </el-col>
      </el-row>
      <!--分页-->
      <el-pagination v-if="datas !== null" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pageNum"
                     :page-sizes="[5, 10, 15, 30, 100]" :page-size="queryInfo.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
      <!--列表-->
      <vxe-table ref="xTable" class="mytable-style" border stripe highlight-hover-row
                 :data="datas" :edit-config="{trigger: 'manual', mode: 'row', autoClear: false}"
                 size="mini" :row-class-name="rowClassName" max-height="350">
        <vxe-table-column type="checkbox" width="40"></vxe-table-column>
        <vxe-table-column title="操作" width="140">
          <template #default="{ row }">
            <template v-if="$refs.xTable.isActiveByRow(row)">
              <el-tooltip effect="dark" content="保存" placement="bottom">
                <el-button type="success" size="mini" circle  icon="el-icon-s-order" @click="saveRowEvent(row)"></el-button>
              </el-tooltip>
              <el-tooltip effect="dark" content="取消" placement="bottom">
                <el-button type="info" size="mini" circle  icon="el-icon-close" @click="cancelRowEvent(row)"></el-button>
              </el-tooltip>
              <el-tooltip effect="dark" content="删除" placement="bottom">
                <el-button type="danger" size="mini" circle  icon="el-icon-delete-solid" @click="removeEvent(row)"></el-button>
              </el-tooltip>
            </template>
            <template v-else>
              <el-tooltip effect="dark" content="编辑" placement="bottom">
                <el-button circle size="mini" icon="el-icon-setting" @click="editRowEvent(row)"></el-button>
              </el-tooltip>
            </template>
          </template>
        </vxe-table-column>
        <vxe-table-column v-for="item in queryInfo.fields" v-if="data[1] !== item"
                          :field="item" :title="item" min-width="80px" height="20px"
                          show-header-overflow show-overflow="title" show-footer-overflow
                          :edit-render="{name: 'input', attrs: {type: 'text'}}">
        </vxe-table-column>
      </vxe-table>
      <el-empty v-if="datas === null" description="无数据"></el-empty>
    </el-card>
  </div>
</template>

<script>
import "ol/ol.css";
import Map from 'ol/map';
import View from 'ol/view';
import TileLayer from 'ol/layer/tile';
import VectorLayer from 'ol/layer/vector';
import OSM from "ol/source/osm";
import VectorSource from 'ol/source/vector';
import WKT from 'ol/format/wkt';
import WFS from 'ol/format/wfs';
import Draw from 'ol/interaction/draw';
import Select from 'ol/interaction/select';
import Modify from 'ol/interaction/modify';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Fill from 'ol/style/fill';
import ScaleLine from 'ol/control/scaleline';
import CircleStyle from "ol/style/circle";
import Extent from 'ol/extent';
export default {
  name: "Table",
  data() {
    return {
      data: [],//[表名,geometry字段名,geometry类型],例[china_boundry,geom,LINE]
      //存储表格数据
      datas: [],
      //表数据总条数
      total: 0,
      geoURL: process.env.VUE_APP_GEOSERVER,
      //查询数据请求表单
      queryInfo: {
        //表名
        tableName: '',
        //geometry字段名
        geom: '',
        //查询内容
        query: '',
        //页码
        pageNum: 1,
        //每页数据量
        pageSize: 10,
        //表所有字段名称
        fields: []
      },
      //修改数据行请求
      rowInfo: {
        //表名
        tableName: '',
        //geometry字段名称
        geom: '',
        //旧geometry字段值
        oldGeom: "",
        //新数据行
        newRow: {},
        //表格字段名称
        fields: []
      },
      //是否处于查询状态
      isFind: false,
      //是否处于输入状态
      isInput: false,
      //地图对象
      map: null,
      //存储更新前wkt数据，取消时使用
      oldWKT: '',
      //wkt图层对象
      wktLayer: null,
      //select查询对象
      select: null,
      //modify编辑对象
      modify: null,
      //draw绘画对象
      draw:null,
      //绘制新要素对象
      drawFeature: null,
      //是否处于编辑状态
      isDraw:false
    }
  },
  created() {
    //切割路由name参数 [表名,geometry字段名,geometry类型],例[china_boundry,geom,LINE]
    this.data = this.$route.params.name.split(',');
    //表名赋值
    this.queryInfo.tableName = this.data[0];
    //表geometry字段赋值
    this.queryInfo.geom = this.data[1];
  },
  mounted() {
    //初始化地图
    this.initMap();
    //请求获取指定表数据
    this.getTableData();
  },
  methods: {
    //初始化地图
    initMap() {
      let that = this;
      //创建地图对象
      const map = new Map({
        target: "map", //绑定页面中DOM元素的id
        layers: [
          new TileLayer({
            //数据源
            source: new OSM(), //openlayers内置的数据源
            name: "OSM地图",
          }),
        ],
        view: new View({
          projection: "EPSG:3857", //坐标系3857
          //视图配置
          center: [12233037.3, 4861921.87], //地图中心点坐标，经度、纬度
          zoom: 5, //定义地图显示层级
        }),
      });
      // 为map添加鼠标移动事件监听，当指向标注时改变鼠标光标状态
      map.on('pointermove', function (e) {
        let pixel = map.getEventPixel(e.originalEvent);
        let hit = map.hasFeatureAtPixel(pixel);
        map.getTargetElement().style.cursor = hit ? 'pointer' : '';
      });
      //添加比例尺控件
      map.addControl(new ScaleLine());
      //创建select对象
      let select = new Select();
      this.select = select;
      map.addInteraction(select);
      //select处于选择事件时
      select.on("select", function (e) {
        let feature = e.selected;
        //获取选中要素属性信息
        let row = feature[0].getProperties().info;
        //存储修改前geom数据
        that.rowInfo.oldGeom = row[that.queryInfo.geom].value;
        //表格字段赋值
        that.rowInfo.fields = that.queryInfo.fields;
        //表名赋值
        that.rowInfo.tableName = that.queryInfo.tableName;
        //geometry字段赋值
        that.rowInfo.geom = that.queryInfo.geom;
        //保存选中要素旧wkt数据
        that.oldWKT = row.wkt;
        //获取table元素
        const $table = that.$refs.xTable;
        //添加wkt图层
        that.addWKT([row]);
        //激活编辑数据行
        $table.setActiveRow(row);
        //激活要素编辑
        that.editFeatures(row);
      });
      this.map = map;

    },
    //请求获取表数据
    async getTableData() {
      //非查询状态
      this.isFind = false;
      //请求指定表格数据
      const {data: ref} = await this.$API.getTableData(this.queryInfo);
      //如果请求状态码404，则返回
      if (ref.status !== 200) return;
      //表格数据赋值
      this.datas = ref.data.data;
      //表字段名称赋值
      this.queryInfo.fields = ref.data.fields;
      //如果表数据为空 数据总条数赋值0
      if (this.datas === null || this.datas.length === 0) this.total = 0;
      else this.total = ref.data.data[0].total;
      //添加WKT图层到地图上
      this.addWKT(this.datas);
    },
    //分页大小修改事件
    handleSizeChange(e) {
      //分页数据量更改 跳转到第一页
      this.queryInfo.pageNum = 1;
      this.queryInfo.pageSize = e;
      //如果查询状态 请求查询接口
      if (this.isFind) this.getFindData();
      else this.getTableData();
    },
    //下一页分页修改事件
    handleCurrentChange(e) {
      this.queryInfo.pageNum = e;
      if (this.isFind) this.getFindData();
      else this.getTableData();
    },
    //查询输入框清除时更新数据
    getQueryData() {
      this.queryInfo.pageNum = 1;
      this.getTableData();
    },
    //请求查询表数据
    async getFindData() {
      if(!this.isFind || this.isInput) this.queryInfo.pageNum = 1;
      //输入状态
      this.isInput = false;
      //查询状态赋值为真
      this.isFind = true;
      //请求查询接口
      const {data: ref} = await this.$API.findTableData( this.queryInfo);
      if (ref.status !== 200) return;
      this.datas = ref.data;
      if (this.datas === null || this.datas.length === 0) this.total = 0;
      else this.total = ref.data[0].total;
      this.addWKT(this.datas);
    },
    //输入框事件
    inputChange(){
      this.isInput = true;
    },
    //创建样式
    styleFunction(feature){
      const image = new CircleStyle({
        radius: 4,
        fill: new Fill({
          color: 'rgb(255,0,0)',
        }),
        stroke: new Stroke({color: 'rgb(0,7,255)', width: 1}),
      });
      const styles = {
        'Point': new Style({
          image: image,
        }),
        'LineString': new Style({
          stroke: new Stroke({
            color: 'rgb(255,89,0)',
            width: 3,
          }),
        }),
        'MultiLineString': new Style({
          stroke: new Stroke({
            color: 'rgb(255,89,0)',
            width: 3,
          }),
        }),
        'MultiPoint': new Style({
          image: image,
        }),
        'MultiPolygon': new Style({
          stroke: new Stroke({
            color: 'yellow',
            width: 1,
          }),
          fill: new Fill({
            color: 'rgba(255,255,0,0.29)',
          }),
        }),
        'Polygon': new Style({
          stroke: new Stroke({
            color: 'blue',
            lineDash: [4],
            width: 3,
          }),
          fill: new Fill({
            color: 'rgba(0,0,255,0.47)',
          }),
        }),
        'GeometryCollection': new Style({
          stroke: new Stroke({
            color: 'rgb(208,0,255)',
            width: 2,
          }),
          fill: new Fill({
            color: 'rgb(208,0,255)',
          }),
          image: new CircleStyle({
            radius: 10,
            fill: null,
            stroke: new Stroke({
              color: 'rgb(208,0,255)',
            }),
          }),
        }),
        'Circle': new Style({
          stroke: new Stroke({
            color: 'rgba(255,0,0,0.2)',
            width: 2,
          }),
          fill: new Fill({
            color: 'rgba(255,0,0,0.2)',
          }),
        }),
      };
      return styles[feature.getGeometry().getType()];
    },
    //添加wkt到地图
    addWKT(data) {
      const map = this.map;
      //清除上一次加载的wkt图层
      map.removeLayer(this.wktLayer);
      if (data === null) return;
      const source = new VectorSource();
      let features = []
      //循环data 加载要素到地图
      data.forEach(item => {
        //读取数据表中wkt创建为feature对象
        let feature = new WKT().readFeature(item.wkt);
        //feature赋值属性信息
        feature.setProperties({info: item});
        features.push(feature);
      });
      source.addFeatures(features);
      this.wktLayer = new VectorLayer({
        source: source,
        style: this.styleFunction,
      });
      //添加wkt图层到地图
      map.addLayer(this.wktLayer);
      //通过extent缩放至范围
      let extent = source.getExtent();
      let r = map.getView().getResolutionForExtent(extent, map.getSize());
      if (data.length > 1) {
        //设置地图显示缩放等级
        map.getView().setResolution(r);
        //设置地图中心点
        map.getView().setCenter(Extent.getCenter(extent));
      } else {//如果只有一条数据
        //设置地图中心点
        map.getView().setCenter(Extent.getCenter(extent));
        if (this.data[2] === 'Point') {//如果地理要素类型为点
          //设置地图缩放等级为11
          map.getView().setZoom(11);
        } else {//如果地理要素类型不是点
          //设置地图缩放等级
          map.getView().setResolution(r);
        }
      }
    },
    //编辑地图要素
    editFeatures(row) {
      let map = this.map;
      //创建Modify编辑对象
      let modify = new Modify({source: this.wktLayer.getSource()});
      map.addInteraction(modify);
      //编辑结束后
      modify.on("modifyend", function (e) {
        let features = e.features;
        let feature = features.array_[0];
        //获取要素的wkt数据
        row.wkt = new WKT().writeFeature(feature);
      });
      this.modify = modify;
    },
    //绘制新要素
    addFeatures() {
      let that = this;
      this.isDraw = true;
      let map = this.map;
      this.select.setActive(false);
      //创建编辑对象Draw
      let draw = new Draw({
        source: this.wktLayer.getSource(),//绑定wkt图层源
        type: this.data[2],//要素类型
      });
      map.addInteraction(draw);
      const $table = this.$refs.xTable;
      //编辑结束时
      draw.on("drawend", async function (e) {
        //存储feature
        that.drawFeature = e.feature;
        const { row: newRow } = await $table.insertAt({wkt:new WKT().writeFeature(e.feature)}, 0)
        await $table.setActiveRow(newRow);
        that.editFeatures(newRow);
        //移除编辑对象
        map.removeInteraction(draw);
      });
    },
    //删除选中数据
    removeCheck() {
      const $table = this.$refs.xTable;
      const selectRecords = $table.getCheckboxRecords();
      if (selectRecords.length === 0) {
        this.$message.error("没有选中数据！");
        return;
      }
      //打开弹窗
      this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let geoms = [];
        selectRecords.forEach(item => {
          geoms.push(item[this.queryInfo.geom].value);
        });
        //当弹窗点击修改后 请求删除接口
        const {data: ref} = await this.$API.delTableData({
          tableName: this.queryInfo.tableName,
          geom: this.queryInfo.geom,
          oldGeom: geoms + "",
          newRow: {},
          fields: this.queryInfo.fields
        });
        if (ref.status === 404) return;
        this.$message.success("删除成功！");
        await this.$refs.xTable.removeCheckboxRow();
        this.handleCurrentChange(1);
      });
    },
    //编辑表格某一行数据
    editRowEvent(row) {
      //存储修改前geom数据
      this.rowInfo.oldGeom = row[this.queryInfo.geom].value;
      //表格字段赋值
      this.rowInfo.fields = this.queryInfo.fields;
      //表名赋值
      this.rowInfo.tableName = this.queryInfo.tableName;
      //geometry字段赋值
      this.rowInfo.geom = this.queryInfo.geom;
      //修改前的wkt赋值，供取消修改时使用
      this.oldWKT = row.wkt;
      this.isDraw = false;
      //获取tabley元素
      const $table = this.$refs.xTable;
      //激活编辑的数据行
      $table.setActiveRow(row);
      //将编辑数据行的地理数据在地图上显示
      this.addWKT([row]);
      //编辑feature
      this.editFeatures(row);
    },
    //保存修改行数据
    saveRowEvent(row) {
      let that = this;
      const $table = this.$refs.xTable;
      //表格数据保存时
      $table.clearActived().then(async () => {
        //移除编辑对象modify
        that.map.removeInteraction(that.modify);
        //将geometry字段赋值为wkt
        row[that.data[1]] = row.wkt;
        //更新地图要素
        that.addWKT(that.datas);
        //修改后数据行赋值
        that.rowInfo.newRow = row;
        //是否处于绘制状态
        if (!that.isDraw) {
          //请求修改数据接口
          const {data: ref} = await that.$API.updateTableData(that.rowInfo);
          if (ref.status !== 200) return;
          that.$message.success(ref.msg);
        } else {
          that.rowInfo.fields = that.queryInfo.fields
          that.rowInfo.geom = that.queryInfo.geom
          that.rowInfo.tableName = that.queryInfo.tableName
          const {data: ref} = await that.$API.addTableData(that.rowInfo)
          if (ref.status !== 200) return;
          that.$message.success(ref.msg);
        }
        //如果查询状态 请求查询接口
        if (this.isFind) await this.getFindData();
        else await this.getTableData();
      });
    },
    //取消修改行数据
    cancelRowEvent(row) {
      let that = this;
      const $table = this.$refs.xTable;
      //表格数据取消修改时
      $table.clearActived().then(() => {
        //将旧wkt地理数据还原
        row.wkt = that.oldWKT;
        // 还原行数据
        $table.revertData(row);
        //移除编辑modify对象
        that.map.removeInteraction(that.modify);
        //更新wkt图层数据
        that.addWKT(that.datas);
      });
    },
    //删除行数据
    removeEvent(row) {
      //打开弹窗
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        //当弹窗点击修改后 请求删除接口
        const {data: ref} = await this.$API.delTableData(this.rowInfo);
        if (ref.status === 404) return;
        this.$message.success("删除成功！");
        this.$refs.xTable.remove(row);
        this.handleCurrentChange(1);
      });
    },
    //设置表格行样式
    rowClassName() {
      // 返回类名row-height
      return 'row-height';
    }
  },
  watch: {
    //监听路由变化
    $route: {
      handler: function (val, oldVal) {
        //更新[表名,geometry字段名,geometry类型],
        this.data = val.params.name.split(',');
        //更新表名
        this.queryInfo.tableName = this.data[0];
        //更新geometry字段名称
        this.queryInfo.geom = this.data[1];
        //更新分页页码为1
        this.queryInfo.pageNum = 1;
        //获取该表格数据
        this.getTableData();
      }
    }
  }
}
</script>
<style>
</style>
