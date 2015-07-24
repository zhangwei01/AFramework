
package com.autonavi.navigation.framework.nfc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;

import com.autonavi.navigation.framework.nfc.bean.NfcInfo;
import com.autonavi.navigation.framework.nfc.bean.NfcInfo.MifareClassicInfo;
import com.autonavi.navigation.framework.nfc.bean.NfcInfo.MifareUltralightInfo;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

/**
 * Nfc通信管理类
 * 
 * <pre>
 * AndroidMainfest.xml 中加入以下权限
 * <uses-permission android:name="android.permission.NFC" />
 * <uses-feature android:name="android.hardware.nfc" />
 * 
 * <intent-filter>
 * 	<action android:name="android.nfc.action.TAG_DISCOVERED" />
 * 	<category android:name="android.intent.category.DEFAULT" />
 * </intent-filter>
 * </pre>
 * 
 * @author hao.zhong
 * @author wangcheng.sun
 */
public class Nfc {

    private final Activity mActivity;

    /**
     * Nfc适配器
     */
    private final NfcAdapter mNfcAdapter;

    /**
     * 需要推送的消息
     */
    private NdefMessage mNdefMessage;

    private static Nfc sNfcManager;

    private PendingIntent mPendingIntent;

    private Nfc(Activity activity) {
        mActivity = activity;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(mActivity);
    }

    public static synchronized Nfc getInstance(Activity mActivity) {
        if (sNfcManager == null) {
            sNfcManager = new Nfc(mActivity);
        }
        return sNfcManager;
    }

    public NfcAdapter getNfcAdapter() {
        return mNfcAdapter;
    }

    /**
     * 判断该机型是否存在NFC硬件
     * 
     * @return
     */
    public boolean isNfcExist() {
        if (mNfcAdapter == null) {
            return false;
        }
        return true;
    }

    /**
     * 判断Nfc是否开启
     * 
     * @return
     */
    public boolean isNfcEnable() {
        if (!isNfcExist()) {
            return false;
        }
        return mNfcAdapter.isEnabled();
    }

    /**
     * 进入系统Nfc设置界面
     */
    public void enterSettings() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        mActivity.startActivity(intent);
    }

    /**
     * pendingIntent == null时使用默认pendingIntent
     * 
     * @param pendingIntent
     */
    public void onResume(PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            mPendingIntent = PendingIntent.getActivity(mActivity, 0, new Intent(mActivity,
                    mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        } else {
            mPendingIntent = pendingIntent;
        }
        if (isNfcEnable()) {
            mNfcAdapter.enableForegroundDispatch(mActivity, mPendingIntent, null, null);
        }
    }

    public void onPause() {
        if (isNfcEnable()) {
            mNfcAdapter.disableForegroundDispatch(mActivity);
        }
    }

    /***********************************************************************/

    /**
     * Ndef封装消息
     * 
     * @param ndefRecords
     */
    public void PushText(String message, CreateNdefMessageCallback callback) {
        PushNdefMessage(new NdefRecord[] {
            buildNdefRecord(message, Locale.ENGLISH, true)
        }, callback);
    }

    /**
     * Ndef封装消息
     * 
     * @param message
     */
    public void PushUri(Uri uri, CreateNdefMessageCallback callback) {
        PushNdefMessage(new NdefRecord[] {
            NdefRecord.createUri(uri)
        }, callback);
    }

    /**
     * Ndef封装消息
     * 
     * @param ndefRecords
     */
    public void PushNdefMessage(NdefRecord[] ndefRecords, CreateNdefMessageCallback callback) {
        mNdefMessage = new NdefMessage(ndefRecords);
        mNfcAdapter.setNdefPushMessage(mNdefMessage, mActivity);
        mNfcAdapter.setNdefPushMessageCallback(callback, mActivity);
    }

    /**
     * Call requires API level 16
     * 
     * @param callback
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startBeamPush(Uri[] uris) {
        mNfcAdapter.setBeamPushUris(uris, mActivity);
    }

    /**
     * Call requires API level 16
     * 
     * @param callback
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startBeamPushCallBack(CreateBeamUrisCallback callback) {
        mNfcAdapter.setBeamPushUrisCallback(callback, mActivity);
    }

    /**
     * 构建NdefRecord
     * 
     * @param text
     * @param locale
     * @param encodeInUtf8
     * @return
     */
    private NdefRecord buildNdefRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    public NfcInfo resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                NfcInfo nfcInfo = new NfcInfo();
                nfcInfo.isNdefMessage = true;
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    nfcInfo.ndefRecords.addAll(Arrays.asList(msgs[i].getRecords()));
                }
                return nfcInfo;
            } else {
                // Unknown tag type
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                return getNfcTagInfo(tag);
            }
        }
        return null;
    }

    public NfcInfo getNfcTagInfo(Parcelable p) {
        NfcInfo nfcInfo = new NfcInfo();
        nfcInfo.isNdefMessage = false;
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        nfcInfo.hex = getHex(id);
        nfcInfo.dec = getDec(id);
        nfcInfo.reversed = getReversed(id);
        String prefix = "android.nfc.tech.";
        for (String tech : tag.getTechList()) {
            nfcInfo.technologies.add(tech);
            nfcInfo.nfcTypes.add(tech.substring(prefix.length()));
        }
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                MifareClassicInfo mifareClassicInfo = new MifareClassicInfo();
                mifareClassicInfo.type = type;
                mifareClassicInfo.size = mifareTag.getSize();
                mifareClassicInfo.sectors = mifareTag.getSectorCount();
                mifareClassicInfo.blocks = mifareTag.getBlockCount();
                nfcInfo.mifareClassicInfo.add(mifareClassicInfo);
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                MifareUltralightInfo mifareUltralightInfo = new MifareUltralightInfo();
                mifareUltralightInfo.type = type;
            }
        }

        return nfcInfo;
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

}
