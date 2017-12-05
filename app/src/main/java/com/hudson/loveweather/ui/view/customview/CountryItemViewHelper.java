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
import com.hudson.loveweather.db.DatabaseUtils;
import com.hudson.loveweather.db.SelectedCountry;
import com.hudson.loveweather.ui.activity.CountryManagerActivity;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.UIUtils;
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
    private boolean mCanDelete = true;//是否可以删除
    private SelectedCountry mSelectedCountry;

    public CountryItemViewHelper(Context context, SelectedCountry country, CountryManagerActivity.OnItemEventListener listener) {
        initSubView(context,country,listener);
    }

    private void initSubView(Context context, final SelectedCountry country, final CountryManagerActivity.OnItemEventListener listener){
        mRoot = View.inflate(context, R.layout.item_country_manager,null);
        mCountry = (TextView) mRoot.findViewById(R.id.tv_country);
        mCountry.setText(WeatherChooseUtils.clipLocationInfo(country.getCityName(),country.getCountryName()));
        mTemp = (TextView) mRoot.findViewById(R.id.tv_temp);
        mDesc = (TextView) mRoot.findViewById(R.id.tv_desc);
        mSelectedCountry = country;
        //如果是定位的城市，那么不允许删除
        mCanDelete = !SharedPreferenceUtils.getInstance().getLastLocationWeatherId().equals(country.getWeatherId());
        if(!mCanDelete){//是定位的城市
            //注意：给textView设置drawable时，需要指定bounds，这里使用的方法会自动使用drawable内部的bounds
            mCountry.setCompoundDrawablesWithIntrinsicBounds(UIUtils.getDrawable(R.drawable.location),
                    null,null,null);
        }
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
                    listener.onItemDelete(CountryItemViewHelper.this,mRoot,country);
                }
            }
        });
        mContentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckBox.getVisibility() == View.VISIBLE&&!mCaught){//如果是选择时，那么应该由checkBox接管
                    mCheckBox.setChecked(!mCheckBox.isChecked());
                }else{
                    if(listener!=null&&!mCaught){
                        listener.onItemClick(country);
                    }
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
                        mLastX = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCaught = true;
                        int scrolledX = mContentContainer.getScrollX();
                        int scrollX = mLastX - (int) event.getX();
                        if(scrolledX <mMaxScroll){//向左滑动最大限制
                            scrollX = ((scrollX+scrolledX)>mMaxScroll)?(mMaxScroll-scrolledX):scrollX;
                            if(scrolledX == 0&&scrollX>0&&!mCanDelete){
                                //如果当前Item不可以被delete,而当前scroll是0，同时又向左拉（即打开delete）
                                //我们是不允许的
                            }else{
                                mContentContainer.scrollTo(scrollX + scrolledX,0);
                            }
                        }else if(scrolledX == mMaxScroll&&scrollX<0){
                            //delete被拉出来了
                            mContentContainer.scrollBy(scrollX,0);
                        }
                        mLastX = (int) event.getX();
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
                        mLastX = 0;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public void showCheckBox(){
        if(mCanDelete){
            mCheckBox.setVisibility(View.VISIBLE);
        }
    }

    public void hideCheckBox(){
        mCheckBox.setVisibility(View.GONE);
    }

    public boolean isSelected(){
        return mCheckBox.isChecked();//注意，不要弄成mCheckBox.isSelected了
    }


    private void animateEnd(View view,int startX,int endX){
        mAnimator = ObjectAnimator.ofInt(view,"scrollX",startX,endX);
        mAnimator.setDuration(300);
        mAnimator.start();
    }

    /**
     * 从数据库中删除本记录
     * @return
     */
    public int deleteFromDataBase(){
        return DatabaseUtils.removeSelectedCountry(mSelectedCountry);
    }


}
