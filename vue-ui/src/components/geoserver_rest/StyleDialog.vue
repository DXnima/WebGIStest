<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-select v-model="ruleIndex" placeholder="请选择" @change="changeStyleValue">
          <el-option v-for="(item,index) in styleValue"
                     :key="index"
                     :label="item.name"
                     :value="index">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="12">
        <el-button v-if="false" type="primary" @click="changeDefaultStyles">默 认</el-button>
        <el-button type="primary" @click="changeStyles">修 改</el-button>
      </el-col>
    </el-row>
    <el-tabs v-model="activeName">
      <el-tab-pane label="常规" name="general">
        <el-row v-if="styleForm.geomType === 'POINT'">
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">大小：</p>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.size" :min="0" :step="2"></el-input-number>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">旋转角度：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.rotation"
                               :min="0" :max="360" :step="5"
                               size="mini"></el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">偏移量：</p>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.offset[0]" :step="2">
              </el-input-number>
              <div class="style-edit-input-des">X</div>
            </div>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.offset[1]" :step="2">
              </el-input-number>
              <div class="style-edit-input-des">Y</div>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">比例尺范围：</p>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.minscale" :min="0" :step="100000" :max="559000000">
              </el-input-number>
              <div class="style-edit-input-des">最小比例尺</div>
            </div>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.maxscale" :min="0" :step="100000" :max="559000000">
              </el-input-number>
              <div class="style-edit-input-des">最大比例尺</div>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="边框" name="border">
        <el-divider content-position="center">
          <el-switch class="el-switch-bottom" v-model="styleForm.borderenable" active-text="启用边框"></el-switch>
        </el-divider>
        <el-row v-if="styleForm.geomType === 'POINT'">
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">边框宽度：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.borderwidth"
                               size="mini"
                               :disabled="!styleForm.borderenable"
                               :min="0" :max="10" :step="0.1" ></el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">边框颜色：</p>
            <div class="col-2 style-edit-input">
              <el-color-picker v-model="styleForm.bordercolor" size="mini"
                               :disabled="!styleForm.borderenable"></el-color-picker>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">边框透明度：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.borderopacity" size="mini"
                               :min="0.0" :max="1.0" :step="0.1"></el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-divider v-if="styleForm.geomType !== 'POINT'" content-position="center">设置图片作为边框</el-divider>
        <el-row v-if="styleForm.geomType !== 'POINT'">
          <el-col class="style-edit-wrapper" :span="24">
            <p class="style-title">图片路径：</p>
            <div class="col-2 style-edit-input">
              <el-input v-model="styleForm.graphicspathborder[0]"></el-input>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="styleForm.geomType !== 'POINT' ">
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">图片宽度：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.graphicspathborder[1]"
                               size="mini"
                               :min="0" :max="10" :step="0.1" ></el-input-number>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">图片大小：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.graphicspathborder[3]"
                               size="mini"
                               :min="0" :step="2" ></el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-divider v-if="styleForm.geomType !== 'POINT'" content-position="center">设置虚线样式</el-divider>
        <el-row v-if="styleForm.geomType !== 'POINT'">
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">虚线间隔：</p>
            <div class="col-2 style-edit-input">
              <el-input v-model="styleForm.dash" placeholder="请用“,”隔开,如:1,3,5,7,9" style="width: 70%"></el-input>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">虚线偏移：</p>
            <div class="col-1 style-edit-input">
              <el-input-number v-model="styleForm.dashoffset" :min="0" :max="100"
                         size="mini"></el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="styleForm.geomType !== 'POINT'">
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">线帽样式：</p>
            <div class="col-2 style-edit-input">
              <el-select size="mini" v-model="styleForm.linecap" placeholder="请选择线帽样式">
                <el-option v-for="item in linecaps" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">拐点样式：</p>
            <div class="col-2 style-edit-input">
              <el-select size="mini" v-model="styleForm.linejoin" placeholder="请选择拐点样式">
                <el-option v-for="item in linejoins" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="填充" name="fill">
        <el-divider v-if="styleForm.geomType === 'LINE' " content-position="center">线要素没有填充样式</el-divider>
        <el-divider v-if="styleForm.geomType !== 'LINE' " content-position="center">
          <el-switch class="el-switch-bottom" v-model="styleForm.fillenable" active-text="启用填充" @change="changeFillenable"></el-switch>
        </el-divider>
        <el-row v-if="styleForm.geomType!=='LINE'">
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">填充颜色：</p>
            <div class="col-2 style-edit-input">
              <el-color-picker size="mini" v-model="styleForm.fillcolor"></el-color-picker>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">透明度：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.fillopacity"
                               :min="0.0" :max="1.0" :step="0.1"
                               size="mini"></el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-divider v-if="styleForm.geomType !== 'LINE' " content-position="center">
          <el-switch v-if="styleForm.geomType !== 'LINE' " class="el-switch-bottom" v-model="useGraphic" active-text="使用图形填充" inactive-text="使用图片填充" @change="changeFill"></el-switch>
        </el-divider>
        <el-row v-if="!useGraphic && styleForm.geomType === 'POINT' ">
          <el-col class="style-edit-wrapper" :span="24">
            <p class="style-title">图片路径：</p>
            <div class="col-2 style-edit-input">
              <el-input v-model="styleForm.path" placeholder="请输入URL" style="width: 100%"></el-input>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="!useGraphic && styleForm.geomType === 'POLYGON' ">
          <el-col class="style-edit-wrapper" :span="24">
            <p class="style-title">图片路径：</p>
            <div class="col-2 style-edit-input">
              <el-input v-model="styleForm.graphicspathfill[0]" placeholder="请输入URL" style="width: 100%"></el-input>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="24">
            <p class="style-title">图片大小：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.graphicspathfill[1]"
                               size="mini"
                               :min="0" :step="2" ></el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="useGraphic">
          <el-col v-if="styleForm.geomType ==='POINT'" class="style-edit-wrapper" :span="12">
            <p class="style-title">图形形状：</p>
            <div class="col-2 style-edit-input">
              <el-select size="mini" v-model="styleForm.markname" placeholder="请选择形状">
                <el-option v-for="item in marks" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </div>
          </el-col>
          <el-col v-if="styleForm.geomType ==='POLYGON'" class="style-edit-wrapper" :span="12">
            <p class="style-title">图形形状：</p>
            <div class="col-2 style-edit-input">
              <el-select size="mini" v-model="styleForm.wkmgraphicsfill[0]" placeholder="请选择形状">
                <el-option v-for="item in marks" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </div>
          </el-col>
          <el-col v-if="styleForm.geomType==='POLYGON'" class="style-edit-wrapper" :span="12">
            <p class="style-title">图形宽度：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.wkmgraphicsfill[1]"
                               size="mini"
                               :min="0" :max="10" :step="0.1" ></el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="useGraphic && styleForm.geomType==='POLYGON'">
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">图形颜色：</p>
            <div class="col-2 style-edit-input">
              <el-color-picker size="mini" v-model="styleForm.wkmgraphicsfill[2]"></el-color-picker>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">图形大小：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.wkmgraphicsfill[3]"
                               size="mini"
                               :min="0.1" :step="2" ></el-input-number>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="标签" name="label">
        <el-divider content-position="center">
          <el-switch class="el-switch-bottom" v-model="styleForm.labelenable" active-text="启用标签" inactive-text="不启用标签"></el-switch>
        </el-divider>
        <el-row>
          <el-col class="style-edit-wrapper" :span="24">
            <p class="style-title">标签内容：</p>
            <div class="col-2 style-edit-input" :disabled="!styleForm.labelenable">
              <el-row>
                <el-col :span="4">
                  <el-switch v-model="styleForm.label.fromField"></el-switch>
                  <div class="style-edit-input-des">是否启用字段</div>
                </el-col>
                <el-col v-if="!styleForm.label.fromField" :span="20">
                  <el-input placeholder="请输入内容"
                            size="mini" style="width: 80%"
                            v-model="styleForm.label.values[0]"
                            :disabled="styleForm.label.fromField"></el-input>
                  <div class="style-edit-input-des">输入内容</div>
                </el-col>
                <el-col v-else :span="20">
                  <el-select v-model="styleForm.label.values[0]" placeholder="请选择字段"
                             size="mini" :disabled="!styleForm.label.fromField">
                    <el-option v-for="item in layerInfo.attributes" :value="item"></el-option>
                  </el-select>
                  <div class="style-edit-input-des">选择字段</div>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
        <el-divider content-position="center">标签样式设置</el-divider>
        <el-row>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">字体设置：</p>
            <div class="col-2 style-edit-input">
              <el-select v-model="styleForm.labelfont[0]"
                         size="mini"
                         filterable allow-create
                         placeholder="请选择字体"
                         :disabled="!styleForm.labelenable">
                <el-option v-for="item in fonts" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">字形设置：</p>
            <div class="col-3 style-edit-input">
              <div class="align-type-item clearFlex" @click="handleFont2Click('')">
                <el-tooltip effect="dark" content="默认" placement="bottom">
                  <i ref="isNull" class="el-icon-s-operation"></i>
                </el-tooltip>
              </div>
              <div class="align-type-item clearFlex" @click="handleFont2Click('italic')">
                <el-tooltip effect="dark" content="斜体" placement="bottom">
                  <i ref="italic" class="el-icon-s-operation"></i>
                </el-tooltip>
              </div>
              <div class="align-type-item clearFlex" @click="handleFont2Click('normal')">
                <el-tooltip effect="dark" content="粗体" placement="bottom">
                  <i ref="normal" class="el-icon-s-operation"></i>
                </el-tooltip>
              </div>
              <div class="col-2 style-edit-input">
                <el-input-number v-model="styleForm.labelfont[2]"
                                 size="mini"
                                 :disabled="!styleForm.labelenable"
                                 :min="1" :max="50" :step="1" ></el-input-number>
                <div class="style-edit-input-des">字体大小</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="style-edit-wrapper" :span="4">
            <p class="style-title">字体颜色：</p>
            <div class="col-2 style-edit-input">
              <el-color-picker v-model="styleForm.labelcolor" size="mini"
                               :disabled="!styleForm.labelenable"></el-color-picker>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="4">
            <p class="style-title">光晕颜色：</p>
            <div class="col-2 style-edit-input">
              <el-color-picker v-model="styleForm.labelhalocolor" size="mini"
                               :disabled="!styleForm.labelenable"></el-color-picker>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="8">
            <p class="style-title">光晕半径：</p>
            <div class="col-2 style-edit-input">
              <el-input-number v-model="styleForm.labelhaloradius" size="mini"
                         :min="0" :max="5" :step="0.2"
                         :disabled="!styleForm.labelenable"></el-input-number>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="8">
            <p class="style-title">旋转角度：</p>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.labelrotation" :min="0" :step="1">
              </el-input-number>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">轴心位移：</p>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.labelanchor[0]" :step="0.1">
              </el-input-number>
              <div class="style-edit-input-des">X</div>
            </div>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.labelanchor[1]" :step="0.1">
              </el-input-number>
              <div class="style-edit-input-des">Y</div>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">偏移量：</p>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.labeldisplacement[0]" :step="1">
              </el-input-number>
              <div class="style-edit-input-des">X</div>
            </div>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.labeldisplacement[1]" :step="1">
              </el-input-number>
              <div class="style-edit-input-des">Y</div>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="styleForm.geomType === 'LINE'">
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">初始间距：</p>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.labelinitialgap" :min="0" :step="1">
              </el-input-number>
            </div>
          </el-col>
          <el-col class="style-edit-wrapper" :span="12">
            <p class="style-title">垂直偏移量：</p>
            <div class="col-2 style-edit-input">
              <el-input-number size="mini" v-model="styleForm.labelperpendicularoffset" :step="1">
              </el-input-number>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane v-if="false" label="过滤" name="filter">
        <el-input  type="textarea" :rows="2" placeholder="请输入过滤条件"  v-model="styleForm.filter"></el-input>
      </el-tab-pane>
      <el-tab-pane v-if="false" label="XML" name="xml">
        <el-input  type="textarea" :rows="12" v-model="xml" readonly></el-input>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import {changeStyleByStyleName, getStyleValueByLayerName} from "@/api";

export default {
  name: "StyleDialog",
  props: {
    layerName: {
      type: String,
      default: '',
    }
  },
  data() {
    return {
      layerInfo: {},
      geoName:'',
      activeName: 'general',
      styleValue: [],
      useGraphic: true,
      xml: '',
      styleForm: {
        geomType: '',
        //常规属性
        name: 'default rule',
        path: '', //点样式
        markname: '', //点样式
        size: 0, //点样式
        rotation: 0,//点样式
        offset: [0, 0, ''],
        minscale: 0,
        maxscale: 0,
        //边框属性
        borderenable: false,
        borderwidth: 0,
        bordercolor: '',
        borderopacity: 0,
        //填充属性
        fillenable: false,//点、面样式
        fillcolor: '',//点、面样式
        fillopacity: 0,//点、面样式
        //图片路径属性
        graphicspathborder: ['', 0, 0],//线、面样式
        graphicspathfill: ['', 0],//面样式
        //填充标记属性
        wkmgraphicsfill: ['circle', 0, '#00008B', 0],//面样式
        //虚线和线属性
        dash: '1,3,5',//线、面样式
        dashoffset: '0',//线、面样式
        linecap: '',//线、面样式
        linejoin: '',//线、面样式
        //标签属性
        labelenable: false,
        label: {
          fromField: false,
          values: ['default label']
        },
        labelfont: ["宋体", "normal", 0],
        labelcolor: '',
        labelhalocolor: '',
        labelhaloradius: 0,
        labelanchor: [0, 0],
        labeldisplacement: [0, 0],
        labelrotation: 0,
        labelinitialgap: 0,//线样式
        labelperpendicularoffset: 0,//线样式
        //过滤属性
        filter: ''
      },
      ruleIndex: 0,
      useFill: false,
      marks: [
        {name: '不使用图形', value: ''},
        {name: '十字架', value: 'cross'},
        {name: '圆形', value: 'circle'},
        {name: "三角形", value: 'triangle'},
        {name: "x形", value: 'X'},
        {name: "星形", value: 'star'},
        {name: "箭头", value: 'arrow'},
        {name: 'hatch', value: 'hatch'},
        {name: '正方形', value: 'square'}
      ],
      fonts: [
        {name: 'Arial', value: 'Arial'},
        {name: 'Arial Black', value: 'Arial Black'},
        {name: 'Arial Italic', value: 'Arial Italic'},
        {name: 'Arial Bold Italic', value: 'Arial Bold Italic'},
        {name: '宋体', value: 'SimSun'},
        {name: '黑体', value: 'SimHei'},
        {name: '微软雅黑', value: 'Microsoft YaHei'},
        {name: '楷体', value: 'KaiTi'},
      ],
      linecaps: [
        {name: '默认', value: 'butt'},
        {name: '圆形', value: 'round'},
        {name: '方形', value: 'square'}
      ],
      linejoins: [
        {name: '默认', value: 'miter'},
        {name: '斜面', value: 'bevel'},
        {name: '弧形', value: 'round'}
      ]
    };
  },
  created() {
    this.getLayerInfo()
  },
  methods: {
    handleFont2Click(e) {
      switch (e) {
        case 'italic':
          this.$refs.isNull.style.background = "#DCDFE6"
          this.$refs.italic.style.background = "#449bd2"
          this.$refs.normal.style.background = "#DCDFE6"
          break;
        case 'normal':
          this.$refs.isNull.style.background = "#DCDFE6"
          this.$refs.italic.style.background = "#DCDFE6"
          this.$refs.normal.style.background = "#449bd2"
          break;
        default:
          this.$refs.isNull.style.background = "#449bd2"
          this.$refs.italic.style.background = "#DCDFE6"
          this.$refs.normal.style.background = "#DCDFE6"
          break;
      }
      this.styleForm.labelfont[1] = e
    },
    changeStyleValue(e) {
      this.styleForm = this.styleValue[e]
      this.styleForm.offset[2] = this.geoName
    },
    changeFillenable(e){
      console.log(e);
    },
    changeFill(e) {
      if (!e) {
        //使用图片填充
        this.styleForm.markname = null
        this.styleForm.graphicspathfill = ['', 0]
        this.styleForm.wkmgraphicsfill = []
      } else {
        //使用图形填充
        this.styleForm.path = null
        this.styleForm.graphicspathfill = []
        this.styleForm.wkmgraphicsfill = ['', 0, '#000000', 0]
      }
    },
    //获取指定图层信息
    async getLayerInfo() {
      const { data: ref } = await this.$API.getLayerInfo({ 'layerName': this.layerName });
      if (ref.status !== 200) return;
      this.layerInfo = ref.data
      this.layerInfo.attributes.forEach(item => {
        if (item.search("geom") !== -1) {
          this.geoName = item;
        }
      })
      await this.getStyleXML(ref.data.style);
      await this.getStyleValue();
    },
    async getStyleXML(style) {
      const {data: ref} = await this.$API.getSLD({'styleName':style});
      if (ref.status !== 200) return;
      this.xml = ref.data;
    },
    async getStyleValue() {
      const {data: ref} = await this.$API.getStyleValueByLayerName({'layerName':this.layerName});
      if (ref.status !== 200) {
        this.$message.error(ref.msg);
        return;
      }
      this.$message.success(ref.msg);
      this.styleValue = ref.data
      this.styleForm = this.styleValue[0]
      this.styleForm.offset[2] = this.geoName
      this.handleFont2Click(this.styleForm.labelfont[1])
    },
    changeDefaultStyles() {
    },
    async changeStyles() {
      const {data: ref} = await this.$API.changeStyleByStyleName(this.layerInfo.style,this.styleValue);
      if (ref.status !== 200) {
        this.$message.error(ref.msg);
        return;
      }
      this.$message.success(ref.msg);
      //刷新页面
      window.location.reload();
    }
  },
  watch: {
    layerName(val) {
      this.getLayerInfo();
    }
  }
}
</script>

<style lang="less" scoped>
.style-edit-wrapper {
  text-align: center;
  padding-bottom: 10px;

  .style-title {
    text-align: left;
    min-width: 60px;
    font-size: 12px;
    margin-bottom: 10px;
  }

  .style-edit-input {
    &.col-2 {
      margin-bottom: 10px;
    }

    &.col-1 {
      margin-bottom: 10px;
      margin-right: 10px;
    }

    &.col-3 {
      margin-bottom: 10px;
      display: flex;
    }

    &.col-4 {
      width: 50px;
      margin-left: 10px;
    }

    .style-edit-input-des {
      text-align: center;
      line-height: 1;
      margin-top: 2px;
      font-size: 12px;
      color: #000000;
    }
  }
}

.el-switch-bottom{
  margin-bottom: 10px;
}

.el-switch-right{
  margin-right: 10px;
}

.el-switch-left{
  margin-left: 10px;
}

.align-type-item {
  flex: 1;
  cursor: pointer;
  text-align: center;
  &.clearFlex {
    width: 42px;
    flex: none;
  }
  i {
    line-height: 1;
    display: inline-block;
    padding: 6px;
    border-radius: 4px;
    background: #DCDFE6;
    color: white;
  }
  &:hover {
    i {
      background: #449bd2;
    }
  }
}

</style>
