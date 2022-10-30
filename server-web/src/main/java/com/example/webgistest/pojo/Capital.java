package com.example.webgistest.pojo;

public class Capital {
    private Integer gid;

    private String name;

    private Double lat;

    private Double lon;

    private Object geom;

    public Capital(Integer gid, String name, Double lat, Double lon, Object geom) {
        this.gid = gid;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.geom = geom;
    }

    public Capital() {
        super();
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Object getGeom() {
        return geom;
    }

    public void setGeom(Object geom) {
        this.geom = geom;
    }
}