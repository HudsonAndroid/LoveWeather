package com.hudson.loveweather.utils.voice;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * Created by Hudson on 2017/9/12.
 */

public class VoiceSpeechUtils {

    private static VoiceSpeechUtils instance;
    public SpeechSynthesizer mTts;

    /**
     * 单例模式
     * @return 单例对象
     */
    public static VoiceSpeechUtils getInstance(){
        synchronized (VoiceSpeechUtils.class) {
            if(instance==null){
                instance = new VoiceSpeechUtils();
            }
        }
        return instance;
    }

    /**
     * 初始化引擎
     * @param context
     */
    public void initEngine(Context context){
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"=584a312a");
    }

    /**
     * 开始听用户说话
     * @param context
     * @param listener
     */
    public void startListenUserVoice(Context context,RecognizerDialogListener listener){
        // 1.创建RecognizerDialog对象
        RecognizerDialog dialog = new RecognizerDialog(context, null);
        // 2.设置accent、language等参数
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 3.设置回调接口
        dialog.setListener(listener);
        // 4.显示dialog，接收语音输入
        dialog.show();
    }

    /**
     * 开始朗读内容
     * @param context
     * @param text
     */
    public void startSpeakText(Context context, final String text, final SpeakListener listener){
        // 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer
                .createSynthesizer(context, null);
        // 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        // 设置发音人（更多在线发音人，用户可参见 附录12.2
        String reader = SharedPreferenceUtils.getInstance().getVoiceReader();
        if(TextUtils.isEmpty(reader)){
            reader = "nannan";
        }
        mTts.setParameter(SpeechConstant.VOICE_NAME, reader); // 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "40");// 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
        // 设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        // 保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        // 仅支持保存为pcm和wav格式，如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, AppStorageUtils.getCustomVoiceCachePath()+"/iflytek.pcm");
        // 3.开始合成
        mTts.startSpeaking(text, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
                if(listener!=null){
                    listener.onStart();
                }
            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {
                LogUtils.e("内容是"+text);
            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if(listener!=null){
                    listener.onCompleted(speechError);
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    public void pause(){
        if(mTts.isSpeaking()){
            mTts.pauseSpeaking();
        }
    }

    public void continueSpeak(){
        mTts.resumeSpeaking();
    }

    public interface SpeakListener{
        void onCompleted(SpeechError speechError);
        void onStart();
    }

    public void stop(){
        mTts.stopSpeaking();
    }


}
