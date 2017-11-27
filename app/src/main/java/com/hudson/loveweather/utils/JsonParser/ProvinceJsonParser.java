package com.hudson.loveweather.utils.jsonparser;

import com.hudson.loveweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hudson on 2017/11/26.
 * 省解析
 */

public class ProvinceJsonParser extends JsonParser<ArrayList<Province>> {
    @Override
    public boolean parseJsonData(String json, ArrayList<Province> dest, Object... args) throws JSONException {
        JSONArray provinces = new JSONArray(json);
        Province province;
        for (int i = 0; i < provinces.length(); i++) {
            JSONObject cityObject = provinces.getJSONObject(i);
            province = new Province();
            province.setProvinceName(cityObject.getString("name"));
            province.setProvinceCode(cityObject.getInt("id"));
            dest.add(province);
        }
        return true;
    }
}
