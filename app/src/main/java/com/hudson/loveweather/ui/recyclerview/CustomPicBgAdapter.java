package com.hudson.loveweather.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hudson.loveweather.R;
import com.hudson.loveweather.utils.log.LogUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hudson on 2017/12/11.
 */

public class CustomPicBgAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_ADD = 1;
    private ArrayList<File> mDatas;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private boolean isSelectStatus = false;
    private boolean isDeleteClick = false;

    public CustomPicBgAdapter(Context context){
        mContext =context;
        mLayoutInflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    public void setData(ArrayList<File> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }


    public void setFlags(boolean isSelectStatus,boolean isDeleteClick){
        this.isSelectStatus = isSelectStatus;
        this.isDeleteClick = isDeleteClick;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_ADD:
                return new CustomBgAddViewHolder(mLayoutInflater.inflate(R.layout.item_custom_bg_add,parent,false));
            case TYPE_NORMAL:
                return new CustomBgViewHolder(mLayoutInflater.inflate(R.layout.item_custom_bg,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_ADD:

                break;
            case TYPE_NORMAL:
                holder.refreshView(mDatas.get(position));
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDatas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position!=mDatas.size()){
            return TYPE_NORMAL;
        }else{
            return TYPE_ADD;
        }
    }

    public void setItemListener(ItemListener itemListener) {
        mItemListener = itemListener;
    }
    private ItemListener mItemListener;
    public interface ItemListener{
        void onItemClick(File data,int position);
        void onItemDelete(File data,int position);
        void onAddClick();
    }



    class CustomBgViewHolder extends BaseViewHolder<File> {
        private CheckBox mCheckBox;
        private ImageView mImageView;
        private File data;

         CustomBgViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cv_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemListener!=null){
                        mItemListener.onItemClick(data,getLayoutPosition());
                    }
                    if(isSelectStatus){
                        mCheckBox.setChecked(!mCheckBox.isChecked());
                    }
                }
            });
            mImageView = (ImageView) itemView.findViewById(R.id.iv_bg);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cb_delete);
        }

        @Override
        public void refreshView(File data, Object... objects) {
            this.data = data;
            // 这个地方我们加载的不是网络图片，所以不会出现图片错位问题,
            // 但是glide每次加载图片时都有一个渐变过程，由于我们recyclerView的重用机制
            // 导致每次虽然图片都是同一个位置使用，但是glide只要回调了方法，就会出现一次渐变过程
            // 因此需要防止每次都去加载
            String tag = data.getAbsolutePath();
            Object tag1 = mCheckBox.getTag();
            if(tag1 ==null||!tag.equals(tag1)){
                // 禁用glide缓存策略，否则glide会自动使用缓存的图片显示
                Glide.with(mContext).load(data).skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.default_loading).into(mImageView);
                mCheckBox.setTag(tag);
            }
            if(isDeleteClick&&mCheckBox.isChecked()){//点击了删除
                if(mItemListener!=null){
                    mItemListener.onItemDelete(data,getLayoutPosition());
                }
            }
            if(isSelectStatus){
                mCheckBox.setVisibility(View.VISIBLE);
            }else{
                mCheckBox.setChecked(false);
                mCheckBox.setVisibility(View.GONE);
            }
        }
    }

    class CustomBgAddViewHolder extends BaseViewHolder {

        CustomBgAddViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemListener!=null){
                        mItemListener.onAddClick();
                    }
                }
            });
        }

        @Override
        public void refreshView(Object data, Object... objects) {

        }
    }
}
