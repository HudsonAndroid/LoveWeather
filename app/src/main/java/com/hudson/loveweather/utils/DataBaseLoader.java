package com.hudson.loveweather.utils;

import com.hudson.loveweather.db.City;
import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.db.Province;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.jsonparser.CityJsonParser;
import com.hudson.loveweather.utils.jsonparser.CountryJsonParser;
import com.hudson.loveweather.utils.jsonparser.ProvinceJsonParser;
import com.hudson.loveweather.utils.log.LogUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Hudson on 2017/11/26.
 * 首次启动应用需要初始化数据库
 */

public class DataBaseLoader {
    public volatile static boolean loadStatus = false;

    /**
     * 加载数据到本地数据库
     * 首次应用加载
     */
    public static void loadDatabaseToLocal(){
        if(!SharedPreferenceUtils.getInstance().isLocalDatabaseLoaded()){
            loadStatus = true;
            loadProvinceData();
        }
    }

     static void loadProvinceData(){
        HttpUtils.requestNetData(Constants.DB_BASE_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadStatus = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ProvinceJsonParser provinceJsonParser = new ProvinceJsonParser();
                ArrayList<Province> dest = new ArrayList<>();
                loadStatus = loadStatus&&provinceJsonParser.parseJson(response.body().string(), dest, new Object[]{});
                if(loadStatus){
                    //继续加载city
                    Province province;
                    for (int i = 0; i < dest.size(); i++) {
                        province = dest.get(i);
                        loadCityData(Constants.DB_BASE_URL+ "/" + province.getProvinceCode(),
                                province.getProvinceName());
                    }
                }else{
                    DataBaseLoader.loadStatus = false;
                    LogUtils.log("省数据解析失败");
                }
            }
        });
    }

     static void loadCityData(final String url, final String provinceName){
        HttpUtils.requestNetData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadStatus = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CityJsonParser parser = new CityJsonParser();
                ArrayList<City> dest = new ArrayList<>();
                loadStatus = loadStatus&&parser.parseJson(response.body().string(), dest, provinceName);
                if(loadStatus){
                    //继续加载country数据，这是我们最终需要保存的数据库
                    City city;
                    for (int i = 0; i < dest.size(); i++) {
                        city = dest.get(i);
                        loadCountryData(url + "/" +city.getCityCode(),provinceName,city.getCityName());
                    }
                }
            }
        });
    }

     static void loadCountryData(String url, final String provinceName, final String cityName){
        HttpUtils.requestNetData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadStatus = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CountryJsonParser parser = new CountryJsonParser();
                ArrayList<Country> dest = new ArrayList<>();
                loadStatus = loadStatus&&parser.parseJson(response.body().string(), dest, provinceName, cityName);
                if(loadStatus){
                    DataSupport.saveAll(dest);
                }
            }
        });
    }

}
