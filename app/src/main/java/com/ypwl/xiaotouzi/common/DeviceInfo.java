package com.ypwl.xiaotouzi.common;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.ypwl.xiaotouzi.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.UUID;

/**
 * 本地设备信息配置<br/>
 * 包含设备屏幕信息、系统语言、设备唯一ID <br/>
 * Created by lzj on 2015/11/2.
 */
@SuppressWarnings("unused")
public final class DeviceInfo {
    private static final String TAG = DeviceInfo.class.getSimpleName();
    /**
     * 设备类型
     */
    public static final int DEVICE_TYPE_android = 2;
    /**
     * 屏幕高度
     */
    public static int ScreenHeightPixels = -1;
    /**
     * 屏幕宽度
     */
    public static int ScreenWidthPixels = -1;
    /**
     * 屏幕密度
     */
    public static float ScreenDensity = -1;
    /**
     * 屏幕密度
     */
    public static int ScreenDensityDpi = -1;
    /**
     * 屏幕字体密度
     */
    public static float ScreenScaledDensity = -1;
    /**
     * 初次运行时系统语言环境
     */
    public static String systemLastLocale = null;
    /**
     * 当前设备的IMEI
     */
    public static String deviceIMEI;
    /**
     * 当前设备的MAC地址
     */
    public static String deviceMAC;
    /**
     * 该设备在此程序上的唯一标识符
     */
    public static String deviceUUID;

    private static final String INSTALLATION = "INSTALLATION-" + UUID.nameUUIDFromBytes("androidkit".getBytes());
    /** 设备品牌厂商信息 */
    public static String DEVICES_INFO = "(android; BRAND=" + Build.BRAND + "; MODEL=" + Build.MODEL + "; OS=" + Build.VERSION.RELEASE + " )";

    /**
     * 初始化本地配置
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        initDisplayMetrics(context);
        systemLastLocale = Locale.getDefault().toString();
        deviceIMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        initMacAddress(context);
        initDevicesID(context);

        printfDeviceInfo();
    }

    /**
     * 屏幕信息获取<br/>
     * 使用Resources获取
     */
    private static void initDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScreenHeightPixels = dm.heightPixels;// 高
        ScreenWidthPixels = dm.widthPixels;// 宽
        ScreenDensity = dm.density;// 密度
        ScreenDensityDpi = dm.densityDpi;//
        ScreenScaledDensity = dm.scaledDensity;
    }

    private static void printfDeviceInfo() {
        LogUtil.i(TAG, "----------DeviceInfo start----------");
        LogUtil.i(TAG, "DEBUG:", Configs.DEBUG);
        LogUtil.i(TAG, "ScreenHeightPixels:", ScreenHeightPixels, ", ScreenWidthPixels:", ScreenWidthPixels);
        LogUtil.i(TAG, "ScreenDensity:", ScreenDensity, ", ScreenDensityDpi:", ScreenDensityDpi, ", ScreenScaledDensity:", ScreenScaledDensity);
        LogUtil.i(TAG, "systemLastLocale:", systemLastLocale);
        LogUtil.i(TAG, "deviceIMEI:", deviceIMEI, " , deviceMAC:", deviceMAC, " , deviceUUID:", deviceUUID);
        LogUtil.i(TAG, "----------DeviceInfo end----------");
    }

    /**
     * 获取mac地址
     */
    private static void initMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager != null && manager.getConnectionInfo() != null) {
            deviceMAC = manager.getConnectionInfo().getMacAddress();
        }
    }

    /**
     * 返回该设备在此程序上的唯一标识符。
     *
     * @param context Context对象。
     */
    public static void initDevicesID(Context context) {
        if (deviceUUID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists()) {
                    writeInstallationFile(context, installation);
                }
                deviceUUID = readInstallationFile(installation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将表示此设备在该程序上的唯一标识符写入程序文件系统中。
     *
     * @param installation 保存唯一标识符的File对象。
     * @return 唯一标识符。
     * @throws IOException
     */
    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(installation, "r");
        byte[] bs = new byte[(int) accessFile.length()];
        accessFile.readFully(bs);
        accessFile.close();
        return new String(bs);
    }

    /**
     * 读出保存在程序文件系统中的表示该设备在此程序上的唯一标识符。
     *
     * @param installation 保存唯一标识符的File对象。
     * @throws IOException
     */
    private static void writeInstallationFile(Context context, File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String uuid = UUID.nameUUIDFromBytes(Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID).getBytes()).toString();
        out.write(uuid.getBytes());
        out.close();
    }
}
