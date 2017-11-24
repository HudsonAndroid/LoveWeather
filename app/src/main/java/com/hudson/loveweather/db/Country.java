package com.hudson.loveweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Hudson on 2017/11/24.
 * 区县
 */

public class Country extends DataSupport {
    private int id;
    private int cityId;
    private String weatherId;
    private String countryName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
