package com.hudson.loveweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Hudson on 2017/11/24.
 * 城市
 */

public class City extends DataSupport {
    private int id;
    private int cityCode;
    private int provinceId;
    private String cityName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}