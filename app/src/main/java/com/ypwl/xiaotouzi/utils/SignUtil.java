package com.ypwl.xiaotouzi.utils;


import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;


/**
 * function : 签名算法摘要工具类.
 * </p>
 * Created by lzj on 2015/11/18.
 */
public class SignUtil {

    private static final String TAG = SignUtil.class.getSimpleName();

    /**
     * MD5 加密
     */
    public static String md5(String origin) {
        return md5Encode(origin, "UTF-8");
    }

    /**
     * MD5加密
     *
     * @param str         加密字符串
     * @param charsetname 字符集
     * @return 加密后的字符串, 异常则返回null
     */
    public static String md5Encode(String str, String charsetname) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(charsetname));
            byte[] byteDigest = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte aByteDigest : byteDigest) {
                i = aByteDigest;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String sha1Encode(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(content.getBytes("UTF-8"));
            byte[] byteDigest = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte aByteDigest : byteDigest) {
                i = aByteDigest;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return content;
        }
    }

    /**
     * 摘要文件的MD5值
     *
     * @param file 需要被加密的文件
     * @return 返回加密后的MD5密文摘要字符串
     */
    public static String md5EncodeFile(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            byte[] result = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                String str = Integer.toHexString(b & 0xff);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            fis.close();
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /*
    sign计算方法：
    a=md5(uuid+ imei+ mac)
    b=md5(token)
    c=截取a的前16位
    d=截取b的后16位
    sign=md5(c+d)
    注：md5的计算结果为32位小写字母+数字
    */

    /** 签名登录后上传的设备信息 */
    public static String signDeviceInfoAfterLogin(String uuid, String imei, String mac, String token) {
        LogUtil.e(TAG, "uuid=" + uuid + " ,imei=" + imei + " ,mac=" + mac + " ,token=" + token);
        String result;
        String a = md5(uuid + imei + mac);
        String b = md5(token);
        LogUtil.e(TAG, "a=" + a);
        LogUtil.e(TAG, "b=" + b);
        String c = null;
        String d = null;
        if (!TextUtils.isEmpty(a) && a.length() >= 16) {
            c = a.substring(0, 16);
        }
        if (!TextUtils.isEmpty(b) && b.length() >= 32) {
            d = b.substring(16, 32);
        }
        LogUtil.e(TAG, "c+d = " + (c + d));
        result = md5(c + d);
        LogUtil.e(TAG, "result = " + result);
        return result;
    }

    /*
    sign计算方法：
    a=md5(uuid+chid+version)
    b=md5(brand+model+os_version)
    c=截取a的前16位
    d=截取b的后16位
    sign=md5(c+d)
    */

    /** 签名统计上传的设备信息 */
    public static String signDeviceInfoForStatictics(String uuid, String chid, String version, String brand, String model, String os_version) {
        LogUtil.e(TAG, "uuid=" + uuid + " ,chid=" + chid + " ,version=" + version
                + " ,brand=" + brand + " ,model=" + model + " ,os_version=" + os_version);
        String result;
        String a = md5(uuid + chid + version);
        String b = md5(brand + model + os_version);
        LogUtil.e(TAG, "a=" + a);
        LogUtil.e(TAG, "b=" + b);
        String c = null;
        String d = null;
        if (!TextUtils.isEmpty(a) && a.length() >= 16) {
            c = a.substring(0, 16);
        }
        if (!TextUtils.isEmpty(b) && b.length() >= 32) {
            d = b.substring(16, 32);
        }
        LogUtil.e(TAG, "c+d = " + (c + d));
        result = md5(c + d);
        LogUtil.e(TAG, "result = " + result);
        return result;
    }

}