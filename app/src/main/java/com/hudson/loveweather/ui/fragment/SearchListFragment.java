package com.hudson.loveweather.ui.fragment;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.hudson.loveweather.R;
import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.ui.recyclerview.SearchListAdapter;
import com.hudson.loveweather.utils.SearchHistoryUtils;

import java.util.List;

/**
 * Created by Hudson on 2017/12/1.
 */

public class SearchListFragment extends BaseFragment implements SearchListAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private SearchListAdapter mAdapter;
    private View mEmptyResultView;

    @Override
    public View initView(LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.fragment_search_list,null);
        mEmptyResultView = root.findViewById(R.id.tv_empty_tips);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_history);
        return root;
    }

    @Override
    public void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SearchListAdapter(mActivity);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void refreshFragment(){
        List<Country> searchResults = mActivity.getSearchResults();
        if(searchResults == null|(searchResults != null && searchResults.size() == 0)){
            mEmptyResultView.setVisibility(View.VISIBLE);
        }else{
            mEmptyResultView.setVisibility(View.GONE);
        }
        mAdapter.setDatas(searchResults, mActivity.getInputStr());
    }

    @Override
    public void onItemClick(@Nullable Country country) {
        if(country!=null){//点击了一项
            SearchHistoryUtils instance = SearchHistoryUtils.getInstance();
            instance.addHistory(country.getCountryName());//作为历史记录存入
            instance.commit();
            mActivity.toggleSelectedCountry(country);
        }
    }

}
