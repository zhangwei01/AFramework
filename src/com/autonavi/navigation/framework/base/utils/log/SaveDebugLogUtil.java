
package com.autonavi.navigation.framework.base.utils.log;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveDebugLogUtil {
    private static SaveDebugLogUtil sDebugLog;

    // log文件最大大小
    private static final long mMaxTotalSize = 10 * 1024 * 1024; // 5M大小，超过则删除

    // log文件名称
    private static final String mDebugLogName = "DevHelper.txt";

    // log文件路径
    private static String mDebugLogPath = new String();

    private File mDebugLogFile = null;

    private RandomAccessFile mDebugLogAccessFile;

    public static final synchronized SaveDebugLogUtil getInstance() {
        if (sDebugLog == null) {
            sDebugLog = new SaveDebugLogUtil();
            sDebugLog.WriteHeader();
        }
        return sDebugLog;
    }

    public SaveDebugLogUtil() {
        if (!LogUtil.DEBUG) {
            return;
        }
        mDebugLogPath = Environment.getExternalStorageDirectory().getPath();
        mDebugLogPath += File.separator;
        mDebugLogPath += mDebugLogName;
        try {
            mDebugLogFile = new File(mDebugLogPath);
            if (mDebugLogFile != null && !mDebugLogFile.exists()) {
                mDebugLogFile.createNewFile();
            }
            mDebugLogAccessFile = new RandomAccessFile(mDebugLogFile, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void WriteHeader() {
        if (mDebugLogFile == null || mDebugLogAccessFile == null) {
            return;
        }

        try {
            long totalSize;
            totalSize = mDebugLogAccessFile.length();
            if (totalSize > mMaxTotalSize) {
                mDebugLogFile.delete();
                return;
            }

            mDebugLogAccessFile.seek(totalSize);

            String szInput = "\r\n[";
            // szInput += Global.getSoftVersion() + "\t";
            szInput += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            szInput += "]\r\n";
            mDebugLogAccessFile.writeBytes(szInput);

            totalSize = mDebugLogAccessFile.length();
            if (totalSize > mMaxTotalSize) {
                mDebugLogFile.delete();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 返回值是写入文本的长度
    public synchronized int DebugWriteLog(String logStr) {
        if (logStr == null || logStr.length() <= 0 || mDebugLogFile == null
                || mDebugLogAccessFile == null) {
            return -1;
        }

        try {
            long totalSize = mDebugLogAccessFile.length();
            if (totalSize > mMaxTotalSize) {
                mDebugLogFile.delete();
                return -1;
            }
            mDebugLogAccessFile.seek(totalSize);
            String szInput = "\r\nDebug:";
            mDebugLogAccessFile.writeBytes(szInput);
            if (!logStr.endsWith("\r\n")) {
                logStr += "\r\n";
            }
            szInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": "
                    + logStr;
            mDebugLogAccessFile.writeBytes(szInput);
            totalSize = mDebugLogAccessFile.length();
            if (totalSize > mMaxTotalSize) {
                mDebugLogFile.delete();
                return -1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return logStr.length();
    }

    public void DebugLogExit() {
        if (mDebugLogFile == null || mDebugLogAccessFile == null) {

        } else {
            try {
                mDebugLogAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
