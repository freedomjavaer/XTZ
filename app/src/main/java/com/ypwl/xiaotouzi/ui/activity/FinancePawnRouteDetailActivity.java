package com.ypwl.xiaotouzi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.BusResultListAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.utils.AMapUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

/**
 * function : 金融超市---典当：路线规划详情页面
 * <p/>
 * Created by tengtao on 2016/4/18.
 */
public class FinancePawnRouteDetailActivity extends BaseActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter,
        RouteSearch.OnRouteSearchListener, View.OnClickListener {

    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint;//起点，
    private LatLonPoint mEndPoint;//终点，
    private String mCurrentCityName = "深圳";
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;

    private LinearLayout mBusResultLayout;
    private RelativeLayout mBottomLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private ImageView mBus;
    private ImageView mDrive;
    private ImageView mWalk;
    private ListView mBusResultList;
    private KProgressHUD mLoading;//数据搜索进度框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_market_pawn_route_detail);
        mLoading = KProgressHUDHelper.createLoading(this);
        ((TextView)findViewById(R.id.tv_title)).setText("路线选择");
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mStartPoint = getIntent().getParcelableExtra("start");
        mEndPoint = getIntent().getParcelableExtra("end");
        mCurrentCityName = getIntent().getStringExtra("city");
        mContext = this.getApplicationContext();
        mapView = (MapView) findViewById(R.id.route_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        setfromandtoMarker();
        /** 默认自驾 */
        searchRoute(ROUTE_TYPE_DRIVE);
    }

    /** 标记起点和终点 */
    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        mDrive = (ImageView)findViewById(R.id.route_drive);
        mBus = (ImageView)findViewById(R.id.route_bus);
        mWalk = (ImageView)findViewById(R.id.route_walk);
        mBusResultList = (ListView) findViewById(R.id.bus_result_list);
    }

    /** 注册监听 */
    private void registerListener() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
        findViewById(R.id.pawn_detail_by_drive).setOnClickListener(this);
        findViewById(R.id.pawn_detail_by_bus).setOnClickListener(this);
        findViewById(R.id.pawn_detail_by_walk).setOnClickListener(this);
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
    public void onInfoWindowClick(Marker arg0) {

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pawn_detail_by_drive://自驾
                searchRoute(ROUTE_TYPE_DRIVE);
                break;
            case R.id.pawn_detail_by_bus://公交
                searchRoute(ROUTE_TYPE_BUS);
                break;
            case R.id.pawn_detail_by_walk://步行
                searchRoute(ROUTE_TYPE_WALK);
                break;
            case R.id.layout_title_back:
                finish();
                break;
        }
    }

    private void searchRoute(int type){
        switch (type){
            case ROUTE_TYPE_DRIVE://自驾
                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                break;
            case ROUTE_TYPE_BUS://公交
                searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
                break;
            case ROUTE_TYPE_WALK://步行
                searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                break;
        }
        mDrive.setImageResource(type == ROUTE_TYPE_DRIVE ? R.mipmap.route_drive_select : R.mipmap.route_drive_normal);
        mBus.setImageResource(type == ROUTE_TYPE_BUS ? R.mipmap.route_bus_select : R.mipmap.route_bus_normal);
        mWalk.setImageResource(type == ROUTE_TYPE_WALK ? R.mipmap.route_walk_select : R.mipmap.route_walk_normal);
        mapView.setVisibility(type == ROUTE_TYPE_BUS ? View.GONE : View.VISIBLE);
        mBusResultLayout.setVisibility(type == ROUTE_TYPE_BUS ? View.VISIBLE : View.GONE);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            UIUtil.showToastShort("定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            UIUtil.showToastShort("终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
        mBottomLayout.setVisibility(View.GONE);
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
                    mBusResultList.setAdapter(mBusResultListAdapter);
                } else if (result != null && result.getPaths() == null) {
                    UIUtil.showToastShort("对不起，没有搜到相关数据");
                }
            } else {
                UIUtil.showToastShort("对不起，没有搜到相关数据");
            }
        } else {
            UIUtil.showToastShort("服务异常，请重新搜索");
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(), mDriveRouteResult.getTargetPos());
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约"+taxiCost+"元");
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, DriveRouteDetailActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result", mDriveRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    UIUtil.showToastShort("对不起，没有搜到相关数据");
                }

            } else {
                UIUtil.showToastShort("对不起，没有搜到相关数据");
            }
        } else {
            UIUtil.showToastShort("服务异常，请重新搜索");
        }


    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths().get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, WalkRouteDetailActivity.class);
                            intent.putExtra("walk_path", walkPath);
                            intent.putExtra("walk_result", mWalkRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    UIUtil.showToastShort("对不起，没有搜到相关数据");
                }
            } else {
                UIUtil.showToastShort("对不起，没有搜到相关数据");
            }
        } else {
            UIUtil.showToastShort("服务异常，请重新搜索");
        }
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        mLoading.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
