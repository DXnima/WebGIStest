(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-506cf6e4"],{"05b1":function(t,e,i){"use strict";var r=i("9d79"),a=i("9cff"),n=i("9f5e"),s=i("0af5"),o=i("38f3"),u=i("256f"),l=i("40f5"),c={KVP:"KVP",REST:"REST"},h=c,p=i("a8d0"),g=i("c721"),d=function(t){this.version_=void 0!==t.version?t.version:"1.0.0",this.format_=void 0!==t.format?t.format:"image/jpeg",this.dimensions_=void 0!==t.dimensions?t.dimensions:{},this.layer_=t.layer,this.matrixSet_=t.matrixSet,this.style_=t.style;var e=t.urls;void 0===e&&void 0!==t.url&&(e=a["a"].expandUrl(t.url)),this.requestEncoding_=void 0!==t.requestEncoding?t.requestEncoding:h.KVP;var i=this.requestEncoding_,r=t.tileGrid,n={layer:this.layer_,style:this.style_,tilematrixset:this.matrixSet_};i==h.KVP&&o["a"].assign(n,{Service:"WMTS",Request:"GetTile",Version:this.version_,Format:this.format_});var s=this.dimensions_;this.createFromWMTSTemplate_=function(t){return t=i==h.KVP?g["a"].appendParams(t,n):t.replace(/\{(\w+?)\}/g,(function(t,e){return e.toLowerCase()in n?n[e.toLowerCase()]:t})),function(e,a,n){if(e){var u={TileMatrix:r.getMatrixId(e[0]),TileCol:e[1],TileRow:-e[2]-1};o["a"].assign(u,s);var l=t;return l=i==h.KVP?g["a"].appendParams(l,u):l.replace(/\{(\w+?)\}/g,(function(t,e){return u[e]})),l}}};var u=e&&e.length>0?a["a"].createFromTileUrlFunctions(e.map(this.createFromWMTSTemplate_)):a["a"].nullTileUrlFunction;l["a"].call(this,{attributions:t.attributions,cacheSize:t.cacheSize,crossOrigin:t.crossOrigin,logo:t.logo,projection:t.projection,reprojectionErrorThreshold:t.reprojectionErrorThreshold,tileClass:t.tileClass,tileGrid:r,tileLoadFunction:t.tileLoadFunction,tilePixelRatio:t.tilePixelRatio,tileUrlFunction:u,urls:e,wrapX:void 0!==t.wrapX&&t.wrapX,transition:t.transition}),this.setKey(this.getKeyForDimensions_())};r["a"].inherits(d,l["a"]),d.prototype.setUrls=function(t){this.urls=t;var e=t.join("\n");this.setTileUrlFunction(this.fixedTileUrlFunction?this.fixedTileUrlFunction.bind(this):a["a"].createFromTileUrlFunctions(t.map(this.createFromWMTSTemplate_.bind(this))),e)},d.prototype.getDimensions=function(){return this.dimensions_},d.prototype.getFormat=function(){return this.format_},d.prototype.getLayer=function(){return this.layer_},d.prototype.getMatrixSet=function(){return this.matrixSet_},d.prototype.getRequestEncoding=function(){return this.requestEncoding_},d.prototype.getStyle=function(){return this.style_},d.prototype.getVersion=function(){return this.version_},d.prototype.getKeyForDimensions_=function(){var t=0,e=[];for(var i in this.dimensions_)e[t++]=i+"-"+this.dimensions_[i];return e.join("/")},d.prototype.updateDimensions=function(t){o["a"].assign(this.dimensions_,t),this.setKey(this.getKeyForDimensions_())},d.optionsFromCapabilities=function(t,e){var i=t["Contents"]["Layer"],r=n["a"].find(i,(function(t,i,r){return t["Identifier"]==e["layer"]}));if(null===r)return null;var a,o,l,c=t["Contents"]["TileMatrixSet"];a=r["TileMatrixSetLink"].length>1?"projection"in e?n["a"].findIndex(r["TileMatrixSetLink"],(function(t,i,r){var a=n["a"].find(c,(function(e){return e["Identifier"]==t["TileMatrixSet"]})),s=a["SupportedCRS"],o=u["a"].get(s.replace(/urn:ogc:def:crs:(\w+):(.*:)?(\w+)$/,"$1:$3"))||u["a"].get(s),l=u["a"].get(e["projection"]);return o&&l?u["a"].equivalent(o,l):s==e["projection"]})):n["a"].findIndex(r["TileMatrixSetLink"],(function(t,i,r){return t["TileMatrixSet"]==e["matrixSet"]})):0,a<0&&(a=0),o=r["TileMatrixSetLink"][a]["TileMatrixSet"],l=r["TileMatrixSetLink"][a]["TileMatrixSetLimits"];var g=r["Format"][0];"format"in e&&(g=e["format"]),a=n["a"].findIndex(r["Style"],(function(t,i,r){return"style"in e?t["Title"]==e["style"]:t["isDefault"]})),a<0&&(a=0);var d=r["Style"][a]["Identifier"],m={};"Dimension"in r&&r["Dimension"].forEach((function(t,e,i){var r=t["Identifier"],a=t["Default"];void 0===a&&(a=t["Value"][0]),m[r]=a}));var _,f=t["Contents"]["TileMatrixSet"],v=n["a"].find(f,(function(t,e,i){return t["Identifier"]==o})),E=v["SupportedCRS"];if(E&&(_=u["a"].get(E.replace(/urn:ogc:def:crs:(\w+):(.*:)?(\w+)$/,"$1:$3"))||u["a"].get(E)),"projection"in e){var S=u["a"].get(e["projection"]);S&&(_&&!u["a"].equivalent(S,_)||(_=S))}var R,T,I=r["WGS84BoundingBox"];if(void 0!==I){var y=u["a"].get("EPSG:4326").getExtent();T=I[0]==y[0]&&I[2]==y[2],R=u["a"].transformExtent(I,"EPSG:4326",_);var w=_.getExtent();w&&(s["a"].containsExtent(w,R)||(R=void 0))}var b=p["a"].createFromCapabilitiesMatrixSet(v,R,l),O=[],x=e["requestEncoding"];if(x=void 0!==x?x:"","OperationsMetadata"in t&&"GetTile"in t["OperationsMetadata"])for(var M=t["OperationsMetadata"]["GetTile"]["DCP"]["HTTP"]["Get"],A=0,L=M.length;A<L;++A)if(M[A]["Constraint"]){var F=n["a"].find(M[A]["Constraint"],(function(t){return"GetEncoding"==t["name"]})),G=F["AllowedValues"]["Value"];if(""===x&&(x=G[0]),x!==h.KVP)break;n["a"].includes(G,h.KVP)&&O.push(M[A]["href"])}else M[A]["href"]&&(x=h.KVP,O.push(M[A]["href"]));return 0===O.length&&(x=h.REST,r["ResourceURL"].forEach((function(t){"tile"===t["resourceType"]&&(g=t["format"],O.push(t["template"]))}))),{urls:O,layer:e["layer"],matrixSet:o,format:g,projection:_,requestEncoding:x,tileGrid:b,style:d,dimensions:m,wrapX:T,crossOrigin:e["crossOrigin"]}};e["a"]=d},"0f62":function(t,e,i){"use strict";var r=i("9d79"),a=i("0258"),n=i("0511"),s=function(t){var e=t||{};n["a"].call(this,e),this.type=a["a"].IMAGE};r["a"].inherits(s,n["a"]),s.prototype.getSource,e["a"]=s},2350:function(t,e,i){"use strict";var r={CARMENTA_SERVER:"carmentaserver",GEOSERVER:"geoserver",MAPSERVER:"mapserver",QGIS:"qgis"};e["a"]=r},"24df":function(t,e,i){"use strict";var r=i("9d79"),a=i("4e0a"),n=i("5bc0"),s=i("1e8d"),o=i("8fc0"),u=i("0af5"),l=function(t,e,i,r,s,o){a["a"].call(this,t,e,i,n["a"].IDLE),this.src_=r,this.image_=new Image,null!==s&&(this.image_.crossOrigin=s),this.imageListenerKeys_=null,this.state=n["a"].IDLE,this.imageLoadFunction_=o};r["a"].inherits(l,a["a"]),l.prototype.getImage=function(){return this.image_},l.prototype.handleImageError_=function(){this.state=n["a"].ERROR,this.unlistenImage_(),this.changed()},l.prototype.handleImageLoad_=function(){void 0===this.resolution&&(this.resolution=u["a"].getHeight(this.extent)/this.image_.height),this.state=n["a"].LOADED,this.unlistenImage_(),this.changed()},l.prototype.load=function(){this.state!=n["a"].IDLE&&this.state!=n["a"].ERROR||(this.state=n["a"].LOADING,this.changed(),this.imageListenerKeys_=[s["a"].listenOnce(this.image_,o["a"].ERROR,this.handleImageError_,this),s["a"].listenOnce(this.image_,o["a"].LOAD,this.handleImageLoad_,this)],this.imageLoadFunction_(this,this.src_))},l.prototype.setImage=function(t){this.image_=t},l.prototype.unlistenImage_=function(){this.imageListenerKeys_.forEach(s["a"].unlistenByKey),this.imageListenerKeys_=null};var c=l,h=i("92fa"),p=i("38f3"),g=i("256f"),d=i("3c81"),m=i("9f5e"),_=i("d60d"),f=i("c9f1"),v=function(t,e,i,s,o,l){this.targetProj_=e,this.maxSourceExtent_=t.getExtent();var c=e.getExtent(),h=c?u["a"].getIntersection(i,c):i,p=u["a"].getCenter(h),g=d["a"].calculateSourceResolution(t,e,p,s),m=r["a"].DEFAULT_RASTER_REPROJECTION_ERROR_THRESHOLD;this.triangulation_=new f["a"](t,e,h,this.maxSourceExtent_,g*m),this.targetResolution_=s,this.targetExtent_=i;var _=this.triangulation_.calculateSourceExtent();this.sourceImage_=l(_,g,o),this.sourcePixelRatio_=this.sourceImage_?this.sourceImage_.getPixelRatio():1,this.canvas_=null,this.sourceListenerKey_=null;var v=n["a"].LOADED;this.sourceImage_&&(v=n["a"].IDLE),a["a"].call(this,i,s,this.sourcePixelRatio_,v)};r["a"].inherits(v,a["a"]),v.prototype.disposeInternal=function(){this.state==n["a"].LOADING&&this.unlistenSource_(),a["a"].prototype.disposeInternal.call(this)},v.prototype.getImage=function(){return this.canvas_},v.prototype.getProjection=function(){return this.targetProj_},v.prototype.reproject_=function(){var t=this.sourceImage_.getState();if(t==n["a"].LOADED){var e=u["a"].getWidth(this.targetExtent_)/this.targetResolution_,i=u["a"].getHeight(this.targetExtent_)/this.targetResolution_;this.canvas_=d["a"].render(e,i,this.sourcePixelRatio_,this.sourceImage_.getResolution(),this.maxSourceExtent_,this.targetResolution_,this.targetExtent_,this.triangulation_,[{extent:this.sourceImage_.getExtent(),image:this.sourceImage_.getImage()}],0)}this.state=t,this.changed()},v.prototype.load=function(){if(this.state==n["a"].IDLE){this.state=n["a"].LOADING,this.changed();var t=this.sourceImage_.getState();t==n["a"].LOADED||t==n["a"].ERROR?this.reproject_():(this.sourceListenerKey_=s["a"].listen(this.sourceImage_,o["a"].CHANGE,(function(t){var e=this.sourceImage_.getState();e!=n["a"].LOADED&&e!=n["a"].ERROR||(this.unlistenSource_(),this.reproject_())}),this),this.sourceImage_.load())}},v.prototype.unlistenSource_=function(){s["a"].unlistenByKey(this.sourceListenerKey_),this.sourceListenerKey_=null};var E=v,S=i("ef85"),R=function(t){S["a"].call(this,{attributions:t.attributions,extent:t.extent,logo:t.logo,projection:t.projection,state:t.state}),this.resolutions_=void 0!==t.resolutions?t.resolutions:null,this.reprojectedImage_=null,this.reprojectedRevision_=0};r["a"].inherits(R,S["a"]),R.prototype.getResolutions=function(){return this.resolutions_},R.prototype.findNearestResolution=function(t){if(this.resolutions_){var e=m["a"].linearFindNearest(this.resolutions_,t,0);t=this.resolutions_[e]}return t},R.prototype.getImage=function(t,e,i,a){var n=this.getProjection();if(r["a"].ENABLE_RASTER_REPROJECTION&&n&&a&&!g["a"].equivalent(n,a)){if(this.reprojectedImage_){if(this.reprojectedRevision_==this.getRevision()&&g["a"].equivalent(this.reprojectedImage_.getProjection(),a)&&this.reprojectedImage_.getResolution()==e&&u["a"].equals(this.reprojectedImage_.getExtent(),t))return this.reprojectedImage_;this.reprojectedImage_.dispose(),this.reprojectedImage_=null}return this.reprojectedImage_=new E(n,a,t,e,i,function(t,e,i){return this.getImageInternal(t,e,i,n)}.bind(this)),this.reprojectedRevision_=this.getRevision(),this.reprojectedImage_}return n&&(a=n),this.getImageInternal(t,e,i,a)},R.prototype.getImageInternal=function(t,e,i,r){},R.prototype.handleImageChange=function(t){var e=t.target;switch(e.getState()){case n["a"].LOADING:this.dispatchEvent(new R.Event(R.EventType_.IMAGELOADSTART,e));break;case n["a"].LOADED:this.dispatchEvent(new R.Event(R.EventType_.IMAGELOADEND,e));break;case n["a"].ERROR:this.dispatchEvent(new R.Event(R.EventType_.IMAGELOADERROR,e));break;default:}},R.defaultImageLoadFunction=function(t,e){t.getImage().src=e},R.Event=function(t,e){_["a"].call(this,t),this.image=e},r["a"].inherits(R.Event,_["a"]),R.EventType_={IMAGELOADSTART:"imageloadstart",IMAGELOADEND:"imageloadend",IMAGELOADERROR:"imageloaderror"};var T=R,I=i("2350"),y=i("b0c1"),w=i("c721"),b=function(t){var e=t||{};T.call(this,{attributions:e.attributions,logo:e.logo,projection:e.projection,resolutions:e.resolutions}),this.crossOrigin_=void 0!==e.crossOrigin?e.crossOrigin:null,this.url_=e.url,this.imageLoadFunction_=void 0!==e.imageLoadFunction?e.imageLoadFunction:T.defaultImageLoadFunction,this.params_=e.params||{},this.v13_=!0,this.updateV13_(),this.serverType_=e.serverType,this.hidpi_=void 0===e.hidpi||e.hidpi,this.image_=null,this.imageSize_=[0,0],this.renderedRevision_=0,this.ratio_=void 0!==e.ratio?e.ratio:1.5};r["a"].inherits(b,T),b.GETFEATUREINFO_IMAGE_SIZE_=[101,101],b.prototype.getGetFeatureInfoUrl=function(t,e,i,a){if(void 0!==this.url_){var n=g["a"].get(i),s=this.getProjection();s&&s!==n&&(e=d["a"].calculateSourceResolution(s,n,t,e),t=g["a"].transform(t,n,s));var o=u["a"].getForViewAndSize(t,e,0,b.GETFEATUREINFO_IMAGE_SIZE_),l={SERVICE:"WMS",VERSION:r["a"].DEFAULT_WMS_VERSION,REQUEST:"GetFeatureInfo",FORMAT:"image/png",TRANSPARENT:!0,QUERY_LAYERS:this.params_["LAYERS"]};p["a"].assign(l,this.params_,a);var c=Math.floor((t[0]-o[0])/e),h=Math.floor((o[3]-t[1])/e);return l[this.v13_?"I":"X"]=c,l[this.v13_?"J":"Y"]=h,this.getRequestUrl_(o,b.GETFEATUREINFO_IMAGE_SIZE_,1,s||n,l)}},b.prototype.getParams=function(){return this.params_},b.prototype.getImageInternal=function(t,e,i,a){if(void 0===this.url_)return null;e=this.findNearestResolution(e),1==i||this.hidpi_&&void 0!==this.serverType_||(i=1);var n=e/i,l=u["a"].getCenter(t),h=Math.ceil(u["a"].getWidth(t)/n),g=Math.ceil(u["a"].getHeight(t)/n),d=u["a"].getForViewAndSize(l,n,0,[h,g]),m=Math.ceil(this.ratio_*u["a"].getWidth(t)/n),_=Math.ceil(this.ratio_*u["a"].getHeight(t)/n),f=u["a"].getForViewAndSize(l,n,0,[m,_]),v=this.image_;if(v&&this.renderedRevision_==this.getRevision()&&v.getResolution()==e&&v.getPixelRatio()==i&&u["a"].containsExtent(v.getExtent(),d))return v;var E={SERVICE:"WMS",VERSION:r["a"].DEFAULT_WMS_VERSION,REQUEST:"GetMap",FORMAT:"image/png",TRANSPARENT:!0};p["a"].assign(E,this.params_),this.imageSize_[0]=Math.round(u["a"].getWidth(f)/n),this.imageSize_[1]=Math.round(u["a"].getHeight(f)/n);var S=this.getRequestUrl_(f,this.imageSize_,i,a,E);return this.image_=new c(f,e,i,S,this.crossOrigin_,this.imageLoadFunction_),this.renderedRevision_=this.getRevision(),s["a"].listen(this.image_,o["a"].CHANGE,this.handleImageChange,this),this.image_},b.prototype.getImageLoadFunction=function(){return this.imageLoadFunction_},b.prototype.getRequestUrl_=function(t,e,i,r,a){if(h["a"].assert(void 0!==this.url_,9),a[this.v13_?"CRS":"SRS"]=r.getCode(),"STYLES"in this.params_||(a["STYLES"]=""),1!=i)switch(this.serverType_){case I["a"].GEOSERVER:var n=90*i+.5|0;"FORMAT_OPTIONS"in a?a["FORMAT_OPTIONS"]+=";dpi:"+n:a["FORMAT_OPTIONS"]="dpi:"+n;break;case I["a"].MAPSERVER:a["MAP_RESOLUTION"]=90*i;break;case I["a"].CARMENTA_SERVER:case I["a"].QGIS:a["DPI"]=90*i;break;default:h["a"].assert(!1,8);break}a["WIDTH"]=e[0],a["HEIGHT"]=e[1];var s,o=r.getAxisOrientation();return s=this.v13_&&"ne"==o.substr(0,2)?[t[1],t[0],t[3],t[2]]:t,a["BBOX"]=s.join(","),w["a"].appendParams(this.url_,a)},b.prototype.getUrl=function(){return this.url_},b.prototype.setImageLoadFunction=function(t){this.image_=null,this.imageLoadFunction_=t,this.changed()},b.prototype.setUrl=function(t){t!=this.url_&&(this.url_=t,this.image_=null,this.changed())},b.prototype.updateParams=function(t){p["a"].assign(this.params_,t),this.updateV13_(),this.image_=null,this.changed()},b.prototype.updateV13_=function(){var t=this.params_["VERSION"]||r["a"].DEFAULT_WMS_VERSION;this.v13_=y["a"].compareVersions(t,"1.3")>=0};e["a"]=b},"5f60":function(t,e,i){"use strict";i("c2dc")},8956:function(t,e,i){"use strict";var r=i("9d79"),a=i("403d"),n=function(t){var e,i=t||{};e=void 0!==i.attributions?i.attributions:[n.ATTRIBUTION];var r=void 0!==i.crossOrigin?i.crossOrigin:"anonymous",s=void 0!==i.url?i.url:"https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png";a["a"].call(this,{attributions:e,cacheSize:i.cacheSize,crossOrigin:r,opaque:void 0===i.opaque||i.opaque,maxZoom:void 0!==i.maxZoom?i.maxZoom:19,reprojectionErrorThreshold:i.reprojectionErrorThreshold,tileLoadFunction:i.tileLoadFunction,url:s,wrapX:i.wrapX})};r["a"].inherits(n,a["a"]),n.ATTRIBUTION='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors.',e["a"]=n},a15b:function(t,e,i){"use strict";var r=i("23e7"),a=i("e330"),n=i("44ad"),s=i("fc6a"),o=i("a640"),u=a([].join),l=n!==Object,c=l||!o("join",",");r({target:"Array",proto:!0,forced:c},{join:function(t){return u(s(this),void 0===t?",":t)}})},a8d0:function(t,e,i){"use strict";var r=i("9d79"),a=i("9f5e"),n=i("256f"),s=i("97ab"),o=function(t){this.matrixIds_=t.matrixIds,s["a"].call(this,{extent:t.extent,origin:t.origin,origins:t.origins,resolutions:t.resolutions,tileSize:t.tileSize,tileSizes:t.tileSizes,sizes:t.sizes})};r["a"].inherits(o,s["a"]),o.prototype.getMatrixId=function(t){return this.matrixIds_[t]},o.prototype.getMatrixIds=function(){return this.matrixIds_},o.createFromCapabilitiesMatrixSet=function(t,e,i){var r=[],s=[],u=[],l=[],c=[],h=void 0!==i?i:[],p="SupportedCRS",g="TileMatrix",d="Identifier",m="ScaleDenominator",_="TopLeftCorner",f="TileWidth",v="TileHeight",E=t[p],S=n["a"].get(E.replace(/urn:ogc:def:crs:(\w+):(.*:)?(\w+)$/,"$1:$3"))||n["a"].get(E),R=S.getMetersPerUnit(),T="ne"==S.getAxisOrientation().substr(0,2);return t[g].sort((function(t,e){return e[m]-t[m]})),t[g].forEach((function(t,e,i){var n;if(n=!(h.length>0)||a["a"].find(h,(function(e,i,r){return t[d]==e[g]})),n){s.push(t[d]);var o=28e-5*t[m]/R,p=t[f],E=t[v];T?u.push([t[_][1],t[_][0]]):u.push(t[_]),r.push(o),l.push(p==E?p:[p,E]),c.push([t["MatrixWidth"],-t["MatrixHeight"]])}})),new o({extent:e,origins:u,resolutions:r,matrixIds:s,tileSizes:l,sizes:c})},e["a"]=o},bcb4:function(t,e,i){"use strict";var r=i("9d79"),a=i("92fa"),n=i("0af5"),s=i("38f3"),o=i("7fc9"),u=i("256f"),l=i("3c81"),c=i("345d"),h=i("40f5"),p=i("2350"),g=i("2c30"),d=i("b0c1"),m=i("c721"),_=function(t){var e=t||{},i=e.params||{},r=!("TRANSPARENT"in i)||i["TRANSPARENT"];h["a"].call(this,{attributions:e.attributions,cacheSize:e.cacheSize,crossOrigin:e.crossOrigin,logo:e.logo,opaque:!r,projection:e.projection,reprojectionErrorThreshold:e.reprojectionErrorThreshold,tileClass:e.tileClass,tileGrid:e.tileGrid,tileLoadFunction:e.tileLoadFunction,url:e.url,urls:e.urls,wrapX:void 0===e.wrapX||e.wrapX,transition:e.transition}),this.gutter_=void 0!==e.gutter?e.gutter:0,this.params_=i,this.v13_=!0,this.serverType_=e.serverType,this.hidpi_=void 0===e.hidpi||e.hidpi,this.tmpExtent_=n["a"].createEmpty(),this.updateV13_(),this.setKey(this.getKeyForParams_())};r["a"].inherits(_,h["a"]),_.prototype.getGetFeatureInfoUrl=function(t,e,i,a){var o=u["a"].get(i),h=this.getProjection(),p=this.getTileGrid();p||(p=this.getTileGridForProjection(o));var g=p.getTileCoordForCoordAndResolution(t,e);if(!(p.getResolutions().length<=g[0])){var d=p.getResolution(g[0]),m=p.getTileCoordExtent(g,this.tmpExtent_),_=c["a"].toSize(p.getTileSize(g[0]),this.tmpSize),f=this.gutter_;0!==f&&(_=c["a"].buffer(_,f,this.tmpSize),m=n["a"].buffer(m,d*f,m)),h&&h!==o&&(d=l["a"].calculateSourceResolution(h,o,t,d),m=u["a"].transformExtent(m,o,h),t=u["a"].transform(t,o,h));var v={SERVICE:"WMS",VERSION:r["a"].DEFAULT_WMS_VERSION,REQUEST:"GetFeatureInfo",FORMAT:"image/png",TRANSPARENT:!0,QUERY_LAYERS:this.params_["LAYERS"]};s["a"].assign(v,this.params_,a);var E=Math.floor((t[0]-m[0])/d),S=Math.floor((m[3]-t[1])/d);return v[this.v13_?"I":"X"]=E,v[this.v13_?"J":"Y"]=S,this.getRequestUrl_(g,_,m,1,h||o,v)}},_.prototype.getGutterInternal=function(){return this.gutter_},_.prototype.getParams=function(){return this.params_},_.prototype.getRequestUrl_=function(t,e,i,r,n,s){var u=this.urls;if(u){if(s["WIDTH"]=e[0],s["HEIGHT"]=e[1],s[this.v13_?"CRS":"SRS"]=n.getCode(),"STYLES"in this.params_||(s["STYLES"]=""),1!=r)switch(this.serverType_){case p["a"].GEOSERVER:var l=90*r+.5|0;"FORMAT_OPTIONS"in s?s["FORMAT_OPTIONS"]+=";dpi:"+l:s["FORMAT_OPTIONS"]="dpi:"+l;break;case p["a"].MAPSERVER:s["MAP_RESOLUTION"]=90*r;break;case p["a"].CARMENTA_SERVER:case p["a"].QGIS:s["DPI"]=90*r;break;default:a["a"].assert(!1,52);break}var c,h,d=n.getAxisOrientation(),_=i;if(this.v13_&&"ne"==d.substr(0,2))c=i[0],_[0]=i[1],_[1]=c,c=i[2],_[2]=i[3],_[3]=c;if(s["BBOX"]=_.join(","),1==u.length)h=u[0];else{var f=o["a"].modulo(g["a"].hash(t),u.length);h=u[f]}return m["a"].appendParams(h,s)}},_.prototype.getTilePixelRatio=function(t){return this.hidpi_&&void 0!==this.serverType_?t:1},_.prototype.getKeyForParams_=function(){var t=0,e=[];for(var i in this.params_)e[t++]=i+"-"+this.params_[i];return e.join("/")},_.prototype.fixedTileUrlFunction=function(t,e,i){var a=this.getTileGrid();if(a||(a=this.getTileGridForProjection(i)),!(a.getResolutions().length<=t[0])){1==e||this.hidpi_&&void 0!==this.serverType_||(e=1);var o=a.getResolution(t[0]),u=a.getTileCoordExtent(t,this.tmpExtent_),l=c["a"].toSize(a.getTileSize(t[0]),this.tmpSize),h=this.gutter_;0!==h&&(l=c["a"].buffer(l,h,this.tmpSize),u=n["a"].buffer(u,o*h,u)),1!=e&&(l=c["a"].scale(l,e,this.tmpSize));var p={SERVICE:"WMS",VERSION:r["a"].DEFAULT_WMS_VERSION,REQUEST:"GetMap",FORMAT:"image/png",TRANSPARENT:!0};return s["a"].assign(p,this.params_),this.getRequestUrl_(t,l,u,e,i,p)}},_.prototype.updateParams=function(t){s["a"].assign(this.params_,t),this.updateV13_(),this.setKey(this.getKeyForParams_())},_.prototype.updateV13_=function(){var t=this.params_["VERSION"]||r["a"].DEFAULT_WMS_VERSION;this.v13_=d["a"].compareVersions(t,"1.3")>=0},e["a"]=_},c2dc:function(t,e,i){},c721:function(t,e,i){"use strict";var r={appendParams:function(t,e){var i=[];Object.keys(e).forEach((function(t){null!==e[t]&&void 0!==e[t]&&i.push(t+"="+encodeURIComponent(e[t]))}));var r=i.join("&");return t=t.replace(/[?&]$/,""),t=-1===t.indexOf("?")?t+"?":t+"&",t+r}};e["a"]=r},d02c:function(t,e,i){"use strict";i.r(e);var r=function(){var t=this,e=t._self._c;return e("div",[e("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[e("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[t._v("首页")]),e("el-breadcrumb-item",[t._v("OpenLayers")]),e("el-breadcrumb-item",[t._v("图层切换")])],1),e("el-card",[e("el-container",[e("el-aside",{attrs:{width:"200px"}},[e("h2",{staticStyle:{"margin-left":"10px"}},[t._v("切换图层")]),e("el-radio-group",{staticStyle:{"padding-left":"10px"},attrs:{size:"medium"},on:{change:t.radioChange},model:{value:t.radio,callback:function(e){t.radio=e},expression:"radio"}},[e("el-radio",{attrs:{label:"osm"}},[t._v("OSM地图")]),e("br"),e("el-radio",{attrs:{label:"baidu"}},[t._v("百度地图")]),e("br"),e("el-radio",{attrs:{label:"tian"}},[t._v("天地图")]),e("br"),e("el-radio",{attrs:{label:"wmsImage"}},[t._v("WmsUtils Image服务")]),e("br"),e("el-radio",{attrs:{label:"wmsTile"}},[t._v("WmsUtils Tile服务")]),e("br"),e("el-radio",{attrs:{label:"wmsGrid"}},[t._v("WmsUtils Grid服务")]),e("br"),e("el-radio",{attrs:{label:"wmtsMap"}},[t._v("WMTS服务")]),e("br"),e("el-radio",{attrs:{label:"wfsMap"}},[t._v("WFS服务")]),e("br")],1)],1),e("el-main",[e("div",{staticStyle:{width:"100%",height:"100%"},attrs:{id:"map"}})])],1)],1)],1)},a=[],n=(i("d81d"),i("d3b7"),i("159b"),i("a15b"),i("43df")),s=i("332e"),o=i("4ca6"),u=i("0f62"),l=i("e671"),c=i("a5cb"),h=i("97ab"),p=i("a8d0"),g=i("8956"),d=i("403d"),m=i("40f5"),_=i("24df"),f=i("bcb4"),v=i("05b1"),E=i("02df"),S=i("169c"),R=i("3e31"),T=i("3493"),I=i("256f"),y=i("0af5"),w=i("715e"),b={data:function(){return{radio:"osm",map:null,layerGroup:null}},created:function(){},mounted:function(){this.initMap()},methods:{initMap:function(){var t=new n["a"]({view:new s["a"]({projection:"EPSG:3857",center:[12955655.890681803,4849776.570637863],zoom:5}),target:"map"}),e=new c["a"]({layers:[this.osmMap(),this.baiduMap(),this.tianMap("vec_w"),this.tianMap("cva_w"),this.wmsImage(),this.wmsTile(),this.wmsGrid(),this.wmtsMap(),this.wfsRaster(),this.wfsVector()]});t.addLayer(e),t.on("pointermove",(function(t){I["a"].transform(t.coordinate,"EPSG:3857","EPSG:4326")})),this.layerGroup=e,this.map=t},radioChange:function(t){var e=this;this.layerGroup.getLayers().forEach((function(i,r,a){var n=i.get("title");if(i.setVisible(n===t),n===t){var o;switch(n){case"baidu":case"tian":o=new s["a"]({projection:"EPSG:3857",center:[12955655.890681803,4849776.570637863],zoom:8});break;default:o=new s["a"]({center:[12959773,4853101],zoom:5});break}e.map.setView(o)}}))},osmMap:function(){return new o["a"]({source:new g["a"],visible:!0,title:"osm"})},baiduMap:function(){for(var t,e,i=[],r=0;r<19;r++)i[r]=Math.pow(2,18-r);var a=new h["a"]({origin:[0,0],resolutions:i}),n=new m["a"]({projection:"EPSG:3857",tileGrid:a,tileUrlFunction:function(i){if(!i)return"";var r=i[0],a=i[1],n=i[2];return t=a<0?a:"M"+-a,e=-n,"http://online3.map.bdimg.com/onlinelabel/?qt=tile&x="+t+"&y="+e+"&z="+r+"&styles=pl&udt=20151021&scaler=1&p=1"}});return new o["a"]({source:n,visible:!1,title:"baidu"})},tianMap:function(t){return new o["a"]({source:new d["a"]({url:"http://t0.tianditu.gov.cn/DataServer?T="+t+"&X={x}&Y={y}&L={z}&tk=16c5722fed64bcdbb390cc21a5548cf9"}),visible:!1,title:"tian"})},wmsImage:function(){return new u["a"]({extent:[-13884991,2870341,-7455066,6338219],source:new _["a"]({url:"https://maps6.geosolutionsgroup.com/geoserver/wms",params:{LAYERS:"topp:states"},ratio:1,serverType:"geoserver"}),visible:!1,title:"wmsImage"})},wmsTile:function(){var t=new f["a"]({url:"https://maps6.geosolutionsgroup.com/geoserver/wms",params:{LAYERS:"ne:ne_10m_admin_0_countries",TILED:!0},serverType:"geoserver"});return new o["a"]({source:t,visible:!1,title:"wmsTile"})},wmsGrid:function(){for(var t=I["a"].get("EPSG:3857").getExtent(),e=y["a"].getWidth(t)/256,i=new Array(22),r=0,a=i.length;r<a;++r)i[r]=e/Math.pow(2,r);var n=new h["a"]({extent:[-13884991,2870341,-7455066,6338219],resolutions:i,tileSize:[512,256]}),s=new f["a"]({url:"https://maps6.geosolutionsgroup.com/geoserver/wms",params:{LAYERS:"topp:states",TILED:!0},serverType:"geoserver",tileGrid:n});return new o["a"]({source:s,visible:!1,title:"wmsGrid"})},wmtsMap:function(){for(var t=I["a"].get("EPSG:3857"),e=t.getExtent(),i=y["a"].getWidth(e)/256,r=new Array(14),a=new Array(14),n=0;n<14;++n)r[n]=i/Math.pow(2,n),a[n]=n;var s=new v["a"]({attributions:'Tiles © <a href="https://server.arcgisonline.com/ArcGIS/rest/services/Demographics/USA_Population_Density/MapServer/">ArcGIS</a>',url:"https://server.arcgisonline.com/ArcGIS/rest/services/Demographics/USA_Population_Density/MapServer/WMTS/",layer:"0",matrixSet:"EPSG:3857",format:"image/png",projection:t,tileGrid:new p["a"]({origin:new y["a"].getTopLeft(e),resolutions:r,matrixIds:a}),style:"default",wrapX:!0});return new o["a"]({opacity:.7,source:s,visible:!1,title:"wmtsMap"})},wfsVector:function(){var t,e=new S["a"];return t=new E["a"]({format:e,url:function(t){return"https://maps6.geosolutionsgroup.com/geoserver/wfs?service=WFS&version=1.1.0&request=GetFeature&typename=osm:water_areas&outputFormat=application/json&srsname=EPSG:3857&bbox="+t.join(",")+",EPSG:3857"},strategy:w["a"].bbox()}),new l["a"]({source:t,style:new R["a"]({stroke:new T["a"]({color:"rgba(0, 0, 255, 1.0)",width:2})}),visible:!1,title:"wfsMap"})},wfsRaster:function(){var t="get_your_own_D6rA4zTHduk6KOKTXzGB",e='<a href="https://www.maptiler.com/copyright/" target="_blank">&copy; MapTiler</a> <a href="https://www.openstreetmap.org/copyright" target="_blank">&copy; OpenStreetMap contributors</a>';return new o["a"]({source:new d["a"]({attributions:e,url:"https://api.maptiler.com/tiles/satellite/{z}/{x}/{y}.jpg?key="+t,maxZoom:20}),visible:!1,title:"wfsMap"})}}},O=b,x=(i("5f60"),i("2877")),M=Object(x["a"])(O,r,a,!1,null,"5a4ae465",null);e["default"]=M.exports}}]);
//# sourceMappingURL=chunk-506cf6e4.fff1cff7.js.map