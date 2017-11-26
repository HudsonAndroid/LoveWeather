package com.hudson.loveweather.utils.JsonParser;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONException;

/**
 * Created by Hudson on 2017/11/26.
 */

public abstract class JsonParser<T> {

    /**
     * 解析json
     * @param json
     * @param dest
     * @param args
     * @return
     */
    public boolean parseJson(String json,T dest,@Nullable Object... args){
        if(!TextUtils.isEmpty(json)&&dest!=null){
            try{
                return parseJsonData(json,dest,args);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 你应该使用的是上面的方法，而不是本方法
     * @param json
     * @param dest
     * @param args
     * @return
     * @throws JSONException
     */
    abstract boolean parseJsonData(String json,T dest,Object... args) throws JSONException;

}
