<template>
  <div>
    <!--面包屑导航区域-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>Geoserver REST</el-breadcrumb-item>
      <el-breadcrumb-item>PostGIS编辑</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片区域-->
    <el-card>
      <el-container>
        <el-aside width="200px">
          <el-scrollbar class="elScrollbar">
            <el-menu
              :default-active="$route.path"
              :default-openeds="['1']"
              class="el-menu-vertical-demo" :router="true">
              <el-submenu index="1">
                <template slot="title">
                  <i class="el-icon-location"></i>
                  <span>表格</span>
                </template>
                <el-menu-item v-for="item in tables" :index="'/TableRest/'+item.name+','+item.geom+','+item.type" :key = item.name>{{ item.name }}</el-menu-item>
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
      const {data:ref} = await this.$API.getTablesInfo();
      if (ref.status !== 200) return;
      this.tables = ref.data;
      if (this.tables.length > 0) {
        let active = '/TableRest/' + this.tables[0].name + ',' + this.tables[0].geom + ',' + this.tables[0].type;
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
