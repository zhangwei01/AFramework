
package com.autonavi.navigation.framework.base.utils.file;

import android.os.StatFs;
import android.text.TextUtils;

import com.autonavi.navigation.framework.base.utils.log.LogUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * 文件工具类.
 */
public class FileUtil11 {

    public FileUtil11() {

    }

    /**
     * 指定目录下，指定后缀的文件个数
     * 
     * @param directiory
     * @param fileFilter
     * @return
     */
    public static int getFileCount(String directiory, String fileFilter) {
        if (TextUtils.isEmpty(directiory) || TextUtils.isEmpty(fileFilter)) {
            return 0;
        }
        File tFile = new File(directiory);
        FileFilter11 tFileFilter11 = new FileFilter11(fileFilter);
        File[] tFiles = tFile.listFiles(tFileFilter11);
        return tFiles.length;
    }

    /**
     * 获得指定路径下的所有目录
     * 
     * @param path
     * @return
     */
    public static ArrayList<String> getAllDirectory(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        int count = 0;
        File tFile = new File(path);
        File[] tFiles = tFile.listFiles();
        count = tFiles.length;

        ArrayList<String> fileList = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            if (tFiles[i].isDirectory()) {
                fileList.add(tFiles[i].getName());
            }
        }
        return fileList;
    }

    /**
     * 获得指定路径下,指定后缀的所有文件
     * 
     * @param path
     * @param fileFilter
     * @return
     */
    public static ArrayList<String> getAllFiles(String path, String fileFilter) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(fileFilter)) {
            return null;
        }

        ArrayList<String> fileList = new ArrayList<String>();
        int count = 0;
        File tFile = new File(path);
        FileFilter11 tFileFilter11 = new FileFilter11(fileFilter);
        File[] tFiles = tFile.listFiles(tFileFilter11);
        if (tFiles == null || tFiles.length <= 0) {
            return fileList;
        }

        count = tFiles.length;
        for (int i = 0; i < count; i++) {
            if (tFiles[i].isFile()) {
                fileList.add(tFiles[i].getName());
            }
        }
        return fileList;
    }

    /**
     * 删除文件
     * 
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File tFile = new File(filePath);
            if (tFile != null && tFile.isFile() && tFile.exists()) {
                return tFile.delete();
            }
        }
        return false;
    }

    /**
     * 判断一个路径下的文件（文件夹）是否存在
     * 
     * @param path
     * @return true存在，false不存在
     */
    public static boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File tFile = new File(path);
        if (tFile != null && tFile.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 判断指定目录下，指定的文件是否存在
     * 
     * @param path
     * @param fileName
     * @return true存在，false不存在
     */
    public static boolean checkFileExist(String path, String fileName) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(fileName)) {
            return false;
        }
        String tPath = path + fileName;
        return isFileExist(tPath);
    }

    /**
     * 删除目录
     * 
     * @param directiory 要删除的目录路径
     * @param fileFilter 文件过滤器
     */
    public static boolean deleteDirectiory(File directiory, FileFilter fileFilter) {
        if (directiory == null || !directiory.isDirectory()) {
            return false;
        }
        if (fileFilter != null) {
            if (!fileFilter.accept(directiory)) {
                return false;
            }
        }
        boolean isSuccess = true;
        File[] tFiles = directiory.listFiles();
        for (File tFile : tFiles) {
            if (tFile.isFile()) {
                if (fileFilter != null) {
                    if (!fileFilter.accept(tFile)) {
                        isSuccess = false;
                        continue;
                    }
                }
                isSuccess = tFile.delete() && isSuccess;
            } else if (tFile.isDirectory()) {
                isSuccess = deleteDirectiory(tFile, fileFilter) && isSuccess;
            }
        }
        isSuccess = directiory.delete() && isSuccess;
        return isSuccess;
    }

    /**
     * 删除目录
     * 
     * @param directiory 要删除的目录路径
     * @param fileFilter 文件过滤器
     */
    public static boolean deleteDirectioryOther(File directiory, FileFilter fileFilter) {
        if (directiory == null || !directiory.isDirectory()) {
            return false;
        }
        if (fileFilter != null) {
            if (!fileFilter.accept(directiory)) {
                return false;
            }
        }
        boolean isSuccess = true;
        File[] tFiles = directiory.listFiles();
        for (File tFile : tFiles) {
            if (tFile.isFile()) {
                if (fileFilter != null) {
                    if (!fileFilter.accept(tFile)) {
                        isSuccess = false;
                        continue;
                    }
                }
                isSuccess = tFile.delete() && isSuccess;
            } else if (tFile.isDirectory()) {
                isSuccess = deleteDirectiory(tFile, fileFilter) && isSuccess;
            }
        }
        return isSuccess;
    }

    /**
     * 获取指定目录的剩余空间
     * 
     * @return 指定目录剩余空间大小，单位：byte
     */
    long getAvailablePathSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        StatFs tStatFs = new StatFs(path);
        long availableSize = tStatFs.getBlockSize() * (tStatFs.getAvailableBlocks() - 4l);
        return availableSize;
    }

    /**
     * 指定的目录不存在，则创建目录
     * 
     * @param path
     * @return
     */
    public static boolean createDirectory(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdir()) {
                path += "/";
                return true;
            }
        }
        return false;
    }

    /**
     * 搜索指定名称的文件夹
     * 
     * @param directiory
     * @param fileFilter
     * @return
     */
    public static File searchFile(File directiory, FileFilter fileFilter) {
        File[] tFiles = directiory.listFiles();
        if (tFiles == null) {
            return null;
        }

        File file = null;
        for (int i = 0; i < tFiles.length; i++) {
            file = tFiles[i];
            if (fileFilter.accept(file)) {
                return file;
            }
            if (file.isDirectory()) {
                File result = searchFile(file, fileFilter);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 搜索指定名称的文件夹
     * 
     * @param directiory
     * @param fileFilter
     * @return
     */

    public static File searchFileOther(File directiory, FileFilter fileFilter) {
        File[] tFiles = directiory.listFiles();
        if (tFiles == null) {
            return null;
        }

        File tFile = null;
        for (int i = 0; i < tFiles.length; i++) {
            for (int j = 0; j < tFiles.length; j++) {
                tFile = tFiles[j];
                LogUtil.d("GdFileUtil", tFile.getPath());
                if (fileFilter.accept(tFile)) {
                    LogUtil.d(tFile.getPath());
                    return tFile;
                }
            }

            tFile = tFiles[i];
            if (tFile.isDirectory()) {
                File result = searchFileOther(tFile, fileFilter);
                if (result != null) {
                    LogUtil.d("GdFileUtil", tFile.getPath());
                    return result;
                }
            }
        }
        return null;
    }
}

/**
 * 文件过滤器
 */
class FileFilter11 implements FileFilter {
    private String mFilter = null;

    public FileFilter11(String fileFilter) {
        mFilter = fileFilter;
    }

    @Override
    public boolean accept(File pathname) {
        String tmp = pathname.getName().toLowerCase();
        if (tmp.endsWith(mFilter)) {
            return true;
        }
        return false;
    }
}
