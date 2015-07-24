
package com.autonavi.navigation.framework.base.utils.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 常用工具类
 */
public class CommonUtil {

    public static File getSdcardFile() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取sdcard下以包名命名的文件夹
     * 
     * @param context
     * @return
     */
    public static String getPackageDir(Context context) {
        String simplePackageName = context.getPackageName().substring(
                context.getPackageName().lastIndexOf(".") + 1);
        return new StringBuilder(getSdcardFile().getAbsolutePath()).append(File.separator)
                .append(simplePackageName).append(File.separator).toString();
    }

    /**
     * 创建目录
     * 
     * @param filePath
     */
    public static void createFileDirs(String filePath) {
        StringTokenizer st = new StringTokenizer(filePath, File.separator);
        String path1 = st.nextToken() + File.separator;
        String path2 = path1;
        while (st.hasMoreTokens()) {
            path1 = st.nextToken() + File.separator;
            path2 += path1;
            File inbox = new File(path2);
            if (!inbox.exists()) {
                inbox.mkdir();
            }
        }
    }

    /**
     * 获取imei
     * 
     * @param mContext
     * @return
     */
    public static String getImei(Context mContext) {
        TelephonyManager telephonyMgr = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyMgr.getDeviceId();
    }

    /**
     * 获取imsi
     * 
     * @param mContext
     * @return
     */
    public static String getImsi(Context mContext) {
        TelephonyManager telephonyMgr = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyMgr.getSubscriberId();
    }

    /**
     * 获取md5
     * 
     * @param string
     * @return
     */
    public static String getMD5(String string) {
        if ((string == null) || (string.trim().length() < 1)) {
            return null;
        }
        try {
            return getMD5(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取长宽都不超过160dip的图片，基本思想是设置Options.inSampleSize按比例取得缩略图
     */
    public static Options getOptionsWithInSampleSize(String filePath, int maxWidth) {
        Options bitmapOptions = new Options();
        bitmapOptions.inJustDecodeBounds = true;// 只取得outHeight(图片原始高度)和
        // outWidth(图片的原始宽度)而不加载图片
        BitmapFactory.decodeFile(filePath, bitmapOptions);
        bitmapOptions.inJustDecodeBounds = false;
        int inSampleSize = bitmapOptions.outWidth / (maxWidth / 10);// 应该直接除160的，但这里出16是为了增加一位数的精度
        if ((inSampleSize % 10) != 0) {
            inSampleSize += 10;// 尽量取大点图片，否则会模糊
        }
        inSampleSize = inSampleSize / 10;
        if (inSampleSize <= 0) {// 判断200是否超过原始图片高度
            inSampleSize = 1;// 如果超过，则不进行缩放
        }
        bitmapOptions.inSampleSize = inSampleSize;
        return bitmapOptions;
    }

    /**
     * @param context
     * @return
     */
    public static int getScreenMin(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return (screenWidth > screenHeight) ? screenHeight : screenWidth;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * 获取当前应用的名称
     * 
     * @param context
     * @return
     */
    public static String getSimplePackageName(Context context) {
        return context.getPackageName().substring(context.getPackageName().lastIndexOf(".") + 1);
    }

    /**
     * 获取包版本号
     * 
     * @param context
     * @return
     */
    public static int getPackageVersionCode(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
        }
        return 0;
    }

    /**
     * 获取包版本名
     * 
     * @param context
     * @return
     */
    public static String getPackageVersionName(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
        }
        return "";
    }

    public static String getTimeString() {
        return getTimeString("yyyyMMddHHmmss");
    }

    public static String getTimeString(long time) {
        return getTimeString("yyyyMMddHHmmss");
    }

    public static String getTimeString(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String getTimeString(String pattern, long time) {
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    /**
     * 判断当前context是否横屏
     * 
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        Configuration cf = context.getResources().getConfiguration();
        return cf.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 是否需要缩放图片 1.图片尺寸大于200k 2.图片大小大于屏幕最宽
     */
    public static Boolean isNeedScaleImage(String filename) {
        long MAX_SIZE_IMAGE = 20 * 1024;
        // 判断文件大小
        File imageFile = new File(filename);
        if (!imageFile.exists()) {
            return false;
        }
        if (imageFile.length() > MAX_SIZE_IMAGE) {
            return true;
        }
        // 判断文件尺寸
        Options bitmapOptions = new Options();
        bitmapOptions.inJustDecodeBounds = true;// 只取得outHeight(图片原始高度)和
        bitmapOptions.inSampleSize = 1;
        // outWidth(图片的原始宽度)而不加载图片
        Bitmap bitmap = BitmapFactory.decodeFile(filename, bitmapOptions);
        if (bitmap == null) {
            return false;
        }
        return false;
    }

    /**
     * 是否有可用网络
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return false;
        }
        if (((tm.getDataState() == TelephonyManager.DATA_CONNECTED) || (tm.getDataState() == TelephonyManager.DATA_ACTIVITY_NONE))
                && info.isAvailable()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @param context
     * @return
     */
    public static boolean isNetworkEnabled(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        // wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
            return true;
        }
        if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否为Wifi网络
     * 
     * @param mContext
     * @return
     */
    public static boolean isWifiNetwrok(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否有SDCARD
     * 
     * @return
     */
    public static boolean isSDcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static void killMyProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 等比例缩放图片（带滤波器）
     * 
     * @param srcFile 来源文件
     * @param dstFile 目标文件
     * @param dstMaxWH 目标文件宽高最大值
     * @param bContrast 提高对比度滤波器，可使图片变亮丽
     * @param bSharp 锐化图片，可使图片清晰（暂时无效果）
     */
    public static boolean scaleImageWithFilter(File srcFile, File dstFile, int dstMaxWH,
            Boolean bContrast) {
        boolean bRet = false;
        // 路径文件不存在
        if (!srcFile.exists()) {
            return bRet;
        }
        try {
            // 打开源文件
            Bitmap srcBitmap;
            {
                java.io.InputStream is;
                is = new FileInputStream(srcFile);
                BitmapFactory.Options opts = getOptionsWithInSampleSize(srcFile.getPath(), dstMaxWH);
                srcBitmap = BitmapFactory.decodeStream(is, null, opts);
                if (srcBitmap == null) {
                    return bRet;
                }
            }
            // 原图片宽高
            int width = srcBitmap.getWidth();
            int height = srcBitmap.getHeight();
            // 获得缩放因子
            float scale = 1.f;
            {
                if ((width > dstMaxWH) || (height > dstMaxWH)) {
                    float scaleTemp = (float) dstMaxWH / (float) width;
                    float scaleTemp2 = (float) dstMaxWH / (float) height;
                    if (scaleTemp > scaleTemp2) {
                        scale = scaleTemp2;
                    } else {
                        scale = scaleTemp;
                    }
                }
            }
            // 图片缩放
            Bitmap dstBitmap;
            if (scale == 1.f) {
                dstBitmap = srcBitmap;
            } else {
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
                if (!srcBitmap.isRecycled()) {
                    srcBitmap.recycle();
                }
                srcBitmap = null;
            }
            // 提高对比度
            if (bContrast) {
                Bitmap tempBitmap = Bitmap.createBitmap(dstBitmap.getWidth(),
                        dstBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(tempBitmap);
                ColorMatrix cm = new ColorMatrix();
                float contrast = 30.f / 180.f; // 提高30对比度
                setContrast(cm, contrast);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColorFilter(new ColorMatrixColorFilter(cm));
                canvas.drawBitmap(dstBitmap, 0, 0, paint);
                if (!dstBitmap.isRecycled()) {
                    dstBitmap.recycle();
                }
                dstBitmap = null;
                dstBitmap = tempBitmap;
            }
            // 保存文件
            if (dstFile.exists()) {
                dstFile.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dstFile));
            dstBitmap.compress(CompressFormat.JPEG, 90, bos);
            if (!dstBitmap.isRecycled()) {
                dstBitmap.recycle();
            }
            dstBitmap = null;
            bos.flush();
            bos.close();
            bRet = true;
        } catch (Exception e) {
            return bRet;
        }
        return bRet;
    }

    // public static void showToast(Context mContext, int resId) {
    // Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    // }
    //
    // public static void showToast(Context mContext, String text) {
    // Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    // }

    private static String getMD5(byte[] source) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            for (byte b : md5.digest(source)) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 设置对比度矩阵
     */
    private static void setContrast(ColorMatrix cm, float contrast) {
        float scale = contrast + 1.f;
        float translate = ((-.5f * scale) + .5f) * 255.f;
        cm.set(new float[] {
                scale, 0, 0, 0, translate, 0, scale, 0, 0, translate, 0, 0, scale, 0, translate, 0,
                0, 0, 1, 0
        });
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getBitmapByView(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * 获取状态栏高度，不能在onCreate获取
     * 
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static int getTitleBarHeight(Activity activity) {
        return getStatusTitleBarHeight(activity) - getStatusBarHeight(activity);
    }

    public static int getStatusTitleBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    public static boolean isLatestTask(Context mContext) {
        ActivityManager mActivityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RecentTaskInfo> mRunningTaskInfoList = mActivityManager.getRecentTasks(1, 0);
        if (mRunningTaskInfoList != null) {
            if ((mRunningTaskInfoList.get(0).baseIntent.getComponent().getPackageName())
                    .equals(mContext.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pkg = null;
        try {
            pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException exp) {
            exp.printStackTrace();
        }
        return pkg;
    }

    public static int getDisplayDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    public static String getDisplayDensityName(Context context) {
        int density = getDisplayDensity(context);
        String drawableDir = "hdpi";
        switch (density) {
            case DisplayMetrics.DENSITY_HIGH:
                drawableDir = "hdpi";
                break;
            case DisplayMetrics.DENSITY_LOW:
                drawableDir = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                drawableDir = "mdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                drawableDir = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                drawableDir = "xxhdpi";
                break;
            case DisplayMetrics.DENSITY_TV:
                drawableDir = "tvdpi";
                break;
        }
        return drawableDir;
    }

    /**
     * 按名称获取图片id
     * 
     * @param context
     * @param name 图片名称
     * @return 图片id
     */
    public static int getDrawableId(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    /**
     * 格式化距离
     * 
     * @param meter
     * @return 格式化后的字符串
     */
    public static String formatDistance(int meter) {
        if (Math.abs(meter) < 1000) {
            // 单位是米
            return String.format("%d", meter);
        } else {
            float km = meter / 1000f;
            if (km >= 10000.0f) {
                // 单位是千米
                return String.format("%.0f", km);
            } else {
                // 单位是千米
                return String.format("%.1f", km);
            }
        }
    }

    /**
     * 判断是否点击在view上，此方法必须在界面绘制之后使用有效，建议此方法在
     * {@link Activity#dispatchTouchEvent(MotionEvent ev)}中使用
     * 由于float型坐标值转int型，有可能有1像素的误差
     * <p>
     * 使用场景，点击某个view之外收起键盘
     * 
     * @param x 点击的x坐标
     * @param y 点击的y坐标
     * @param view 要判断的view
     * @return true点击在view上，否则不在
     */
    public static boolean contains(int x, int y, View view) {
        int[] location = new int[2];
        //获取view的父容器左上角在屏幕上的坐标
        ((View) view.getParent()).getLocationOnScreen(location);
        Rect frame = new Rect();
        //获取view的绘制区域，此区域是相对于其父容器的区域
        view.getHitRect(frame);//也可使用view.getDrawingRect(outRect);
        //修改绘制区域为相对于整个界面
        frame.left += location[0];
        frame.right += location[0];
        frame.bottom += location[1];
        frame.top += location[1];
        return frame.contains(x, y);
    }
}
