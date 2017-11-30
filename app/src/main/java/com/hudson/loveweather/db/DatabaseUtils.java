package com.hudson.loveweather.db;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Hudson on 2017/11/30.
 */

public class DatabaseUtils {

    public static String queryWeatherId(String provinceName,String cityName,String countryName){
        List<Country> results = DataSupport.where("provinceName = ? and cityName = ? and countryName = ?"
        ,provinceName,cityName,countryName).find(Country.class);
        if(results.size() == 0){
            return null;
        }
        return results.get(0).getWeatherId();
    }

}
