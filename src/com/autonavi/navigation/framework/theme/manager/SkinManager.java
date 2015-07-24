
package com.autonavi.navigation.framework.theme.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.autonavi.navigation.framework.theme.configuration.SkinConfiguration;
import com.autonavi.navigation.framework.theme.listener.OnSkinListener;
import com.autonavi.navigation.framework.theme.model.Skin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SkinManager {

    private SharedPreferences mSp;

    private static SkinManager sSkinManager;

    //保存注册的所有OnSkinListener
    private final CopyOnWriteArrayList<OnSkinListener> mOnSkinListeners = new CopyOnWriteArrayList<OnSkinListener>();

    private Context mContext;

    /**
     * 当前程序支持皮肤的最低版本
     */
    private int mMinVersion = -1;

    /**
     * 当前支持皮肤包最高版本
     */
    private int mTargetVersion = -1;

    private SkinManager(Context context) {
        mContext = context;
        mSp = context.getSharedPreferences(SkinConfiguration.SP_SKIN, Activity.MODE_PRIVATE);
        getVersion(context);
        Log.d("SkinManager", "minVersion:" + mMinVersion + ",targetVersion:" + mTargetVersion);
    }

    public synchronized static SkinManager getInstance(Context context) {
        if (sSkinManager == null) {
            sSkinManager = new SkinManager(context);
        }
        return sSkinManager;
    }

    /**
     * 获得支持最低的皮肤包版本
     * 
     * @return -1为没有获取到版本
     */
    public int getMinVersion() {
        return mMinVersion;
    }

    /**
     * 获得当前支持的皮肤包版本
     * 
     * @return -1为没有获取到版本
     */
    public int getTargetVersion() {
        return mTargetVersion;
    }

    /**
     * 检测皮肤包是否在支持版本范围内
     * 
     * @param skin 皮肤包
     * @return true支持，false不支持
     */
    public boolean isSkinAvailable(Skin skin) {
        return isSkinAvailable(skin.mVersion);
    }

    /**
     * 检测皮肤包是否在支持版本范围内
     * 
     * @param skinTargetVersion 皮肤包版本
     * @return true支持，false不支持
     */
    public boolean isSkinAvailable(int skinTargetVersion) {
        if (skinTargetVersion >= mMinVersion && skinTargetVersion <= mTargetVersion) {
            return true;
        }
        return false;
    }

    /**
     * 改变皮肤
     * 
     * @param skin 皮肤包
     */
    public void changeSkin(Skin skin) {
        this.setCurSkin(skin);
        for (OnSkinListener listener : mOnSkinListeners) {
            if (listener.isChangeSkin(skin)) {
                listener.onChangeSkin(mContext, skin);
            }
        }
    }

    /**
     * 增加OnSkinListener
     * 
     * @param onSkinListener
     */
    public void addOnSkinListener(OnSkinListener onSkinListener) {
        if (onSkinListener != null) {
            mOnSkinListeners.add(onSkinListener);
        }
    }

    /**
     * 移除onSkinListener
     * 
     * @param onSkinListener
     */
    public void removeOnSkinListener(OnSkinListener onSkinListener) {
        if (onSkinListener != null) {
            mOnSkinListeners.remove(onSkinListener);
        }
    }

    /**
     * 获取当前使用的皮肤路径
     * 
     * @return 皮肤包skin
     */
    public Skin getCurSkin() {
        String skinPath = mSp.getString(SkinConfiguration.SP_SKIN_PATH, "");
        if (TextUtils.isEmpty(skinPath)) {
            return null;
        }
        File file = new File(skinPath);
        if (!file.exists()) {
            return null;
        }
        Skin skin = new Skin();
        skin.mName = file.getName().replace(SkinConfiguration.FIEL_SKIN_SUFFIX, "");
        skin.mPath = skinPath;
        skin.mFileSize = file.length();
        return skin;
    }

    /**
     * 保存当前使用的皮肤路径
     * 
     * @param skin 皮肤包
     */
    public void setCurSkin(Skin skin) {
        mSp.edit().putString(SkinConfiguration.SP_SKIN_PATH, skin == null ? "" : skin.mPath)
                .commit();
    }

    /**
     * 获取skinfolderPath文件夹下的所有皮肤包
     * 
     * @param skinfolderPath 皮肤包所在文件夹路径
     * @return 皮肤包集合
     */
    public Skin[] getSkins(String skinfolderPath) {
        if (TextUtils.isEmpty(skinfolderPath)) {
            return null;
        }
        return getSkins(new File(skinfolderPath));
    }

    /**
     * 获取skinfolder文件夹下的所有皮肤包
     * 
     * @param skinfolder 皮肤包所在文件夹
     * @return 皮肤包集合
     */
    public Skin[] getSkins(final File skinfolder) {
        if (skinfolder == null || !skinfolder.exists()) {
            return null;
        }
        File[] skinFiles = skinfolder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                //获取符合的皮肤包
                return filename.endsWith(SkinConfiguration.FIEL_SKIN_SUFFIX);
            }
        });
        if (skinFiles == null || skinFiles.length == 0) {
            return null;
        }
        Skin[] skins = new Skin[skinFiles.length];
        for (int i = 0; i < skins.length; i++) {
            skins[i] = new Skin();
            //去掉文件后缀名
            skins[i].mName = skinFiles[i].getName().replace(SkinConfiguration.FIEL_SKIN_SUFFIX, "");
            skins[i].mPath = skinFiles[i].getAbsolutePath();
            skins[i].mFileSize = skinFiles[i].length();
            skins[i].mVersion = getSkinVersion(skinfolder + File.separator + skins[i].mName
                    + SkinConfiguration.FIEL_SKIN_SUFFIX);
            skins[i].mIcPath = zipSkinIcon(skins[i].mPath);//解压其中的皮肤包icon图片
        }

        return skins;
    }

    /**
     * 获得应用支持的皮肤包版本
     * 
     * @param context
     */
    private void getVersion(Context context) {
        InputStream stream;
        try {
            stream = context.getAssets().open(SkinConfiguration.APPLICATION_INI);
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.length() > 0 && !line.startsWith("#")) {
                    line = line.trim();
                    if (line.startsWith(SkinConfiguration.MIN_VERSION)) {
                        line = line.substring(line.lastIndexOf("=") + 1, line.length());
                        mMinVersion = Integer.valueOf(line);
                    } else if (line.startsWith(SkinConfiguration.TARGET_VERSION)) {
                        line = line.substring(line.lastIndexOf("=") + 1, line.length());
                        mTargetVersion = Integer.valueOf(line);
                    }
                }
            }
            in.close();
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 得到皮肤包的版本
     * 
     * @param skinPath 皮肤包路径
     * @return 此皮肤包的版本，-1获取版本失败，其他获取成功
     */
    private int getSkinVersion(String skinPath) {
        int targetVersion = -1;
        try {
            ZipFile zipfile = new ZipFile(skinPath);
            ZipEntry entry = zipfile.getEntry(SkinConfiguration.SKIN_INI);
            if (entry != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        zipfile.getInputStream(entry)));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.length() > 0 && !line.startsWith("#")) {
                        line = line.trim();
                        if (line.startsWith(SkinConfiguration.TARGET_VERSION)) {
                            line = line.substring(line.lastIndexOf("=") + 1, line.length());
                            targetVersion = Integer.valueOf(line);
                            Log.d("SkinManager", "targetVersion:" + targetVersion);
                        }
                    }
                }
                in.close();
            }
            zipfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetVersion;
    }

    /**
     * 解压皮肤压缩包里的icon图片
     * 
     * @param skinPath skinPath 皮肤包路径
     * @return 返回icon解压出的路径，路径为null表示解压失败
     * @throws IOException
     */
    private String zipSkinIcon(String skinPath) {
        try {
            ZipFile zipfile = new ZipFile(skinPath);
            @SuppressWarnings("unchecked")
            Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zipfile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                String icPath = entry.getName();
                if (icPath.startsWith(SkinConfiguration.SKIN_ICON)) {
                    //获取icon图片后缀
                    icPath = icPath.substring(icPath.lastIndexOf("."), icPath.length());
                    //得到保存icon图片的路径
                    icPath = skinPath.replace(SkinConfiguration.FIEL_SKIN_SUFFIX, icPath);
                    File icFile = new File(icPath);
                    if (!icFile.exists()) {
                        BufferedInputStream is = new BufferedInputStream(
                                zipfile.getInputStream(entry));
                        int size = (int) entry.getSize();
                        if (size > 100) {
                            size = 100;
                        }
                        byte data[] = new byte[size];
                        BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(
                                icFile), size);
                        int count;
                        while ((count = is.read(data, 0, size)) != -1) {
                            dest.write(data, 0, count);
                        }
                        dest.flush();
                        dest.close();
                        is.close();
                    }
                    zipfile.close();
                    return icPath;
                }
            }
            zipfile.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
