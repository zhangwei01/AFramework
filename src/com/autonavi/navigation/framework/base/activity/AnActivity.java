
package com.autonavi.navigation.framework.base.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Locale;

/**
 * 基类
 * 
 * @author hao.zhong
 */
public class AnActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    /* 
     * <pre>
     * AndroidMainfest.xml中加入权限   
     * <uses-permission android:name="android.permission.CHANGE_CONFIGURATION" />
     * 
     * Activity标签加入属性
     * Android:configChanges="orientation|keyboard|locale" 
     * </pre>
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 横竖屏
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
        }
        // 语言
        if (newConfig.locale == Locale.SIMPLIFIED_CHINESE) {
            // 简体中文
        } else if (newConfig.locale == Locale.TRADITIONAL_CHINESE) {
            // 繁体中文
        } else if (newConfig.locale == Locale.ENGLISH) {
            // 英语
        }
        // 
    }

}
