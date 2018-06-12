package com.hudson.loveweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Hudson on 2018/3/22.
 */

public class Weather6 {

    private List<HeWeather6Bean> HeWeather6;

    public List<HeWeather6Bean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather6Bean> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public static class HeWeather6Bean {
        /**
         * basic : {"cid":"CN101230106","location":"鼓楼","parent_city":"福州","admin_area":"福建","cnty":"中国","lat":"26.08228493","lon":"119.29929352","tz":"+8.00"}
         * update : {"loc":"2018-03-22 16:47","utc":"2018-03-22 08:47"}
         * status : ok
         * now : {"cloud":"0","cond_code":"101","cond_txt":"多云","fl":"2","hum":"45","pcpn":"0.0","pres":"1017","tmp":"17","vis":"10","wind_deg":"144","wind_dir":"东南风","wind_sc":"4","wind_spd":"22"}
         * daily_forecast : [{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2018-03-22","hum":"62","mr":"09:22","ms":"22:53","pcpn":"0.0","pop":"0","pres":"1020","sr":"06:04","ss":"18:14","tmp_max":"20","tmp_min":"9","uv_index":"10","vis":"20","wind_deg":"162","wind_dir":"东南风","wind_sc":"1-2","wind_spd":"2"},{"cond_code_d":"100","cond_code_n":"101","cond_txt_d":"晴","cond_txt_n":"多云","date":"2018-03-23","hum":"75","mr":"10:10","ms":"23:55","pcpn":"0.0","pop":"0","pres":"1021","sr":"06:03","ss":"18:15","tmp_max":"23","tmp_min":"11","uv_index":"9","vis":"20","wind_deg":"121","wind_dir":"东南风","wind_sc":"1-2","wind_spd":"4"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2018-03-24","hum":"81","mr":"11:02","ms":"08:46","pcpn":"0.0","pop":"0","pres":"1024","sr":"06:02","ss":"18:15","tmp_max":"22","tmp_min":"13","uv_index":"10","vis":"20","wind_deg":"94","wind_dir":"东风","wind_sc":"1-2","wind_spd":"11"},{"cond_code_d":"101","cond_code_n":"305","cond_txt_d":"多云","cond_txt_n":"小雨","date":"2018-03-25","hum":"80","mr":"11:59","ms":"00:55","pcpn":"0.0","pop":"0","pres":"1023","sr":"06:01","ss":"18:16","tmp_max":"23","tmp_min":"15","uv_index":"7","vis":"20","wind_deg":"70","wind_dir":"东北风","wind_sc":"3-4","wind_spd":"14"},{"cond_code_d":"305","cond_code_n":"101","cond_txt_d":"小雨","cond_txt_n":"多云","date":"2018-03-26","hum":"89","mr":"13:00","ms":"01:53","pcpn":"0.1","pop":"22","pres":"1022","sr":"06:00","ss":"18:16","tmp_max":"22","tmp_min":"14","uv_index":"4","vis":"12","wind_deg":"108","wind_dir":"东南风","wind_sc":"1-2","wind_spd":"1"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2018-03-27","hum":"86","mr":"14:03","ms":"02:48","pcpn":"0.0","pop":"0","pres":"1019","sr":"05:59","ss":"18:17","tmp_max":"25","tmp_min":"15","uv_index":"4","vis":"19","wind_deg":"79","wind_dir":"东风","wind_sc":"1-2","wind_spd":"10"},{"cond_code_d":"101","cond_code_n":"104","cond_txt_d":"多云","cond_txt_n":"阴","date":"2018-03-28","hum":"82","mr":"15:06","ms":"03:39","pcpn":"0.0","pop":"0","pres":"1016","sr":"05:58","ss":"18:17","tmp_max":"26","tmp_min":"17","uv_index":"6","vis":"20","wind_deg":"43","wind_dir":"东北风","wind_sc":"1-2","wind_spd":"11"}]
         * hourly : [{"cloud":"4","cond_code":"103","cond_txt":"晴间多云","dew":"6","hum":"67","pop":"0","pres":"1018","time":"2018-03-22 19:00","tmp":"14","wind_deg":"91","wind_dir":"东风","wind_sc":"1-2","wind_spd":"4"},{"cloud":"3","cond_code":"103","cond_txt":"晴间多云","dew":"7","hum":"86","pop":"0","pres":"1020","time":"2018-03-22 22:00","tmp":"11","wind_deg":"60","wind_dir":"东北风","wind_sc":"1-2","wind_spd":"3"},{"cloud":"2","cond_code":"103","cond_txt":"晴间多云","dew":"5","hum":"81","pop":"0","pres":"1021","time":"2018-03-23 01:00","tmp":"10","wind_deg":"17","wind_dir":"东北风","wind_sc":"1-2","wind_spd":"6"},{"cloud":"1","cond_code":"103","cond_txt":"晴间多云","dew":"4","hum":"75","pop":"0","pres":"1021","time":"2018-03-23 04:00","tmp":"9","wind_deg":"233","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"4"},{"cloud":"0","cond_code":"103","cond_txt":"晴间多云","dew":"7","hum":"84","pop":"0","pres":"1022","time":"2018-03-23 07:00","tmp":"10","wind_deg":"141","wind_dir":"东南风","wind_sc":"1-2","wind_spd":"4"},{"cloud":"0","cond_code":"103","cond_txt":"晴间多云","dew":"8","hum":"56","pop":"0","pres":"1023","time":"2018-03-23 10:00","tmp":"13","wind_deg":"78","wind_dir":"东北风","wind_sc":"1-2","wind_spd":"4"},{"cloud":"0","cond_code":"103","cond_txt":"晴间多云","dew":"8","hum":"47","pop":"0","pres":"1021","time":"2018-03-23 13:00","tmp":"19","wind_deg":"101","wind_dir":"东南风","wind_sc":"1-2","wind_spd":"5"},{"cloud":"0","cond_code":"103","cond_txt":"晴间多云","dew":"10","hum":"55","pop":"0","pres":"1020","time":"2018-03-23 16:00","tmp":"21","wind_deg":"101","wind_dir":"东风","wind_sc":"1-2","wind_spd":"3"}]
         * lifestyle : [{"brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。","type":"comf"},{"brf":"较舒适","txt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。","type":"drsg"},{"brf":"较易发","txt":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。","type":"flu"},{"brf":"适宜","txt":"天气较好，赶快投身大自然参与户外运动，尽情感受运动的快乐吧。","type":"sport"},{"brf":"适宜","txt":"天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。","type":"trav"},{"brf":"弱","txt":"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。","type":"uv"},{"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。","type":"cw"},{"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。","type":"air"}]
         */

        private BasicBean basic;
        private UpdateBean update;
        private String status;
        private NowBean now;
        private List<DailyForecastBean> daily_forecast;
        private List<HourlyBean> hourly;
        private List<LifestyleBean> lifestyle;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public NowBean getNow() {
            return now;
        }

        public void setNow(NowBean now) {
            this.now = now;
        }

        public List<DailyForecastBean> getDaily_forecast() {
            return daily_forecast;
        }

        public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public List<HourlyBean> getHourly() {
            return hourly;
        }

        public void setHourly(List<HourlyBean> hourly) {
            this.hourly = hourly;
        }

        public List<LifestyleBean> getLifestyle() {
            return lifestyle;
        }

        public void setLifestyle(List<LifestyleBean> lifestyle) {
            this.lifestyle = lifestyle;
        }

        public static class BasicBean {
            /**
             * cid : CN101230106
             * location : 鼓楼
             * parent_city : 福州
             * admin_area : 福建
             * cnty : 中国
             * lat : 26.08228493
             * lon : 119.29929352
             * tz : +8.00
             */

            private String cid;
            private String location;
            private String parent_city;
            private String admin_area;
            private String cnty;
            private String lat;
            private String lon;
            private String tz;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getParent_city() {
                return parent_city;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public void setAdmin_area(String admin_area) {
                this.admin_area = admin_area;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getTz() {
                return tz;
            }

            public void setTz(String tz) {
                this.tz = tz;
            }
        }

        public static class UpdateBean {
            /**
             * loc : 2018-03-22 16:47
             * utc : 2018-03-22 08:47
             */

            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }

        public static class NowBean implements Parcelable {
            /**
             * cloud : 0
             * cond_code : 101
             * cond_txt : 多云
             * fl : 2
             * hum : 45
             * pcpn : 0.0
             * pres : 1017
             * tmp : 17
             * vis : 10
             * wind_deg : 144
             * wind_dir : 东南风
             * wind_sc : 4
             * wind_spd : 22
             */

            private String cloud;
            private String cond_code;
            private String cond_txt;
            private String fl;
            private String hum;
            private String pcpn;
            private String pres;
            private String tmp;
            private String vis;
            private String wind_deg;
            private String wind_dir;
            private String wind_sc;
            private String wind_spd;

            public String getCloud() {
                return cloud;
            }

            public void setCloud(String cloud) {
                this.cloud = cloud;
            }

            public String getCond_code() {
                return cond_code;
            }

            public void setCond_code(String cond_code) {
                this.cond_code = cond_code;
            }

            public String getCond_txt() {
                return cond_txt;
            }

            public void setCond_txt(String cond_txt) {
                this.cond_txt = cond_txt;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
            }

            public String getWind_dir() {
                return wind_dir;
            }

            public void setWind_dir(String wind_dir) {
                this.wind_dir = wind_dir;
            }

            public String getWind_sc() {
                return wind_sc;
            }

            public void setWind_sc(String wind_sc) {
                this.wind_sc = wind_sc;
            }

            public String getWind_spd() {
                return wind_spd;
            }

            public void setWind_spd(String wind_spd) {
                this.wind_spd = wind_spd;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.cloud);
                dest.writeString(this.cond_code);
                dest.writeString(this.cond_txt);
                dest.writeString(this.fl);
                dest.writeString(this.hum);
                dest.writeString(this.pcpn);
                dest.writeString(this.pres);
                dest.writeString(this.tmp);
                dest.writeString(this.vis);
                dest.writeString(this.wind_deg);
                dest.writeString(this.wind_dir);
                dest.writeString(this.wind_sc);
                dest.writeString(this.wind_spd);
            }

            public NowBean() {
            }

            protected NowBean(Parcel in) {
                this.cloud = in.readString();
                this.cond_code = in.readString();
                this.cond_txt = in.readString();
                this.fl = in.readString();
                this.hum = in.readString();
                this.pcpn = in.readString();
                this.pres = in.readString();
                this.tmp = in.readString();
                this.vis = in.readString();
                this.wind_deg = in.readString();
                this.wind_dir = in.readString();
                this.wind_sc = in.readString();
                this.wind_spd = in.readString();
            }

            public static final Parcelable.Creator<NowBean> CREATOR = new Parcelable.Creator<NowBean>() {
                @Override
                public NowBean createFromParcel(Parcel source) {
                    return new NowBean(source);
                }

                @Override
                public NowBean[] newArray(int size) {
                    return new NowBean[size];
                }
            };
        }

        public static class DailyForecastBean {
            /**
             * cond_code_d : 101
             * cond_code_n : 101
             * cond_txt_d : 多云
             * cond_txt_n : 多云
             * date : 2018-03-22
             * hum : 62
             * mr : 09:22
             * ms : 22:53
             * pcpn : 0.0
             * pop : 0
             * pres : 1020
             * sr : 06:04
             * ss : 18:14
             * tmp_max : 20
             * tmp_min : 9
             * uv_index : 10
             * vis : 20
             * wind_deg : 162
             * wind_dir : 东南风
             * wind_sc : 1-2
             * wind_spd : 2
             */

            private String cond_code_d;
            private String cond_code_n;
            private String cond_txt_d;
            private String cond_txt_n;
            private String date;
            private String hum;
            private String mr;
            private String ms;
            private String pcpn;
            private String pop;
            private String pres;
            private String sr;
            private String ss;
            private String tmp_max;
            private String tmp_min;
            private String uv_index;
            private String vis;
            private String wind_deg;
            private String wind_dir;
            private String wind_sc;
            private String wind_spd;

            public String getCond_code_d() {
                return cond_code_d;
            }

            public void setCond_code_d(String cond_code_d) {
                this.cond_code_d = cond_code_d;
            }

            public String getCond_code_n() {
                return cond_code_n;
            }

            public void setCond_code_n(String cond_code_n) {
                this.cond_code_n = cond_code_n;
            }

            public String getCond_txt_d() {
                return cond_txt_d;
            }

            public void setCond_txt_d(String cond_txt_d) {
                this.cond_txt_d = cond_txt_d;
            }

            public String getCond_txt_n() {
                return cond_txt_n;
            }

            public void setCond_txt_n(String cond_txt_n) {
                this.cond_txt_n = cond_txt_n;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getMr() {
                return mr;
            }

            public void setMr(String mr) {
                this.mr = mr;
            }

            public String getMs() {
                return ms;
            }

            public void setMs(String ms) {
                this.ms = ms;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }

            public String getTmp_max() {
                return tmp_max;
            }

            public void setTmp_max(String tmp_max) {
                this.tmp_max = tmp_max;
            }

            public String getTmp_min() {
                return tmp_min;
            }

            public void setTmp_min(String tmp_min) {
                this.tmp_min = tmp_min;
            }

            public String getUv_index() {
                return uv_index;
            }

            public void setUv_index(String uv_index) {
                this.uv_index = uv_index;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
            }

            public String getWind_dir() {
                return wind_dir;
            }

            public void setWind_dir(String wind_dir) {
                this.wind_dir = wind_dir;
            }

            public String getWind_sc() {
                return wind_sc;
            }

            public void setWind_sc(String wind_sc) {
                this.wind_sc = wind_sc;
            }

            public String getWind_spd() {
                return wind_spd;
            }

            public void setWind_spd(String wind_spd) {
                this.wind_spd = wind_spd;
            }
        }

        public static class HourlyBean {
            /**
             * cloud : 4
             * cond_code : 103
             * cond_txt : 晴间多云
             * dew : 6
             * hum : 67
             * pop : 0
             * pres : 1018
             * time : 2018-03-22 19:00
             * tmp : 14
             * wind_deg : 91
             * wind_dir : 东风
             * wind_sc : 1-2
             * wind_spd : 4
             */

            private String cloud;
            private String cond_code;
            private String cond_txt;
            private String dew;
            private String hum;
            private String pop;
            private String pres;
            private String time;
            private String tmp;
            private String wind_deg;
            private String wind_dir;
            private String wind_sc;
            private String wind_spd;

            public String getCloud() {
                return cloud;
            }

            public void setCloud(String cloud) {
                this.cloud = cloud;
            }

            public String getCond_code() {
                return cond_code;
            }

            public void setCond_code(String cond_code) {
                this.cond_code = cond_code;
            }

            public String getCond_txt() {
                return cond_txt;
            }

            public void setCond_txt(String cond_txt) {
                this.cond_txt = cond_txt;
            }

            public String getDew() {
                return dew;
            }

            public void setDew(String dew) {
                this.dew = dew;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
            }

            public String getWind_dir() {
                return wind_dir;
            }

            public void setWind_dir(String wind_dir) {
                this.wind_dir = wind_dir;
            }

            public String getWind_sc() {
                return wind_sc;
            }

            public void setWind_sc(String wind_sc) {
                this.wind_sc = wind_sc;
            }

            public String getWind_spd() {
                return wind_spd;
            }

            public void setWind_spd(String wind_spd) {
                this.wind_spd = wind_spd;
            }
        }

        public static class LifestyleBean {
            /**
             * brf : 舒适
             * txt : 白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。
             * type : comf
             */

            private String brf;
            private String txt;
            private String type;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
