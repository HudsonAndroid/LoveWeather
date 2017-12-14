package com.hudson.loveweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hudson.loveweather.R;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.ui.dialog.SelectDialogHelper;
import com.hudson.loveweather.ui.dialog.params.ParamsRunnable;
import com.hudson.loveweather.ui.dialog.params.SelectParams;
import com.hudson.loveweather.ui.view.customview.SelectProgressBar;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.hudson.loveweather.global.Constants.CUSTOM_CATEGORY;
import static com.hudson.loveweather.service.ScheduledTaskService.TYPE_UPDATE_PIC;
import static com.hudson.loveweather.service.ScheduledTaskService.TYPE_UPDATE_WEATHER;
import static com.hudson.loveweather.utils.AlarmClockUtils.scheduleTask;

public class SettingsActivity extends BaseSubActivity {
    private SelectProgressBar mWeatherUpdateSelector,mPicUpdateSelector;
    private SharedPreferenceUtils mSharedPreferenceUtils;
    private TextView mWarningTips,mBackgroundType;
    private ToggleButton mUpdatePicWifiOnly,mShowNotification;
    private boolean mIsBackgroundPicCategoryChanged = false;

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
        mBackgroundType = (TextView) root.findViewById(R.id.tv_background_type);
        mBackgroundType.setText(mSharedPreferenceUtils.getBackgroundPicCategory());
        root.findViewById(R.id.ll_background_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < Constants.PIC_CATEGORY.length; i++) {
                    datas.add(Constants.PIC_CATEGORY[i]);
                }
                datas.add(CUSTOM_CATEGORY);
                SelectDialogHelper selectDialogHelper = new SelectDialogHelper(
                        SettingsActivity.this,new SelectParams(datas,
                        mSharedPreferenceUtils.getBackgroundPicCategory(), new ParamsRunnable() {
                    @Override
                    public void run(Bundle bundle) {
                        String select = bundle.getString("select");
                        if(!TextUtils.isEmpty(select)){
                            if(select.equals(CUSTOM_CATEGORY)){
                                //启动让用户选择图片
                                Intent intent = new Intent(SettingsActivity.this,CustomPicBgListActivity.class);
                                startActivityForResult(intent,0);
                            }else{
                                mSharedPreferenceUtils.saveBackgroundPicCategory(select);
                                mBackgroundType.setText(select);
                                mIsBackgroundPicCategoryChanged = true;
                            }
                        }
                    }
                }));
                selectDialogHelper.show();
            }
        });
        root.findViewById(R.id.ll_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,AboutActivity.class));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    mBackgroundType.setText(mSharedPreferenceUtils.getBackgroundPicCategory());
                    mIsBackgroundPicCategoryChanged = true;
                }
                break;
        }
    }

    @Override
    public String getActivityTitle() {
        return "设置";
    }

    @Override
    public void recycle() {
        if(mIsBackgroundPicCategoryChanged){
            notifyServiceUpdateBackgroundPic();
        }
    }

    private void notifyServiceUpdateBackgroundPic() {
        LogUtils.e("告诉服务了");
        //告诉服务
        Intent service = new Intent(SettingsActivity.this, ScheduledTaskService.class);
        service.putExtra("type",ScheduledTaskService.TYPE_CHANGE_BACKGROUND_CATEGORY);
        startService(service);
    }
}
