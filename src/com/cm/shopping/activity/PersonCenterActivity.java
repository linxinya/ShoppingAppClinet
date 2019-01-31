package com.cm.shopping.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cm.shopping.activity.R;
import com.cm.shopping.adapter.PersonCenterListAdapter;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;

/**
 *
 *
 * @author zlus
 *
 */
public class PersonCenterActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private PersonCenterListAdapter adapter;
	private ListView listview1;
	private TextView tvTopTitleCenter;
	private final Gson gson = new Gson();
	private List<String> list;
	private int row = 0;
	private final int type = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findview();
		init();
	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText("个人");
		btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setOnClickListener(this);
		// btnTopTitleRight.setText("筛选");
		// btnTopTitleRight.setVisibility(View.VISIBLE);
		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				row = position;
				intent = null;
				if (list.get(row).equals("我的购物车")) {
					intent = new Intent(PersonCenterActivity.this,
							MyCarsListActivity.class);
				}
				if (list.get(row).equals("我的订单")) {
					intent = new Intent(PersonCenterActivity.this,
							MyOrdersListActivity.class);
				}
				if (list.get(row).equals("个人信息")) {
					intent = new Intent(PersonCenterActivity.this,
							RegisterActivity.class);

				}
				if (intent != null) {
					startActivity(intent);
				}

			}

		});
		listview1.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				row = position;
				return true;
			}
		});
	}

	private void init() {
		list = new ArrayList<String>();
		list.add("我的购物车");
		list.add("我的订单");
		list.add("个人信息");
		adapter = new PersonCenterListAdapter(this, list);
		listview1.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnTopTitleRight:

				break;

		}

	}

}
