package com.hudson.loveweather.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Hudson on 2017/12/2.
 * 将历史记录用一个字符串保存在SharedPreference中
 */

public class SearchHistoryUtils implements Iterator<String>{
    private static final int MAX_COUNT = 10;//最多10个记录
    private ArrayList<String> mHistoryList;
    private boolean mIsModify = false;

    private int position;

    @Override
    public boolean hasNext() {
        return !(position>getSize()-1||getHistory(position) == null);
    }


    /**
     * after call one time iterate,you should call resetPosition method,
     * because this class is a singleTon
     */
    @Override
    public String next() {
        return getHistory(position++);
    }

    /**
     * after call one time iterate,you should call this method
     */
    public void resetPosition(){
        position = 0;
    }

    private static class HistoryHelper{
        static final SearchHistoryUtils sInstance = new SearchHistoryUtils();
    }

    public static SearchHistoryUtils getInstance(){
        return HistoryHelper.sInstance;
    }

    private SearchHistoryUtils(){
        if(mHistoryList == null){
            String history = SharedPreferenceUtils.getInstance().getHistory();
            if(!TextUtils.isEmpty(history)){
                mHistoryList = parseHistoryString(history);
            }else{
                mHistoryList = new ArrayList<>();
            }
        }
    }

    private ArrayList<String> parseHistoryString(String history) {
        ArrayList<String> results = new ArrayList<>();
        String[] strArray = history.split("\n");
        int length = strArray.length;
        if(length >0){
            for(int i = 0; i <length; i++) {
                results.add(strArray[i]);
            }
        }
        return results;
    }

    public void addHistory(String history){
        if(!TextUtils.isEmpty(history)){
            if(!mHistoryList.contains(history)){
                if(mHistoryList.size()>=MAX_COUNT){
                    mHistoryList.remove(mHistoryList.size()-1);
                }
            }else{//将该数据移除，后面在加进来，即移动到开头
                mHistoryList.remove(history);
            }
            mHistoryList.add(0,history);
            mIsModify = true;
        }
    }

    public String getHistory(int index){
        if(index>=0&&index<getSize()){
            return mHistoryList.get(index);
        }
        return null;
    }

    public int getSize(){
        return mHistoryList.size();
    }

    /**
     * 请确保在退出前commit
     * 如果是单例，可以在应用退出时再commit
     */
    public void commit(){
        if(mHistoryList.size()>0&&mIsModify){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mHistoryList.size(); i++) {
                sb = sb.append(mHistoryList.get(i));
                if(i != mHistoryList.size() - 1){
                    sb.append("\n");
                }
            }
            SharedPreferenceUtils.getInstance().saveHistory(sb.toString());
        }
    }

}
