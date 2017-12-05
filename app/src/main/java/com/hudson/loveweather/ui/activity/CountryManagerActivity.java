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
import com.hudson.loveweather.utils.UIUtils;
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
        mExtendTextView.setVisibility(View.VISIBLE);
        mExtendTextView.setText("编辑");
        mExtendTextView.setOnClickListener(this);
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
        }
    }

    @Override
    public String getActivityTitle() {
        return "城市管理";
    }

    @Override
    public void recycle() {
        if(mHelpers!=null){
            mHelpers.removeAll(mHelpers);
            mHelpers = null;
        }
    }

    private void notifyShowCheckBox(){
        if(!isCheckBoxShowing){
            for (CountryItemViewHelper helper : mHelpers) {
                helper.showCheckBox();
            }
            mExtendTextView.setText("删除");
            mExtendTextView.setTextColor(UIUtils.getColor(R.color.red));
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
            mExtendTextView.setText("编辑");
            mExtendTextView.setTextColor(UIUtils.getColor(R.color.white));
            isCheckBoxShowing = false;
            return true;
        }
        return false;
    }

    private void deleteSelectedItem(){
        CountryItemViewHelper helper;
        for (int i = 0; i < mHelpers.size();) {
            helper = mHelpers.get(i);
            boolean selected = helper.isSelected();
            if(selected){
                mCountryContainer.removeView(helper.mRoot);
                helper.deleteFromDataBase();
                mHelpers.remove(helper);//后面的元素会往前面移，所以不需要i自增
            }else{
                i++;
            }
        }
        notifyHideCheckBox();//hide checkbox
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
        public void onItemDelete(CountryItemViewHelper helper,View root,SelectedCountry country) {
            helper.deleteFromDataBase();
            mHelpers.remove(helper);
            mCountryContainer.removeView(root);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_country:
                startActivity(new Intent(CountryManagerActivity.this,SearchActivity.class));
                break;
            case R.id.tv_extend:
                if(isCheckBoxShowing){//删除
                    deleteSelectedItem();
                }else{//选择
                    notifyShowCheckBox();
                }
                break;
        }
    }

    public interface OnItemEventListener{
        void onItemClick(SelectedCountry country);
        void onItemDelete(CountryItemViewHelper helper,View root,SelectedCountry country);
    }
}
