<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>Geoserver REST</el-breadcrumb-item>
      <el-breadcrumb-item>地图服务后台系统</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div class="main" style="height: 80vh">
        <div class="bar">
          <el-row>
            <el-col :span="21"><div class="tianchong"></div></el-col>
            <el-col :span="3">
              <div class="button">
                <el-row>
                  <el-col :span="24"
                  ><el-dropdown split-button type="text" @command="handleCommand">
                    <span class="el-dropdown-link"> 地图发布 </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="Shapefile"
                      >Shapefile</el-dropdown-item
                      >
                      <el-dropdown-item command="PostGIS"
                      >PostGIS</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown></el-col
                  >
<!--                  <el-col :span="12"-->
<!--                  ><el-button type="text" @click="editLayer"-->
<!--                  >编辑</el-button-->
<!--                  ></el-col-->
<!--                  >-->
                </el-row>
              </div></el-col
            >
          </el-row>
        </div>
        <div class="content">
          <el-row>
            <el-col :span="4">
              <div class="list">
              <el-row>
                <el-col :span="24">
                  <div class="layerlist">
                    <el-row>
                      <el-col :span="2">&nbsp;</el-col>
                      <el-col :span="22">
                        <div style="margin: 5px 0"></div>
                        <div>图层列表</div>
                        <div style="margin: 5px 0"></div>
                        <el-checkbox-group
                          v-model="checkedLayers"
                          @change="handleCheckedLayersChange"
                          :disabled="layerGroup"
                        >
                          <el-checkbox
                            v-for="item in data"
                            :label="item.title"
                            :key="item.layerName"
                            style="display: block; height: 25px"
                          >
                            {{ item.title }}
                            <i
                              class="el-icon-more"
                              @click.prevent="showDetails(item.layerName)"
                            ></i>
                          </el-checkbox>
                        </el-checkbox-group>
                      </el-col>
                    </el-row>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div class="legend">
                    <el-row>
                      <el-col :span="2">&nbsp;</el-col>
                      <el-col :span="22">
                        <div style="margin: 5px 0"></div>
                        <p>图例</p>
                        <div style="margin: 5px 0"></div>
                        <div
                          v-for="item in data"
                          :key="item.title"
                          @click="changeStyle(item.layerName)"
                          style="cursor: pointer"
                        >
                          <p style="font-size: 14px">{{ item.title }}</p>
                          <div style="margin: 5px 0"></div>
                          <img :src="imgBaseUrl + item.layerName" alt="" />
                          <div style="margin: 5px 0"></div>
                        </div>
                      </el-col>
                    </el-row>
                  </div>
                </el-col>
              </el-row></div
            ></el-col>
            <el-col :span="20">
              <div id="map">
                <el-card class="box-card" v-show="editModel">
                  <el-button type="text" class="shutDown" @click="shutDownEdit"
                  >X</el-button
                  >
                  <div
                    class="editTitle"
                    style="margin: 0 0 15px 0; font-size: 14px"
                  >
                    编辑面板
                  </div>
                  <div class="editLayer">
                    <div style="font-size: 14px; display: inline-block">图层：</div>
                    <div style="display: inline-block">
                      <el-select
                        v-model="value"
                        size="small"
                        @change="selectLayerChange"
                        placeholder="选择图层开始编辑"
                      >
                        <el-option
                          v-for="item in data"
                          :key="item.layerName"
                          :label="item.title"
                          :value="item.layerName"
                        >
                        </el-option>
                      </el-select>
                    </div>
                    <div style="margin: 10px 0"></div>
                    <el-row>
                      <el-button
                        type="primary"
                        size="mini"
                        @click="insertNewFeature"
                      >新增</el-button
                      >
                      <el-button type="primary" size="mini" @click="deleteFeature"
                      >删除</el-button
                      >
                      <el-button type="primary" size="mini" @click="editModify"
                      >修改</el-button
                      >
                      <el-button type="primary" size="mini" @click="editBtn"
                      >保存</el-button
                      >
                    </el-row>
                    <div style="margin: 10px 0"></div>
                    <div class="editAttr">
                      <el-form
                        ref="editform"
                        :model="editform"
                        label-width="80px"
                        size="mini"
                      >
                        <el-form-item
                          v-for="(item, i) in attributes"
                          :key="i"
                          :label="item"
                          v-show="item != 'geom' && item != 'the_geom'"
                        >
                          <el-input v-model="editformlist[i]"></el-input>
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </div>
        <el-dialog
          title="图层详细信息"
          :visible.sync="dialogVisible"
          width="30%"
          center
        >
          <div class="layerDetails">
            <ul>
              <li>名称：{{ layerInfo.name }}</li>
              <li>
                <div style="display: flex">
                  <label style="width: 50px">标题：</label>
                  <el-input v-model="layerInfo.title" size="small"></el-input>
                </div>
              </li>
              <li>
                <div style="display: flex">
                  <label style="width: 50px">摘要：</label>
                  <el-input v-model="layerInfo.abstracts" size="small"></el-input>
                </div>
              </li>
              <li>坐标系统代码：{{ layerInfo.crs }}</li>
              <li>类型：{{ layerInfo.type }}</li>
              <li>工作空间名称：{{ layerInfo.workspace }}</li>
              <li>样式名称：{{ layerInfo.style }}</li>
              <li>图层属性字段：{{ layerInfo.attributes }}</li>
              <li>工作空间URI：{{ layerInfo.uri }}</li>
              <li>Geoserver URI：{{ layerInfo.restURI }}</li>
            </ul>
          </div>
          <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateInfo">更新</el-button>
        <el-button type="primary" @click="deleteLayer">删除</el-button>
      </span>
        </el-dialog>
        <el-dialog
          title="地图发布(PostGIS)"
          :visible.sync="mapPublishDialogVisible"
          width="40%"
        >
          <el-tabs v-model="activeName" tab-position="left" style="height: 350px">
            <el-tab-pane label="配置" name="first" disabled>
              <el-form ref="form" :model="form" label-width="90px">
                <el-form-item label="数据库名称">
                  <el-input v-model="form.database"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                  <el-input v-model="form.host"></el-input>
                </el-form-item>
                <el-form-item label="端口">
                  <el-input v-model="form.port"></el-input>
                </el-form-item>
                <el-form-item label="用户名">
                  <el-input v-model="form.user"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                  <el-input v-model="form.password"></el-input>
                </el-form-item>
              </el-form>
              <el-button
                type="primary"
                style="display: block; margin: 0 auto"
                @click="conBtn"
                size="small"
              >建立连接</el-button
              >
            </el-tab-pane>
            <el-tab-pane label="发布" name="second" disabled>
              <el-table
                :data="tableData"
                highlight-current-row
                @current-change="handleCurrentChange"
                height="290"
                border
                style="width: 100%"
              >
                <el-table-column
                  prop="name"
                  label="表名"
                  width="325"
                ></el-table-column>
                <el-table-column
                  prop="type"
                  label="类型"
                  width="325"
                ></el-table-column>
              </el-table>
              <div style="display: flex; margin: 20px auto">
                <el-select
                  v-model="postGISInfo.workspaceName"
                  placeholder="请选择工作空间"
                >
                  <el-option
                    :value="item"
                    v-for="item in workspacelist"
                    :label="item"
                    :key="item"
                  ></el-option>
                </el-select>
                <el-button
                  type="primary"
                  style="margin: auto 20px"
                  size="small"
                  @click="publishPostGIS"
                >发布</el-button
                >
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-dialog>
        <el-dialog
          title="地图发布(Shapefile)"
          :visible.sync="publishShpaedialogVisible"
          width="30%"
        >
          <div class="publishShp">
            <el-upload
              class="upload-demo"
              action
              accept=".zip"
              :limit="1"
              :before-upload="beforeUpload"
              :show-file-list="false"
            >
              <el-button type="primary" size="small">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">
                只能上传shapefile类型的zip压缩文件
              </div>
            </el-upload>
            <br />
            <el-form ref="form" :model="shpForm" label-width="80px">
              <el-form-item label="文件路径">
                <el-input
                  v-model="shpForm.shpFile"
                  readonly
                  style="width: 215px"
                ></el-input>
              </el-form-item>
              <el-form-item label="工作空间">
                <el-select
                  v-model="shpForm.workspaceName"
                  placeholder="请选择工作空间"
                >
                  <el-option
                    :value="item"
                    v-for="item in workspacelist"
                    :label="item"
                    :key="item"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="类型">
                <el-select v-model="shpType" placeholder="请选择shp类型">
                  <el-option label="点" value="POINT"></el-option>
                  <el-option label="线" value="LINE"></el-option>
                  <el-option label="面" value="POLYGON"></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
          <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="shpUpload" size="small"
        >发布</el-button
        >
        <el-button
          type="primary"
          @click="publishShpaedialogVisible = false"
          size="small"
        >退出</el-button
        >
      </span>
        </el-dialog>
        <el-dialog
          title="图层样式修改"
          :visible.sync="changeStyleDialogVisible"
          width="30%"
        >
          <style-dialog :layer-name="changeStyledLayerName"></style-dialog>
        </el-dialog>
      </div>
    </el-card>
  </div>
</template>

<script>
import "ol/ol.css";
import Map from "ol/map";
import View from "ol/view";
import TileLayer from 'ol/layer/tile';
import VectorLayer from 'ol/layer/vector';
import OSM from "ol/source/osm";
import VectorSource from 'ol/source/vector';
import TileWMS from 'ol/source/tilewms';
import GeoJSON from 'ol/format/geojson';
import WFS from "ol/format/wfs";
import Draw from 'ol/interaction/draw';
import Select from 'ol/interaction/select';
import Modify from 'ol/interaction/modify';
import ScaleLine from 'ol/control/scaleline';
import loadingstrategy from 'ol/loadingstrategy';
//添加修改样式弹窗
import StyleDialog from "./StyleDialog";
export default {
  components: { StyleDialog },
  data() {
    return {
      editformlist: [],
      editform: {},
      //图层禁止选择
      layerGroup: false,
      //选中的图层
      value: "",
      //激活的tabs
      activeName: "first",
      map: null,
      checkedLayers: [],
      data: [],
      workspacelist: [],
      addLayerlist: [],
      dialogVisible: false,
      mapPublishDialogVisible: false,
      publishShpaedialogVisible: false,
      baseURL: process.env.VUE_APP_GEOSERVER,
      imgBaseUrl:
        process.env.VUE_APP_GEOSERVER +
        "geoserver/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=15&HEIGHT=15&LAYER=",
      layerInfo: {},
      //数据库中的表
      tableData: [],
      //选中表
      selectTableData: {},
      postGISInfo: {
        crsCode: 4326,
        postGIS: {},
        styleName: "",
        styleWorkspace: "",
        tableName: "",
        workspaceName: "",
      },
      form: {
        dataStoreName: "",
        database: "webgistest",
        host: "localhost",
        password: 123456,
        port: 5432,
        user: "postgres",
      },
      shpForm: {
        crsCode: 4326,
        shpFile: "",
        styleName: "",
        styleWorkspace: "",
        workspaceName: "",
      },
      //shp类型
      shpType: "",
      wfsLayer: null,
      selectLayerInfo: {},
      //属性表的表头信息
      attributes: [],
      //选择要素
      select: null,
      changeStyleDialogVisible: false,
      changeStyledLayerName: "",
      //修改要素
      modify: null,
      changeFeature: null,
      //编辑模式
      editModel: false,
    };
  },
  mounted() {
    this.init();
  },
  created() {
    this.getWorkSpace();
    this.getLayers().then(() => {
      this.addLayerToMap();
    });
  },
  methods: {
    disposeLayer(layer, layername) {
      return new TileLayer({
        name: layername,
        source: new TileWMS({
          ratio: 1,
          url: `${this.baseURL}geoserver/wms`,
          // Layers需要指定要显示的图层名
          params: {
            LAYERS: layer,
            STYLES: "",
            TILED: true,
            VERSION: "1.1.1",
          },
          // serverType明显为geoserver
          serverType: "geoserver",
          // Countries have transparency, so do not fade tiles:
          transition: 0,
        }),
        visible: true,
      });
    },
    init() {
      let baseLayer = [
        new TileLayer({
          //数据源
          source: new OSM(), //openlayers内置的数据源
          name: "OSM地图",
        }),
      ];
      this.map = new Map({
        target: "map", //绑定页面中DOM元素的id
        layers: baseLayer,
        view: new View({
          projection: "EPSG:3857",
          //视图配置
          center: [11799866.024087, 4300621.37204427], //地图中心点坐标，经度、纬度
          zoom: 5, //定义地图显示层级
        }),
      });
      this.map.addControl(new ScaleLine());
    },
    //图层checkbox控制
    handleCheckedLayersChange(value) {
      //控制图层显示隐藏
      this.addLayerlist.forEach((item) => {
        if (!value.includes(item.get("name"))) {
          item.setVisible(false);
          return;
        }
        item.setVisible(true);
      });
    },
    //获取数据
    async getLayers() {
      const { data: ref } = await this.$API.getLayersInfo();
      if (ref.status !== 200) return;
      this.data = ref.data;
      this.checkedLayers = this.data.map((obj) => {
        return obj.title;
      });
    },
    //获取所有工作空间
    async getWorkSpace() {
      const { data: ref } = await this.$API.getWorkspaceNames();
      if (ref.status === 404) return;
      this.workspacelist = ref.data;
    },
    //添加图层
    addLayerToMap() {
      for (let i in this.data) {
        let layer = this.data[i].layerName;
        let title = this.data[i].title;
        this.addLayerlist.push(this.disposeLayer(layer, title));
      }
      for (let index in this.addLayerlist) {
        this.map.addLayer(this.addLayerlist[index]);
      }
    },
    //获取指定图层信息
    async getLayerInfo(layername) {
      const { data: ref } = await this.$API.getLayerInfo({ 'layerName': layername });
      if (ref.status === 404) return;
      this.layerInfo = ref.data;
    },
    //展示详情
    showDetails(e) {
      this.getLayerInfo(e).then(() => {
        this.dialogVisible = true;
      });
    },
    //更改指定图层信息：标题和摘要
    async updateInfo() {
      // let param = {
      //   'abstracts': this.layerInfo.abstracts,
      //   'layerName': this.layerInfo.workspace + ":" + this.layerInfo.name,
      //   'title': this.layerInfo.title
      // };
      let param = `abstracts=${this.layerInfo.abstracts}&layerName=${
        this.layerInfo.workspace + ":" + this.layerInfo.name
      }&title=${this.layerInfo.title}`;
      const { data: ref } = await this.$API.updateLayer(param);
      if (ref.status === 200) {
        this.$message.success(ref.msg);
        //刷新图层列表
        await this.getLayers();
      } else {
        this.$message.error(ref.msg);
      }
    },
    //地图发布
    handleCommand(commend) {
      if (commend === "PostGIS") {
        this.activeName = "first";
        this.mapPublishDialogVisible = true;
      }
      if (commend === "Shapefile") {
        this.publishShpaedialogVisible = true;
      }
    },
    //连接PostGIS
    async conBtn() {
      const { data: ref } = await this.$API.getTableInfo(this.form);
      if (ref.status === 200) {
        this.$message.success(ref.msg);
        this.activeName = "second";
        this.tableData = ref.data;
      } else {
        this.$message.error(ref.msg);
        this.activeName = "first";
        return;
      }
    },
    //选中表
    handleCurrentChange(val) {
      this.selectTableData = val;
    },
    //发布PostGIS中的表
    async publishPostGIS() {
      this.postGISInfo.tableName = this.selectTableData["name"];
      this.postGISInfo.postGIS = this.form;
      const { data:res} = await this.$API.addPostGISLayer1(this.selectTableData["type"], this.postGISInfo)
      if (res.status === 200) {
        this.$message.success("发布成功！");
        this.addLayerlist = [];
        this.getLayers().then(() => {
          this.addLayerToMap();
        });
        this.mapPublishDialogVisible = false;
      } else {
        this.$message.error(res.msg);
      }
    },
    //删除指定图层
    async deleteLayer() {
      let selectLayer = this.layerInfo.workspace + ":" + this.layerInfo.name;
      const {data:res} = await this.$API.delLayer({'layerName':selectLayer})
      if (res.status === 200) {
        this.$message.success(res.msg);
        this.addLayerlist = [];
        await this.getLayers();
        this.dialogVisible = false;
        this.map.getLayers().forEach((item) => {
          if (
            item.get("name") === this.layerInfo.name ||
            item.get("name") === this.layerInfo.title
          ) {
            this.map.removeLayer(item);
          }
        });
      } else {
        this.$message.error(res.msg);
      }
    },
    //上传文件
    async beforeUpload(file) {
      const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
      const whiteList = ["zip"];
      if (whiteList.indexOf(fileSuffix) === -1) {
        this.$message.error("上传文件只能是 zip ");
        return false;
      } else {
        const formData = new FormData();
        formData.append("file", file);
        const config = {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        };
        const { data: res } = await this.$API.uploadFile(formData, config)
        if (res.status === 200) {
          this.shpForm.shpFile = res.data;
          this.$message.success(res.msg);
        } else {
          this.$message.error(res.msg);
        }
      }
    },
    //发布shp
    async shpUpload() {
      const { data: res } = await this.$API.addShpLayer1(this.shpType, this.shpForm)
      if (res.status === 200) {
        this.$message.success("发布成功！");
        this.addLayerlist = [];
        this.getLayers().then(() => {
          this.addLayerToMap();
        });
        this.publishShpaedialogVisible = false;
      } else {
        this.$message.error(res.msg);
      }
    },
    //开始编辑
    editLayer() {
      this.$router.push("/TableRest")
      this.editModel = true;
      //关闭所有wms图层
      this.checkedLayers = [];
      this.layerGroup = true;
      this.addLayerlist.forEach((item) => {
        // item.setVisible(false);
        //删除图层
        this.map.removeLayer(item);
      });
      this.select = new Select();
      this.map.addInteraction(this.select);
      // this.select.on("select", function (e) {
      //   let feature = e.selected;
      //   console.log(feature[0].getProperties());
      // });
    },
    //加载WFS图层
    addVectorLayer(wsName, layer) {
      // 服务配置，命名空间、图层、服务地址等
      let geoserverData = {
        wsName: wsName,
        uri: `${this.baseURL}/${wsName}`,
        wfsURL: `${this.baseURL}geoserver/wfs?`,
        layer: layer,
      };
      //测试数据 添加WFS数据  添加所有
      let wfsSource = new VectorSource({
        format: new GeoJSON(),
        url: function (extent) {
          return (
            geoserverData.wfsURL +
            "service=WFS&version=1.1.0&request=GetFeature&typeName=" +
            geoserverData.wsName +
            ":" +
            geoserverData.layer +
            "&" +
            "outputFormat=application/json&srsname=EPSG:3857&bbox=" +
            extent.join(",") +
            ",EPSG:3857"
          );
        },
        strategy: loadingstrategy.bbox(),
      });

      this.wfsLayer = new VectorLayer({
        name: "Vector图层",
        source: wfsSource,
      });

      this.map.addLayer(this.wfsLayer);
    },
    //当选择图层发生改变
    async selectLayerChange(e) {
      this.editform = {};
      //获取指定图层信息
      const { data: ref } = await this.$API.getLayerInfo({ 'layerName': e });
      if (ref.status === 404) return;
      this.selectLayerInfo = ref.data;
      this.attributes = ref.data.attributes;

      for (let i in this.attributes) {
        let key = this.attributes[i];
        this.editform[key] = "";
      }
      this.editformlist = Object.values(this.editform);
      //将工作空间与图层名分开
      let res = e.split(":");
      //移除图层
      if (this.wfsLayer != null) {
        this.map.removeLayer(this.wfsLayer);
      }
      //加载矢量图层
      this.addVectorLayer(res[0], res[1]);

      //获取要素类型
      // this.wfsLayer.getSource().on("change", function (evt) {
      //   const source = evt.target;
      //   if (source.getState() === "ready") {
      //     let numFeatures = source.getFeatures();
      //     // console.log(numFeatures[0].getGeometry().getType());
      //   }
      // });
    },
    //新增要素
    insertNewFeature() {
      let type = this.wfsLayer
        .getSource()
        .getFeatures()[0]
        .getGeometry()
        .getType();
      let draw = new Draw({
        source: this.wfsLayer.getSource(),
        type: type,
      });

      //构造属性信息
      let form = [];
      let key = Object.keys(this.editform);
      for (let i in key) {
        form.push({ name: key[i], value: this.editformlist[i] });
      }
      let that = this;
      let data = this.editformlist.join("");
      if (data.length === 0) {
        this.$message.warning("请先填写属性信息！");
      } else {
        this.map.addInteraction(draw);
        draw.on("drawend", function (e) {
          //存储feature
          let feature = e.feature;
          that.map.removeInteraction(draw);
          that.sendWFS(feature, "insert", form);
        });
      }
    },
    //删除选定的要素
    deleteFeature() {
      let features = this.select.getFeatures();
      let feature = features.array_[0];
      let properties = feature.getProperties();
      let form = [];
      let key = Object.keys(properties);
      let value = Object.values(properties);
      for (let i in key) {
        form.push({ name: key[i], value: value[i] });
      }
      this.sendWFS(feature, "delete", form);
      this.select.getFeatures().clear();
      this.wfsLayer.getSource().removeFeature(feature);
    },
    //修改要素，移位，更改属性
    editModify() {
      this.modify = new Modify({ source: this.wfsLayer.getSource() });
      this.map.addInteraction(this.modify);
      let that = this;
      this.modify.on("modifyend", function (e) {
        let features = e.features;
        let feature = features.array_[0];
        that.changeFeature = feature;
      });
    },
    //保存
    editBtn() {
      this.map.removeInteraction(this.modify);
      //构造属性信息
      let form = [];
      let key = Object.keys(this.editform);
      for (let i in key) {
        form.push({ name: key[i], value: this.editformlist[i] });
      }
      //判断是否修改属性
      if (form[form.length - 2].value === "") {
        let properties = this.changeFeature.getProperties();
        let changeform = [];
        let key = this.attributes;
        let value = Object.values(properties);
        for (let i in key) {
          changeform.push({ name: key[i], value: value[i] });
        }
        this.sendWFS(this.changeFeature, "update", changeform);
      } else {
        this.sendWFS(this.changeFeature, "update", form);
      }
    },
    //构造WFS进行发布
    async sendWFS(feature, type, form) {
      //发送WFS 请求 ，type 分别为insert update delect
      // 1、构造Feature
      let ft = feature.clone();
      // 更新操作必须要有id
      let id = feature.getId();
      let properties = ft.getProperties();
      let featObject;
      let WFSTSerializer = new WFS();
      let _this = {
        workspace: this.selectLayerInfo.workspace,
        uri: this.selectLayerInfo.uri,
        restURI: `${this.baseURL}geoserver`,
        name: this.selectLayerInfo.name,
      };
      let geomKey = "";
      let options = {
        featureNS: _this.uri,
        featurePrefix: _this.workspace, //工作空间名称
        featureType: _this.name, //图层名称
        srsName: "EPSG:3857",
      };

      for (const item of form) {
        if (item.name.search("geom") !== -1) {
          geomKey = item.name;
        }
        properties[item.name] = item.value;
      }

      if (type === "insert") {
        // let coordinates = ft.getGeometry().getCoordinates();
        // properties.lng = coordinates[0];
        // properties.lat = coordinates[1];

        // ft.setProperties(properties);

        let geom = ft.getGeometry();
        geom.applyTransform((flatCoordinates, flatCoordinates2, stride) => {
          for (let j = 0; j < flatCoordinates.length; j += stride) {
            let y = flatCoordinates[j];
            let x = flatCoordinates[j + 1];
            flatCoordinates[j] = x;
            flatCoordinates[j + 1] = y;
          }
        });
        ft.setGeometryName(geomKey); // postgis字段为geom、shp字段名为the_geom
        ft.setGeometry(geom);

        featObject = WFSTSerializer.writeTransaction([ft], null, null, options);
      } else if (type === "update") {
        ft.setId(id);
        // let coordinates = ft.getGeometry().getCoordinates();
        // properties.lng = coordinates[0];
        // properties.lat = coordinates[1];
        let geom = ft.getGeometry();
        geom.applyTransform((flatCoordinates, flatCoordinates2, stride) => {
          for (let j = 0; j < flatCoordinates.length; j += stride) {
            let y = flatCoordinates[j];
            let x = flatCoordinates[j + 1];
            flatCoordinates[j] = x;
            flatCoordinates[j + 1] = y;
          }
        });
        ft.setGeometryName(geomKey); // postgis字段为geom、shp字段名为the_geom
        featObject = WFSTSerializer.writeTransaction(null, [ft], null, options);
      } else if (type === "delete") {
        ft.setId(id);
        featObject = WFSTSerializer.writeTransaction(null, null, [ft], options);
      }
      let serializer = new XMLSerializer();
      let featString = serializer.serializeToString(featObject);

      this.$http.defaults.headers.post["Content-Type"] = "text/xml";
      let url = `${this.baseURL}geoserver/wfs`;
      await this.$http.post(url, featString).then((res) => {
        let message = res.data;
        console.log(message);
        let issuccess = res.data.search(/NoApplicableCode/);
        if (issuccess === -1) {
          this.$message.success("操作成功！");
        } else {
          this.$message.error("操作失败！");
        }
      });
    },
    //退出编辑模式
    shutDownEdit() {
      this.editModel = false;
      this.layerGroup = false;
      this.getLayers().then(() => {
        this.addLayerToMap();
      });
    },
    //样式更改
    changeStyle(e) {
      this.changeStyledLayerName = e;
      this.changeStyleDialogVisible = true;
    },
  },
  watch: {
    changeFeature(e) {
      this.changeFeature = e;
    },
  },
};
</script>

<style lang="less" scoped>
.main {
  min-width: 900px;
}

.title {
  .text {
    min-height: 44px;
    font-size: 24px;
    height: 6vh;
    line-height: 48px;
    text-align: center;
    color: #409eff;
  }
}
.bar {
  .tianchong {
    min-height: 42px;
    height: 5vh;
    background-color: #f7f8f3;
    border-bottom: 1px solid #cccccc;
    box-sizing: border-box;
  }
  .button {
    min-height: 42px;
    height: 5vh;
    text-align: center;
    line-height: 5vh;
    background-color: #f7f8f3;
    border-bottom: 1px solid #cccccc;
    box-sizing: border-box;
  }
}
.list {
  height: 80vh;
}
.layerlist {
  min-height: 200px;
  height: 40vh;
  overflow-y: auto;
  border-bottom: 1px solid #cccccc;
  border-left: 1px solid #cccccc;
  border-right: 1px solid #cccccc;
  box-sizing: border-box;
}
.legend {
  height: 40vh;
  overflow-y: auto;
  border-bottom: 1px solid #cccccc;
  border-left: 1px solid #cccccc;
  border-right: 1px solid #cccccc;
  box-sizing: border-box;
}
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.layerDetails {
  ul {
    li {
      margin: 15px;
    }
  }
}
.box-card {
  z-index: 10;
  position: absolute;
  top: 20px;
  right: 20px;
  width: 300px;
}
.shutDown {
  position: relative;
  left: 250px;
  font-size: 12px;
  font-family: "Gill Sans", "Gill Sans MT", Calibri, "Trebuchet MS", sans-serif;
}
</style>
