package com.hudson.loveweather.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.hudson.loveweather.R;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.ui.recyclerview.CustomPicBgAdapter;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;

import java.io.File;
import java.util.ArrayList;

public class CustomPicBgListActivity extends BaseSubActivity implements CustomPicBgAdapter.ItemListener, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private CustomPicBgAdapter mAdapter;
    private static final int REQUEST_CODE = 0;
    public ArrayList<File> mDatas;
    private ArrayList<File> mWaitingForDeleteFiles;

    @Override
    public View setContent() {
        View root = LayoutInflater.from(this).inflate(R.layout.activity_custom_pic_bg_list,null);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_custom_pic);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2,
                LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CustomPicBgAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemListener(this);
        return root;
    }

    @Override
    public void init() {
        File[] sortFiles = managerImageName();
        mExtendTextView.setVisibility(View.VISIBLE);
        mExtendTextView.setText("编辑");
        mExtendTextView.setOnClickListener(this);
        mDatas = new ArrayList<>();
        for (File file : sortFiles) {
            mDatas.add(file);
        }
        mAdapter.setData(mDatas);
    }

    @Override
    public String getActivityTitle() {
        return "自定义背景图片";
    }

    @Override
    public void onBackPressed() {
        //如果图片集合不为空，那么我们就修改当前的背景主题为自定义的
        if(mDatas!=null&&mDatas.size()>0){
            SharedPreferenceUtils.getInstance().saveBackgroundPicCategory(Constants.CUSTOM_CATEGORY);
        }
        Intent backIntent = new Intent();
        backIntent.putExtra("success",true);
        setResult(RESULT_OK,backIntent);
        finish();
    }

    @Override
    public void recycle() {
        managerImageName();//重新处理一下图片
    }


    /**
     * 从小到大管理图片名
     */
    private File[] managerImageName(){
        String customPicCachePath = AppStorageUtils.getCustomPicCachePath();
        File[] files = new File(customPicCachePath).listFiles();
        File[] sortFiles = new File[files.length];//由于结果并不是按文件名排的，需要重排，文件名从0开始的
        String path = customPicCachePath+"/"+ Constants.PIC_CACHE_NAME;
        //用两个指针完成，i指向目的值，j指向文件实际值
        for (int i = 0,j=0; i < files.length; i++) {
            File curFile = new File(path + i + ".jpg");
            if(curFile.exists()){
                LogUtils.e("文件"+i+"存在"+",,,当前i"+i);
                j++;
            }else{//发现文件不存在，那么只增加j
                while(!new File(path+(++j)+".jpg").exists());
                if(new File(path+j+".jpg").renameTo(curFile)){
                    LogUtils.e("重命名成功"+i);
                }
            }
            sortFiles[i] = curFile;
        }
        return sortFiles;
    }


    @Override
    public void onItemClick(File data, int position) {
    }

    @Override
    public void onItemDelete(final File data, int position) {
        // problem:can not call adapter notifyDataSetChanged method while recyclerView is scrolling
        // or layout ,so we use handler to help us do it after a short time
        //意思就是我们不能够在布局的时候对原数据进行修改来刷新布局
        LogUtils.e("删除"+position);
        if(mWaitingForDeleteFiles ==null){
            mWaitingForDeleteFiles = new ArrayList<>();
        }
        mWaitingForDeleteFiles.add(data);//把要删除的项记下来
        new Thread(){
            @Override
            public void run(){
                if(data.delete()){
                    LogUtils.e("文件删除成功"+data.getAbsolutePath());
                }else{
                    data.delete();
                }
            }
        }.start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mWaitingForDeleteFiles !=null){
                for (int i = 0; i < mWaitingForDeleteFiles.size(); i++) {
                    mDatas.remove(mWaitingForDeleteFiles.get(i));
                }
                LogUtils.e("开始刷新列表");
                mAdapter.notifyDataSetChanged();
                mWaitingForDeleteFiles = null;
            }
        }
    };

    @Override
    public void onAddClick() {
        startActivityForResult(new Intent(this,AddCustomPicBgActivity.class),REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE&&resultCode == RESULT_OK){
            String path = data.getStringExtra("path");
            if(!TextUtils.isEmpty(path)){
                File file = new File(path);
                if(file.exists()){
                    mDatas.add(file);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private boolean isCheckBoxShowing = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_extend:
                if(isCheckBoxShowing){//删除
                    isCheckBoxShowing = false;
                    mAdapter.setFlags(isCheckBoxShowing,true);
                    mExtendTextView.setText("编辑");
                    mExtendTextView.setTextColor(UIUtils.getColor(R.color.white));
                    mHandler.sendEmptyMessageDelayed(0,100);//send message to ask for refresh
                }else{//选择
                    isCheckBoxShowing = true;
                    mAdapter.setFlags(isCheckBoxShowing,false);
                    mExtendTextView.setText("删除");
                    mExtendTextView.setTextColor(UIUtils.getColor(R.color.red));
                }
                break;
        }
    }
}
