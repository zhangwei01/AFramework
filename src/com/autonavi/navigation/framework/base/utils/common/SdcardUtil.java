
package com.autonavi.navigation.framework.base.utils.common;

import android.os.Environment;

import com.autonavi.navigation.framework.base.utils.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SdcardUtil {

    /**
     * private constructor
     */
    // private sdcard() {
    // }

    /**
     * Check if the SD Card is Available
     * 
     * @return true if the sd card is available and false if it is not
     */
    public static boolean isSDCardAvailable() {
        boolean mExternalStorageAvailable = false;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;

        } else {
            // Something else is wrong. It may be one of many other states,
            // but all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }

        return mExternalStorageAvailable;

    }

    /**
     * Delete directory
     * 
     * @param path path to be deleted
     * @return returns true if deletion was successful
     */
    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    /**
     * Check if the SD Card is writable
     * 
     * @return true if the sd card is writable and false if it is not
     */
    public static boolean isSDCardWritable() {

        boolean mExternalStorageWriteable = false;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media

            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states,
            // but all we need
            // to know is we can neither read nor write
            mExternalStorageWriteable = false;
        }

        return mExternalStorageWriteable;

    }

    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     * 
     * @param fromFile - FileInputStream for the file to copy from.
     * @param toFile - FileOutpubStream for the file to copy to.
     */
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile)
            throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     * 
     * @param fromFile - File to copy from.
     * @param toFile - File to copy to.
     */
    public static void copyFile(File fromFile, File toFile) throws IOException {
        copyFile(new FileInputStream(fromFile), new FileOutputStream(toFile));
    }

    /**
     * Get the SDCard Path
     * 
     * @return the complete path to the SDCard
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().toString() + "/";
    }

    /**
     * Get the SDCard Path as a File
     * 
     * @return the complete path to the SDCard
     */
    public static File getSDCardPathFile() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * Check if file exists on SDCard or not
     * 
     * @param filePath - its the path of the file after SDCardDirectory (no need
     *            for getExternalStorageDirectory())
     * @return boolean - if file exist on SDCard or not
     */
    public static boolean checkIfFileExists(String filePath) {
        File file = new File(filePath);// getSDCardPath(), filePath);
        return (file.exists() ? true : false);
    }

    /**
     * Create folder in the SDCard
     * 
     * @param path
     * @return
     */
    public static boolean createFolder(String path) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/" + path);

        if (!direct.exists()) {
            if (direct.mkdir()) {
                return true;
            }

        }
        return false;
    }

    /**
     * Detailed log with a "yyyy/MM/dd HH:mm:ss.SSS" timestamp
     * 
     * @param text text to append
     * @param logFilePath path to the file
     */
    public static void appendTextToLog(String text, String logFilePath) {
        writeToFile(text, logFilePath, true);

    }

    /**
     * Append a new line of text to a certain file provided by `logFilePath`
     * 
     * @param text text to append
     * @param logFilePath path to the file
     */
    public static void appendTextToFile(String text, String logFilePath) {
        writeToFile(text, logFilePath, false);
    }

    /**
     * private write to file method
     * 
     * @param text text to append
     * @param logFilePath path to the file
     * @param isDetailed if it should show the timestamp or not
     */
    private static void writeToFile(String text, String logFilePath, boolean isDetailed) {
        if (isSDCardAvailable() && isSDCardWritable() && text != null) {

            try {
                File file = new File(logFilePath);
                OutputStream os = new FileOutputStream(file, true);
                if (isDetailed) {
                    os.write(("---"
                            + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Calendar
                                    .getInstance().getTime()) + "---\n").getBytes());
                }
                os.write((text + "\n").getBytes());
                // os.write(("------\n").getBytes());
                os.close();
            } catch (Exception e) {
                LogUtil.e("Exception" + e);
            }
        } else {
            return;
        }
    }

}
