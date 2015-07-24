
package com.autonavi.navigation.framework.base.utils.common;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {
    public short wYear;

    public short wMonth;

    public short wDayOfWeek;

    public short wDay;

    public short wHour;

    public short wMinute;

    public short wSecond;

    public short wMilliseconds;

    public static void getCurSystemTime(TimeUtil timeUtil) {
        if (timeUtil == null) {
            timeUtil = new TimeUtil();
        }

        long utcTime = System.currentTimeMillis();
        GregorianCalendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));
        c.setTimeInMillis(utcTime);

        timeUtil.wYear = (short) c.get(Calendar.YEAR);
        timeUtil.wMonth = (short) ((short) c.get(Calendar.MONTH) + 1);
        timeUtil.wDayOfWeek = (short) c.get(Calendar.DAY_OF_WEEK);
        timeUtil.wDay = (short) c.get(Calendar.DAY_OF_MONTH);
        timeUtil.wHour = (short) c.get(Calendar.HOUR_OF_DAY);
        timeUtil.wMinute = (short) c.get(Calendar.MINUTE);
        timeUtil.wSecond = (short) c.get(Calendar.SECOND);
        timeUtil.wMilliseconds = 0;

    }

    public static long getTickCount() {
        Calendar calendar = Calendar.getInstance();// 获取当前日历对象
        long unixTime = calendar.getTimeInMillis();// 获取当前时区下日期时间对应的时间戳
        return unixTime;
    }
}
