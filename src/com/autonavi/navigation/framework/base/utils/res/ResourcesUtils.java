
package com.autonavi.navigation.framework.base.utils.res;

import android.content.Context;

/**
 * 资源相关工具类
 */
public final class ResourcesUtils {

    private ResourcesUtils() {
        /* 不能被实例化 */
    }

    private static final String DRAWABLE_NAME = "ic_xxx_%s";

    private static final String STRING_NAME = "text_xxx_%s";

    /**
     * drawable
     * 
     * @param context
     * @param drawableName
     * @return
     */
    public static int getDrawableResId(Context context, String drawableName) {
        return getResId(context, String.format(DRAWABLE_NAME, drawableName), "drawable");
    }

    /**
     * string
     * 
     * @param context
     * @param stringName
     * @return
     */
    public static int getStringResId(Context context, String stringName) {
        return getResId(context, String.format(STRING_NAME, stringName), "string");
    }

    public static int getResId(Context context, String name, String defType) {
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

}
