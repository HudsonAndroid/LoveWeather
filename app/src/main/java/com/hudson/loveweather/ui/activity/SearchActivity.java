package com.hudson.loveweather.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.hudson.loveweather.R;
import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.db.DatabaseUtils;
import com.hudson.loveweather.ui.fragment.SearchDefaultFragment;
import com.hudson.loveweather.ui.fragment.SearchListFragment;
import com.hudson.loveweather.utils.WeatherChooseUtils;

import java.util.List;

public class SearchActivity extends BaseSubActivity implements View.OnClickListener, TextWatcher {
    private EditText mInput;
    private List<Country> mSearchResults;
    private String mInputStr;
    private SearchDefaultFragment mSearchDefaultFragment;
    private SearchListFragment mSearchListFragment;

    @Override
    public View setContent() {
        View root = View.inflate(this, R.layout.content_activity_search,null);
        root.findViewById(R.id.iv_clean_text).setOnClickListener(this);
        mInput = (EditText) root.findViewById(R.id.et_input);
        mInput.addTextChangedListener(this);
        return root;
    }

    @Override
    public void init() {
        mSearchDefaultFragment = new SearchDefaultFragment();
        mSearchListFragment = new SearchListFragment();
        initFragment();
    }

    @Override
    public String getActivityTitle() {
        return "添加城市";
    }

    @Override
    public void recycle() {

    }

    public List<Country> getSearchResults(){
        return mSearchResults;
    }

    public String getInputStr(){
        return mInputStr;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_clean_text:
                mInput.setText("");
                break;
//            case :
//
//                break;
            default:
                break;
        }
    }

    private void initFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content,mSearchDefaultFragment);
        //发现系统一个bug，如果在editText文本变化了再动态加入searchListFragment的话，
        //由于后面立刻会通过getActivity来获取数据，但是可能是由于系统对fragment还没有初始化
        //完成，导致getActivity获取的是空值，所以将searchListFragment在一开始就添加进来
        //以避免出现空指针异常。
        transaction.add(R.id.fl_content,mSearchListFragment).hide(mSearchListFragment);
        transaction.commit();
    }

    private void toggleFragment(boolean showList){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(showList){
            if(mSearchListFragment.isHidden()){
                transaction.hide(mSearchDefaultFragment).show(mSearchListFragment).commit();
            }
        }else{
            if(mSearchDefaultFragment.isHidden()){
                transaction.hide(mSearchListFragment).show(mSearchDefaultFragment).commit();
            }
        }
    }

    public void toggleSelectedCountry(Country country){
        WeatherChooseUtils.getInstance().toggleSelectedCountry(this,country);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mInputStr = s.toString().trim();
        boolean showList = !TextUtils.isEmpty(mInputStr);
        if(showList){
            //数据库耗时不长，所以这里就没有用子线程
            mSearchResults = DatabaseUtils.queryCountry(mInputStr);
            mSearchListFragment.refreshFragment();
        }
        toggleFragment(showList);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }



}
