package com.hudson.loveweather.db;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Hudson on 2017/11/30.
 */

public class DatabaseUtils {

    /**
     * 查询目标地区的weatherId
     * @param provinceName 该地区的省
     * @param cityName 该地区的城市（市级单位）
     * @param countryName 该地区的名字
     * @return weatherId 如果返回null,表示没有找到
     */
    public static String queryWeatherId(String provinceName,String cityName,String countryName){
        if(TextUtils.isEmpty(provinceName)||TextUtils.isEmpty(cityName)||TextUtils.isEmpty(countryName)){
            return null;
        }
        List<Country> results = DataSupport.where("provinceName = ? and cityName = ? and countryName = ?"
        ,provinceName,cityName,countryName).find(Country.class);
        if(results.size() == 0){
            return queryFailedSolution(results,provinceName,cityName,countryName);
        }
        return results.get(0).getWeatherId();
    }

    public static Country queryCountryByWeatherId(String weatherId){
        if(TextUtils.isEmpty(weatherId)){
            return null;
        }
        List<Country> results = DataSupport.where("weatherId = ?"
                ,weatherId).find(Country.class);
        if(results.size()>0){
            return results.get(0);
        }
        return null;
    }


    /**
     * 在搜索weatherId失败的情况下的解决方案
     *      1.可能是定位的地址含有“区”字，然数据库中没有，所以导致匹配失败
     *          解决：将百度定位的结果的地区字符进行修改，若含有“区”去除
     *          （还有一种情况，百度地位的不含“区”，数据库含有，这种情况极为少见）
     *      2.在条件1仍然不成立的情况下，将城市定位上一级
     *          即，例如数据库是  福建  福州  福州   那么直接将地区定为福州
     * @return
     */
    private static String queryFailedSolution(List<Country> results,String provinceName,String cityName,String countryName){
        if(countryName.endsWith("区")){
            countryName = countryName.replace("区", "");
            results = DataSupport.where("provinceName = ? and cityName = ? and countryName = ?"
                    ,provinceName,cityName,countryName).find(Country.class);
            if(results.size() == 0){
                return useLastLevelCity(provinceName, cityName);
            }
            return results.get(0).getWeatherId();
        }else{
            return useLastLevelCity(provinceName,cityName);
        }
    }

    /**
     * 直接使用上一级城市作为目标城市
     * @param provinceName
     * @param cityName
     * @return
     */
    @Nullable
    private static String useLastLevelCity(String provinceName, String cityName) {
        List<Country> results;
        results = DataSupport.where("provinceName = ? and cityName = ? and countryName = ?"
                ,provinceName,cityName,cityName).find(Country.class);
        if(results.size()==0){
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

    /**
     * 从选定的城市天气数据库缓存中获取数据
     * @param weatherId
     * @return
     */
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

    /**
     * 删除在城市管理页面中已经选择的城市
     * @param country
     * @return
     */
    public static int removeSelectedCountry(SelectedCountry country){
        return DataSupport.deleteAll(SelectedCountry.class, "weatherId = ? and cityName = ? and countryName = ?",
                country.getWeatherId(),country.getCityName(),country.getCountryName());
    }

    public static List<SelectedCountry> queryAllSelectedCountry(){
        return DataSupport.findAll(SelectedCountry.class);
    }



    /**
     * 从选定的城市天气质量数据库缓存中获取数据
     * @param weatherId
     * @return
     */
    public static String queryAirQualityJson(String weatherId){
        if(TextUtils.isEmpty(weatherId)){
            return null;
        }
        List<SelectedCountryAirQuality> selectedCountryAirQualities = DataSupport.where("weatherId = ?",
                weatherId).find(SelectedCountryAirQuality.class);
        if(selectedCountryAirQualities.size() == 0){
            return null;
        }
        return selectedCountryAirQualities.get(0).getAirQualityJson();
    }

    /**
     * 删除空气质量在城市管理页面中已经选择的城市
     * @param country
     * @return
     */
    public static int removeSelectedCountryAirQuality(SelectedCountry country){
        return DataSupport.deleteAll(SelectedCountryAirQuality.class, "weatherId = ? and cityName = ? and countryName = ?",
                country.getWeatherId(),country.getCityName(),country.getCountryName());
    }

    public static List<SelectedCountryAirQuality> queryAllSelectedCountryAirQuality(){
        return DataSupport.findAll(SelectedCountryAirQuality.class);
    }

}
