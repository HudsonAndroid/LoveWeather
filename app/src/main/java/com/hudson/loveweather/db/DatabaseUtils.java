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

}
