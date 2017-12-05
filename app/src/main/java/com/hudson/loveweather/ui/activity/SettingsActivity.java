package com.hudson.loveweather.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hudson.loveweather.R;
import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.ui.view.customview.SelectProgressBar;
import com.hudson.loveweather.utils.SharedPreferenceUtils;

import static com.hudson.loveweather.service.ScheduledTaskService.TYPE_UPDATE_PIC;
import static com.hudson.loveweather.service.ScheduledTaskService.TYPE_UPDATE_WEATHER;
import static com.hudson.loveweather.utils.AlarmClockUtils.scheduleTask;

public class SettingsActivity extends BaseSubActivity {
    private SelectProgressBar mWeatherUpdateSelector,mPicUpdateSelector;
    private SharedPreferenceUtils mSharedPreferenceUtils;
    private TextView mWarningTips;
    private ToggleButton mUpdatePicWifiOnly,mShowNotification;

    @Override
    public View setContent() {
        View root = View.inflate(this, R.layout.activity_settings,null);
        mWarningTips = (TextView) root.findViewById(R.id.tv_warning_tips);
        mSharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        mWeatherUpdateSelector = (SelectProgressBar) root.findViewById(R.id.spb_weather_update);
        mPicUpdateSelector = (SelectProgressBar) root.findViewById(R.id.spb_pic_update);
        mUpdatePicWifiOnly = (ToggleButton) root.findViewById(R.id.tb_wifi_only);
        mUpdatePicWifiOnly.setChecked(mSharedPreferenceUtils.getOnlyWifiAccessNetUpdatePic());
        root.findViewById(R.id.ll_wifi_only).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !mUpdatePicWifiOnly.isChecked();
                mSharedPreferenceUtils.saveOnlyWifiAccessNetUpdatePic(checked);
                mUpdatePicWifiOnly.setChecked(checked);
            }
        });
        mShowNotification = (ToggleButton) root.findViewById(R.id.tb_show_notification);
        mShowNotification.setChecked(mSharedPreferenceUtils.isShowNotification());
        root.findViewById(R.id.ll_show_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !mShowNotification.isChecked();
                mSharedPreferenceUtils.saveShowNotification(checked);
                mShowNotification.setChecked(checked);
                Intent intent = new Intent(SettingsActivity.this, ScheduledTaskService.class);
                if(checked){
                    intent.putExtra("type",ScheduledTaskService.TYPE_SHOW_NOTIFICATION);
                }else{
                    intent.putExtra("type",ScheduledTaskService.TYPE_CANCEL_NOTIFICATION);
                }
                startService(intent);
            }
        });
        return root;
    }

    @Override
    public void init() {
        mWeatherUpdateSelector.setSelectedValue(mSharedPreferenceUtils.getUpdateWeatherTriggerTime());
        mWeatherUpdateSelector.setOnValueSelectedListener(new SelectProgressBar.OnValueSelectedListener() {
            @Override
            public void onValueSelected(int selectedValue) {
                mSharedPreferenceUtils.saveUpdateWeatherTriggerTime(selectedValue);
                scheduleTask(TYPE_UPDATE_WEATHER,selectedValue*60*60*1000);
            }
        });
        mPicUpdateSelector.setSelectedValue(mSharedPreferenceUtils.getUpdateBackgroundPicTriggerTime());
        mPicUpdateSelector.setWarningValue(mPicUpdateSelector.getMinValue(),3);
        checkWarningState();
        mPicUpdateSelector.setOnValueSelectedListener(new SelectProgressBar.OnValueSelectedListener() {
            @Override
            public void onValueSelected(int selectedValue) {
                checkWarningState();
                mSharedPreferenceUtils.saveUpdateBackgroundPicTriggerTime(selectedValue);
                scheduleTask(TYPE_UPDATE_PIC,selectedValue*60*1000);
            }
        });
    }

    private void checkWarningState() {
        if(mPicUpdateSelector.isWarningState()){
            mWarningTips.setVisibility(View.VISIBLE);
        }else{
            mWarningTips.setVisibility(View.GONE);
        }
    }

    @Override
    public String getActivityTitle() {
        return "设置";
    }

    @Override
    public void recycle() {

    }
}
