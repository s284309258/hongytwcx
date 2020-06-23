package com.platform.entity;

import java.io.Serializable;

public class MendianEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public String mendian_name;
    public String mendian_address;
    public String mendian_tel;
    public String mendian_bustime;
    public int id;

    public String getMendian_name() {
        return mendian_name;
    }

    public void setMendian_name(String mendian_name) {
        this.mendian_name = mendian_name;
    }

    public String getMendian_address() {
        return mendian_address;
    }

    public void setMendian_address(String mendian_address) {
        this.mendian_address = mendian_address;
    }

    public String getMendian_tel() {
        return mendian_tel;
    }

    public void setMendian_tel(String mendian_tel) {
        this.mendian_tel = mendian_tel;
    }

    public String getMendian_bustime() {
        return mendian_bustime;
    }

    public void setMendian_bustime(String mendian_bustime) {
        this.mendian_bustime = mendian_bustime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
