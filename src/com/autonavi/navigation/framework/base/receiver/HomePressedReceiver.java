package com.autonavi.navigation.framework.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Home键按下监听器
 * <p>
 * 注意：该监听器不是只有当前应用才生效，而是对所有应用都有效，所以建议在所属Activity注册
 * <p>
 * 使用方法：
 * 
 * <pre>
 * HomeKeyReceiver mHomeKeyReceiver = new HomeKeyReceiver();
 * mHomePressedReceiver.setOnHomePressedLisenter(new OnHomePressedLisenter() {
 * 
 * 	&#064;Override
 * 	public void onHomePressed() {
 * 	}
 * });
 * registerReceiver(mHomeKeyReceiver, new IntentFilter(
 * 		Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
 * unregisterReceiver(mHomeKeyReceiver);
 * </pre>
 * 
 * @author wencan.yang
 * 
 */
public class HomePressedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
			String reason = intent.getStringExtra("reason");
			if (reason != null) {
				if (reason.equals("homekey")) {
					if (mOnHomePressedLisenter != null) {
						mOnHomePressedLisenter.onHomePressed();
					}
				} else if (reason.equals("recentapps")) {
					// 4.0的最近应用
				}
			}
		}
	}

	private OnHomePressedLisenter mOnHomePressedLisenter;

	public void setOnHomePressedLisenter(OnHomePressedLisenter l) {
		mOnHomePressedLisenter = l;
	}

	public interface OnHomePressedLisenter {
		void onHomePressed();
	}
}