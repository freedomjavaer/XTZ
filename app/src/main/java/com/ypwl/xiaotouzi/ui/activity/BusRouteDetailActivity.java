package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.BusSegmentListAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.utils.AMapUtil;

public class BusRouteDetailActivity extends BaseActivity implements View.OnClickListener {

	private BusPath mBuspath;
	private BusRouteResult mBusRouteResult;
	private TextView mTitle, mTitleBusRoute, mDesBusRoute;
	private ListView mBusSegmentList;
	private BusSegmentListAdapter mBusSegmentListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		getIntentData();
		init();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent != null) {
			mBuspath = intent.getParcelableExtra("bus_path");
			mBusRouteResult = intent.getParcelableExtra("bus_result");
		}
	}

	private void init() {
		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText("公交路线详情");
		findViewById(R.id.layout_title_back).setOnClickListener(this);
		mTitleBusRoute = (TextView) findViewById(R.id.firstline);
		mDesBusRoute = (TextView) findViewById(R.id.secondline);
		String dur = AMapUtil.getFriendlyTime((int) mBuspath.getDuration());
		String dis = AMapUtil.getFriendlyTime((int) mBuspath.getDistance());
		mTitleBusRoute.setText(dur + "(" + dis + ")");
		int taxiCost = (int) mBusRouteResult.getTaxiCost();
		mDesBusRoute.setText("打车约"+taxiCost+"元");
		mDesBusRoute.setVisibility(View.VISIBLE);
		configureListView();
	}

	private void configureListView() {
		mBusSegmentList = (ListView) findViewById(R.id.bus_segment_list);
		mBusSegmentListAdapter = new BusSegmentListAdapter(getApplicationContext(), mBuspath.getSteps());
		mBusSegmentList.setAdapter(mBusSegmentListAdapter);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.layout_title_back:
				finish();
				break;
		}
	}
}
