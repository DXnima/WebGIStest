(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6a1855e2"],{5281:function(t,e,i){"use strict";var n=i("9d79"),o=i("d920"),a=i("b49e"),r={BOTTOM_LEFT:"bottom-left",BOTTOM_CENTER:"bottom-center",BOTTOM_RIGHT:"bottom-right",CENTER_LEFT:"center-left",CENTER_CENTER:"center-center",CENTER_RIGHT:"center-right",TOP_LEFT:"top-left",TOP_CENTER:"top-center",TOP_RIGHT:"top-right"},s=r,p=i("cd7e"),d=i("0999"),h=i("1e8d"),l=i("0af5"),u=function(t){a["a"].call(this),this.options=t,this.id=t.id,this.insertFirst=void 0===t.insertFirst||t.insertFirst,this.stopEvent=void 0===t.stopEvent||t.stopEvent,this.element=document.createElement("DIV"),this.element.className=void 0!==t.className?t.className:"ol-overlay-container "+p["a"].CLASS_SELECTABLE,this.element.style.position="absolute",this.autoPan=void 0!==t.autoPan&&t.autoPan,this.autoPanAnimation=t.autoPanAnimation||{},this.autoPanMargin=void 0!==t.autoPanMargin?t.autoPanMargin:20,this.rendered={bottom_:"",left_:"",right_:"",top_:"",visible:!0},this.mapPostrenderListenerKey=null,h["a"].listen(this,a["a"].getChangeEventType(u.Property.ELEMENT),this.handleElementChanged,this),h["a"].listen(this,a["a"].getChangeEventType(u.Property.MAP),this.handleMapChanged,this),h["a"].listen(this,a["a"].getChangeEventType(u.Property.OFFSET),this.handleOffsetChanged,this),h["a"].listen(this,a["a"].getChangeEventType(u.Property.POSITION),this.handlePositionChanged,this),h["a"].listen(this,a["a"].getChangeEventType(u.Property.POSITIONING),this.handlePositioningChanged,this),void 0!==t.element&&this.setElement(t.element),this.setOffset(void 0!==t.offset?t.offset:[0,0]),this.setPositioning(void 0!==t.positioning?t.positioning:s.TOP_LEFT),void 0!==t.position&&this.setPosition(t.position)};n["a"].inherits(u,a["a"]),u.prototype.getElement=function(){return this.get(u.Property.ELEMENT)},u.prototype.getId=function(){return this.id},u.prototype.getMap=function(){return this.get(u.Property.MAP)},u.prototype.getOffset=function(){return this.get(u.Property.OFFSET)},u.prototype.getPosition=function(){return this.get(u.Property.POSITION)},u.prototype.getPositioning=function(){return this.get(u.Property.POSITIONING)},u.prototype.handleElementChanged=function(){d["a"].removeChildren(this.element);var t=this.getElement();t&&this.element.appendChild(t)},u.prototype.handleMapChanged=function(){this.mapPostrenderListenerKey&&(d["a"].removeNode(this.element),h["a"].unlistenByKey(this.mapPostrenderListenerKey),this.mapPostrenderListenerKey=null);var t=this.getMap();if(t){this.mapPostrenderListenerKey=h["a"].listen(t,o["a"].POSTRENDER,this.render,this),this.updatePixelPosition();var e=this.stopEvent?t.getOverlayContainerStopEvent():t.getOverlayContainer();this.insertFirst?e.insertBefore(this.element,e.childNodes[0]||null):e.appendChild(this.element)}},u.prototype.render=function(){this.updatePixelPosition()},u.prototype.handleOffsetChanged=function(){this.updatePixelPosition()},u.prototype.handlePositionChanged=function(){this.updatePixelPosition(),this.get(u.Property.POSITION)&&this.autoPan&&this.panIntoView()},u.prototype.handlePositioningChanged=function(){this.updatePixelPosition()},u.prototype.setElement=function(t){this.set(u.Property.ELEMENT,t)},u.prototype.setMap=function(t){this.set(u.Property.MAP,t)},u.prototype.setOffset=function(t){this.set(u.Property.OFFSET,t)},u.prototype.setPosition=function(t){this.set(u.Property.POSITION,t)},u.prototype.panIntoView=function(){var t=this.getMap();if(t&&t.getTargetElement()){var e=this.getRect(t.getTargetElement(),t.getSize()),i=this.getElement(),n=this.getRect(i,[d["a"].outerWidth(i),d["a"].outerHeight(i)]),o=this.autoPanMargin;if(!l["a"].containsExtent(e,n)){var a=n[0]-e[0],r=e[2]-n[2],s=n[1]-e[1],p=e[3]-n[3],h=[0,0];if(a<0?h[0]=a-o:r<0&&(h[0]=Math.abs(r)+o),s<0?h[1]=s-o:p<0&&(h[1]=Math.abs(p)+o),0!==h[0]||0!==h[1]){var u=t.getView().getCenter(),c=t.getPixelFromCoordinate(u),g=[c[0]+h[0],c[1]+h[1]];t.getView().animate({center:t.getCoordinateFromPixel(g),duration:this.autoPanAnimation.duration,easing:this.autoPanAnimation.easing})}}}},u.prototype.getRect=function(t,e){var i=t.getBoundingClientRect(),n=i.left+window.pageXOffset,o=i.top+window.pageYOffset;return[n,o,n+e[0],o+e[1]]},u.prototype.setPositioning=function(t){this.set(u.Property.POSITIONING,t)},u.prototype.setVisible=function(t){this.rendered.visible!==t&&(this.element.style.display=t?"":"none",this.rendered.visible=t)},u.prototype.updatePixelPosition=function(){var t=this.getMap(),e=this.getPosition();if(t&&t.isRendered()&&e){var i=t.getPixelFromCoordinate(e),n=t.getSize();this.updateRenderedPosition(i,n)}else this.setVisible(!1)},u.prototype.updateRenderedPosition=function(t,e){var i=this.element.style,n=this.getOffset(),o=this.getPositioning();this.setVisible(!0);var a=n[0],r=n[1];if(o==s.BOTTOM_RIGHT||o==s.CENTER_RIGHT||o==s.TOP_RIGHT){""!==this.rendered.left_&&(this.rendered.left_=i.left="");var p=Math.round(e[0]-t[0]-a)+"px";this.rendered.right_!=p&&(this.rendered.right_=i.right=p)}else{""!==this.rendered.right_&&(this.rendered.right_=i.right=""),o!=s.BOTTOM_CENTER&&o!=s.CENTER_CENTER&&o!=s.TOP_CENTER||(a-=this.element.offsetWidth/2);var d=Math.round(t[0]+a)+"px";this.rendered.left_!=d&&(this.rendered.left_=i.left=d)}if(o==s.BOTTOM_LEFT||o==s.BOTTOM_CENTER||o==s.BOTTOM_RIGHT){""!==this.rendered.top_&&(this.rendered.top_=i.top="");var h=Math.round(e[1]-t[1]-r)+"px";this.rendered.bottom_!=h&&(this.rendered.bottom_=i.bottom=h)}else{""!==this.rendered.bottom_&&(this.rendered.bottom_=i.bottom=""),o!=s.CENTER_LEFT&&o!=s.CENTER_CENTER&&o!=s.CENTER_RIGHT||(r-=this.element.offsetHeight/2);var l=Math.round(t[1]+r)+"px";this.rendered.top_!=l&&(this.rendered.top_=i.top=l)}},u.prototype.getOptions=function(){return this.options},u.Property={ELEMENT:"element",MAP:"map",OFFSET:"offset",POSITION:"position",POSITIONING:"positioning"};e["a"]=u},"56ee":function(t,e,i){},d81d:function(t,e,i){"use strict";var n=i("23e7"),o=i("b727").map,a=i("1dde"),r=a("map");n({target:"Array",proto:!0,forced:!r},{map:function(t){return o(this,t,arguments.length>1?arguments[1]:void 0)}})},dc93:function(t,e,i){"use strict";i.r(e);var n=function(){var t=this,e=t._self._c;return e("div",[e("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[e("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[t._v("首页")]),e("el-breadcrumb-item",[t._v("OpenLayers")]),e("el-breadcrumb-item",[t._v("结合Echarts")])],1),e("el-card",[e("div",{attrs:{id:"map"}},[e("div",{staticClass:"ol-popup",attrs:{id:"popup"}},[e("a",{staticClass:"ol-popup-closer",attrs:{href:"#",id:"popup-closer"}}),e("div",{staticClass:"popup-title",attrs:{id:"popup-title"}}),e("div",{staticClass:"popup-content",attrs:{id:"popup-content"}})])])])],1)},o=[],a=(i("d81d"),i("164e")),r=i.n(a),s=i("43df"),p=i("332e"),d=i("5281"),h=i("256f"),l=i("21bc"),u=i("92cf"),c={data:function(){return{map:null}},created:function(){},mounted:function(){this.initMap()},methods:{initMap:function(){var t=new p["a"]({zoom:4,center:h["a"].transform([110,39],"EPSG:4326","EPSG:3857")}),e=new s["a"]({controls:l["a"].defaults({attribution:!1}).extend([]),target:"map",layers:[Object(u["d"])("vec_w"),Object(u["d"])("cva_w")],view:t});this.map=e,setTimeout(this.addMapChart,1e3)},addMapChart:function(){for(var t=[{name:"乌鲁木齐",x:87.5758285931,y:43.782211646,data:[{name:"男",value:40},{name:"女",value:60}]},{name:"拉萨",x:91.162997504,y:29.7104204643,data:[{name:"男",value:45},{name:"女",value:55}]},{name:"北京",x:116.4575803581078,y:40.04054437977018,data:[{name:"男",value:35},{name:"女",value:65}]},{name:"兰州",x:103.584297498,y:36.1190864503,data:[{name:"男",value:44},{name:"女",value:56}]}],e=0,i=t.length;e<i;e++){var n=t[e],o=h["a"].fromLonLat([n.x,n.y]),a=document.createElement("div");a.style.position="absolute",a.style.width="40px",a.style.height="40px";var r=new d["a"]({element:a,positioning:"center-center",position:o});this.map.addOverlay(r),this.addChart(a,n.data)}},addChart:function(t,e){var i=r.a.init(t),n={animation:!0,tooltip:{trigger:"item",formatter:"{b}:{c}"},series:[{type:"pie",radius:"100%",center:["50%","50%"],itemStyle:{normal:{labelLine:{show:!1}}},data:e,line:!1}]};i.setOption(n)}}},g=c,m=(i("e9d6a"),i("2877")),f=Object(m["a"])(g,n,o,!1,null,"2d0e5b0a",null);e["default"]=f.exports},e9d6a:function(t,e,i){"use strict";i("56ee")}}]);
//# sourceMappingURL=chunk-6a1855e2.6924277e.js.map