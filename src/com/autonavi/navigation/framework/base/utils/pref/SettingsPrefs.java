
package com.autonavi.navigation.framework.base.utils.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 新闻配置相关工具类
 */
public class SettingsPrefs {

    private static SettingsPrefs instance;

    private static final String PREFS_MODULE_INFO = "musicPrefs";

    private final SharedPreferences mSharedPreferences;

    /**
     * 播放模式
     * <p>
     * int
     */
    public static final String XIAMI_PLAY_MODE = "play_mode";

    /**
     * 播放历史id
     * <p>
     * string
     */
    public static final String XIAMI_PLAY_HISTORY = "play_history_item";

    /**
     * isCache
     * <p>
     * boolean
     */
    public static final String XIAMI_CAHCE_MODE = "cache_mode";

    private SettingsPrefs(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS_MODULE_INFO, Context.MODE_PRIVATE);
    }

    public static SettingsPrefs intent(Context context) {
        if (instance == null) {
            instance = new SettingsPrefs(context.getApplicationContext());
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public Editor getEditor() {
        SharedPreferences pref = getSharedPreferences();
        return pref.edit();
    }

    public String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return getSharedPreferences().getFloat(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return getSharedPreferences().getLong(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public boolean putInt(String key, int value) {
        Editor edit = getEditor();
        edit.putInt(key, value);
        boolean isSuccess = edit.commit();
        return isSuccess;
    }

    public boolean putBoolean(String key, boolean value) {
        Editor edit = getEditor();
        edit.putBoolean(key, value);
        boolean isSuccess = edit.commit();
        return isSuccess;
    }

    public boolean putFloat(String key, float value) {
        Editor edit = getEditor();
        edit.putFloat(key, value);
        boolean isSuccess = edit.commit();
        return isSuccess;
    }

    public boolean putLong(String key, long value) {
        Editor edit = getEditor();
        edit.putLong(key, value);
        boolean isSuccess = edit.commit();
        return isSuccess;
    }

    public boolean putString(String key, String value) {
        Editor edit = getEditor();
        edit.putString(key, value);
        boolean isSuccess = edit.commit();
        return isSuccess;
    }

    public boolean putObject(String key, Object song) {
        Editor edit = getEditor();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(song);
        } catch (IOException e) {
            return false;
        }
        // 将Product对象放到OutputStream中
        // 将Product对象转换成byte数组，并将其进行base64编码
        String productBase64 = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        // 将编码后的字符串写到base64.xml文件中
        edit.putString(key, productBase64);
        boolean isSuccess = edit.commit();
        return isSuccess;
    }

    public Object getObject(String key) {
        String productBase64 = getSharedPreferences().getString(key, "");
        // 对Base64格式的字符串进行解码
        byte[] base64Bytes = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(bais);
            Object product = ois.readObject();
            return product;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 清除所有配置
     */
    public void clearAllPreferences() {
        getEditor().clear().commit();
    }
}
