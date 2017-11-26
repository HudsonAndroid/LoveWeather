package com.hudson.loveweather.utils.JsonParser;

import com.hudson.loveweather.db.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hudson on 2017/11/26.
 */

public class CountryJsonParser extends JsonParser<ArrayList<Country>> {
    @Override
    boolean parseJsonData(String json, ArrayList<Country> dest, Object... args) throws JSONException {
        JSONArray countries = new JSONArray(json);
        Country country;
        for (int i = 0; i < countries.length(); i++) {
            JSONObject cityObject = countries.getJSONObject(i);
            country = new Country();
            country.setCountryName(cityObject.getString("name"));
            country.setWeatherId(cityObject.getString("id"));
            country.setProvinceName((String)args[0]);
            country.setCityName((String)args[1]);
            dest.add(country);
        }
        return true;
    }
}
