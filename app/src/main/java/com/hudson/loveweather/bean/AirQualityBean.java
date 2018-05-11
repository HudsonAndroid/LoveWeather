package com.hudson.loveweather.bean;

import java.util.List;

/**
 * Created by Hudson on 2018/4/1.
 * 天气质量实例
 */

public class AirQualityBean {

    private List<HeWeather6Bean> HeWeather6;

    public List<HeWeather6Bean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather6Bean> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public static class HeWeather6Bean {
        /**
         * basic : {"cid":"CN101010100","location":"北京","parent_city":"北京","admin_area":"北京","cnty":"中国","lat":"39.90498734","lon":"116.4052887","tz":"+8.00"}
         * update : {"loc":"2018-04-01 09:47","utc":"2018-04-01 01:47"}
         * status : ok
         * air_now_city : {"aqi":"243","qlty":"重度污染","main":"PM2.5","pm25":"193","pm10":"222","no2":"92","so2":"9","co":"1.46","o3":"12","pub_time":"2018-04-01 09:00"}
         * air_now_station : [{"air_sta":"万寿西宫","aqi":"261","asid":"CNA1001","co":"1.8","lat":"39.8673","lon":"116.366","main":"PM2.5","no2":"109","o3":"2","pm10":"261","pm25":"211","pub_time":"2018-04-01 08:00","qlty":"重度污染","so2":"9"},{"air_sta":"定陵","aqi":"134","asid":"CNA1002","co":"0.8","lat":"40.2865","lon":"116.17","main":"PM2.5","no2":"31","o3":"52","pm10":"142","pm25":"102","pub_time":"2018-04-01 08:00","qlty":"轻度污染","so2":"5"},{"air_sta":"东四","aqi":"269","asid":"CNA1003","co":"1.9","lat":"39.9522","lon":"116.434","main":"PM2.5","no2":"104","o3":"4","pm10":"244","pm25":"219","pub_time":"2018-04-01 08:00","qlty":"重度污染","so2":"18"},{"air_sta":"天坛","aqi":"279","asid":"CNA1004","co":"1.8","lat":"39.8745","lon":"116.434","main":"PM2.5","no2":"79","o3":"4","pm10":"240","pm25":"229","pub_time":"2018-04-01 08:00","qlty":"重度污染","so2":"2"},{"air_sta":"农展馆","aqi":"259","asid":"CNA1005","co":"1.8","lat":"39.9716","lon":"116.473","main":"PM2.5","no2":"112","o3":"2","pm10":"243","pm25":"209","pub_time":"2018-04-01 08:00","qlty":"重度污染","so2":"13"},{"air_sta":"官园","aqi":"221","asid":"CNA1006","co":"1.5","lat":"39.9425","lon":"116.361","main":"PM2.5","no2":"123","o3":"2","pm10":"226","pm25":"171","pub_time":"2018-04-01 08:00","qlty":"重度污染","so2":"12"},{"air_sta":"海淀区万柳","aqi":"160","asid":"CNA1007","co":"1.4","lat":"39.9934","lon":"116.315","main":"PM2.5","no2":"112","o3":"4","pm10":"184","pm25":"122","pub_time":"2018-04-01 08:00","qlty":"中度污染","so2":"8"},{"air_sta":"顺义新城","aqi":"199","asid":"CNA1008","co":"1.5","lat":"40.1438","lon":"116.72","main":"PM2.5","no2":"104","o3":"9","pm10":"190","pm25":"149","pub_time":"2018-04-01 08:00","qlty":"中度污染","so2":"13"},{"air_sta":"怀柔镇","aqi":"152","asid":"CNA1009","co":"1.1","lat":"40.3937","lon":"116.644","main":"PM2.5","no2":"36","o3":"46","pm10":"135","pm25":"116","pub_time":"2018-04-01 08:00","qlty":"中度污染","so2":"5"},{"air_sta":"昌平镇","aqi":"172","asid":"CNA1010","co":"1.1","lat":"40.1952","lon":"116.23","main":"PM2.5","no2":"74","o3":"17","pm10":"156","pm25":"130","pub_time":"2018-04-01 08:00","qlty":"中度污染","so2":"9"},{"air_sta":"奥体中心","aqi":"253","asid":"CNA1011","co":"1.7","lat":"40.0031","lon":"116.407","main":"PM2.5","no2":"114","o3":"2","pm10":"249","pm25":"203","pub_time":"2018-04-01 08:00","qlty":"重度污染","so2":"12"},{"air_sta":"古城","aqi":"179","asid":"CNA1012","co":"1.1","lat":"39.9279","lon":"116.225","main":"PM2.5","no2":"115","o3":"2","pm10":"261","pm25":"135","pub_time":"2018-04-01 08:00","qlty":"中度污染","so2":"5"}]
         */

        private BasicBean basic;
        private UpdateBean update;
        private String status;
        private AirNowCityBean air_now_city;
        private List<AirNowStationBean> air_now_station;

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

        public AirNowCityBean getAir_now_city() {
            return air_now_city;
        }

        public void setAir_now_city(AirNowCityBean air_now_city) {
            this.air_now_city = air_now_city;
        }

        public List<AirNowStationBean> getAir_now_station() {
            return air_now_station;
        }

        public void setAir_now_station(List<AirNowStationBean> air_now_station) {
            this.air_now_station = air_now_station;
        }

        public static class BasicBean {
            /**
             * cid : CN101010100
             * location : 北京
             * parent_city : 北京
             * admin_area : 北京
             * cnty : 中国
             * lat : 39.90498734
             * lon : 116.4052887
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
             * loc : 2018-04-01 09:47
             * utc : 2018-04-01 01:47
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

        public static class AirNowCityBean {
            /**
             * aqi : 243
             * qlty : 重度污染
             * main : PM2.5
             * pm25 : 193
             * pm10 : 222
             * no2 : 92
             * so2 : 9
             * co : 1.46
             * o3 : 12
             * pub_time : 2018-04-01 09:00
             */

            private String aqi;
            private String qlty;
            private String main;
            private String pm25;
            private String pm10;
            private String no2;
            private String so2;
            private String co;
            private String o3;
            private String pub_time;

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getPm10() {
                return pm10;
            }

            public void setPm10(String pm10) {
                this.pm10 = pm10;
            }

            public String getNo2() {
                return no2;
            }

            public void setNo2(String no2) {
                this.no2 = no2;
            }

            public String getSo2() {
                return so2;
            }

            public void setSo2(String so2) {
                this.so2 = so2;
            }

            public String getCo() {
                return co;
            }

            public void setCo(String co) {
                this.co = co;
            }

            public String getO3() {
                return o3;
            }

            public void setO3(String o3) {
                this.o3 = o3;
            }

            public String getPub_time() {
                return pub_time;
            }

            public void setPub_time(String pub_time) {
                this.pub_time = pub_time;
            }
        }

        public static class AirNowStationBean {
            /**
             * air_sta : 万寿西宫
             * aqi : 261
             * asid : CNA1001
             * co : 1.8
             * lat : 39.8673
             * lon : 116.366
             * main : PM2.5
             * no2 : 109
             * o3 : 2
             * pm10 : 261
             * pm25 : 211
             * pub_time : 2018-04-01 08:00
             * qlty : 重度污染
             * so2 : 9
             */

            private String air_sta;
            private String aqi;
            private String asid;
            private String co;
            private String lat;
            private String lon;
            private String main;
            private String no2;
            private String o3;
            private String pm10;
            private String pm25;
            private String pub_time;
            private String qlty;
            private String so2;

            public String getAir_sta() {
                return air_sta;
            }

            public void setAir_sta(String air_sta) {
                this.air_sta = air_sta;
            }

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getAsid() {
                return asid;
            }

            public void setAsid(String asid) {
                this.asid = asid;
            }

            public String getCo() {
                return co;
            }

            public void setCo(String co) {
                this.co = co;
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

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getNo2() {
                return no2;
            }

            public void setNo2(String no2) {
                this.no2 = no2;
            }

            public String getO3() {
                return o3;
            }

            public void setO3(String o3) {
                this.o3 = o3;
            }

            public String getPm10() {
                return pm10;
            }

            public void setPm10(String pm10) {
                this.pm10 = pm10;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getPub_time() {
                return pub_time;
            }

            public void setPub_time(String pub_time) {
                this.pub_time = pub_time;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getSo2() {
                return so2;
            }

            public void setSo2(String so2) {
                this.so2 = so2;
            }
        }
    }
}
