
package com.autonavi.navigation.framework.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.autonavi.navigation.framework.R;
import com.autonavi.navigation.framework.nfc.Nfc;
import com.autonavi.navigation.framework.nfc.bean.NfcInfo;
import com.autonavi.navigation.framework.nfc.utils.NfcUtil;

public class NfcActivity extends Activity {

    private Nfc mNfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_activity);
        initNfc();
    }

    void initNfc() {
        mNfc = Nfc.getInstance(this);
        mNfc.PushText("test push text", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNfc.onResume(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfc.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NfcInfo nfcInfo = mNfc.resolveIntent(intent);
        if (nfcInfo != null) {
            if (nfcInfo.isNdefMessage) {
                if (nfcInfo.ndefRecords.size() == 0) {
                    return;
                }
                for (int i = 0; i < nfcInfo.ndefRecords.size(); i++) {
                    if (NfcUtil.isText(nfcInfo.ndefRecords.get(i))) {
                        Log.e("11", "text : " + NfcUtil.parseText(nfcInfo.ndefRecords.get(i)));
                        updateText.obtainMessage(0, NfcUtil.parseText(nfcInfo.ndefRecords.get(i)))
                                .sendToTarget();
                    } else if (NfcUtil.isUri(nfcInfo.ndefRecords.get(i))) {
                        Log.e("11", "uri : " + NfcUtil.parseUri(nfcInfo.ndefRecords.get(i)));
                        updateText.obtainMessage(0,
                                NfcUtil.parseUri(nfcInfo.ndefRecords.get(i)).toString())
                                .sendToTarget();
                    }
                }
            } else {
                updateText.obtainMessage(0, nfcInfo.toString()).sendToTarget();
            }
        }
    }

    Handler updateText = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ((TextView) findViewById(R.id.ndefMessageTV)).setText((String) msg.obj);
        }
    };

}
