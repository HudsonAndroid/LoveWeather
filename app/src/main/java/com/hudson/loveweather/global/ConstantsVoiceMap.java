package com.hudson.loveweather.global;

import com.hudson.loveweather.R;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hudson on 2018/5/13.
 */

public class ConstantsVoiceMap{
    private Map<String,String> mVoiceMap = new HashMap<>();
    private static class VoiceMapHelper{
        static final ConstantsVoiceMap sInstance = new ConstantsVoiceMap();
    }

    private ConstantsVoiceMap(){
        initMap();
    }

    /*<item name="xiaoyan"/>
        <item name="xiaoyu"/>
        <item name="catherine"/>
        <item name="henry"/>
        <item name="vimary"/>
        <item name="vixy"/>
        <item name="xiaoqi"/>
        <item name="vixf"/>
        <item name="xiaomei"/>
        <item name="vixl"/>
        <item name="xiaolin"/>
        <item name="xiaorong"/>
        <item name="vixyun"/>
        <item name="xiaoqian"/>
        <item name="xiaokun"/>
        <item name="xiaoqiang"/>
        <item name="vixying"/>
        <item name="xiaoxin"/>
        <item name="nannan"/>
        <item name="vils"/>
        <item name="mariane"/>
        <item name="allabent"/>
        <item name="gabriela"/>
        <item name="abha"/>
        <item name="xiaoyun"/>*/

    private void initMap(){
        mVoiceMap.put("xiaoyan", UIUtils.getString(R.string.xiaoyan));
        mVoiceMap.put("xiaoyu",UIUtils.getString(R.string.xiaoyu));
        mVoiceMap.put("catherine",UIUtils.getString(R.string.catherine));
        mVoiceMap.put("henry",UIUtils.getString(R.string.henry));
        mVoiceMap.put("vimary",UIUtils.getString(R.string.vimary));
        mVoiceMap.put("vixy",UIUtils.getString(R.string.vixy));
        mVoiceMap.put("xiaoqi",UIUtils.getString(R.string.xiaoqi));
        mVoiceMap.put("vixf",UIUtils.getString(R.string.vixf));
        mVoiceMap.put("xiaomei",UIUtils.getString(R.string.xiaomei));
        mVoiceMap.put("vixl",UIUtils.getString(R.string.vixl));
        mVoiceMap.put("xiaolin",UIUtils.getString(R.string.xiaolin));
        mVoiceMap.put("xiaorong",UIUtils.getString(R.string.xiaorong));
        mVoiceMap.put("vixyun",UIUtils.getString(R.string.vixyun));
        mVoiceMap.put("xiaoqian",UIUtils.getString(R.string.xiaoqian));
        mVoiceMap.put("xiaokun",UIUtils.getString(R.string.xiaokun));
        mVoiceMap.put("xiaoqiang",UIUtils.getString(R.string.xiaoqiang));
        mVoiceMap.put("vixying",UIUtils.getString(R.string.vixying));
        mVoiceMap.put("xiaoxin",UIUtils.getString(R.string.xiaoxin));
        mVoiceMap.put("nannan",UIUtils.getString(R.string.nannan));
        mVoiceMap.put("vils",UIUtils.getString(R.string.vils));
        mVoiceMap.put("mariane",UIUtils.getString(R.string.mariane));
        mVoiceMap.put("allabent",UIUtils.getString(R.string.allabent));
        mVoiceMap.put("gabriela",UIUtils.getString(R.string.gabriela));
        mVoiceMap.put("abha",UIUtils.getString(R.string.abha));
        mVoiceMap.put("xiaoyun",UIUtils.getString(R.string.xiaoyun));
    }

    public static String getValue(String key){
        return VoiceMapHelper.sInstance.mVoiceMap.get(key);
    }

    public static List<String> getVoiceReadList(){
        List<String> datas = new ArrayList<>();
        String[] voiceReaderArray = UIUtils.getContext().getResources().getStringArray(R.array.voice_list);
        for (String s : voiceReaderArray) {
            datas.add(ConstantsVoiceMap.getValue(s));
        }
        return datas;
    }

    public static String getReaderKey(String reader){
        String[] voiceReaderArray = UIUtils.getContext().getResources().getStringArray(R.array.voice_list);
        for (String s : voiceReaderArray) {
            if(reader.equals(getValue(s))){
                return s;
            }
        }
        return "";
    }

}
