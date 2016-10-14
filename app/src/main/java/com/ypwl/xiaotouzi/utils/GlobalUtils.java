package com.ypwl.xiaotouzi.utils;

import android.content.Context;
import android.os.Environment;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/17.
 */
public class GlobalUtils {

    /*long类型时间戳转换成字符窜*/
    public static String getDataByLong(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(time * 1000);
        return sf.format(d);
    }

    /*String类型时间戳转换成字符窜*/
    public static String getDataByString(String time) {
        if ("".equals(time) || time == null) {
            return "--";
        }
        long longTime = Long.parseLong(time) * 1000;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(longTime);
        return sf.format(d);
    }

    /*String类型时间戳转换成字符窜*/
    public static String dateFormat(String time) {
        if ("".equals(time) || time == null) {
            return "--";
        }
        long longTime = Long.parseLong(time) * 1000;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Date d = new Date(longTime);
        return sf.format(d);
    }

    //
    public static String token;


    public static int STATUS = 2;//统计分析---收益排行---状态类型
    public static int B_TYPE = 1;//统计分析---收益排行---项目类型

    /**
     * 根据返回的String判断是否token不匹配
     *
     * @return true:token有效不需重登陆。false:重登陆
     */
    public static boolean isTokenMatach(String result) {
        if (result == null) {
            return false;
        }
        return !result.contains("{\"status\":1202}");
    }

    /**
     * 根据返回的String判断是否成功正确
     */
    public static boolean responseOK(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int status = jsonObject.optInt("status");
            String ret_message = jsonObject.optString("ret_message");
            if (status == 0) {
                return true;
            } else {
                UIUtil.showToastShort(ret_message);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取逗号隔开的字符串的各段
     *
     * @param longString "2,4"  ---->    2~4个月   2~4%
     * @param index      想要哪一段
     * @return 2 或 4
     */
    public static int getIntByDouString(String longString, int index) {
        String s;
        if (longString.endsWith("个月")) {
            s = longString.split("个月")[0];//2~4
        } else {
            s = longString.split("%")[0]; //2~4
        }

        String[] strings = s.split("~");

        return Integer.parseInt(strings[index]);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "ContactBackup" + File.separator + "backup.dat");

    /**
     * 获取数据(搜索历史)
     */
    public ArrayList getData() {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        ArrayList arrayList = new ArrayList();
        //取出数据
        try {
            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList savedArrayList = (ArrayList) objectInputStream.readObject();
            arrayList.addAll(savedArrayList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;

    }


    /**
     * 检测字符串是否为空
     *
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        if (input == null || input.length() == 0 || input.equals("null"))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是手号码
     */
    public static boolean isTelNum(String telnum) {
//        return telnum.matches("^1\\d{10}$");
        return telnum.matches("^[1][34578][0-9]{9}$");
        /*if(11 == telnum.length()){
            return  true;
        }else {
            return  false;
        }*/
    }

    /**
     * http请求的url参数进行URL编码
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return "";
    }

}
