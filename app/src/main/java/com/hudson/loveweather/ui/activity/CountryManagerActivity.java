package com.hudson.loveweather.ui.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hudson.loveweather.R;
import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.db.DatabaseUtils;
import com.hudson.loveweather.db.SelectedCountry;
import com.hudson.loveweather.ui.view.customview.CountryItemViewHelper;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.WeatherChooseUtils;

import java.util.ArrayList;
import java.util.List;

public class CountryManagerActivity extends BaseSubActivity implements View.OnClickListener {
    private LinearLayout mCountryContainer;
    private ArrayList<CountryItemViewHelper> mHelpers;
    private boolean isCheckBoxShowing = false;

    @Override
    public View setContent() {
        View root = View.inflate(this, R.layout.content_activity_country_manager, null);
        mCountryContainer = (LinearLayout) root.findViewById(R.id.ll_country_container);
        root.findViewById(R.id.iv_add_country).setOnClickListener(this);
        return root;
    }

    @Override
    public void init() {
        mHelpers = new ArrayList<>();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        List<SelectedCountry> countries = DatabaseUtils.queryAllSelectedCountry();
        View.inflate(this,R.layout.line_main,mCountryContainer);
        CountryItemViewHelper countryItemViewHelper;
        for (int i = 0; i < countries.size(); i++) {
            countryItemViewHelper = new CountryItemViewHelper(this,
                    countries.get(i), mOnItemEventListener);
            mCountryContainer.addView(countryItemViewHelper.mRoot,layoutParams);
            mHelpers.add(countryItemViewHelper);
            View.inflate(this,R.layout.line_main,mCountryContainer);
        }

        mCountryContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtils.showToast("长按 了");
                notifyShowCheckBox();
                return true;
            }
        });
    }

    @Override
    public String getActivityTitle() {
        return "城市管理";
    }

    @Override
    public void recycle() {

    }

    private void notifyShowCheckBox(){
        if(!isCheckBoxShowing){
            for (CountryItemViewHelper helper : mHelpers) {
                helper.showCheckBox();
            }
            isCheckBoxShowing = true;
        }
    }

    /**
     *
     * @return 事件是否处理了
     */
    private boolean notifyHideCheckBox(){
        if(isCheckBoxShowing){
            for (CountryItemViewHelper helper : mHelpers) {
                helper.hideCheckBox();
            }
            isCheckBoxShowing = false;
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(!notifyHideCheckBox()){
            super.onBackPressed();
        }
    }

    private OnItemEventListener mOnItemEventListener = new OnItemEventListener() {
        @Override
        public void onItemClick(SelectedCountry country) {
            Country countryInstance = new Country();
            countryInstance.setCityName(country.getCityName());
            countryInstance.setCountryName(country.getCountryName());
            countryInstance.setWeatherId(country.getWeatherId());
            //需要注意，SelectedCountry我们没有把provinceName属性加入
            WeatherChooseUtils.getInstance().toggleSelectedCountry(
                    CountryManagerActivity.this,countryInstance);
        }

        @Override
        public void onItemDelete(View root,SelectedCountry country) {
            mCountryContainer.removeView(root);
            DatabaseUtils.removeSelectedCountry(country);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_country:
                startActivity(new Intent(CountryManagerActivity.this,SearchActivity.class));
                finish();
                break;
        }
    }

    public interface OnItemEventListener{
        void onItemClick(SelectedCountry country);
        void onItemDelete(View root,SelectedCountry country);
    }
}
