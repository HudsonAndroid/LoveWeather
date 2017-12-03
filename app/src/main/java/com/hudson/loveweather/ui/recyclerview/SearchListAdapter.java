package com.hudson.loveweather.ui.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hudson.loveweather.R;
import com.hudson.loveweather.db.Country;

import java.util.List;

/**
 * Created by Hudson on 2017/12/2.
 */

public class SearchListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Country> mResults;
    private LayoutInflater mLayoutInflater;
    private String mReferStr;

    public SearchListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<Country> datas, String referStr){
        mReferStr = referStr;
        mResults  = datas;
        if(mResults!=null){
            notifyDataSetChanged();
        }
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(mLayoutInflater.inflate(R.layout.item_search_list,parent,false)
                ,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.refreshView(mResults.get(position),mReferStr);
    }

    @Override
    public int getItemCount() {
        if(mResults == null){
            return 0;
        }
        return mResults.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(@Nullable Country country);
    }
}
