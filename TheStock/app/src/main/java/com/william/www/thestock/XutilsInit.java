package com.william.www.thestock;

import android.app.Application;

import org.xutils.x;

/**
 * Created by WILLIAM on 2017/6/13.
 */

public class XutilsInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils的操作，初始化当前的类对象，和初始化可以degu调试
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
