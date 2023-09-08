<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>Geoserver REST</el-breadcrumb-item>
      <el-breadcrumb-item>WFS编辑</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <el-container>
        <el-aside width="300px">
          <el-scrollbar class="elScrollbar">
            <el-menu
              :default-active="$route.path"
              :default-openeds="['1']"
              class="el-menu-vertical-demo" :router="true">
              <el-submenu index="1">
                <template slot="title">
                  <i class="el-icon-location"></i>
                  <span>图层</span>
                </template>
                <el-menu-item v-for="item in tables" :index="'/FeatureRest/'+item.layerName" :key = item.layerName>{{ item.layerName }}</el-menu-item>
              </el-submenu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        <el-main class="edit_main">
          <!--陆游占位符-->
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-card>
  </div>
</template>
<script>
export default {
  name: "Edit",
  data(){
    return {
      tables:[]
    }
  },
  created() {
    this.getTables()
  },
  methods: {
    async getTables(){
      const { data: ref } = await this.$API.getLayersInfo();
      if (ref.status === 404) return;
      this.tables = ref.data;
      if (this.tables.length > 0) {
        let active = '/FeatureRest/' + this.tables[0].layerName;
        await this.$router.push(active)
      }
    }
  }
}
</script>

<style lang="less">
.edit_main{
  padding: 0;
}
.elScrollbar{
  height: 700px;
}
.el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>
