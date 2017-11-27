package com.hudson.loveweather.utils.jsonparser;

import com.hudson.loveweather.db.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hudson on 2017/11/26.
 * 城市Json解析器
 */

public class CityJsonParser extends JsonParser<ArrayList<City>>{

    @Override
    boolean parseJsonData(String json, ArrayList<City> dest, Object... args) throws JSONException {
        JSONArray citys = new JSONArray(json);
        City city;
        for (int i = 0; i < citys.length(); i++) {
            JSONObject cityObject = citys.getJSONObject(i);
            city = new City();
            city.setCityName(cityObject.getString("name"));
            city.setCityCode(cityObject.getInt("id"));
            city.setProvinceName((String)args[0]);//需要在解析province时把provinceId传进来
            dest.add(city);
        }
        return true;
    }
}
