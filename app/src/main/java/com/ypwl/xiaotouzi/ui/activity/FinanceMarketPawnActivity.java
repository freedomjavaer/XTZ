package com.ypwl.xiaotouzi.ui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.FinanceMarketPawnAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.manager.LocationManager;
import com.ypwl.xiaotouzi.ui.helper.FinancePawnMapPoiOverlayHelper;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import java.util.List;

/**
 * function : 金融超市--典当页面
 * <p/>
 * Created by tengtao on 2016/4/15.
 */
public class FinanceMarketPawnActivity extends BaseActivity implements View.OnClickListener,
        AMap.OnMapClickListener, AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener, FinanceMarketPawnAdapter.OnPawnListItemClickListener{

    private RelativeLayout mErrorView;
    private RecyclerView mPawnSearchResult;
    private FinanceMarketPawnAdapter mPawnAdapter;
    private MapView mapview;
    private AMap mAMap;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp;//自己所在位置
    private Marker locationMarker; // 用户所在点
    private Marker detailMarker;//显示详情所在点
    private Marker mlastMarker;
    private PoiSearch poiSearch;
    private FinancePawnMapPoiOverlayHelper poiOverlayHelper;// poi图层
    private List<PoiItem> poiItems;// poi数据
    private String keyWord = "典当";
    private String city = "深圳";
    private Double mLatitude;//纬度
    private Double mLongitude;//经度
    private KProgressHUD mLoading;

    private int[] markers = {R.drawable.poi_marker_1, R.drawable.poi_marker_2, R.drawable.poi_marker_3,
            R.drawable.poi_marker_4, R.drawable.poi_marker_5, R.drawable.poi_marker_6, R.drawable.poi_marker_7,
            R.drawable.poi_marker_8, R.drawable.poi_marker_9, R.drawable.poi_marker_10,R.drawable.poi_marker_11,
            R.drawable.poi_marker_12, R.drawable.poi_marker_13, R.drawable.poi_marker_14, R.drawable.poi_marker_15,
            R.drawable.poi_marker_16, R.drawable.poi_marker_17, R.drawable.poi_marker_18, R.drawable.poi_marker_19,
            R.drawable.poi_marker_20 };

    private int[] selectedMarkers = {R.drawable.poi_marker_selected_1, R.drawable.poi_marker_selected_2,
            R.drawable.poi_marker_selected_3, R.drawable.poi_marker_selected_4, R.drawable.poi_marker_selected_5,
            R.drawable.poi_marker_selected_6, R.drawable.poi_marker_selected_7, R.drawable.poi_marker_selected_8,
            R.drawable.poi_marker_selected_9, R.drawable.poi_marker_selected_10,R.drawable.poi_marker_selected_11,
            R.drawable.poi_marker_selected_12, R.drawable.poi_marker_selected_13, R.drawable.poi_marker_selected_14,
            R.drawable.poi_marker_selected_15, R.drawable.poi_marker_selected_16, R.drawable.poi_marker_selected_17,
            R.drawable.poi_marker_selected_18, R.drawable.poi_marker_selected_19, R.drawable.poi_marker_selected_20 };

    private RelativeLayout mDataContentView;
    private View mNoDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_market_pawn);
        //mLoading = KProgressHUDHelper.createLoading(this).setDimAmount(0f).show();
        //标题部分
        ((TextView)findViewById(R.id.tv_title)).setText("典当");
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mErrorView = (RelativeLayout) findViewById(R.id.rl_error_layout);
        findViewById(R.id.btn_error_load_again).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_title_back)).setText("金融超市");

        mDataContentView = (RelativeLayout) findViewById(R.id.rl_data_content_view);
        mNoDataView = findViewById(R.id.layout_no_data_view);

        //结果列表
        mPawnSearchResult = (RecyclerView) findViewById(R.id.rv_pawn_search_result_list);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mPawnSearchResult.setLayoutManager(manager);
        mPawnAdapter = new FinanceMarketPawnAdapter(this);
        mPawnAdapter.setOnPawnListItemClickListener(this);
        mPawnSearchResult.setAdapter(mPawnAdapter);
        //地图
        mapview = (MapView)findViewById(R.id.pawn_mapView);
        mapview.onCreate(savedInstanceState);
        //定位
        initLocation();
    }

    /** 定位用户位置 */
    private void initLocation() {
        LocationManager.startLocation(new LocationManager.ILocationCallback() {

            @Override
            public void onStart() {
                ViewUtil.showContentLayout(Const.LAYOUT_LOADING,mNoDataView,mDataContentView);
            }

            @Override
            public void onError() {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR,mNoDataView,mDataContentView);
            }

            @Override
            public void onComplete(AMapLocation amapLocation) {
                city = amapLocation.getCity();//城市
                mLatitude = amapLocation.getLatitude();//纬度
                mLongitude = amapLocation.getLongitude();//经度
                lp = new LatLonPoint(mLatitude, mLongitude);
                init();
                ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mDataContentView);
            }

        });
    }

    /** 初始化AMap对象 */
    private void init() {
        if (mAMap == null) {
            mAMap = mapview.getMap();
            mAMap.setOnMapClickListener(this);
            mAMap.setOnMarkerClickListener(this);
            mAMap.setInfoWindowAdapter(this);
//            mAMap.setOnPOIClickListener(this);
            /**自己当前位置 */
            locationMarker = mAMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.my_location_icon)))
                    .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
            locationMarker.showInfoWindow();
        }
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 16));
        /** 执行搜索 */
        doSearchQuery();
    }

    /** 开始进行poi搜索 */
    protected void doSearchQuery() {
        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyWord, "", city);
        query.setPageSize(20);// 设置每页最多返回多少条
        query.setPageNum(currentPage);// 设置查第一页

        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            // 设置搜索区域为以lp点为圆心，其周围4000米范围
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 10000, true));
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    /** 方法必须重写 */
    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
    }

    /** 方法必须重写 */
    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    /** 方法必须重写 */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    /** 方法必须重写 */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiitem, int rcode) {

    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        // 1000表示搜索成功
        if (rcode == 1000) {
            mErrorView.setVisibility(View.GONE);
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得第一页的poiitem数据，页数从数字0开始
                    poiItems = poiResult.getPois();
                    // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
                    if (poiItems != null && poiItems.size() > 0) {
                        /**结果列表清单加载 */
                        mPawnAdapter.loadData(poiItems, lp, city);
                        //并还原点击marker样式
                        if (mlastMarker != null) {
                            resetlastmarker();
                        }
                        //清理之前搜索结果的marker
                        if (poiOverlayHelper !=null) {
                            poiOverlayHelper.removeFromMap();
                        }
                        mAMap.clear();
                        poiOverlayHelper = new FinancePawnMapPoiOverlayHelper(mAMap, poiItems, markers);
                        poiOverlayHelper.addToMap();
                        poiOverlayHelper.zoomToSpan();

                        mAMap.addMarker(new MarkerOptions()
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.
                                        decodeResource(getResources(), R.mipmap.my_location_icon)))
                                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));

                        /**添加搜索区域标记 */
//                        mAMap.addCircle(new CircleOptions()
//                                .center(new LatLng(lp.getLatitude(), lp.getLongitude()))
//                                .radius(5000).strokeColor(Color.BLUE)
//                                .fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(2));
                    } else if (suggestionCities != null && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        UIUtil.showToastShort("对不起，没有搜索到相关数据！");
                    }
                    mapview.setVisibility(View.VISIBLE);
                    mPawnSearchResult.setVisibility(View.VISIBLE);
                }
            } else {
                UIUtil.showToastShort("对不起，没有搜索到相关数据！");
            }
        }else{
            /** 错误提示 */
            mErrorView.setVisibility(View.VISIBLE);
        }
        //mLoading.dismiss();
    }

    /** 点击地图上的某个结果标记点 */
    @Override
    public boolean onMarkerClick(Marker marker) {
        LogUtil.e(TAG,"onMarkerClick被回调了。。。");
        marker.showInfoWindow();//显示info窗口
        if (marker.getObject() != null) {
            try {
                PoiItem mCurrentPoi = (PoiItem) marker.getObject();
                if (mlastMarker != null) {
                    resetlastmarker();// 将之前被点击的marker置为原来的状态
                }
                mlastMarker = marker;
                detailMarker = marker;
                int poiIndex = poiOverlayHelper.getPoiIndex(marker);
                detailMarker.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(getResources(), selectedMarkers[poiIndex])));
                /** 滚动到点击item位置 */
                int index = poiOverlayHelper.getPoiIndex(mlastMarker);
                ((LinearLayoutManager)mPawnSearchResult.getLayoutManager()).scrollToPositionWithOffset(index,0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            resetlastmarker();
        }
        return true;
//        NaviPara naviPara = new NaviPara();// 构造导航参数
//        naviPara.setTargetPoint(marker.getPosition());// 设置终点位置
//        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);// 设置导航策略，这里是避免拥堵
//        try {
//            AMapUtils.openAMapNavi(naviPara, getApplicationContext());// 调起高德地图导航
//        } catch (com.amap.api.maps.AMapException e) {
//            AMapUtils.getLatestAMapApp(getApplicationContext());// 如果没安装会进入异常，调起下载页面
//        }
//        mAMap.clear();
//        return false;
    }

    /** 将之前被点击的marker置为原来的状态 */
    private void resetlastmarker() {
        mlastMarker.hideInfoWindow();//隐藏infochuangkou
        int index = poiOverlayHelper.getPoiIndex(mlastMarker);
        if (index < 20) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(), markers[index])));
        }else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_pressed)));
        }
        mlastMarker = null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        if (mlastMarker != null) {
            resetlastmarker();
        }
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:" + cities.get(i).getAdCode() + "\n";
        }
        UIUtil.showToastLong(infomation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.btn_error_load_again://重新加载
                //mLoading.show();
                doSearchQuery();
                break;
        }
    }

    /** 点击典当列表条目 */
    @Override
    public void onPawnItemClick(int position) {
        Marker marker = poiOverlayHelper.getMarker(position);
        if(marker.isInfoWindowShown()){
            return;
        }
        onMarkerClick(marker);
    }

//    @Override
//    public void onPOIClick(Poi poi) {
//        mAMap.clear();
//        MarkerOptions markOptiopns = new MarkerOptions();
//        markOptiopns.position(poi.getCoordinate());
//        TextView textView = new TextView(getApplicationContext());
//        textView.setText("到"+poi.getName()+"去");
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.BLACK);
//        textView.setBackgroundResource(R.drawable.custom_info_bubble);
//        markOptiopns.icon(BitmapDescriptorFactory.fromView(textView));
//        mAMap.addMarker(markOptiopns);
//    }
}
