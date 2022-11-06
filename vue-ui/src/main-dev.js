import Vue from 'vue'
import App from './App.vue'
import router from './router'
import * as API from '@/api/index'
//导入全局样式表
import './assets/css/global.css'
import './assets/css/ol.css'
//导入Element
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'; //样式文件需要单独引入
Vue.use(ElementUI)
//导入VXETable
import 'xe-utils'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'
Vue.use(VXETable)
//导入加载 进度条
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
//导入axios
import axios from 'axios'
//配置请求的根路径
axios.defaults.baseURL = process.env.VUE_APP_BASE_API
// 请求拦截 请求头添加token request展示进度条 NProgress.start()
axios.interceptors.request.use(config => {
  NProgress.start()
  config.headers.Authorization = window.sessionStorage.getItem("token")
  return config
})
//response隐藏进度条 NProgress.done()
axios.interceptors.response.use(config => {
  NProgress.done()
  return config
})
Vue.prototype.$http = axios
Vue.prototype.$api = API;
Vue.prototype.$API = API;

Vue.config.productionTip = false

//格式化时间组件
Vue.filter('dateFormat', function (originVal) {
  const dt = new Date(originVal)

  const y = dt.getFullYear()
  const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  const d = (dt.getDay() + '').padStart(2, '0')

  const hh = (dt.getHours() + '').padStart(2, '0')
  const mm = (dt.getMinutes() + '').padStart(2, '0')
  const ss = (dt.getSeconds() + '').padStart(2, '0')
  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
})

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
