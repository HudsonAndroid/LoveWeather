package com.hudson.loveweather.ui.view.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudson.loveweather.R;

/**
 * Created by Hudson on 2017/12/9.
 */

public class ItemSuggestionViewHelper {
    private TextView mSuggestionTitle,mSummary,mDesc;
    public View mRoot;

    public ItemSuggestionViewHelper(Context context, ViewGroup parent){
        mRoot = LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent,false);
        mSuggestionTitle = (TextView) mRoot.findViewById(R.id.tv_suggestion_name);
        mSummary = (TextView) mRoot.findViewById(R.id.tv_summary);
        mDesc = (TextView) mRoot.findViewById(R.id.tv_desc);
    }

    public void refreshView(String title,String summary,String desc){
        mSuggestionTitle.setText(title);
        mSummary.setText(summary);
        mDesc.setText(desc);
    }

}
