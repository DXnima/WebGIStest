<template>
  <el-container class="home-container">
    <!--头部区域-->
    <el-header>
      <div>
        <img src="../assets/logo.svg" alt="" style="width: 60px">
        <span>WebGISTest系统</span>
      </div>
      <el-button type="info" @click="logout">退出</el-button>
    </el-header>
    <!--页面主体区-->
    <el-container>
      <!--左边区域-->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!---侧边菜单栏-->
        <el-menu @open="handleMenuOpenClose" @close="handleMenuOpenClose" background-color="#333744" text-color="#fff"
          active-text-color="#409eff" :unique-opened="true" :collapse="isCollapse" :collapse-transition="false"
          :router="true" :default-active="activePath">
          <!--一级菜单-->
          <el-submenu :index="item.id + ''" v-for="item in menulist" :key="item.id">
            <!--一级菜单模版区-->
            <template slot="title">
              <!--图标-->
              <i :class="iconObj[item.id] ? iconObj[item.id] : 'el-icon-location'"></i>
              <!--文本-->
              <span>{{ item.authName }}</span>
            </template>

            <!--二级菜单-->
            <el-menu-item :index="'/' + subItem.path" v-for="subItem in item.children" :key="subItem.id"
              @click="saveNavState('/' + subItem.path)">
              <template slot="title">
                <!--图标-->
                <i class="el-icon-menu"></i>
                <!--文本-->
                <span>{{ subItem.authName }}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!--右边区域-->
      <el-main>
        <!--陆游占位符-->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      menulist: [{
        "id": 100,  // 菜单一级图标id
        "authName": "OpenLayers", // 菜单一级标题
        "path": null, // 菜单一级跳转路由
        "children": [
          {
            "id": 101, // 菜单二级图标id
            "authName": "图层切换", // 菜单二级标题
            "path": 'LayerSwitch', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 102,
            "authName": "图层多选控件",
            "path": 'LayerCheckBox',
            "children": []
          },
          {
            "id": 103,
            "authName": "获取经纬度",
            "path": 'ClikLatLon',
            "children": []
          },
          {
            "id": 104,
            "authName": "Target和View",
            "path": 'TargetView',
            "children": []
          },
          {
            "id": 105,
            "authName": "地图联动",
            "path": 'LayerLink',
            "children": []
          },
          {
            "id": 106,
            "authName": "地图弹窗",
            "path": 'ShowPopup',
            "children": []
          },
          {
            "id": 107,
            "authName": "加载后端图层",
            "path": 'PostPoint',
            "children": []
          },
          {
            "id": 108,
            "authName": "加载GeoJSON",
            "path": 'ShowGeoJSON',
            "children": []
          },
          {
            "id": 109,
            "authName": "加载WKT",
            "path": 'ShowWKT',
            "children": []
          },
          {
            "id": 110,
            "authName": "图层高亮",
            "path": 'ClickHighlight',
            "children": []
          },
          {
            "id": 111,
            "authName": "在线编辑",
            "path": 'LayerEdit',
            "children": []
          },
          {
            "id": 112,
            "authName": "结合Echarts",
            "path": 'LayerEcharts',
            "children": []
          },
          {
            "id": 113,
            "authName": "GIS系统模板",
            "path": 'WebGISUI',
            "children": []
          },
          {
            "id": 114,
            "authName": "综合例子",
            "path": 'ShowCollege',
            "children": []
          },
        ]
      },
      {
        "id": 200,  // 菜单一级图标id
        "authName": "MapBox", // 菜单一级标题
        "path": null, // 菜单一级跳转路由
        children: [
          {
            "id": 201, // 菜单二级图标id
            "authName": "加载矢量瓦片", // 菜单二级标题
            "path": 'VectorTiles', // 菜单二级跳转路由
            "children": []
          },
        ]
      },
      {
        "id": 300,  // 菜单一级图标id
        "authName": "Geoserver", // 菜单一级
        "children": [
          {
            "id": 301, // 菜单二级图标id
            "authName": "加载WMS", // 菜单二级标题
            "path": 'WMS', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 302, // 菜单二级图标id
            "authName": "加载WMTS", // 菜单二级标题
            "path": 'WMTS', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 303,
            "authName": "加载WFS",
            "path": 'WFS',
            "children": []
          },
          {
            "id": 304,
            "authName": "WFS添加数据",
            "path": 'AddWFS',
            "children": []
          },
          {
            "id": 305,
            "authName": "WFS修改数据",
            "path": 'UpdateWFS',
            "children": []
          },
          {
            "id": 306,
            "authName": "WFS删除数据",
            "path": 'DeleteWFS',
            "children": []
          },
          {
            "id": 307,
            "authName": "WFS综合操作",
            "path": 'AllWFS',
            "children": []
          },
          {
            "id": 308,
            "authName": "WFS查询数据",
            "path": 'QueryWFS',
            "children": []
          },
          {
            "id": 309,
            "authName": "WFS综合查询",
            "path": 'AllQueryWFS',
            "children": []
          }
        ]
      },
      {
        "id": 400,  // 菜单一级图标id
        "authName": "Geoserver REST", // 菜单一级
        "children": [
          {
            "id": 401, // 菜单二级图标id
            "authName": "地图服务后台", // 菜单二级标题
            "path": 'GeoserverRest', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 402, // 菜单二级图标id
            "authName": "PostGIS编辑", // 菜单二级标题
            "path": 'TableRest', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 403, // 菜单二级图标id
            "authName": "Geoserver编辑", // 菜单二级标题
            "path": 'FeatureRest', // 菜单二级跳转路由
            "children": []
          },
        ]
      },
      {
        "id": 500,  // 菜单一级图标id
        "authName": "空间分析", // 菜单一级标题
        "path": null, // 菜单一级跳转路由
        "children": [
          {
            "id": 501, // 菜单二级图标id
            "authName": "空间关系", // 菜单二级标题
            "path": 'SpaceRelation', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 502, // 菜单二级图标id
            "authName": "叠加分析", // 菜单二级标题
            "path": 'InterAnalysis', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 503, // 菜单二级图标id
            "authName": "合并分析", // 菜单二级标题
            "path": 'UnionAnalysis', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 504, // 菜单二级图标id
            "authName": "差异分析", // 菜单二级标题
            "path": 'DiffAnalysis', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 505, // 菜单二级图标id
            "authName": "Sym差异分析", // 菜单二级标题
            "path": 'SymDiffAnalysis', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 506, // 菜单二级图标id
            "authName": "缓冲区分析", // 菜单二级标题
            "path": 'BuffAnalysis', // 菜单二级跳转路由
            "children": []
          },
          {
            "id": 507, // 菜单二级图标id
            "authName": "网络分析", // 菜单二级标题
            "path": 'NetAnalysis', // 菜单二级跳转路由
            "children": []
          }
        ]
      },
      {
        "id": 600,  // 菜单一级图标id
        "authName": "GDAL", // 菜单一级标题
        "path": null, // 菜单一级跳转路由
        "children": [
          {
            "id": 601, // 菜单二级图标id
            "authName": "读取GDB", // 菜单二级标题
            "path": 'ReadGdb', // 菜单二级跳转路由
            "children": []
          }]
      },
      {
        "id": 700,  // 菜单一级图标id
        "authName": "地图样式修改", // 菜单一级标题
        "path": null, // 菜单一级跳转路由
        "children": []
      }],
      iconObj: {
        '125': 'el-icon-menu',
        '103': 'el-icon-location',
        '101': 'el-icon-eleme',
        '102': 'el-icon-delete',
        '145': 'el-icon-setting'
      },
      //默认不折叠
      isCollapse: false,
      //被激活的链接地址
      activePath: ''
    }
  },
  created() {
    this.getMenuList()
    this.activePath = window.sessionStorage.getItem('activePath')
  },
  methods: {
    logout() {
      window.sessionStorage.clear();
      this.$router.push("/login");
    },
    //获取所有菜单
    async getMenuList() {
      // const {data:res}=await this.$http.get('menus')
      //   if (res.meta.status !==200) return this.$message.error(res.meta.msg)
      //   this.menulist=res.data
    },
    //按钮切换菜单折叠展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse
    },
    //保存路径的激活状态
    saveNavState(activePath) {
      window.sessionStorage.setItem('activePath', activePath)
      this.activePath = activePath
    },
    handleMenuOpenClose(index) {
      if (index === '700') {
        const newWindow = window.open('./maputnik/index.html', '_blank'); // 在新窗口中打开静态HTML页面
        if (newWindow) {
          newWindow.focus();
        } else {
          // 可以显示提示或执行其他操作
          alert('已阻止弹出窗口。请允许此站点的弹出窗口!');
        }
      }
    }
  }
}
</script>

<style lang="less" scoped>
.home-container {
  height: 100%;
}

.el-header {
  background-color: #373D41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #ffffff;
  font-size: 20px;

  >div {
    display: flex;
    align-items: center;

    span {
      margin-left: 15px;
    }
  }
}

.el-aside {
  background-color: #333747;

  .el-menu {
    border-right: none;
  }
}

.el-main {
  background-color: #eaedf1;
}

.iconfont {
  margin-right: 10px;
}

.toggle-button {
  background-color: #4a5064;
  font-size: 10px;
  line-height: 24px;
  color: #ffffff;
  text-align: center;
  cursor: pointer;
}
</style>
