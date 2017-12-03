package com.hudson.loveweather.db;

import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Hudson on 2017/11/30.
 */

public class DatabaseUtils {

    public static String queryWeatherId(String provinceName,String cityName,String countryName){
        if(TextUtils.isEmpty(provinceName)||TextUtils.isEmpty(cityName)||TextUtils.isEmpty(countryName)){
            return null;
        }
        List<Country> results = DataSupport.where("provinceName = ? and cityName = ? and countryName = ?"
        ,provinceName,cityName,countryName).find(Country.class);
        if(results.size() == 0){
            return null;
        }
        return results.get(0).getWeatherId();
    }

    public static Country queryWeatherId(String countryName){
        if(TextUtils.isEmpty(countryName)){
            return null;
        }
        List<Country> results = DataSupport.where("countryName = ?"
                ,countryName).find(Country.class);
        if(results.size() == 0){
            return null;
        }
        return results.get(0);
    }

    /**
     * 查找包含目标字符串的country
     * @param words 关键字
     * @return
     */
    public static List<Country> queryCountry(String words){
        words = "%" + words + "%";
        return DataSupport.where("provinceName like ? or cityName like ? or countryName like ?",
                words,words,words).find(Country.class);
    }

    public static String queryWeatherJson(String weatherId){
        if(TextUtils.isEmpty(weatherId)){
            return null;
        }
        List<SelectedCountry> selectedCountries = DataSupport.where("weatherId = ?",
                weatherId).find(SelectedCountry.class);
        if(selectedCountries.size() == 0){
            return null;
        }
        return selectedCountries.get(0).getWeatherJson();
    }

    public static int removeSelectedCountry(SelectedCountry country){
        return DataSupport.deleteAll(SelectedCountry.class, "weatherId = ? and cityName = ? and countryName = ?",
                country.getWeatherId(),country.getCityName(),country.getCountryName());
    }

    public static List<SelectedCountry> queryAllSelectedCountry(){
        return DataSupport.findAll(SelectedCountry.class);
    }

}
