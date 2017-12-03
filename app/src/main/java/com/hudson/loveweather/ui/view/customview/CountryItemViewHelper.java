package com.hudson.loveweather.ui.view.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather;
import com.hudson.loveweather.db.SelectedCountry;
import com.hudson.loveweather.ui.activity.CountryManagerActivity;
import com.hudson.loveweather.utils.WeatherChooseUtils;
import com.hudson.loveweather.utils.update.UpdateUtils;

import java.util.List;

/**
 * Created by Hudson on 2017/12/3.
 */

public class CountryItemViewHelper {
    private LinearLayout mContentContainer;
    private TextView mCountry,mTemp,mDesc;
    private CheckBox mCheckBox;
    private View mDeleteView;
    private ObjectAnimator mAnimator;
    public View mRoot;

    public CountryItemViewHelper(Context context, SelectedCountry country, CountryManagerActivity.OnItemEventListener listener) {
        initSubView(context,country,listener);
    }

    private void initSubView(Context context, final SelectedCountry country, final CountryManagerActivity.OnItemEventListener listener){
        mRoot = View.inflate(context, R.layout.item_country_manager,null);
        mCountry = (TextView) mRoot.findViewById(R.id.tv_country);
        mCountry.setText(WeatherChooseUtils.clipLocationInfo(country.getCityName(),country.getCountryName()));
        mTemp = (TextView) mRoot.findViewById(R.id.tv_temp);
        mDesc = (TextView) mRoot.findViewById(R.id.tv_desc);
        Weather weather = UpdateUtils.getInstance().getWeatherInstance(country.getWeatherJson());
        if(weather!=null){
            List<Weather.HeWeatherBean> heWeather = weather.getHeWeather();
            if(heWeather!=null&&heWeather.size()>0){
                Weather.HeWeatherBean heWeatherBean = heWeather.get(0);
                Weather.HeWeatherBean.NowBean now = heWeatherBean.getNow();
                if(now!=null){
                    mTemp.setText(now.getTmp()+"℃");
                    mDesc.setText(now.getCond().getTxt());
                }
            }
        }
        mCheckBox = (CheckBox) mRoot.findViewById(R.id.cb_delete);
        mCheckBox.setVisibility(View.GONE);
        mContentContainer = (LinearLayout) mRoot.findViewById(R.id.ll_content);
        mDeleteView =  mRoot.findViewById(R.id.iv_delete);
        mDeleteView.post(new Runnable() {
            @Override
            public void run() {
                mMaxScroll = mDeleteView.getWidth();
            }
        });
        mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(listener!=null){
                    listener.onItemDelete(mRoot,country);
                }
            }
        });
        mContentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null&&!mCaught){
                    listener.onItemClick(country);
                }
            }
        });
        watchTouchEvent();
    }

    private int mLastX;
    private int mMaxScroll;
    private boolean mCaught;
    private void watchTouchEvent(){
        mContentContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mAnimator!=null&&mAnimator.isRunning()){
                    return false;
                }
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mCaught = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCaught = true;
                        int scrolledX = mContentContainer.getScrollX();
                        int scrollX = mLastX - (int) event.getX();
                        if(scrolledX <mMaxScroll){//向左滑动最大限制
                            scrollX = ((scrollX+scrolledX)>mMaxScroll)?(mMaxScroll-scrolledX):scrollX;
                            mContentContainer.scrollTo(scrollX + scrolledX,0);
                        }else if(scrolledX == mMaxScroll&&scrollX<0){
                            mContentContainer.scrollBy(scrollX,0);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        int start = mContentContainer.getScrollX();
                        int end;
                        if(start >mMaxScroll/2){
                            end = mMaxScroll;
                        }else{
                            end = 0;
                        }
                        animateEnd(mContentContainer,start,end);
                        break;
                    default:
                        break;
                }
                mLastX = (int) event.getX();
                return false;
            }
        });
    }

    public void showCheckBox(){
        mCheckBox.setVisibility(View.VISIBLE);
    }

    public void hideCheckBox(){
        mCheckBox.setVisibility(View.GONE);
    }

    public boolean isSelected(){
        return mCheckBox.isSelected();
    }


    private void animateEnd(View view,int startX,int endX){
        mAnimator = ObjectAnimator.ofInt(view,"scrollX",startX,endX);
        mAnimator.setDuration(300);
        mAnimator.start();
    }


}
