
package com.autonavi.navigation.framework.nfc.bean;

import android.nfc.NdefRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * NFC对象
 * 
 * @author hao.zhong
 */
public class NfcInfo {

    public boolean isNdefMessage = false;

    /**
     * Tag ID (hex): 1b d0 ce 04
     */
    public String hex;

    /**
     * Tag ID (dec): 466669060
     */
    public long dec;

    /**
     * ID (reversed): 80662555
     */
    public long reversed;

    /**
     * android.nfc.tech.NfcA
     */
    public List<String> technologies = new ArrayList<String>();

    /**
     * Technologies: NfcA
     */
    public List<String> nfcTypes = new ArrayList<String>();

    public List<NdefRecord> ndefRecords = new ArrayList<NdefRecord>();

    public List<MifareClassicInfo> mifareClassicInfo = new ArrayList<MifareClassicInfo>();

    public List<MifareUltralightInfo> mifareUltralightInfos = new ArrayList<MifareUltralightInfo>();

    public static class MifareClassicInfo {
        public String type;

        public int size;

        public int sectors;

        public int blocks;

        @Override
        public String toString() {
            return "MifareClassicInfo [type=" + type + ", size=" + size + ", sectors=" + sectors
                    + ", blocks=" + blocks + "]";
        }

    }

    public static class MifareUltralightInfo {
        public String type;

        @Override
        public String toString() {
            return "MifareUltralightInfo [type=" + type + "]";
        }

    }

    @Override
    public String toString() {
        return "NfcInfo [hex=" + hex + ", dec=" + dec + ", reversed=" + reversed
                + ", technologies=" + technologies + ", nfcTypes=" + nfcTypes
                + ", mifareClassicInfo=" + mifareClassicInfo + ", mifareUltralightInfos="
                + mifareUltralightInfos + "]";
    }

}
