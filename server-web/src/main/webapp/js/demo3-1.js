window.onload = initMap;

function initMap() {
    var base =  new ol.layer.Tile({
        source: new ol.source.OSM()
    });

    var vector = new ol.layer.Vector({
        source: null,
        style: styleFunction
    });

    window.map = new ol.Map({
        controls: ol.control.defaults({
            attribution: false
        }).extend([]),
        target: 'map',
        layers: [base, vector],
        view: new ol.View({
            zoom: 4,
            center:ol.proj.transform([110, 39],"EPSG:4326", "EPSG:3857")
        })
    });

    $.get("capital/all", function (result) {
        var features = [];
        for(var i=0,len=result.data.length;i<len;i++){
            var _r = result.data[i],
                coord = ol.proj.transform([_r.lon, _r.lat], "EPSG:4326", "EPSG:3857");
            var feature = new ol.Feature({
                geometry: new ol.geom.Point(coord),
                name:_r.name
            });
            features.push(feature);
        }
        var source = new ol.source.Vector({
            features: features
        });
        vector.setSource(source);
    });

    map.on("pointermove", function(evt){
        var lonlat = ol.proj.transform(evt.coordinate, "EPSG:3857", "EPSG:4326");
        $("#mousepos").html(lonlat[0].toFixed(3)+", "+lonlat[1].toFixed(3)).show();
    });
}

function styleFunction(feature) {
    var stroke = new ol.style.Stroke({
        color: 'black',
        width: 2
    });
    var fill = new ol.style.Fill({
        color: 'red'
    });
    var _name = feature.get("name");
    var _radius = 6,
        _radius2 = 6;
    if(_name==="北京"){
        _radius = 8;
        _radius2 = 4;
    }
    return new ol.style.Style({
        stroke:stroke,
        fill: fill,
        image: new ol.style.RegularShape({
            fill: fill,
            stroke: stroke,
            points: 5,
            radius: _radius,
            radius2: _radius2,
            angle: 0
        })
    });
}


