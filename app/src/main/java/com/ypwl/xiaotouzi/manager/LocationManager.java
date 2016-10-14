package com.ypwl.xiaotouzi.manager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.utils.UIUtil;

/**
 * function :定位管理器
 * </p>
 * Created by lzj on 2016/3/27
 */
public class LocationManager {

    protected final String TAG = LocationManager.class.getSimpleName();
    private static LocationManager instance;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    private static LocationManager getInstance() {
        if (null == instance) {
            synchronized (LocationManager.class) {
                if (null == instance) {
                    instance = new LocationManager();
                }
            }
        }
        return instance;
    }

    private LocationManager() {
    }

    /**
     * 开启进行定位
     *
     * @param callback 定位回调
     */
    public static void startLocation(ILocationCallback callback) {
        getInstance().location(callback);
    }

    private void init() {
        //初始化定位
        mLocationClient = new AMapLocationClient(UIUtil.getContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    private void location(final ILocationCallback callback) {
        callback.onStart();
        init();
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(final AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) { //定位成功回调信息，设置相关消息
                        UIUtil.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onComplete(amapLocation);
                            }
                        });
                    } else {  //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        callback.onError();
                        if (Configs.DEBUG) {
                            UIUtil.showToastShort("定位失败: code = " + amapLocation.getErrorCode() + " , info = " + amapLocation.getErrorInfo());
                        }
                    }
                }
                release();
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    private void release() {
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
    }

    /** 定位回调接口 */
    public interface ILocationCallback {
        void onStart();

        void onError();

        void onComplete(AMapLocation amapLocation);
    }

}
