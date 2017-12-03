package com.hudson.loveweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Hudson on 2017/12/2.
 * 被选中过的城市实例
 */

public class SelectedCountry extends DataSupport{
    private int id;
    private String weatherId;
    private String cityName;//该区县对应的城市
    private String countryName;
    private String weatherJson;//上次访问的天气数据

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherJson() {
        return weatherJson;
    }

    public void setWeatherJson(String weatherJson) {
        this.weatherJson = weatherJson;
    }
}
