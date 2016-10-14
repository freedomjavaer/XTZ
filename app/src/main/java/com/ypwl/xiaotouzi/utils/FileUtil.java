package com.ypwl.xiaotouzi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类.<br/>
 * Created by lzj on 2015/11/3.
 *
 * @author lizhijun
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    /** 将Base64流转换成bitmap图片 */
    public static Bitmap getImgFromBase64Stream(String base64Str) {
        Bitmap result = null;
        try {
            byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
            result = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 获取以/分割的路径文件名 */
    public static String getFileNameOnUrl(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /** 将asset资产目录下的文件复制到指定目录下 */
    public static File copyFileFromAsset(Context context, final String dir, final String assetFileName) {
        File file = new File(dir, assetFileName);
        if (file.exists() && file.length() > 0) {
            return file;
        }
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            InputStream is = context.getAssets().open(assetFileName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 将文件拷贝到指定目录 */
    public static boolean copyFile(String targetDirPath, File sourceFile) {
        if (targetDirPath == null || targetDirPath.length() == 0) {
            return false;
        }
        if (sourceFile == null || !sourceFile.isFile()) {
            return false;
        }

        File targetFile = new File(targetDirPath, sourceFile.getName());
        if (targetFile.exists()) {
            targetFile.delete();
        }
        try {
            InputStream is = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024 * 10];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param filePath 文件路径
     */
    public static String readFileByLines(String filePath) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),
                    System.getProperty("file.encoding")));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            Log.w(TAG, "IOException:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param filePath 文件路径
     * @param encoding 写文件编码
     */
    public static String readFileByLines(String filePath, String encoding) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            Log.w(TAG, "IOException:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 保存内容
     *
     * @param filePath 文件路径
     * @param content  保存的内容
     */
    public static void saveToFile(String filePath, String content) {
        saveToFile(filePath, content, System.getProperty("file.encoding"));
    }

    /**
     * 指定编码保存内容
     *
     * @param filePath 文件路径
     * @param content  保存的内容
     * @param encoding 写文件编码
     */
    public static void saveToFile(String filePath, String content, String encoding) {
        BufferedWriter writer = null;
        File file = new File(filePath);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), encoding));
                writer.write(content);
            } catch (Exception e) {
                Log.w(TAG, "Exception:" + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 向文件中追加文本
     *
     * @param content 需要追加的内容
     * @param file    待追加文件源
     */
    public static void appendToFile(String content, File file) {
        appendToFile(content, file, System.getProperty("file.encoding"));
    }

    /**
     * 向文件中追加文本
     *
     * @param content  需要追加的内容
     * @param file     待追加文件源
     * @param encoding 文件编码
     */
    public static void appendToFile(String content, File file, String encoding) {
        BufferedWriter writer = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), encoding));
                writer.write(content);
            } catch (Exception e) {
                Log.w(TAG, "Exception:" + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断指定路径的文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    public static Boolean isFileExsit(String filePath) {
        Boolean flag = false;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                flag = true;
            }
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
        }
        return flag;
    }

    /**
     * 快速读取程序应用包下的文件内容
     *
     * @param context  上下文
     * @param filename 文件名称
     * @return 文件内容
     */
    public static String readApplicationFile(Context context, String filename) {
        byte[] data = null;
        FileInputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        try {
            inStream = context.openFileInput(filename);
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        if (null != data) {
            return new String(data);
        } else {
            return "";
        }
    }

    /**
     * 读取指定目录文件的文件内容
     *
     * @param fileName 文件名称
     * @return 文件内容
     */
    public static String readFile(String fileName) {
        byte[] data = null;
        FileInputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        try {
            inStream = new FileInputStream(fileName);
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (null != outStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        if (null != data) {
            return new String(data);
        } else {
            return "";
        }
    }

    /***
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param fileName 文件名称
     * @param encoding 文件编码
     * @return 字符串内容
     */
    public static String readFile(String fileName, String encoding) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /***
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param fileName 文件名称
     * @return 字符串内容
     */
    public static byte[] readFile2Byte(String fileName) {
        byte[] data = null;
        FileInputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        try {
            inStream = new FileInputStream(fileName);
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (null != outStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 读取raw目录的文件内容
     *
     * @param context   内容上下文
     * @param rawFileId raw文件名id
     */
    public static String readRawValue(Context context, int rawFileId) {
        String result = "";
        try {
            InputStream is = context.getResources().openRawResource(rawFileId);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context  内容上下文
     * @param fileName 文件名称，包含扩展名
     */
    public static String readAssetsValue(Context context, String fileName) {
        String result = "";
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            int len = inputStream.available();
            byte[] buffer = new byte[len];
            inputStream.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context  内容上下文
     * @param fileName 文件名称，包含扩展名
     */
    public static List<String> readAssetsListValue(Context context, String fileName) {
        List<String> list = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @param content  文件内容
     */
    public static void write(Context context, String fileName, String content) {
        FileOutputStream outStream = null;
        try {
            outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outStream.write(content.getBytes());
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @param content  文件内容
     */
    public static void writeApplicationFile(Context context, String fileName, byte[] content) {
        FileOutputStream outStream = null;
        try {
            outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outStream.write(content);
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @param modeType 文件写入模式（Context.MODE_PRIVATE、Context.MODE_APPEND、Context.
     *                 MODE_WORLD_READABLE、Context.MODE_WORLD_WRITEABLE）
     * @param content  文件内容
     */
    public static void writeApplicationFile(Context context, String fileName, byte[] content, int modeType) {
        FileOutputStream outStream = null;
        try {
            outStream = context.openFileOutput(fileName, modeType);
            outStream.write(content);
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 指定编码将内容写入目标文件
     *
     * @param target   目标文件
     * @param content  文件内容
     * @param encoding 写入文件编码
     */
    public static void write2File(File target, String content, String encoding) {
        BufferedWriter writer = null;
        try {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
            }
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target, false), encoding));
                writer.write(content);
            } catch (Exception e) {
                Log.w(TAG, "Exception:" + e.getMessage());
                e.printStackTrace();
            }

        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 指定目录写入文件内容
     *
     * @param filePath 文件路径+文件名
     * @param content  文件内容
     */
    public static void write2FileByPath(String filePath, byte[] content) {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            fos.write(content);
            fos.flush();
        } catch (Exception e) {
            Log.w(TAG, "Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.w(TAG, "IOException:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入文件
     */
    public static File writeInputStream(InputStream inputStream, String filePath) {
        OutputStream outputStream = null;
        // 在指定目录创建一个空文件并获取文件对象
        File mFile = new File(filePath);
        if (!mFile.getParentFile().exists())
            mFile.getParentFile().mkdirs();
        try {
            outputStream = new FileOutputStream(mFile);
            byte buffer[] = new byte[4 * 1024];
            int lenght;
            while ((lenght = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, lenght);
            }
            outputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "writeInputStream fail, cause ：" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException ：" + e.getMessage());
            }
        }
        return mFile;
    }

    /**
     * 以JPEG格式保存Bitmap到本地文件
     *
     * @param filePath 文件路径+文件名
     * @param bitmap   文件内容
     */
    public static void saveAsJPEG(Bitmap bitmap, String filePath) {
        FileOutputStream fos = null;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            Log.e(TAG, "Exception ：" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException ：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以PNG格式保存Bitmap到本地文件
     *
     * @param filePath 文件路径+文件名
     * @param bitmap   文件内容
     */
    public static void saveAsPNG(Bitmap bitmap, String filePath) {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
            } catch (Exception e) {
                Log.e(TAG, "Exception ：" + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException ：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 规范file地址为Uri格式
     *
     * @param path 文件路径 eg: /sdcard/0/aa.jpg;
     * @return uri文件路径 eg；file:///sdcard/0/aa.jpg.
     */
    public static String formatFileUri(String path) {
        if (!StringUtil.isEmptyOrNull(path) && !path.startsWith("file://")) {
            path = "file://" + path;
        }
        return path;
    }

    public static String formatFileUri2Normal(String uri) {
        if (!StringUtil.isEmptyOrNull(uri) && uri.startsWith("file://")) {
            uri.replace("file://", "");
        }
        return uri;
    }

    /** 压缩图片至300KB一下 */
    public static File compressFile(File file) {
        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getPath(), bitmapOptions);

            bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, 500, 500);
            bitmapOptions.inJustDecodeBounds = false;
            Bitmap image = BitmapFactory.decodeFile(file.getPath(), bitmapOptions);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            int options = 100;
            while (byteArrayOutputStream.toByteArray().length / 1024 > 300) {
                byteArrayOutputStream.reset();
                image.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);
                options -= 10;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArrayOutputStream.toByteArray());
            fos.flush();
            fos.close();
            return file;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 判断集合存储的标记，为true的总个数
     *
     * @param list
     * @return
     */
    public static int judgeStateNum(List<Boolean> list) {
        int num = 0;
        for (Boolean state : list) {
            if (state == true) {
                num++;
            }
        }
        return num;
    }

}
