
package com.autonavi.navigation.framework.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Boot BroadcastReceiver
 * <p>
 * 开机自启动广播
 * <p>
 * AndroidMainfest.xml
 */
public class BootReceiver extends BroadcastReceiver {

    public static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {

        }
    }
}
