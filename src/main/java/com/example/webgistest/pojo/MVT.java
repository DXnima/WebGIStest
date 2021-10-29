package com.example.webgistest.pojo;

public class MVT {

    private Integer z;

    private Integer x;

    private Integer y;

    private byte[] mvt;

    public MVT(Integer z, Integer x, Integer y, byte[] mvt) {
        this.z = z;
        this.x = x;
        this.y = y;
        this.mvt = mvt;
    }

    public MVT() {
        super();
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public byte[] getMvt() {
        return mvt;
    }

    public void setMvt(byte[] mvt) {
        this.mvt = mvt;
    }
}