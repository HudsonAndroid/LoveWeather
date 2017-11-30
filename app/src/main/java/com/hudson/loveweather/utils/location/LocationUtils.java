package com.hudson.loveweather.utils.location;

import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hudson.loveweather.utils.UIUtils;

/**
 * Created by Hudson on 2017/11/30.
 */

public class LocationUtils {
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    public void startLocation(){
        //声明LocationClient类
        mLocationClient = new LocationClient(UIUtils.getContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");//这里使用的是经纬度坐标(这是百度地图对火星坐标的封装)
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(0);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true
        option.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            int locType = location.getLocType();
            if(locType == BDLocation.TypeServerError||
                    locType == BDLocation.TypeNetWorkException||
                    locType == BDLocation.TypeCriteriaException){
                if(mListener!=null){
                    mListener.onLocationGet(false,null,null,null,null,null);
                }
            }else{
                //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
                //以下只列举部分获取位置描述信息相关的结果
                //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
//            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
                String addr = location.getAddrStr();    //获取详细地址信息
//            String country = location.getCountry();    //获取国家
                String province = location.getProvince();    //获取省份
                String city = location.getCity();    //获取城市
                String district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息
                if(mListener!=null){
                    if(TextUtils.isEmpty(province) || TextUtils.isEmpty(city) || TextUtils.isEmpty(district)){
                        mListener.onLocationGet(false,null,null,null,null,null);
                    }else{
                        mListener.onLocationGet(true,addr,province,city,district,street);
                    }
                }
            }
            mLocationClient.stop();
        }
    }

    private LocationInfoGetListener mListener;
    public void setLocationInfoGetListener(LocationInfoGetListener listener) {
        mListener = listener;
    }

    public interface LocationInfoGetListener{
        /**
         * @param success 成功与否
         * @param addr 详细地址
         * @param province 省
         * @param city 城市
         * @param district 区县
         * @param street 街道
         */
        void onLocationGet(boolean success,String addr,String province,String city,
                           String district,String street);
    }

}
