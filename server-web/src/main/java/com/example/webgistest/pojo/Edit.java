package com.example.webgistest.pojo;

public class Edit {
    private Integer id;

    private String name;

    private Object geom;

    public Edit(Integer id, String name, Object geom) {
        this.id = id;
        this.name = name;
        this.geom = geom;
    }

    public Edit() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Object getGeom() {
        return geom;
    }

    public void setGeom(Object geom) {
        this.geom = geom;
    }
}