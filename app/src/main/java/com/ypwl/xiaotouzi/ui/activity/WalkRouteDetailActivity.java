package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.WalkPath;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.WalkSegmentListAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.utils.AMapUtil;

public class WalkRouteDetailActivity extends BaseActivity implements View.OnClickListener {
	private WalkPath mWalkPath;
	private TextView mTitle,mTitleWalkRoute;
	private ListView mWalkSegmentList;
	private WalkSegmentListAdapter mWalkSegmentListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		getIntentData();
		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText("步行路线详情");
		findViewById(R.id.layout_title_back).setOnClickListener(this);
		mTitleWalkRoute = (TextView) findViewById(R.id.firstline);
		String dur = AMapUtil.getFriendlyTime((int) mWalkPath.getDuration());
		String dis = AMapUtil.getFriendlyLength((int) mWalkPath.getDistance());
		mTitleWalkRoute.setText(dur + "(" + dis + ")");
		mWalkSegmentList = (ListView) findViewById(R.id.bus_segment_list);
		mWalkSegmentListAdapter = new WalkSegmentListAdapter(getApplicationContext(), mWalkPath.getSteps());
		mWalkSegmentList.setAdapter(mWalkSegmentListAdapter);

	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		mWalkPath = intent.getParcelableExtra("walk_path");
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
