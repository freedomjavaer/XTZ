package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.DriveSegmentListAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.utils.AMapUtil;


public class DriveRouteDetailActivity extends BaseActivity implements View.OnClickListener {
	private DrivePath mDrivePath;
	private DriveRouteResult mDriveRouteResult;
	private TextView mTitle, mTitleDriveRoute, mDesDriveRoute;
	private ListView mDriveSegmentList;
	private DriveSegmentListAdapter mDriveSegmentListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);

		getIntentData();
		init();
	}

	private void init() {
		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText("驾车路线详情");
		findViewById(R.id.layout_title_back).setOnClickListener(this);
		mTitleDriveRoute = (TextView) findViewById(R.id.firstline);
		mDesDriveRoute = (TextView) findViewById(R.id.secondline);
		String dur = AMapUtil.getFriendlyTime((int) mDrivePath.getDuration());
		String dis = AMapUtil.getFriendlyLength((int) mDrivePath
				.getDistance());
		mTitleDriveRoute.setText(dur + "(" + dis + ")");
		int taxiCost = (int) mDriveRouteResult.getTaxiCost();
		mDesDriveRoute.setText("打车约"+taxiCost+"元");
		mDesDriveRoute.setVisibility(View.VISIBLE);
		configureListView();
	}

	private void configureListView() {
		mDriveSegmentList = (ListView) findViewById(R.id.bus_segment_list);
		mDriveSegmentListAdapter = new DriveSegmentListAdapter(
				this.getApplicationContext(), mDrivePath.getSteps());
		mDriveSegmentList.setAdapter(mDriveSegmentListAdapter);
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		mDrivePath = intent.getParcelableExtra("drive_path");
		mDriveRouteResult = intent.getParcelableExtra("drive_result");
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
