package com.hudson.loveweather.ui.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.BitmapUtils;
import com.hudson.loveweather.utils.StringUtils;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;

import java.io.File;
import java.io.IOException;

public class AddCustomPicBgActivity extends BaseSubActivity {
    private static final int TYPE_TAKE_PHOTO = 0;
    private static final int TYPE_CHOOSE_PHOTO = 1;
    private static int sNextPicIndex = -1;

    private ImageView mImageView;
    private String mNextPicPath;//拍照保存的路径，也是实际确认后保存的路径
//    private String mChoosePicPath;//选择图片方式的图片的路径
    private Bitmap mNextPicBitmap;//bitmap实例，点击确认之后，需要保存的
    private boolean mIsSaving = false;

    static{
        File[] files = new File(AppStorageUtils.getCustomPicCachePath()).listFiles();
        for (int i = 0; i < files.length; i++) {
            int index = StringUtils.decodeFileNameNumber(files[i].getName());
            sNextPicIndex = sNextPicIndex<index?index:sNextPicIndex;
        }
    }


    public void take(View v) {
        startCameraForPicture();
    }

    public void choose(View v) {
        startSystemGalleryForChoosing();
    }

    public void sure(View v){//只有用户走的是这个方法才能算选择图片成功
        if(mNextPicBitmap!=null){
            //把修正过的图片写入本地缓存
            if(!mIsSaving){
                mIsSaving = true;
                new Thread(){
                    @Override
                    public void run(){
                        if(AppStorageUtils.writeFile(
                                mNextPicPath,BitmapUtils.bitmap2InputStream(mNextPicBitmap))){
                            Intent backIntent = new Intent();
                            backIntent.putExtra("path",mNextPicPath);
                            setResult(RESULT_OK,backIntent);
                            finish();
                        }
                    }
                }.start();
            }
        }else{
            ToastUtils.showToast("请先选择或者拍照！");
        }
    }

    private void startCameraForPicture() {
        File nextImage = new File(mNextPicPath);
        if (nextImage.exists()) {
            nextImage.delete();
        }
        try {
            nextImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.hudson.loveweather.fileprovider", nextImage);
        } else {
            imageUri = Uri.fromFile(nextImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TYPE_TAKE_PHOTO);
    }

    private void startSystemGalleryForChoosing() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, TYPE_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TYPE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mNextPicBitmap = BitmapUtils.resizeBitmap(mNextPicPath);
                    mImageView.setImageBitmap(mNextPicBitmap);
                }
                break;
            case TYPE_CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mNextPicBitmap = BitmapUtils.resizeBitmap(parseImagePathUpKitKat(data));
                    mImageView.setImageBitmap(mNextPicBitmap);
                }
                break;
        }
    }

    /**
     * 本质来说，这张图片需要按照规定格式命名
     * 由于recyclerView的tag问题，导致了结果可能复用，所以使用一个静态变量维护名字的变化
     * ，以确保不被复用
     * @return
     */
    private String getNextPicPath() {
        LogUtils.e("下一个是"+(sNextPicIndex+1));
        return new StringBuilder(AppStorageUtils.getCustomPicCachePath()).append("/").append(Constants.PIC_CACHE_NAME)
                .append((++sNextPicIndex))
                .append(".jpg").toString();
    }


    /**
     * 大于19的时候解析方法
     *
     * @param data
     * @return
     */
    private String parseImagePathUpKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            String authority = uri.getAuthority();
            if ("com.android.providers.media.documents".equals(authority)) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(authority)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    /**
     * 低于19的时候解析方法
     *
     * @param data
     * @return
     */
    private String parseImagePathLowKitKat(Intent data) {
        Uri uri = data.getData();
        return getImagePath(uri, null);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onBackPressed() {
        if(!TextUtils.isEmpty(mNextPicPath)){//图片没有确定，所以删除
            File file = new File(mNextPicPath);
            if(file.exists()){
                file.delete();
            }
        }
        super.onBackPressed();
    }

    @Override
    public View setContent() {
        View root = LayoutInflater.from(this).inflate(R.layout.activity_custom_pic_bg, null);
        return root;
    }

    @Override
    public void init() {
        mImageView = (ImageView) findViewById(R.id.iv_content);
        mNextPicPath = getNextPicPath();
    }

    @Override
    public String getActivityTitle() {
        return "添加背景图片";
    }

    @Override
    public void recycle() {

    }
}
