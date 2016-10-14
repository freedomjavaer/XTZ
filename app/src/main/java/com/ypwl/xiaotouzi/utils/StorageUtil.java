package com.ypwl.xiaotouzi.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.ypwl.xiaotouzi.common.Configs;

import java.io.File;
import java.io.IOException;

/**
 * 存储工具类<br/>
 * <br/>
 * 2015年6月19日 - 下午3:13:06
 *
 * @author lizhijun
 */
@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "unused"})
public class StorageUtil {
    private static final String TAG = StorageUtil.class.getSimpleName();

    /** 清除临时文件 */
    public static void clearTempDir(Context context) {
        File[] files = new File(getTempPath(context)).listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    /** 检查是否已挂载SD卡镜像（是否存在SD卡） */
    public static boolean isSDCardMounted() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            Log.w(TAG, "SDCARD is not MOUNTED !");
            return false;
        }
    }

    /** 获取SD卡剩余容量（单位Byte） */
    public static long getSDFreeSize() {
        if (isSDCardMounted()) {
            File path = Environment.getExternalStorageDirectory();// 取得SD卡文件路径
            StatFs sf = new StatFs(path.getPath());
            long blockSize = sf.getBlockSize();// 获取单个数据块的大小(Byte)
            long freeBlocks = sf.getAvailableBlocks();// 空闲的数据块的数量
            return freeBlocks * blockSize; // 返回SD卡空闲大小,单位Byte
        } else {
            return 0;
        }
    }

    /** 获取SD卡总容量（单位Byte） */
    public static long getSDAllSize() {
        if (isSDCardMounted()) {
            File path = Environment.getExternalStorageDirectory();// 取得SD卡文件路径
            StatFs sf = new StatFs(path.getPath());
            long blockSize = sf.getBlockSize();// 获取单个数据块的大小(Byte)
            long allBlocks = sf.getBlockCount(); // 获取所有数据块数
            return allBlocks * blockSize;// 返回SD卡大小（Byte）
        } else {
            return 0;
        }
    }

    /** 获取可用的SD卡路径（若SD卡没有挂载则返回""） */
    public static String getSDCardPath() {
        if (isSDCardMounted()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (!sdcardDir.canWrite()) {
                Log.w(TAG, "SDCARD can not write !");
            }
            return sdcardDir.getAbsolutePath();
        }
        return "";
    }

    /** 获取默认的外部存储目录 */
    public static String getExternalStorageDirectoryPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /** 获取默认的外部存储目录 */
    public static File getExternalStorageDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    /**
     * 获取缓存目录
     *
     * @param context 上下文
     * @return File("/mnt/storage0/") if the phone has SD card,else return File("data/data/com.ypwl.xiaotouzi/cache/")
     */
    public static File getXtzCacheParent(Context context) {
        File externalStorageFile = getExternalStorageDirectory();
        if (null == externalStorageFile) {
            // return context.getDir(Configs.DABAN_BASE_PATH,
            // Context.MODE_WORLD_WRITEABLE);
            return context.getCacheDir();
        } else {
            return externalStorageFile;
        }
    }

    /**
     * 获取缓存目录 daban
     *
     * @param context
     * @return 有SD卡： File("/mnt/storage0/xiaotouzi/"); 无SD卡 ： File("/data/data/com.ypwl.xiaotouzi/cache/xiaotouzi/");
     */
    public static File getXtzCacheRoot(Context context) {
        return getDirInCache(context, Configs.XTZ_BASE_PATH);
    }

    /**
     * 1：如果没有SD卡，获取的是data/data/com.ypwl.xiaotouzi/cache/文件夹下的各文件夹文件 <br/>
     * 2：如果有SD卡，则获取的是eg. /mnt/storage0/ 即SD卡根目录下任何文件夹 <br/>
     * 3：注：只能获取文件夹文件File对象，不能传入具体文件名eg. a.text,传入文件名，否则仍会被创建成文件夹
     *
     * @param context 上下文
     * @param dirName 任何"文件夹"名字
     * @return 无SD卡：eg.: File("/data/data/com.ypwl.xiaotouzi/cache/[参数：dirName]/"); <br/>
     * 有SD卡：eg.: File("/mnt/storage0/[参数：dirName]/");
     */
    public static File getDirInCache(Context context, final String dirName) {
        File dir = new File(getXtzCacheParent(context), dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取缓存目录
     *
     * @param context 上下文
     * @return eg. "/mnt/sdcard0/xiaotouzi/"; or "/data/data/com.ypwl.xiaotouzi/cache/xiaotouzi/";
     */
    public static String getXtzCacheRootPath(Context context) {
        String path = getXtzCacheRoot(context).getPath();
        if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        return path;
    }

    /**
     * 1:如果没有外置SD卡，则获取的是xiaotouzi缓存目录下的各文件 此时返回的是：File("/data/data/com.ypwl.xiaotouzi/cache/xiaotouzi/xx.txt")<br/>
     * 2:如果有外置SD卡，则获取的是外置SD卡根目录下任何的文件<br/>
     *
     * @param context  上下文
     * @param fileName 相对于存储根路径的目录下的"文件名"
     * @return 如果没有外置SD卡 eg.: File("/data/data/com.ypwl.xiaotouzi/cache/[参数：fileName] "); 如果有SD卡 eg.:
     * File("/mnt/storage0/[参数：fileName]"),but the file maybe created fail possibly;
     */
    public static File getFieInCache(Context context, final String fileName) {
        File file = new File(getXtzCacheParent(context), fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtil.e(TAG, "", e);
            }
        }
        return file;
    }

    /** 获取临时目录 */
    public static File getTempDir(Context context) {
        return getDirInCache(context, Configs.XTZ_BASE_TEMP_PATH);
    }

    /** 获取临时文件夹路径 */
    public static String getTempPath(Context context) {
        String path = getTempDir(context).getAbsolutePath();
        if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        return path;
    }

    /** 获取 图片目录 ： ../picture */
    public static File getPictureDir(Context context) {
        return getDirInCache(context, Configs.XTZ_BASE_PICTURE_PATH);
    }

    /** 获取 图片目录路径 ： ../picture/ */
    public static String getPictureDirPath(Context context) {
        String path = getPictureDir(context).getAbsolutePath();
        if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        return path;
    }

}
