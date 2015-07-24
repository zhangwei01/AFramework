
package com.autonavi.navigation.framework.theme.configuration;

import android.os.Environment;

public class SkinConfiguration {

    /**
     * SharedPreferences文件名，保存皮肤包数据的
     */
    public static final String SP_SKIN = "skins";

    /**
     * 在SharedPreferences中保存皮肤包的路径
     */
    public static final String SP_SKIN_PATH = "skin_path";

    /**
     * sd卡路径
     */
    public static final String URL_SDCARD = Environment.getExternalStorageDirectory().getPath();

    /**
     * 皮肤包文件后缀
     */
    public static final String FIEL_SKIN_SUFFIX = ".skin";

    //public static final String SKIN_FILE_PATH = URL_SDCARD + "";
    /**
     * 应用支持皮肤版本文件
     */
    public static final String APPLICATION_INI = "skin.ini";

    /**
     * 皮肤版本文件
     */
    public static final String SKIN_INI = "assets/skin.ini";

    /**
     * 皮肤包icon图名称
     */
    public static final String SKIN_ICON = "assets/ic_skin.";

    /**
     * 目标版本
     */
    public static final String TARGET_VERSION = "targetVersion";

    /**
     * 最小版本
     */
    public static final String MIN_VERSION = "minVersion";
}
