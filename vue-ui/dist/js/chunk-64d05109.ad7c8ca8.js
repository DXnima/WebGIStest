(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-64d05109"],{"0a58":function(t,e,r){},2350:function(t,e,r){"use strict";var a={CARMENTA_SERVER:"carmentaserver",GEOSERVER:"geoserver",MAPSERVER:"mapserver",QGIS:"qgis"};e["a"]=a},"25f0":function(t,e,r){"use strict";var a=r("5e77").PROPER,i=r("cb2d"),n=r("825a"),o=r("577e"),s=r("d039"),u=r("90d8"),l="toString",c=RegExp.prototype,p=c[l],d=s((function(){return"/a/b"!=p.call({source:"a",flags:"b"})})),h=a&&p.name!=l;(d||h)&&i(RegExp.prototype,l,(function(){var t=n(this),e=o(t.source),r=o(u(t));return"/"+e+"/"+r}),{unsafe:!0})},3388:function(t,e,r){"use strict";var a=r("9d79"),i=r("0af5"),n=r("6a24"),o=r("e526"),s=r("b208"),u=r("abb7"),l=function(t,e,r){s["a"].call(this);var a=e||0;this.setCenterAndRadius(t,a,r)};a["a"].inherits(l,s["a"]),l.prototype.clone=function(){var t=new l(null);return t.setFlatCoordinates(this.layout,this.flatCoordinates.slice()),t},l.prototype.closestPointXY=function(t,e,r,a){var i=this.flatCoordinates,n=t-i[0],o=e-i[1],s=n*n+o*o;if(s<a){var u;if(0===s)for(u=0;u<this.stride;++u)r[u]=i[u];else{var l=this.getRadius()/Math.sqrt(s);for(r[0]=i[0]+l*n,r[1]=i[1]+l*o,u=2;u<this.stride;++u)r[u]=i[u]}return r.length=this.stride,s}return a},l.prototype.containsXY=function(t,e){var r=this.flatCoordinates,a=t-r[0],i=e-r[1];return a*a+i*i<=this.getRadiusSquared_()},l.prototype.getCenter=function(){return this.flatCoordinates.slice(0,this.stride)},l.prototype.computeExtent=function(t){var e=this.flatCoordinates,r=e[this.stride]-e[0];return i["a"].createOrUpdate(e[0]-r,e[1]-r,e[0]+r,e[1]+r,t)},l.prototype.getRadius=function(){return Math.sqrt(this.getRadiusSquared_())},l.prototype.getRadiusSquared_=function(){var t=this.flatCoordinates[this.stride]-this.flatCoordinates[0],e=this.flatCoordinates[this.stride+1]-this.flatCoordinates[1];return t*t+e*e},l.prototype.getType=function(){return o["a"].CIRCLE},l.prototype.intersectsExtent=function(t){var e=this.getExtent();if(i["a"].intersects(t,e)){var r=this.getCenter();return t[0]<=r[0]&&t[2]>=r[0]||(t[1]<=r[1]&&t[3]>=r[1]||i["a"].forEachCorner(t,this.intersectsCoordinate,this))}return!1},l.prototype.setCenter=function(t){var e,r=this.stride,a=this.flatCoordinates[r]-this.flatCoordinates[0],i=t.slice();for(i[r]=i[0]+a,e=1;e<r;++e)i[r+e]=t[e];this.setFlatCoordinates(this.layout,i)},l.prototype.setCenterAndRadius=function(t,e,r){if(t){this.setLayout(r,t,0),this.flatCoordinates||(this.flatCoordinates=[]);var a,i,o=this.flatCoordinates,s=u["a"].coordinate(o,0,t,this.stride);for(o[s++]=o[0]+e,a=1,i=this.stride;a<i;++a)o[s++]=o[a];o.length=s,this.changed()}else this.setFlatCoordinates(n["a"].XY,null)},l.prototype.getCoordinates=function(){},l.prototype.setCoordinates=function(t,e){},l.prototype.setFlatCoordinates=function(t,e){this.setFlatCoordinatesInternal(t,e),this.changed()},l.prototype.setRadius=function(t){this.flatCoordinates[this.stride]=this.flatCoordinates[0]+t,this.changed()},l.prototype.transform,e["a"]=l},"408a":function(t,e,r){var a=r("e330");t.exports=a(1..valueOf)},5899:function(t,e){t.exports="\t\n\v\f\r                　\u2028\u2029\ufeff"},"58a8":function(t,e,r){var a=r("e330"),i=r("1d80"),n=r("577e"),o=r("5899"),s=a("".replace),u="["+o+"]",l=RegExp("^"+u+u+"*"),c=RegExp(u+u+"*$"),p=function(t){return function(e){var r=n(i(e));return 1&t&&(r=s(r,l,"")),2&t&&(r=s(r,c,"")),r}};t.exports={start:p(1),end:p(2),trim:p(3)}},7156:function(t,e,r){var a=r("1626"),i=r("861d"),n=r("d2bb");t.exports=function(t,e,r){var o,s;return n&&a(o=e.constructor)&&o!==r&&i(s=o.prototype)&&s!==r.prototype&&n(t,s),t}},"90d8":function(t,e,r){var a=r("c65b"),i=r("1a2d"),n=r("3a9b"),o=r("ad6d"),s=RegExp.prototype;t.exports=function(t){var e=t.flags;return void 0!==e||"flags"in s||i(t,"flags")||!n(s,t)?e:a(o,t)}},"9fce":function(t,e,r){"use strict";r("0a58")},a9e3:function(t,e,r){"use strict";var a=r("83ab"),i=r("da84"),n=r("e330"),o=r("94ca"),s=r("cb2d"),u=r("1a2d"),l=r("7156"),c=r("3a9b"),p=r("d9b5"),d=r("c04e"),h=r("d039"),f=r("241c").f,m=r("06cf").f,g=r("9bf2").f,v=r("408a"),b=r("58a8").trim,E="Number",y=i[E],S=y.prototype,_=i.TypeError,R=n("".slice),T=n("".charCodeAt),C=function(t){var e=d(t,"number");return"bigint"==typeof e?e:F(e)},F=function(t){var e,r,a,i,n,o,s,u,l=d(t,"number");if(p(l))throw _("Cannot convert a Symbol value to a number");if("string"==typeof l&&l.length>2)if(l=b(l),e=T(l,0),43===e||45===e){if(r=T(l,2),88===r||120===r)return NaN}else if(48===e){switch(T(l,1)){case 66:case 98:a=2,i=49;break;case 79:case 111:a=8,i=55;break;default:return+l}for(n=R(l,2),o=n.length,s=0;s<o;s++)if(u=T(n,s),u<48||u>i)return NaN;return parseInt(n,a)}return+l};if(o(E,!y(" 0o1")||!y("0b1")||y("+0x1"))){for(var I,A=function(t){var e=arguments.length<1?0:y(C(t)),r=this;return c(S,r)&&h((function(){v(r)}))?l(Object(e),r,A):e},O=a?f(y):"MAX_VALUE,MIN_VALUE,NaN,NEGATIVE_INFINITY,POSITIVE_INFINITY,EPSILON,MAX_SAFE_INTEGER,MIN_SAFE_INTEGER,isFinite,isInteger,isNaN,isSafeInteger,parseFloat,parseInt,fromString,range".split(","),N=0;O.length>N;N++)u(y,I=O[N])&&!u(A,I)&&g(A,I,m(y,I));A.prototype=S,S.constructor=A,s(i,E,A,{constructor:!0})}},ad6d:function(t,e,r){"use strict";var a=r("825a");t.exports=function(){var t=a(this),e="";return t.hasIndices&&(e+="d"),t.global&&(e+="g"),t.ignoreCase&&(e+="i"),t.multiline&&(e+="m"),t.dotAll&&(e+="s"),t.unicode&&(e+="u"),t.unicodeSets&&(e+="v"),t.sticky&&(e+="y"),e}},bcb4:function(t,e,r){"use strict";var a=r("9d79"),i=r("92fa"),n=r("0af5"),o=r("38f3"),s=r("7fc9"),u=r("256f"),l=r("3c81"),c=r("345d"),p=r("40f5"),d=r("2350"),h=r("2c30"),f=r("b0c1"),m=r("c721"),g=function(t){var e=t||{},r=e.params||{},a=!("TRANSPARENT"in r)||r["TRANSPARENT"];p["a"].call(this,{attributions:e.attributions,cacheSize:e.cacheSize,crossOrigin:e.crossOrigin,logo:e.logo,opaque:!a,projection:e.projection,reprojectionErrorThreshold:e.reprojectionErrorThreshold,tileClass:e.tileClass,tileGrid:e.tileGrid,tileLoadFunction:e.tileLoadFunction,url:e.url,urls:e.urls,wrapX:void 0===e.wrapX||e.wrapX,transition:e.transition}),this.gutter_=void 0!==e.gutter?e.gutter:0,this.params_=r,this.v13_=!0,this.serverType_=e.serverType,this.hidpi_=void 0===e.hidpi||e.hidpi,this.tmpExtent_=n["a"].createEmpty(),this.updateV13_(),this.setKey(this.getKeyForParams_())};a["a"].inherits(g,p["a"]),g.prototype.getGetFeatureInfoUrl=function(t,e,r,i){var s=u["a"].get(r),p=this.getProjection(),d=this.getTileGrid();d||(d=this.getTileGridForProjection(s));var h=d.getTileCoordForCoordAndResolution(t,e);if(!(d.getResolutions().length<=h[0])){var f=d.getResolution(h[0]),m=d.getTileCoordExtent(h,this.tmpExtent_),g=c["a"].toSize(d.getTileSize(h[0]),this.tmpSize),v=this.gutter_;0!==v&&(g=c["a"].buffer(g,v,this.tmpSize),m=n["a"].buffer(m,f*v,m)),p&&p!==s&&(f=l["a"].calculateSourceResolution(p,s,t,f),m=u["a"].transformExtent(m,s,p),t=u["a"].transform(t,s,p));var b={SERVICE:"WMS",VERSION:a["a"].DEFAULT_WMS_VERSION,REQUEST:"GetFeatureInfo",FORMAT:"image/png",TRANSPARENT:!0,QUERY_LAYERS:this.params_["LAYERS"]};o["a"].assign(b,this.params_,i);var E=Math.floor((t[0]-m[0])/f),y=Math.floor((m[3]-t[1])/f);return b[this.v13_?"I":"X"]=E,b[this.v13_?"J":"Y"]=y,this.getRequestUrl_(h,g,m,1,p||s,b)}},g.prototype.getGutterInternal=function(){return this.gutter_},g.prototype.getParams=function(){return this.params_},g.prototype.getRequestUrl_=function(t,e,r,a,n,o){var u=this.urls;if(u){if(o["WIDTH"]=e[0],o["HEIGHT"]=e[1],o[this.v13_?"CRS":"SRS"]=n.getCode(),"STYLES"in this.params_||(o["STYLES"]=""),1!=a)switch(this.serverType_){case d["a"].GEOSERVER:var l=90*a+.5|0;"FORMAT_OPTIONS"in o?o["FORMAT_OPTIONS"]+=";dpi:"+l:o["FORMAT_OPTIONS"]="dpi:"+l;break;case d["a"].MAPSERVER:o["MAP_RESOLUTION"]=90*a;break;case d["a"].CARMENTA_SERVER:case d["a"].QGIS:o["DPI"]=90*a;break;default:i["a"].assert(!1,52);break}var c,p,f=n.getAxisOrientation(),g=r;if(this.v13_&&"ne"==f.substr(0,2))c=r[0],g[0]=r[1],g[1]=c,c=r[2],g[2]=r[3],g[3]=c;if(o["BBOX"]=g.join(","),1==u.length)p=u[0];else{var v=s["a"].modulo(h["a"].hash(t),u.length);p=u[v]}return m["a"].appendParams(p,o)}},g.prototype.getTilePixelRatio=function(t){return this.hidpi_&&void 0!==this.serverType_?t:1},g.prototype.getKeyForParams_=function(){var t=0,e=[];for(var r in this.params_)e[t++]=r+"-"+this.params_[r];return e.join("/")},g.prototype.fixedTileUrlFunction=function(t,e,r){var i=this.getTileGrid();if(i||(i=this.getTileGridForProjection(r)),!(i.getResolutions().length<=t[0])){1==e||this.hidpi_&&void 0!==this.serverType_||(e=1);var s=i.getResolution(t[0]),u=i.getTileCoordExtent(t,this.tmpExtent_),l=c["a"].toSize(i.getTileSize(t[0]),this.tmpSize),p=this.gutter_;0!==p&&(l=c["a"].buffer(l,p,this.tmpSize),u=n["a"].buffer(u,s*p,u)),1!=e&&(l=c["a"].scale(l,e,this.tmpSize));var d={SERVICE:"WMS",VERSION:a["a"].DEFAULT_WMS_VERSION,REQUEST:"GetMap",FORMAT:"image/png",TRANSPARENT:!0};return o["a"].assign(d,this.params_),this.getRequestUrl_(t,l,u,e,r,d)}},g.prototype.updateParams=function(t){o["a"].assign(this.params_,t),this.updateV13_(),this.setKey(this.getKeyForParams_())},g.prototype.updateV13_=function(){var t=this.params_["VERSION"]||a["a"].DEFAULT_WMS_VERSION;this.v13_=f["a"].compareVersions(t,"1.3")>=0},e["a"]=g},c721:function(t,e,r){"use strict";var a={appendParams:function(t,e){var r=[];Object.keys(e).forEach((function(t){null!==e[t]&&void 0!==e[t]&&r.push(t+"="+encodeURIComponent(e[t]))}));var a=r.join("&");return t=t.replace(/[?&]$/,""),t=-1===t.indexOf("?")?t+"?":t+"&",t+a}};e["a"]=a},ca65:function(t,e,r){"use strict";r.r(e);r("b0c0");var a=function(){var t=this,e=t._self._c;return e("div",[e("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[e("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[t._v("首页")]),e("el-breadcrumb-item",[t._v("GeoServer")]),e("el-breadcrumb-item",[t._v("WFS")]),e("el-breadcrumb-item",[t._v("WFS综合查询")])],1),e("el-card",[e("div",{staticClass:"div_auto"},[e("el-autocomplete",{staticStyle:{width:"250px"},attrs:{"fetch-suggestions":t.querySearchAsync,placeholder:"请输入搜索内容","value-key":"name"},on:{select:t.handleSelect,change:t.handleInput},model:{value:t.input,callback:function(e){t.input=e},expression:"input"}})],1),e("div",{staticClass:"div_button",staticStyle:{left:"580px"}},[e("el-select",{attrs:{placeholder:"请选择查询的图层"},model:{value:t.type,callback:function(e){t.type=e},expression:"type"}},t._l(t.selectLayer,(function(t){return e("el-option",{key:t.value,attrs:{label:t.name,value:t.name}})})),1)],1),e("div",{staticClass:"div_button",staticStyle:{right:"50px"}},[e("el-form",{ref:"form",attrs:{model:t.nearForm,"label-width":"80px"}},[e("el-form-item",{attrs:{label:"输入圆心"}},[e("el-input",{attrs:{placeholder:"请输入经纬度"},model:{value:t.nearForm.lnglat,callback:function(e){t.$set(t.nearForm,"lnglat",e)},expression:"nearForm.lnglat"}})],1),e("el-form-item",{attrs:{label:"输入半径"}},[e("el-input",{model:{value:t.nearForm.radius,callback:function(e){t.$set(t.nearForm,"radius",e)},expression:"nearForm.radius"}})],1),e("el-form-item",{attrs:{label:"地图显示"}},[e("el-switch",{model:{value:t.nearForm.show,callback:function(e){t.$set(t.nearForm,"show",e)},expression:"nearForm.show"}})],1),e("el-form-item",[e("el-button",{attrs:{type:"primary"},on:{click:t.searchNearBy}},[t._v("周边搜索")])],1)],1)],1),e("div",{attrs:{id:"map"}})])],1)},i=[],n=(r("d81d"),r("d3b7"),r("25f0"),r("a9e3"),r("92cf")),o=r("43df"),s=r("332e"),u=r("3388"),l=r("09d8"),c=r("4ca6"),p=r("bcb4"),d=r("170b"),h=r("21bc"),f={data:function(){return{url:"http://119.91.20.243:28081/",map:null,input:"",restaurants:[],type:"查询所有信息",selectLayer:[{name:"查询地名信息(poi)",value:"poi"},{name:"查询地标信息(landmarks)",value:"landmarks"},{name:"查询道路信息(roads)",value:"roads"},{name:"查询所有信息",value:""}],nearForm:{lnglat:"-73.98,40.76",radius:500,show:!0}}},created:function(){},mounted:function(){this.initMap()},methods:{initMap:function(){var t=this,e=new s["a"]({zoom:13,projection:"EPSG:4326",center:[-73.98,40.79]}),r=new c["a"]({source:new p["a"]({url:this.url+"geoserver/wms",params:{FORMAT:"image/png",tiled:!0,LAYERS:["tiger:poly_landmarks","tiger:tiger_roads","tiger:poi"],tilesOrigin:"-74.047185,40.679648"}})});this.map=new o["a"]({controls:h["a"].defaults({attribution:!1}).extend([]),target:"map",layers:[Object(n["d"])("vec_w"),Object(n["d"])("cva_w"),r],view:e}),this.map.on("singleclick",(function(e){t.nearForm.lnglat=e.coordinate.toString()}))},querySearchAsync:function(t,e){console.log(t);var r=this;Object(n["b"])(this.url,this.type,t,5).then((function(t){console.log("关键字搜索结果",t),r.restaurants=[],r.restaurants=Object(n["c"])(t),e(r.restaurants)}))},handleSelect:function(t){Object(n["a"])(this.map,t.id)},handleInput:function(t){this.querySearchAsync(t)},searchNearBy:function(){var t=this,e=new d["a"]({code:"EPSG:4326",units:"degrees",global:!0}),r=e.getMetersPerUnit(),a=new u["a"](this.nearForm.lnglat.split(",").map(Number),this.nearForm.radius/r),i=new l["a"].fromCircle(a);console.log(i),Object(n["e"])(this.map,this.url,this.type,10,i).then((function(e){console.log("空间查询结果",e),t.nearForm.show&&Object(n["a"])(t.map,"")}))}}},m=f,g=(r("9fce"),r("2877")),v=Object(g["a"])(m,a,i,!1,null,null,null);e["default"]=v.exports},d81d:function(t,e,r){"use strict";var a=r("23e7"),i=r("b727").map,n=r("1dde"),o=n("map");a({target:"Array",proto:!0,forced:!o},{map:function(t){return i(this,t,arguments.length>1?arguments[1]:void 0)}})}}]);
//# sourceMappingURL=chunk-64d05109.ad7c8ca8.js.map