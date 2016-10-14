package com.ypwl.xiaotouzi.ui.helper;

import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 典当地图覆盖物（标记数据）帮助类
 * <p/>
 * Created by tengtao on 2016/4/18.
 */
public class FinancePawnMapPoiOverlayHelper {
    private AMap mamap;
    private List<PoiItem> mPois;
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
    private int[] mMarkers;

    public FinancePawnMapPoiOverlayHelper(AMap amap, List<PoiItem> pois, int[] markers) {
        mamap = amap;
        mPois = pois;
        mMarkers = markers;
    }

    /**
     * 添加Marker到地图中
     */
    public void addToMap() {
        for (int i = 0; i < mPois.size(); i++) {
            Marker marker = mamap.addMarker(getMarkerOptions(i));
            PoiItem item = mPois.get(i);
            marker.setObject(item);
            mPoiMarks.add(marker);
        }
    }

    /**
     * 去掉PoiOverlay上所有的Marker
     */
    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
    }

    /**
     * 移动镜头到当前的视角
     */
    public void zoomToSpan() {
        if (mPois != null && mPois.size() > 0) {
            if (mamap == null)
                return;
            LatLngBounds bounds = getLatLngBounds();
            mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                    mPois.get(i).getLatLonPoint().getLongitude()));
        }
        return b.build();
    }

    private MarkerOptions getMarkerOptions(int index) {
        return new MarkerOptions()
                .position(new LatLng(mPois.get(index).getLatLonPoint().getLatitude(),
                        mPois.get(index).getLatLonPoint().getLongitude()))
                .title(getTitle(index)).snippet(getSnippet(index))
                .icon(getBitmapDescriptor(index));
    }

    protected String getTitle(int index) {
        return mPois.get(index).getTitle();
    }

    protected String getSnippet(int index) {
        return mPois.get(index).getSnippet();
    }

    /**
     * 从marker中得到poi在list的位置。
     *
     * @param marker 一个标记的对象。
     * @return 返回该marker对应的poi在list的位置。
     */
    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < mPoiMarks.size(); i++) {
            if (mPoiMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

    /** 获取标记对象 */
    public Marker getMarker(int index){
        return mPoiMarks.get(index);
    }

    /** 获取经纬度坐标点 */
    public LatLonPoint getLatLonPoint(Marker marker){
        int index = getPoiIndex(marker);
        if(index==-1){return null;}
        return mPois.get(index).getLatLonPoint();
    }

    /** 获取经纬度坐标点 */
    public LatLonPoint getLatLonPoint(int position){
        return mPois.get(position).getLatLonPoint();
    }

    /**
     * 返回第index的poi的信息。
     *
     * @param index 第几个poi。
     * @return poi的信息。
     */
    public PoiItem getPoiItem(int index) {
        if (index < 0 || index >= mPois.size()) {
            return null;
        }
        return mPois.get(index);
    }

    protected BitmapDescriptor getBitmapDescriptor(int arg0) {
        if (arg0 < 20) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(XtzApp.getApplication().getResources(), mMarkers[arg0]));
            return icon;
        } else {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(XtzApp.getApplication().getResources(), R.drawable.poi_marker_pressed));
            return icon;
        }
    }
}
