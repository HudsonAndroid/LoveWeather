package com.hudson.loveweather.utils.LogUtils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.hudson.loveweather.utils.LogUtils.Constants.TYPE_DEBUG;
import static com.hudson.loveweather.utils.LogUtils.Constants.TYPE_ERROR;
import static com.hudson.loveweather.utils.LogUtils.Constants.TYPE_INFO;
import static com.hudson.loveweather.utils.LogUtils.Constants.TYPE_VERBOSE;
import static com.hudson.loveweather.utils.LogUtils.Constants.TYPE_WARN;

/**
 * Created by Hudson on 2017/11/25.
 * 真正打印
 */

public class Printer {
    private static LogConfig sLogConfig = LogConfig.getInstance();
    private String mTag = null;

    static class PrinterHelper{
        static Printer sPrinter = new Printer();
    }

    public static Printer getInstance(){
        return PrinterHelper.sPrinter;
    }

    public void setTag(String tag){
        mTag = tag;
    }


    public void printLog(int type,Object object){
        printLog(type,null,object);
    }

    /**
     * 主体方法
     * @param type
     * @param additionalInfo
     * @param args
     */
    public synchronized void printLog(int type,String additionalInfo,Object... args){
        if(!sLogConfig.isEnable()){
            return ;
        }
        if(TextUtils.isEmpty(mTag)){
            mTag = sLogConfig.getTag();
        }
        printSystemLog(type,mTag,additionalInfo + args.toString());
    }

    public void d(String additionalInfo, Object... args) {
        printLog(TYPE_DEBUG, additionalInfo, args);
    }

    public void d(Object object) {
        printLog(TYPE_DEBUG, object);
    }

    public void e(String additionalInfo, Object... args) {
        printLog(TYPE_ERROR, additionalInfo, args);
    }

    public void e(Object object) {
        printLog(TYPE_ERROR, object);
    }

    public void w(String additionalInfo, Object... args) {
        printLog(TYPE_WARN, additionalInfo, args);
    }

    public void w(Object object) {
        printLog(TYPE_WARN, object);
    }

    public void i(String additionalInfo, Object... args) {
        printLog(TYPE_INFO, additionalInfo, args);
    }

    public void i(Object object) {
        printLog(TYPE_INFO, object);
    }

    public void v(String additionalInfo, Object... args) {
        printLog(TYPE_VERBOSE, additionalInfo, args);
    }

    public void v(Object object) {
        printLog(TYPE_VERBOSE, object);
    }

    public void log(String additionalInfo, Object... args){
        printLog(sLogConfig.getLogLevel(),additionalInfo,args);
    }

    public void log(Object object){
        printLog(sLogConfig.getLogLevel(),object);
    }

    /**
     * @param json
     */
    public void json(String json) {
        int indent = 4;
        if (TextUtils.isEmpty(json)) {
            d("JSON{json is empty}");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.toString(indent);
                d(msg);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String msg = jsonArray.toString(indent);
                d(msg);
            }
        } catch (JSONException e) {
            e(e.toString() + "\n\njson = " + json);
        }
    }

    /**
     * @param xml
     */
    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("XML{xml is empty}");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(e.toString() + "\n\nxml = " + xml);
        }
    }

    /**
     * 打印日志
     *
     * @param type
     * @param tag
     * @param msg
     */
    private void printSystemLog(int type, String tag, String msg) {
        switch (type) {
            case TYPE_VERBOSE:
                Log.v(tag, msg);
                break;
            case TYPE_DEBUG:
                Log.d(tag, msg);
                break;
            case TYPE_INFO:
                Log.i(tag, msg);
                break;
            case TYPE_WARN:
                Log.w(tag, msg);
                break;
            case TYPE_ERROR:
                Log.e(tag, msg);
                break;
            default:
                break;
        }
    }

}
