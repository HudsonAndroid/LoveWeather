package com.hudson.loveweather.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.AirQualityBean;
import com.hudson.loveweather.ui.dialog.SelectDialog;
import com.hudson.loveweather.ui.dialog.params.ParamsRunnable;
import com.hudson.loveweather.ui.dialog.params.SelectParams;
import com.hudson.loveweather.ui.view.customview.AirQualityView;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.WeatherMsgFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hudson on 2018/5/27.
 */

public class AirQualityActivity extends BaseSubActivity {
    private AirQualityBean mAirQualityBean;
    private AirQualityView mAirQualityView;
    private AirQualityView mAirCo,mAirNo2,mAirO3,mAirPm10,mAirPm25,mAirSo2;
    private TextView mPublicTime;
    private TextView mLocate;
    public List<AirQualityBean.HeWeather6Bean.AirNowStationBean> mAir_now_station;
    public SharedPreferenceUtils mInstance;

    @Override
    public View setContent() {
        View root = LayoutInflater.from(this).inflate(R.layout.activity_air_quality, null);
        mInstance = SharedPreferenceUtils.getInstance();
        mInstance.saveAirSelectLocation(null);
        mAirQualityView = (AirQualityView) root.findViewById(R.id.aqv_progress);
        mLocate = (TextView) root.findViewById(R.id.tv_city);
        mPublicTime = (TextView) root.findViewById(R.id.tv_public_time);
        mAirCo = (AirQualityView) root.findViewById(R.id.aqv_co);
        mAirNo2 = (AirQualityView) root.findViewById(R.id.aqv_no2);
        mAirO3 = (AirQualityView) root.findViewById(R.id.aqv_o3);
        mAirPm10 = (AirQualityView) root.findViewById(R.id.aqv_pm10);
        mAirPm25 = (AirQualityView) root.findViewById(R.id.aqv_pm25);
        mAirSo2 = (AirQualityView) root.findViewById(R.id.aqv_so2);
        root.findViewById(R.id.tv_choose_district).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAir_now_station!=null){
                    SelectParams params = new SelectParams(generateLocations(), mInstance.getAirSelectLocation(), new ParamsRunnable() {
                        @Override
                        public void run(Bundle bundle) {
                            int position = bundle.getInt("position", -1);
                            if(position!=-1){
                                AirQualityBean.HeWeather6Bean.AirNowStationBean airStation = mAir_now_station.get(position);
                                updateView(airStation.getAir_sta(),airStation.getPub_time(),airStation.getQlty(),
                                        airStation.getAqi(),airStation.getCo(),airStation.getNo2(),airStation.getSo2(),
                                        airStation.getO3(),airStation.getPm10(),airStation.getPm25());
                                mInstance.saveAirSelectLocation(bundle.getString("select",null));
                            }
                        }
                    });
                    new SelectDialog(AirQualityActivity.this, params).show();
                }else{
                    ToastUtils.showToast(R.string.error_air_data_not_exist);
                }
            }
        });
        return root;
    }

    private List<String> generateLocations(){
        List<String> locates = new ArrayList<>();
        for (int i = 0; i < mAir_now_station.size(); i++) {
            locates.add(mAir_now_station.get(i).getAir_sta());
        }
        return locates;
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        if(intent==null||intent.getExtras()==null){
            finish();
            return ;
        }
        mAirQualityBean = intent.getParcelableExtra(AIR_NAME);
        AirQualityBean.HeWeather6Bean heWeather6Bean = mAirQualityBean.getHeWeather6().get(0);
        if(heWeather6Bean!=null){
            mAir_now_station = heWeather6Bean.getAir_now_station();
            final String location = heWeather6Bean.getBasic().getLocation();
            final AirQualityBean.HeWeather6Bean.AirNowCityBean air_now_city = heWeather6Bean.getAir_now_city();
            final String qlty = air_now_city.getQlty();
            final String pub_time = air_now_city.getPub_time();

            final String aqi = air_now_city.getAqi();

            mAirQualityView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mAirQualityView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    String co = air_now_city.getCo();
                    String no2 = air_now_city.getNo2();
                    String so2 = air_now_city.getSo2();
                    String o3 = air_now_city.getO3();
                    String pm10 = air_now_city.getPm10();
                    String pm25 = air_now_city.getPm25();
                    updateView(location,pub_time,qlty,aqi,co,no2,so2,o3,pm10,pm25);
                }
            });
        }
    }

    private void updateView(String locate,String publicTime,String qlty,
                            String aqi, String co,String no2,String so2,
                            String o3, String pm10,String pm25){
        mLocate.setText(locate);
        mPublicTime.setText(publicTime);
        mAirQualityView.setText(aqi +" "+qlty);
        mAirQualityView.refresh(WeatherMsgFactory.getAirQualityColor(qlty),
                WeatherMsgFactory.getAirQualityLevel(qlty));
        mAirCo.setText("CO\n"+ co);
        int coLevel = WeatherMsgFactory.getCoLevel(co);
        mAirCo.refresh(WeatherMsgFactory.getLevelColor(coLevel),coLevel);
        mAirNo2.setText("NO2\n"+ no2);
        int no2Level = WeatherMsgFactory.getNo2Level(no2);
        mAirNo2.refresh(WeatherMsgFactory.getLevelColor(no2Level),no2Level);
        mAirSo2.setText("SO2\n"+ so2);
        int so2Level = WeatherMsgFactory.getSo2Level(so2);
        mAirSo2.refresh(WeatherMsgFactory.getLevelColor(so2Level),so2Level);
        mAirO3.setText("O3\n"+ o3);
        int o3Level = WeatherMsgFactory.getO3Level(o3);
        mAirO3.refresh(WeatherMsgFactory.getLevelColor(o3Level),o3Level);
        mAirPm10.setText("PM10\n"+ pm10);
        int pm10Level = WeatherMsgFactory.getPM10Level(pm10);
        mAirPm10.refresh(WeatherMsgFactory.getLevelColor(pm10Level),pm10Level);
        mAirPm25.setText("PM2.5\n"+ pm25);
        int pm25Level = WeatherMsgFactory.getPM25Level(pm25);
        mAirPm25.refresh(WeatherMsgFactory.getLevelColor(pm25Level),pm25Level);
    }

    @Override
    public String getActivityTitle() {
        return UIUtils.getString(R.string.title_air_quality);
    }

    @Override
    public void recycle() {}

    private static final String AIR_NAME = "air_quality";
    public static void start(Activity from,AirQualityBean bean){
        Intent intent = new Intent(from, AirQualityActivity.class);
        intent.putExtra(AIR_NAME,bean);
        from.startActivity(intent);
    }
}
