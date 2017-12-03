package com.hudson.loveweather.ui.recyclerview;

import android.view.View;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.utils.UIUtils;

/**
 * Created by Hudson on 2017/12/2.
 */

public class SearchViewHolder extends BaseViewHolder<Country> {
    private TextView mProvince,mCity,mCountry;
    private Country mCountryBean;

    public SearchViewHolder(View itemView, final SearchListAdapter.OnItemClickListener listener) {
        super(itemView);
        itemView.findViewById(R.id.ll_item_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(mCountryBean);
                }
            }
        });
        mProvince = (TextView) itemView.findViewById(R.id.tv_province);
        mCity = (TextView) itemView.findViewById(R.id.tv_city);
        mCountry = (TextView) itemView.findViewById(R.id.tv_country);
    }

    @Override
    public void refreshView(Country data,Object... objects) {
        if(data!=null){
            mCountryBean = data;
            String provinceName = data.getProvinceName();
            String cityName = data.getCityName();
            String countryName = data.getCountryName();
            mProvince.setText(provinceName);
            mCity.setText(cityName);
            mCountry.setText(countryName);
            mProvince.setTextColor(UIUtils.getColor(R.color.normal));
            mCity.setTextColor(UIUtils.getColor(R.color.normal));
            mCountry.setTextColor(UIUtils.getColor(R.color.normal));
            String referStr = (String) objects[0];
            if(provinceName.contains(referStr)){
                mProvince.setTextColor(UIUtils.getColor(R.color.white));
            }else if(cityName.contains(referStr)){
                mCity.setTextColor(UIUtils.getColor(R.color.white));
            }else if(countryName.contains(referStr)){
                mCountry.setTextColor(UIUtils.getColor(R.color.white));
            }
        }
    }
}
