package com.hudson.weathertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private AirQualityView mAirQualityView;

    private int level = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAirQualityView = (AirQualityView) this.findViewById(R.id.aqv);
    }

    public void change(View v){
        mAirQualityView.setCurLevel(++level);
    }
}
