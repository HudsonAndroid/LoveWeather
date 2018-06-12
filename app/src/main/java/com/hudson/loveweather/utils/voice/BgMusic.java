package com.hudson.loveweather.utils.voice;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.hudson.loveweather.utils.UIUtils;

import java.io.IOException;

/**
 * 背景音乐控制器
 * Created by Hudson on 2018/5/26.
 */

public class BgMusic {
    private MediaPlayer mPlayer;
    private String mMusicPath;

    static class BgMusicHelper{
        static final BgMusic sInstance = new BgMusic();
    }

    private BgMusic(){
        mPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor descriptor = UIUtils.getContext().getAssets().openFd("yuzhouchangwan.mp3");
            mPlayer.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(){
        try {
            mPlayer.setDataSource(mMusicPath);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放，必须在prepare完成之后
     */
    public void play(){
        mPlayer.start();
    }

    public void pause(){
        if(mPlayer.isPlaying()){
            mPlayer.pause();
        }
    }

    public void stop(){
        mPlayer.pause();
        mPlayer.seekTo(0);
    }

    public static BgMusic getInstance(){
        return BgMusicHelper.sInstance;
    }

    public void setMusicPath(String musicPath) {
        mMusicPath = musicPath;
    }
}
