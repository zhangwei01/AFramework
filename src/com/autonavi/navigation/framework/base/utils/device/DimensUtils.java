
package com.autonavi.navigation.framework.base.utils.device;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DimensUtils {
    private static DisplayMetrics display;

    public static int dpToPx(Context context, double dp) {
        int value = (int) (dp * context.getResources().getDisplayMetrics().density);
        return value;
    }

    public static int getScreenWidth(Activity context) {
        //获取屏幕宽
        if (display == null) {
            display = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(display);
        }
        return display.widthPixels;
    }

    public static int getScreenHeight(Activity context) {
        //获取屏幕宽
        if (display == null) {
            display = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(display);
        }
        return display.heightPixels;
    }
}
