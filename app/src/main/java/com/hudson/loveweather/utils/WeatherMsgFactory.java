package com.hudson.loveweather.utils;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather6;

/**
 * 天气信息处理工厂
 * Created by Hudson on 2018/5/13.
 */

public class WeatherMsgFactory {

    public static String generateVoiceMsg(Weather6 weather6){
        if(weather6 == null){
            return null;
        }
        Weather6.HeWeather6Bean heWeather6Bean = weather6.getHeWeather6().get(0);
        if(heWeather6Bean != null){
            String format = "您好，天气达人为您播报。%s当前的天气%s,温度%s摄氏度,风向%s,空气湿度百分之%s。";
            String district =  heWeather6Bean.getBasic().getLocation();
            Weather6.HeWeather6Bean.NowBean now = heWeather6Bean.getNow();
            Weather6.HeWeather6Bean.LifestyleBean lifestyleBean = heWeather6Bean.getLifestyle().get(0);
            return String.format(format,district,now.getCond_txt(),now.getTmp(),now.getWind_dir(),now.getHum())+lifestyleBean.getTxt()+"...";
        }else{
            return null;
        }
    }

    /**
     * 根据温度高低，提示相应内容
     * @return
     */
    public static String generateTmpTips(String temperature){
        try{
            int value = Integer.valueOf(temperature);
            if(value < 0){
                return "当前天气很冷，您应该尽量减少室外运动，避免感冒！";
            }else if(value < 15){
                return "当前温度较低，您要注意保暖，身体是革命的本钱呢！";
            }else if(value < 30){
                return "当前天气十分凉爽，可以外出约一波！";
            }else if(value < 34){
                return "当前天气较热，您要注意避免中暑！";
            }else{
                return "当前天气十分炎热，赶紧找家超市蹭蹭空调，还有记得多喝水，避免中暑！";
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            return null;
        }
    }

    public static int getAirQualityImg(String desc){
        if(desc.equals("优")){
            return R.drawable.leaf_perfect;
        }else if(desc.equals("良")){
            return R.drawable.leaf_good;
        }else if(desc.equals("轻度污染")){
            return R.drawable.leaf_slight;
        }else if(desc.equals("中度污染")){
            return R.drawable.leaf_middle;
        }else if(desc.equals("重度污染")){
            return R.drawable.leaf_bad;
        }else if(desc.equals("严重污染")){
            return R.drawable.leaf_serious;
        }
        return R.drawable.leaf_perfect;
    }

    public static int getAirQualityColor(String desc){
        if(desc.equals("优")){
            return UIUtils.getColor(R.color.air_perfect);
        }else if(desc.equals("良")){
            return UIUtils.getColor(R.color.air_good);
        }else if(desc.equals("轻度污染")){
            return UIUtils.getColor(R.color.air_slight);
        }else if(desc.equals("中度污染")){
            return UIUtils.getColor(R.color.air_middle);
        }else if(desc.equals("重度污染")){
            return UIUtils.getColor(R.color.air_bad);
        }else if(desc.equals("严重污染")){
            return UIUtils.getColor(R.color.air_serious);
        }
        return UIUtils.getColor(R.color.air_perfect);
    }

    public static int getLevelColor(int level){
        switch (level){
            case 1:
                return UIUtils.getColor(R.color.air_perfect);
            case 2:
                return UIUtils.getColor(R.color.air_good);
            case 3:
                return UIUtils.getColor(R.color.air_slight);
            case 4:
                return UIUtils.getColor(R.color.air_middle);
            case 5:
                return UIUtils.getColor(R.color.air_bad);
            case 6:
                return UIUtils.getColor(R.color.air_serious);
            default:
                return UIUtils.getColor(R.color.air_perfect);
        }
    }

    public static int getAirQualityLevel(String desc){
        if(desc.equals("优")){
            return 1;
        }else if(desc.equals("良")){
            return 2;
        }else if(desc.equals("轻度污染")){
            return 3;
        }else if(desc.equals("中度污染")){
            return 4;
        }else if(desc.equals("重度污染")){
            return 5;
        }else if(desc.equals("严重污染")){
            return 6;
        }
        return 1;
    }

    public static int getNo2Level(String value){
        int result = Integer.valueOf(value);
        if(result>=0&&result<=100){
            return 1;//优
        }else if(result<=200){
            return 2;//良
        }else if(result <= 700){
            return 3;//轻度
        }else if(result <= 1200){
            return 4;//中度
        }else if(result <= 2340){
            return 5;//重度
        }else if(result <= 3840){
            return 6;//严重
        }
        return -1;
    }

    public static int getPM25Level(String value){
        int result = Integer.valueOf(value);
        if(result >=0&&result<=35){
            return 1;
        }else if(result<=75){
            return 2;
        }else if(result<=115){
            return 3;
        }else if(result<=150){
            return 4;
        }else if(result<=250){
            return 5;
        }else if(result<=500){
            return 6;
        }
        return -1;
    }

    public static int getO3Level(String value){
        int result = Integer.valueOf(value);
        if(result>=0&&result<=160){
            return 1;
        }else if(result<=200){
            return 2;
        }else if(result<=300){
            return 3;
        }else if(result<=400){
            return 4;
        }else if(result<=800){
            return 5;
        }else if(result<=1200){
            return 6;
        }
        return -1;
    }

    public static int getPM10Level(String value){
        int result = Integer.valueOf(value);
        if(result>=0&&result<=50){
            return 1;
        }else if(result<=150){
            return 2;
        }else if(result<=250){
            return 3;
        }else if(result<=350){
            return 4;
        }else if(result<=420){
            return 5;
        }else if(result<=600){
            return 6;
        }
        return -1;
    }

    public static int getCoLevel(String value){
        float result = Float.valueOf(value);
        if(result>=0&&result<=5){
            return 1;
        }else if(result<=10){
            return 2;
        }else if(result<=35){
            return 3;
        }else if(result<=60){
            return 4;
        }else if(result<=90){
            return 5;
        }else if(result<=150){
            return 6;
        }
        return -1;
    }

    public static int getSo2Level(String value){
        int result = Integer.valueOf(value);
        if(result>=0&&result<=150){
            return 1;
        }else if(result<=500){
            return 2;
        }else if(result<=650){
            return 3;
        }else if(result<=800){
            return 4;
        }else if(result<=1600){
            return 5;
        }else if(result<=2620){
            return 6;
        }
        return -1;
    }

}
