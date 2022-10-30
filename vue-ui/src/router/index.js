import Vue from 'vue'
import Router from 'vue-router'

//路由懒加载
const Login = () => import('../components/Login')
const Home = () => import('../components/Home')
const Welcome = () => import('../components/Welcome')
//OpenLayers
const ClickHighlight = () => import('../components/openlayers/ClickHighlight')
const ClikLatLon = () => import('../components/openlayers/ClikLatLon')
const LayerCheckBox = () => import('../components/openlayers/LayerCheckBox')
const LayerEcharts = () => import('../components/openlayers/LayerEcharts')
const LayerEdit = () => import('../components/openlayers/LayerEdit')
const LayerLink = () => import('../components/openlayers/LayerLink')
const LayerSwitch = () => import('../components/openlayers/LayerSwitch')
const PostPoint = () => import('../components/openlayers/PostPoint')
const ShowCollege = () => import('../components/openlayers/ShowCollege')
const ShowGeoJSON = () => import('../components/openlayers/ShowGeoJSON')
const ShowPopup = () => import('../components/openlayers/ShowPopup')
const ShowWKT = () => import('../components/openlayers/ShowWKT')
const TargetView = () => import('../components/openlayers/TargetView')
const WebGISUI = () => import('../components/openlayers/WebGISUI')
//Geoserver
const WMS = () => import('../components/geoserver/WMS')
const WMTS = () => import('../components/geoserver/WMTS')
const VectorTiles = () => import('../components/geoserver/VectorTiles')
const WFS = () => import('../components/geoserver/wfs/WFS')
const AddWFS = () => import('../components/geoserver/wfs/AddWFS')
const DeleteWFS = () => import('../components/geoserver/wfs/DeleteWFS')
const QueryWFS = () => import('../components/geoserver/wfs/QueryWFS')
const UpdateWFS = () => import('../components/geoserver/wfs/UpdateWFS')
const AllWFS = () => import('../components/geoserver/wfs/AllWFS')
const AllQueryWFS = () => import('../components/geoserver/wfs/AllQueryWFS')
//网络分析
const BuffAnalysis = () => import('../components/analysis/BuffAnalysis')
const DiffAnalysis = () => import('../components/analysis/DiffAnalysis')
const SymDiffAnalysis = () => import('../components/analysis/SymDiffAnalysis')
const InterAnalysis = () => import('../components/analysis/InterAnalysis')
const SpaceRelation = () => import('../components/analysis/SpaceRelation')
const UnionAnalysis = () => import('../components/analysis/UnionAnalysis')
const NetAnalysis = () => import('../components/analysis/NetAnalysis')
//GDAL
const ReadGdb = () => import('../components/gdal/ReadGdb')
Vue.use(Router)

const routes = [
  //重定向
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  {
    path: '/home',
    component: Home,
    redirect: '/welcome',
    //home下子路由规则
    children: [
      { path: '/welcome', component: Welcome },
      //openlayers
      { path: '/ClickHighlight', component: ClickHighlight },
      { path: '/ClikLatLon', component: ClikLatLon },
      { path: '/LayerCheckBox', component: LayerCheckBox },
      { path: '/LayerEcharts', component: LayerEcharts },
      { path: '/LayerEdit', component: LayerEdit },
      { path: '/LayerLink', component: LayerLink },
      { path: '/LayerSwitch', component: LayerSwitch },
      { path: '/PostPoint', component: PostPoint },
      { path: '/ShowCollege', component: ShowCollege },
      { path: '/ShowGeoJSON', component: ShowGeoJSON },
      { path: '/ShowPopup', component: ShowPopup },
      { path: '/ShowWKT', component: ShowWKT },
      { path: '/TargetView', component: TargetView },
      { path: '/WebGISUI', component: WebGISUI },
      //Geoserver
      { path: '/WMS', component: WMS },
      { path: '/WMTS', component: WMTS },
      { path: '/VectorTiles', component: VectorTiles },
      { path: '/WFS', component: WFS },
      { path: '/AddWFS', component: AddWFS },
      { path: '/DeleteWFS', component: DeleteWFS },
      { path: '/QueryWFS', component: QueryWFS },
      { path: '/UpdateWFS', component: UpdateWFS },
      { path: '/AllWFS', component: AllWFS },
      { path: '/AllQueryWFS', component: AllQueryWFS },
      //网络分析
      { path: '/BuffAnalysis', component: BuffAnalysis },
      { path: '/DiffAnalysis', component: DiffAnalysis },
      { path: '/SymDiffAnalysis', component: SymDiffAnalysis },
      { path: '/InterAnalysis', component: InterAnalysis },
      { path: '/SpaceRelation', component: SpaceRelation },
      { path: '/UnionAnalysis', component: UnionAnalysis },
      { path: '/NetAnalysis', component: NetAnalysis },
      //GDAL
      { path: '/ReadGdb', component: ReadGdb },
    ]
  }
]

const router = new Router({
  routes
})


//为路由对象，添加beforeEach导航守卫
//to 将要访问路径
//from 从哪个路径跳转而来
//next 函数 表示方向
router.beforeEach((to, from, next) => {
  //如果用户访问的登录页，直接放行
  if (to.path === '/login') return next()
  //从sessionStorage 中获取到保存的token值
  const tokenStr = window.sessionStorage.getItem('token')
  //没有token,强制跳转到登录页
  if (!tokenStr) return next('/login')
  next()
}
);


export default router
