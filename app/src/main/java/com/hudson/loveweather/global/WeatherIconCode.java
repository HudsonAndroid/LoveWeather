package com.hudson.loveweather.global;

import android.support.annotation.NonNull;

import com.hudson.loveweather.R;

/**
 * 和风天气图标对应码
 * Created by Hudson on 2018/5/27.
 */

public class WeatherIconCode {

    public static int getIconByCode(String code){
        return getIconByCode(Integer.valueOf(code));
    }

    /*  代码	中文	英文
        100	晴	Sunny_d
        101	多云	Cloudy
        102	少云	Few Clouds
        103	晴间多云	Partly Cloudy
        104	阴	Overcast
        1001 晴（夜）	Sunny_n
        1031 晴间多云(夜)	Partly Cloudy_n
        1041	阴(夜)	Overcast_n

        200	有风	Windy
        201	平静	Calm
        202	微风	Light Breeze
        203	和风	Moderate/Gentle Breeze
        204	清风	Fresh Breeze
        205	强风/劲风	Strong Breeze
        206	疾风	High Wind, Near Gale
        207	大风	Gale
        208	烈风	Strong Gale
        209	风暴	Storm
        210	狂爆风	Violent Storm
        211	飓风	Hurricane
        212	龙卷风	Tornado
        213	热带风暴	Tropical Storm
        300	阵雨	Shower Rain
        301	强阵雨	Heavy Shower Rain
        302	雷阵雨	Thundershower
        303	强雷阵雨	Heavy Thunderstorm
        304	雷阵雨伴有冰雹	Hail
        305	小雨	Light Rain
        306	中雨	Moderate Rain
        307	大雨	Heavy Rain
        308	极端降雨	Extreme Rain
        309	毛毛雨/细雨	Drizzle Rain
        310	暴雨	Storm
        311	大暴雨	Heavy Storm
        312	特大暴雨	Severe Storm
        313	冻雨	Freezing Rain
        400	小雪	Light Snow
        401	中雪	Moderate Snow
        402	大雪	Heavy Snow
        403	暴雪	Snowstorm
        404	雨夹雪	Sleet
        405	雨雪天气	Rain And Snow
        406	阵雨夹雪	Shower Snow
        407	阵雪	Snow Flurry
        500	薄雾	Mist
        501	雾	Foggy
        502	霾	Haze
        503	扬沙	Sand
        504	浮尘	Dust
        507	沙尘暴	Duststorm
        508	强沙尘暴	Sandstorm
        900	热	Hot
        901	冷	Cold
        999	未知	Unknown*/
    public static int getIconByCode(@NonNull int code){
        switch (code){
            case 100:
                return R.drawable.icon_sunny_d;
            case 101:
                return R.drawable.icon_cloudy;
            case 102:
                return R.drawable.icon_few_clouds;
            case 103:
                return R.drawable.icon_partly_cloudly;
            case 104:
                return R.drawable.icon_overcast;
            case 1001:
                return R.drawable.icon_sunny_n;
            case 1031:
                return R.drawable.icon_partly_cloudy_n;
            case 1041:
                return R.drawable.icon_overcast_n;
            case 200:
                return R.drawable.icon_windy;
            case 201:
                return R.drawable.icon_wind_calm;
            case 202:
                return R.drawable.icon_wind_light;
            case 203:
                return R.drawable.icon_wind_gentle;
            case 204:
                return R.drawable.icon_wind_fresh;
            case 205:
                return R.drawable.icon_wind_strong;
            case 206:
                return R.drawable.icon_wind_high;
            case 207:
                return R.drawable.icon_wind_gale;
            case 208:
                return R.drawable.icon_wind_strong_gale;
            case 209:
                return R.drawable.icon_wind_strom;
            case 210:
                return R.drawable.icon_wind_violent_strom;
            case 211:
                return R.drawable.icon_wind_hurricane;
            case 212:
                return R.drawable.icon_wind_tornado;
            case 213:
                return R.drawable.icon_wind_tropical_strom;
            case 300:
                return R.drawable.icon_rain_shower;
            case 301:
                return R.drawable.icon_rain_heavy_shower;
            case 302:
                return R.drawable.icon_rain_thunder_shower;
            case 303:
                return R.drawable.icon_rain_heavy_thunder_shower;
            case 304:
                return R.drawable.icon_rain_hail;
            case 305:
                return R.drawable.icon_rain_light;
            case 306:
                return R.drawable.icon_rain_moderate;
            case 307:
                return R.drawable.icon_rain_heavy;
            case 308:
                return R.drawable.icon_rain_extreme;
            case 309:
                return R.drawable.icon_rain_drizzle;
            case 310:
                return R.drawable.icon_rain_strom;
            case 311:
                return R.drawable.icon_rain_heavy_strom;
            case 312:
                return R.drawable.icon_rain_severe_strom;
            case 313:
                return R.drawable.icon_rain_freezing;
            case 3001:
                return R.drawable.icon_rain_shower_n;
            case 3011:
                return R.drawable.icon_rain_heavy_shower_n;
            case 400:
                return R.drawable.icon_snow_light;
            case 401:
                return R.drawable.icon_snow_moderate;
            case 402:
                return R.drawable.icon_snow_heavy;
            case 403:
                return R.drawable.icon_snow_strom;
            case 404:
                return R.drawable.icon_snow_sleet;
            case 405:
                return R.drawable.icon_snow_and_rain;
            case 406:
                return R.drawable.icon_snow_shower;
            case 407:
                return R.drawable.icon_snow_flurry;
            case 4061:
                return R.drawable.icon_snow_shower_n;
            case 4071:
                return R.drawable.icon_snow_flurry_n;
            case 500:
                return R.drawable.icon_fog_mist;
            case 501:
                return R.drawable.icon_fog;
            case 502:
                return R.drawable.icon_haze;
            case 503:
                return R.drawable.icon_sand;
            case 504:
                return R.drawable.icon_dust;
            case 507:
                return R.drawable.icon_dust_strom;
            case 508:
                return R.drawable.icon_sand_strom;
            case 900:
                return R.drawable.icon_hot;
            case 901:
                return R.drawable.icon_cold;
            case 999:
                return R.drawable.icon_unknown;
        }
        return R.drawable.icon_unknown;
    }

    /**
     * 算出夜晚的天气code
     * @param resCode
     * @return
     */
    public static int generateNightCode(String resCode){
        return Integer.valueOf(resCode+"1");
    }

}
