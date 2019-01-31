package com.cm.shopping.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.shopping.adapter.CarsAdapter;
import com.cm.shopping.bean.tb_car;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 
 * @author zlus
 * 
 */
public class MyCarsListActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private List<tb_car> list;
	private CarsAdapter adapter;
	private ListView listview1;
	private TextView tvTopTitleCenter;
	private final Gson gson = new Gson();
	private int row;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findview();
		query();
	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText("我的购物车");
		btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
		btnTopTitleLeft.setVisibility(View.VISIBLE);
		btnTopTitleLeft.setOnClickListener(this);
		btnTopTitleLeft.setText("返回");

		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				row = position;
				showContactDialog();
			}
		});

	}

	private void query() {
		showProgressDialog("获取中,请稍后..");
		mParamMaps.clear();
		mParamMaps.put("Action", "getcarslist");
		mParamMaps.put("userid", user.getId());

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				list = new ArrayList<tb_car>();
				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					list = gson.fromJson(result, new TypeToken<List<tb_car>>() {
					}.getType());
					adapter = new CarsAdapter(MyCarsListActivity.this, list);
					listview1.setAdapter(adapter);
				} else {
					toastUtil.show("没有数据");
				}
			}

		});
	}

	// ���������Ĳ˵�
	private void showContactDialog() {
		final String[] arg = new String[] { "抢单", "删除" };
		new AlertDialog.Builder(this).setTitle("选择操作")
				.setItems(arg, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (arg[which].equals("抢单")) {
							intent = new Intent(MyCarsListActivity.this,
									CreateOrderActivity.class);
							intent.putExtra("id", list.get(row).getProid());
							startActivityForResult(intent, 1);
						}

						if (arg[which].equals("删除")) {
							delete();
						}
					}
				}).show();
	}

	private void delete() {
		showProgressDialog("处理中,请稍后..");
		buildDeleteMap("tb_car", list.get(row).getId());
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (result != null && result.trim().length() > 0) {
					toastUtil.show("删除成功");
					list.remove(row);
					adapter.notifyDataSetChanged();
				} else {
					toastUtil.show("删除失败");

				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleLeft:
			finish();
			break;
		default:
			break;
		}

	}

}
