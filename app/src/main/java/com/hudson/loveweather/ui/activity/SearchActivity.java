package com.hudson.loveweather.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.hudson.loveweather.R;
import com.hudson.loveweather.ui.fragment.SearchDefaultFragment;
import com.hudson.loveweather.utils.log.LogUtils;

public class SearchActivity extends BaseSubActivity implements View.OnClickListener, TextWatcher {
    private EditText mInput;


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
        toggleFragment();
    }

    @Override
    public String getActivityTitle() {
        return "添加城市";
    }

    @Override
    public void recycle() {

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

    private void toggleFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content,new SearchDefaultFragment());
        transaction.commit();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        LogUtils.e("结果"+s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
