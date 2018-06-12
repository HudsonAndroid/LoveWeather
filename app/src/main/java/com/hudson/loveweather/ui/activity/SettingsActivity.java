package com.hudson.loveweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hudson.loveweather.R;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.global.ConstantsVoiceMap;
import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.ui.dialog.SelectDialog;
import com.hudson.loveweather.ui.dialog.params.ParamsRunnable;
import com.hudson.loveweather.ui.dialog.params.SelectParams;
import com.hudson.loveweather.ui.view.customview.SelectProgressBar;
import com.hudson.loveweather.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import static com.hudson.loveweather.global.Constants.CUSTOM_CATEGORY;
import static com.hudson.loveweather.service.ScheduledTaskService.TYPE_UPDATE_PIC;
import static com.hudson.loveweather.service.ScheduledTaskService.TYPE_UPDATE_WEATHER;
import static com.hudson.loveweather.utils.AlarmClockUtils.scheduleTask;

public class SettingsActivity extends BaseSubActivity {
    private SelectProgressBar mWeatherUpdateSelector,mPicUpdateSelector;
    private SharedPreferenceUtils mInstance;
    private TextView mWarningTips,mBackgroundType,mVoiceReader;
    private ToggleButton mUpdatePicWifiOnly,mShowNotification;
    private boolean mIsBackgroundPicCategoryChanged = false;

    @Override
    public View setContent() {
        View root = View.inflate(this, R.layout.activity_settings,null);
        mWarningTips = (TextView) root.findViewById(R.id.tv_warning_tips);
        mInstance = SharedPreferenceUtils.getInstance();
        mWeatherUpdateSelector = (SelectProgressBar) root.findViewById(R.id.spb_weather_update);
        mPicUpdateSelector = (SelectProgressBar) root.findViewById(R.id.spb_pic_update);
        mUpdatePicWifiOnly = (ToggleButton) root.findViewById(R.id.tb_wifi_only);
        mUpdatePicWifiOnly.setChecked(mInstance.getOnlyWifiAccessNetUpdatePic());
        //注意：这里ToggleButton在响应点击事件之前就会把checked状态切换，所以这个时候是不一样的
        root.findViewById(R.id.ll_wifi_only).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !mUpdatePicWifiOnly.isChecked();
                mInstance.saveOnlyWifiAccessNetUpdatePic(checked);
                mUpdatePicWifiOnly.setChecked(checked);
            }
        });
        mUpdatePicWifiOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInstance.saveOnlyWifiAccessNetUpdatePic(mUpdatePicWifiOnly.isChecked());
            }
        });
        mShowNotification = (ToggleButton) root.findViewById(R.id.tb_show_notification);
        mShowNotification.setChecked(mInstance.isShowNotification());
        root.findViewById(R.id.ll_show_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = !mShowNotification.isChecked();
                mInstance.saveShowNotification(checked);
                Intent intent = new Intent(SettingsActivity.this, ScheduledTaskService.class);
                if(checked){
                    intent.putExtra("type",ScheduledTaskService.TYPE_SHOW_NOTIFICATION);
                }else{
                    intent.putExtra("type",ScheduledTaskService.TYPE_CANCEL_NOTIFICATION);
                }
                startService(intent);
                mShowNotification.setChecked(checked);
            }
        });
        mShowNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = mShowNotification.isChecked();//在响应点击事件之前，已经切换好了
                mInstance.saveShowNotification(checked);
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
        mBackgroundType.setText(mInstance.getBackgroundPicCategory());
        root.findViewById(R.id.ll_background_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < Constants.PIC_CATEGORY.length; i++) {
                    datas.add(Constants.PIC_CATEGORY[i]);
                }
                datas.add(CUSTOM_CATEGORY);
                SelectDialog selectDialogHelper = new SelectDialog(
                        SettingsActivity.this,new SelectParams(datas,
                        mInstance.getBackgroundPicCategory(), new ParamsRunnable() {
                    @Override
                    public void run(Bundle bundle) {
                        String select = bundle.getString("select");
                        if(!TextUtils.isEmpty(select)){
                            if(select.equals(CUSTOM_CATEGORY)){
                                //启动让用户选择图片
                                Intent intent = new Intent(SettingsActivity.this,CustomPicBgListActivity.class);
                                startActivityForResult(intent,0);
                            }else{
                                mInstance.saveBackgroundPicCategory(select);
                                mBackgroundType.setText(select);
                                mIsBackgroundPicCategoryChanged = true;
                            }
                        }
                    }
                }));
                selectDialogHelper.show();
            }
        });
        mVoiceReader = (TextView) root.findViewById(R.id.tv_voice_reader);
        mVoiceReader.setText(ConstantsVoiceMap.getValue(mInstance.getVoiceReader()));
        root.findViewById(R.id.ll_voice_reader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> datas = ConstantsVoiceMap.getVoiceReadList();
                new SelectDialog(
                        SettingsActivity.this,new SelectParams(datas,
                        ConstantsVoiceMap.getValue(mInstance.getVoiceReader()), new ParamsRunnable() {
                    @Override
                    public void run(Bundle bundle) {
                        String select = bundle.getString("select");
                        if(!TextUtils.isEmpty(select)){
                            mInstance.saveVoiceReader(ConstantsVoiceMap.getReaderKey(select));
                            mVoiceReader.setText(select);
                        }
                    }
                })).show();
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
        mWeatherUpdateSelector.setSelectedValue(mInstance.getUpdateWeatherTriggerTime());
        mWeatherUpdateSelector.setOnValueSelectedListener(new SelectProgressBar.OnValueSelectedListener() {
            @Override
            public void onValueSelected(int selectedValue) {
                mInstance.saveUpdateWeatherTriggerTime(selectedValue);
                scheduleTask(TYPE_UPDATE_WEATHER,selectedValue*60*60*1000);
            }
        });
        mPicUpdateSelector.setSelectedValue(mInstance.getUpdateBackgroundPicTriggerTime());
        mPicUpdateSelector.setWarningValue(mPicUpdateSelector.getMinValue(),3);
        checkWarningState();
        mPicUpdateSelector.setOnValueSelectedListener(new SelectProgressBar.OnValueSelectedListener() {
            @Override
            public void onValueSelected(int selectedValue) {
                checkWarningState();
                mInstance.saveUpdateBackgroundPicTriggerTime(selectedValue);
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
                    mBackgroundType.setText(mInstance.getBackgroundPicCategory());
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
        //告诉服务
        Intent service = new Intent(SettingsActivity.this, ScheduledTaskService.class);
        service.putExtra("type",ScheduledTaskService.TYPE_CHANGE_BACKGROUND_CATEGORY);
        startService(service);
    }
}
