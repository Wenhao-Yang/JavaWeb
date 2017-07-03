package com.william.www.thestock.api;


import net.sf.json.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StockDataHttp {

    private String murl,kurl,result;
    private JSONArray res;



    public String GetMinuteData(String sid){

        RequestParams requestParams=new RequestParams(murl);
        //封装两个参数
        requestParams.addBodyParameter("sid",sid);
        //requestParams.addBodyParameter("pwd4",upwd);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            //成功返回的 类似于success:function
            @Override
            public void onSuccess(String result) {
                result=result;
                //Intent intent=new Intent(MainActivity.this,CenterActivity.class);
                //startActivity(intent);
            }
            //网络连接error error:function
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {
                //取消方法
            }

            @Override
            public void onFinished() {
                //无论是连接ok，都最终执行该方法
            }

        });
        return null;
    }

    public JSONArray GetKLineData(String sid) throws JSONException {
        JSONArray res=new JSONArray("[\"12\"]");
        kurl="http://47.93.227.240:8080/SSMDemo/usert/kinfo.action";
        RequestParams requestParams=new RequestParams(kurl);
        //封装两个参数
        requestParams.addBodyParameter("code",sid);
        requestParams.addBodyParameter("start","20170101");

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);
        requestParams.addBodyParameter("end",dateNowStr);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            //成功返回的 类似于success:function
            @Override
            public void onSuccess(String result) {
                try {
                    //System.out.println(result);
                    setRes(new JSONArray(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Intent intent=new Intent(MainActivity.this,CenterActivity.class);
                //startActivity(intent);
            }
            //网络连接error error:function
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {
                //取消方法
            }

            @Override
            public void onFinished() {
                //无论是连接ok，都最终执行该方法
            }

        });
        System.out.println("55555555555555555555555555555555555555555555555555555555555"+getRes());
        return getRes();
    }


    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    public String getKurl() {
        return kurl;
    }

    public void setKurl(String kurl) {
        this.kurl = kurl;
    }

    public JSONArray getRes() {
        return res;
    }

    public void setRes(JSONArray res) {
        this.res = res;
    }
}
