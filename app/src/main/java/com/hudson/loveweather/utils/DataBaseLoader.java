package com.hudson.loveweather.utils;

import android.text.TextUtils;

import com.hudson.loveweather.db.City;
import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.db.Province;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.jsonparser.CityJsonParser;
import com.hudson.loveweather.utils.jsonparser.CountryJsonParser;
import com.hudson.loveweather.utils.jsonparser.ProvinceJsonParser;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Hudson on 2017/11/26.
 * 首次启动应用需要初始化数据库
 */

public class DataBaseLoader {
    public volatile static boolean loadStatus = false;
    static final int LOAD_FAILED_TYPE_PROVINCE = 0;
    static final int LOAD_FAILED_TYPE_CITY = 1;

    /**
     * on 2018/3/21  发现郭霖的城市数据库服务器会出现问题，所以在此需要重新处理
     * 中国----
     * 省份-----
     * 城市 -------
     * 区县----
     * 经过分析，发现，如果数据加载失败了，那么肯定是一个城市的（该城市下所有的内容全部加载失败）;或者是
     * 一个省的数据加载失败了（这种情况更加严重），那么旗下所有城市加载失败，相应的所有城市旗下的区县全部
     * 加载失败.
     */
    private static HashMap<String, LoadFailedBean> mLoadFlags = new HashMap<>();

    private static class LoadFailedBean{
        int failedType;
        String lastLevelUrl;
        String provinceName;//仅city加载失败时使用
        int times = 5;

        public LoadFailedBean(int failedType, String lastLevelUrl, String provinceName) {
            this.failedType = failedType;
            this.lastLevelUrl = lastLevelUrl;
            this.provinceName = provinceName;
        }
    }

    /**
     * 加载数据到本地数据库
     * 首次应用加载
     */
    public static void loadDatabaseToLocal() {
        if (!SharedPreferenceUtils.getInstance().isLocalDatabaseLoaded()) {
            loadProvinceData();
        }
    }

    static void loadProvinceData() {
        HttpUtils.requestNetData(Constants.DB_BASE_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //重新尝试
                loadProvinceData();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ProvinceJsonParser provinceJsonParser = new ProvinceJsonParser();
                ArrayList<Province> dest = new ArrayList<>();
                if (provinceJsonParser.parseJson(response.body().string(), dest, new Object[]{})) {
                    //继续加载city
                    Province province;
                    for (int i = 0; i < dest.size(); i++) {//获取省的数据
                        province = dest.get(i);
                        loadCityData(Constants.DB_BASE_URL + "/" + province.getProvinceCode(),
                                province.getProvinceName());
                    }
                }
            }
        });
    }

    static void loadCityData(final String url, final String provinceName) {
        HttpUtils.requestNetData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //把该异常加入加载失败记录中,并尝试重试
                if (!mLoadFlags.containsKey(provinceName)) {//两次加载失败，那么直接判定加载失败
                    LoadFailedBean bean = new LoadFailedBean(LOAD_FAILED_TYPE_PROVINCE,url,null);
                    bean.times --;
                    mLoadFlags.put(provinceName, bean);
                    loadCityData(url, provinceName);
                }else{
                    LoadFailedBean bean = mLoadFlags.get(provinceName);
                    bean.times --;
                    if(bean.times>0){
                        loadCityData(url, provinceName);
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CityJsonParser parser = new CityJsonParser();
                ArrayList<City> dest = new ArrayList<>();
                if (parser.parseJson(response.body().string(), dest, provinceName)) {
                    //查看加载失败的记录中是否存在，如果存在，那么移除该flag
                    if (mLoadFlags.containsKey(provinceName)) {
                        mLoadFlags.remove(provinceName);
                    }
                    //继续加载country数据，这是我们最终需要保存的数据库
                    City city;
                    for (int i = 0; i < dest.size(); i++) {
                        city = dest.get(i);
                        loadCountryData(url + "/" + city.getCityCode(), provinceName, city.getCityName());
                    }
                }
            }
        });
    }

    static void loadCountryData(final String url, final String provinceName, final String cityName) {
        HttpUtils.requestNetData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //尝试重试
                if (!mLoadFlags.containsKey(cityName)) {
                    LoadFailedBean bean = new LoadFailedBean(LOAD_FAILED_TYPE_CITY,url,provinceName);
                    bean.times --;
                    mLoadFlags.put(cityName, bean);
                    loadCountryData(url, provinceName, cityName);
                }else{
                    LoadFailedBean bean = mLoadFlags.get(cityName);
                    bean.times --;
                    if(bean.times>0){
                        loadCountryData(url, provinceName, cityName);
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CountryJsonParser parser = new CountryJsonParser();
                ArrayList<Country> dest = new ArrayList<>();
                if (parser.parseJson(response.body().string(), dest, provinceName, cityName)) {
                    //查看加载失败的记录中是否存在，如果存在，那么移除该flag
                    if (mLoadFlags.containsKey(cityName)) {
                        mLoadFlags.remove(cityName);
                    }
                    DataSupport.saveAll(dest);
                }
            }
        });
    }

    /**
     * 检查数据库是否同步到本地成功了
     *
     * @return 是否同步成功
     */
    public static boolean checkDataLoadStatus() {
        if (SharedPreferenceUtils.getInstance().isLocalDatabaseLoaded()) {
            return true;
        }
        //检查数据库同步状态
        if (mLoadFlags.size() == 0) {//数据是加载成功的
            loadStatus = true;
        } else {//数据是加载失败的，重新尝试加载
            loadStatus = false;
            tryLoadDataAgain();
        }
        SharedPreferenceUtils.getInstance().saveLocalDatabaseFlag(loadStatus);
        return loadStatus;
    }

    /**
     * 尝试再次加载数据
     * 该方法仅在检查数据库状态时回调
     */
    private static void tryLoadDataAgain() {
        //对HashMap进行遍历
        Iterator iter = mLoadFlags.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String name = (String) entry.getKey();
            LoadFailedBean bean = (LoadFailedBean) entry.getValue();
            if(bean.failedType == LOAD_FAILED_TYPE_PROVINCE){//如果是省份加载失败了，那么应该再次加载该省下的城市
                loadCityData(bean.lastLevelUrl, name);
            }else if(bean.failedType == LOAD_FAILED_TYPE_CITY){//如果是城市加载失败了，那么应该再次加载该城市下的区县
                loadCountryData(bean.lastLevelUrl,bean.provinceName,name);
            }
        }
    }

    /**
     * 如果在APP关闭之前，还没有同步完成，那么留给下次启动时加载数据
     * 文件格式:
     *     type name url provinceName
     */
    public static void saveLoadFailedBeanIntoFile(){
        String cityDataLoadFailedFilePath = AppStorageUtils.getCityDataLoadFailedFilePath();
        if(mLoadFlags.size()>0){
            StringBuilder sb = new StringBuilder("");
            Iterator iter = mLoadFlags.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String name = (String) entry.getKey();
                LoadFailedBean bean = (LoadFailedBean) entry.getValue();
                sb.append(bean.failedType).append(name).append(bean.lastLevelUrl).append(bean.provinceName).append("\n");
            }
            AppStorageUtils.writeFile(cityDataLoadFailedFilePath,sb.toString());
        }
    }

    /**
     *
     * @return 是否上次有加载
     */
    public static boolean readLoadFailedBeanFromFile(){
        String cityDataLoadFailedFilePath = AppStorageUtils.getCityDataLoadFailedFilePath();
        String s = AppStorageUtils.readFile(cityDataLoadFailedFilePath);
        if(!TextUtils.isEmpty(s)) {
            String[] split = s.split("\n");
            for (int i = 0; i < split.length; i++) {
                String tmp = split[i];
                if (!TextUtils.isEmpty(tmp)) {
                    String[] item = tmp.split(" ");
                    LoadFailedBean bean = new LoadFailedBean(Integer.valueOf(item[0]), item[2],
                            item[3]);
                    mLoadFlags.put(item[1], bean);
                }
            }
            tryLoadDataAgain();
            return true;
        }else{
            return false;
        }
    }

}
