<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>OpenLayers</el-breadcrumb-item>
      <el-breadcrumb-item>在线编辑</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <div id="map"></div>
      <ul class="tool-box" id="tools">
        <li type="Point" class="draw-btn" @click="liClick($event)">点</li>
        <li type="LineString" class="draw-btn" @click="liClick($event)">线</li>
        <li type="Polygon" class="draw-btn" @click="liClick($event)">面</li>
        <li type="Edit" class="draw-btn" @click="liClick($event)">编辑</li>
      </ul>
      <div class="edit-info">
        <input id="name" type="text" placeholder="名称">
        <input id="submit" type="button" value="提交" @click="submitClick">
        <div class="edit-tools">
          <input id="editName" type="text" placeholder="编辑">
          <input id="editSave" type="button" value="保存" @click="editSave">
          <input id="editClose" type="button" value="关闭" @click="editClose">
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
import Map from 'ol/map';
import View from 'ol/view';
import VectorLayer from 'ol/layer/vector';
import Vector from 'ol/source/vector';
import WKT from 'ol/format/wkt';
import Draw from 'ol/interaction/draw';
import Select from 'ol/interaction/select';
import Modify from 'ol/interaction/modify';
import Style from 'ol/style/style';
import Circle from 'ol/style/circle';
import Stroke from 'ol/style/stroke';
import Text from 'ol/style/text';
import Fill from 'ol/style/fill';
import proj from 'ol/proj';
import control from 'ol/control';
import {getTdtLayer} from "../../utils/searchUtils";
export default {
  data() {
    return {
      map: null,
      source: null,
      element: null,
      editInfoDoc: null,
      draw: null,
      select: null,
      edit: null,
    }
  },
  created() {
  },
  mounted() {
    this.initMap()
  },
  methods: {
    initMap() {
      this.toolsDoc = document.getElementById("tools")
      this.toolsDoc.classList.add('edit-tools')
      this.editInfoDoc = document.getElementsByClassName("edit-info")[0]
      this.editNameDoc = document.getElementById("editName")
      const that = this
      let map;
      let source = new Vector({
        features: []
      });

      let vector = new VectorLayer({
        source: source,
        style: function (feature) {
          return that.styleFunction(feature, false);
        }
      });

      map = new Map({
        controls: control.defaults({
          attribution: false
        }),
        target: 'map',
        layers: [
          getTdtLayer("vec_w"),
          getTdtLayer("cva_w"),
          vector
        ],
        view: new View({
          center: proj.fromLonLat([98.633, 31.607]),
          zoom: 4,
          minZoom: 0,
          maxZoom: 18
        })
      });
      that.addFeatures();

      let select, edit;
      select = new Select({
        layers: [vector],
        style: function (feature) {
          return that.styleFunction(feature, true);
        }
      });
      edit = new Modify({
        features: select.getFeatures()
      });

      select.on("select", function (e) {
        let features = e.selected;
        if (features.length > 0) {
          that.startEdit();
        } else {
          that.deSelectEvt();
        }
      });

      map.addInteraction(select);
      map.addInteraction(edit);
      select.setActive(false);
      edit.setActive(false);

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
      this.map = map
      this.source = source
      this.select = select
      this.edit = edit
    },
    liClick(event) {
      let docm = event.target
      console.log(docm.classList.contains('active'));
      let _type = docm.type;
      let isActive = docm.classList.contains('active')
      if (_type === "Edit") {
        if (isActive) {
          docm.classList.remove('active')
          this.select.setActive(false);
          this.edit.setActive(false);
          this.editInfoDoc.style.display = "none";
        } else {
          docm.classList.add('active')
          this.select.setActive(true);
          this.edit.setActive(true);
        }
      } else {
        if (this.edit.getActive()) {
          if (confirm("正在编辑中，是否保存当前编辑？")) {
            let editName = this.editNameDoc.value
            if (editName === "") {
              docm.classList.add('active')
              console.log(editName);
              alert("保存信息不完整，请检查后再保存！");
            } else {
              this.saveEdit();
              docm.classList.add('active')
              this.addDrawTool(_type);
            }
          }
        } else {
          docm.classList.add('active')
          this.addDrawTool(_type);
        }
        // $(".draw-btn").removeClass("active");
        // $(this).addClass("active");
        // addDrawTool(_type);
      }
    },
    submitClick() {
      let name = document.getElementById("name").value;
      let selectFeatures = this.select.getFeatures();
      let selectFeature = selectFeatures.item(0);
      let WKTGeo = new WKT().writeGeometry(selectFeature.getGeometry());//坐标系转换为4326
      let paras = {
        name: name,
        geom: WKTGeo + ''
      };
      this.$http.get("edit/add", { params: paras })
      this.select.getFeatures().clear();
      this.select.setActive(false);
      this.edit.setActive(false);
      this.addFeatures();
    },
    editClose() {
      this.deSelectEvt();
    },
    editSave() {
      let selectFeatures = this.select.getFeatures();
      let editName = this.editNameDoc.value
      if (editName === "" || selectFeatures.getLength() === 0) {
        console.log(editName, selectFeatures.getLength());
        alert("保存信息不完整，请检查后再保存！");
      } else {
        if (confirm("确定保存该条记录吗？")) {
          this.saveEdit();
        } else {
          return;
        }
      }
    },
    addDrawTool(type) {
      let that = this
      if (this.draw) this.map.removeInteraction(this.draw);
      this.draw = new Draw({
        source: this.source,
        type: type,
        stopClick: true,
        freehand: false//自由手绘
      });
      this.map.addInteraction(this.draw);
      //绘制结束
      this.draw.on("drawend", function (evt) {
        that.draw.setActive(false);
        that.select.setActive(true);
        let feature = evt.feature;
        feature.attr = {
          id: "",
          name: "",
        };
        that.select.getFeatures().insertAt(0, feature);
        that.startEdit();
      });
    },
    saveEdit() {
      let selectFeatures = this.select.getFeatures();
      let selectFeature = selectFeatures.item(0);
      selectFeature.attr.name = this.editNameDoc.value;
      let wkt = new WKT().writeFeature(selectFeature);
      console.log(wkt);

      this.editInfoDoc.style.display = "none";
      this.edit.setActive(false);
      this.select.setActive(false);
      this.select.getFeatures().clear();
      this.toolsDoc.children().removeClass("active");

      this.editNameDoc.value = "";
    },
    startEdit() {
      let feture = this.select.getFeatures().item(0);
      let attr = feture.attr;
      this.editNameDoc.value = attr.name
      this.editInfoDoc.style.display = "block";
      this.edit.setActive(true);
    },
    deSelectEvt() {
      if (this.edit.getActive()) {
        if (confirm("正在编辑，是否保存？")) {
          this.saveEdit();
        }
      } else {
        this.edit.setActive(false);
        this.editInfoDoc.style.display = "none";
      }
    },
    async addFeatures() {
      if (this.source)
        this.source.clear();
      const { data: result } = await this.$http.get('edit/query', { name: null })
      let length = 0;
      let features = [];
      if (result.data)
        length = result.data.length
      for (let i = 0; i < length; i++) {
        let _r = result.data[i];
        let feature = new WKT().readFeature(_r.geom);
        //transform("EPSG:4326", "EPSG:3857");
        feature.attr = {
          id: _r.id,
          name: _r.name
        };
        features.push(feature);
      }
      this.source.addFeatures(features);
      this.source.changed()
    },
    styleFunction(feature, isSelect) {
      let stroke = new Stroke({
        color: 'black',
        width: 2
      });
      let fill = new Fill({
        color: 'red'
      });
      let _name = "",
        _color = "#ccc";
      if (isSelect) {
        _name = feature.get("name");
        _color = "#f00";
      }
      return new Style({
        stroke: stroke,
        fill: fill,
        image: new Circle({
          radius: 8,
          fill: new Fill({
            color: _color
          })
        }),
        text: new Text({
          text: _name,
          textAlign: "left",
          offsetX: 12,
          font: "bold 13px sans-serif",
          fill: new Fill({
            color: 'red'
          }),
          stroke: new Stroke({
            color: 'white',
            width: 2
          })
        })
      });
    }
  }
}
</script>

<style lang="less" scoped>
.tool-box {
  font-size: 12px;
  position: absolute;
  top: 140px;
  right: 50px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 1px 1px 2px #ccc;
  padding: 0;
  margin: 0;
  list-style: none;
  z-index: 9;
}

.tool-box li {
  float: left;
  height: 25px;
  line-height: 25px;
  width: 40px;
  text-align: center;
  cursor: pointer;
}

.tool-box li:first-child {
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
}

.tool-box li:last-child {
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
}

.tool-box li:not(:first-child) {
  border-left: 1px solid #ccc;
}

.tool-box li.active {
  background-color: #5367ec;
  color: white;
}

.edit-info {
  position: absolute;
  top: 50px;
  right: 12px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 1px 1px 2px #ccc;
  padding: 10px;
  list-style: none;
  z-index: 9;
  display: none;
}
</style>
