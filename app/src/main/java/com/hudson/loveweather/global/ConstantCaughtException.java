package com.hudson.loveweather.global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;

import com.hudson.loveweather.ui.activity.SplashActivity;
import com.hudson.loveweather.ui.activity.WeatherActivity;
import com.hudson.loveweather.ui.dialog.InformationDialogHelper;
import com.hudson.loveweather.ui.dialog.params.InformationParams;
import com.hudson.loveweather.ui.dialog.params.ParamsRunnable;
import com.hudson.loveweather.utils.IOUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hudson on 2017/9/27.
 * 全局异常捕获器
 * 异常出现了之后，主线程就不能在显示UI了
 */

public class ConstantCaughtException implements Thread.UncaughtExceptionHandler {

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static ConstantCaughtException INSTANCE = new ConstantCaughtException();
    //程序的Context对象
    private LoveWeatherApplication mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /** 保证只有一个CrashHandler实例 */
    private ConstantCaughtException() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static ConstantCaughtException getInstance() {
        return INSTANCE;
    }

    private volatile boolean mWaitSure = true;

    /**
     * 初始化
     *
     * @param context
     */
    public void init(LoveWeatherApplication context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }else{//收集完成信息，请求重启app对话框（对话框无法正常显示）
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Activity topActivity = LoveWeatherApplication.getActivity(WeatherActivity.class);
                    InformationDialogHelper informationDialogHelper = new InformationDialogHelper(
                            topActivity,
                            new InformationParams("应用出现异常需要重新启动，很抱歉给您带来的不便，如果您想要帮助完善本应用，您可以把应用文件目录下的log文件夹发送至开发者邮箱，非常感谢您的支持！",
                                    new ParamsRunnable() {
                                        @Override
                                        public void run(Bundle bundle) {
                                            mWaitSure = false;
                                        }
                                    },null));
                    LogUtils.e("显示对话框了？？？");
                    informationDialogHelper.setCancelable(false);
                    informationDialogHelper.show();
                    Looper.loop();
                }
            }.start();

//            Activity topActivity = LoveWeatherApplication.getActivity(WeatherActivity.class);
//            InformationDialogHelper informationDialogHelper = new InformationDialogHelper(
//                    topActivity,
//                    new InformationParams("应用出现异常需要重新启动，很抱歉给您带来的不便，如果您想要帮助完善本应用，您可以把应用文件目录下的log文件夹发送至开发者邮箱，非常感谢您的支持！",
//                            new ParamsRunnable() {
//                                @Override
//                                public void run(Bundle bundle) {
//
//                                }
//                            },null));
//            LogUtils.e("显示对话框了？？？");
//            informationDialogHelper.show();
            while(mWaitSure);//等待确认
            //一旦主线程走到这里就没响应了，所以我们需要一个死循环等待子线程对话框的回调
            //直接在主线程中没法显示对话框，原因是对话框虽然是阻塞式的，但是show之后主线程已经死了，所以对话框
            //也就没法显示了
            restartApp();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        LogUtils.e("收集设备信息");
        collectDeviceInfo(mContext);
        //保存日志文件
        LogUtils.e("保存日志文件");
        saveCrashInfo2File(ex);
        return true;
    }


    private void restartApp(){
        //重启app（重新打开应用的splash页面），注意打开的页面不能在application的activity栈中
        Intent intent = new Intent(mContext,SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        //退出程序
        LoveWeatherApplication.exitApp();
    }

    /**
     * 收集设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("an error occurred when collect package info");
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
//                LogUtils.e(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                LogUtils.e("an error occurred when collect crash info");
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return  返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        LogUtils.e("异常了");
//        ex.printStackTrace();
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        LogUtils.e(sb.toString());//异常的结果sb
        FileOutputStream fos = null;
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String filePath = AppStorageUtils.getCaughtLogPath()+"/crash-" + time + "-" + timestamp + ".log";
            fos = new FileOutputStream(filePath);
            fos.write(sb.toString().getBytes());
            return filePath;
        } catch (Exception e) {
            LogUtils.e("an error occurred while writing file...");
        } finally {
            IOUtils.close(fos);
        }
        return null;
    }
}

