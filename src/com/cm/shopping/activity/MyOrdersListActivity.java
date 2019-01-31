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
import com.cm.shopping.adapter.OrdersAdapter;
import com.cm.shopping.bean.orders;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *
 *
 * @author zlus
 *
 */
public class MyOrdersListActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private List<orders> list;
	private OrdersAdapter adapter;
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
		tvTopTitleCenter.setText("我的订单");
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
		mParamMaps.put("Action", "getmyorderslist");
		mParamMaps.put("userid", user.getId());

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				list = new ArrayList<orders>();
				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					list = gson.fromJson(result, new TypeToken<List<orders>>() {
					}.getType());
					adapter = new OrdersAdapter(MyOrdersListActivity.this, list);
					listview1.setAdapter(adapter);
				} else {
					toastUtil.show("没有数据");
				}
			}

		});
	}

	private String[] arg;

	private void showContactDialog() {
		if (list.get(row).getStatus().equals("待付款")) {
			arg = new String[] { "付款", "取消订单" };
		}
		if (list.get(row).getStatus().equals("已付款")) {
			arg = new String[] { "确认收货", "取消订单" };
		}
		if (list.get(row).getStatus().equals("交易完成")) {
			arg = new String[] { "评价" };
		}

		new AlertDialog.Builder(this).setTitle("选择操作")
				.setItems(arg, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (arg[which].equals("取消订单")) {
							delete();
						}
						if (arg[which].equals("付款")) {
							changestatus("已付款");
						}
						if (arg[which].equals("确认收货")) {
							changestatus("交易完成");
						}
						if (arg[which].equals("评价")) {
							intent = new Intent(MyOrdersListActivity.this,
									CommentActivity.class);
							intent.putExtra("dishesid", list.get(row)
									.getDishesid());
							startActivity(intent);
						}
					}
				}).show();
	}

	private void changestatus(String status) {
		showProgressDialog("处理中,请稍后..");
		mParamMaps.clear();
		mParamMaps.put("Action", "ChangeStatus");
		mParamMaps.put("ID", list.get(row).getId());
		mParamMaps.put("status", status);
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (result != null && result.trim().length() > 0) {
					toastUtil.show("操作成功");
					query();
				}
			}

		});
	}

	private void delete() {
		showProgressDialog("处理中,请稍后..");
		buildDeleteMap("orders", list.get(row).getId());
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (result != null && result.trim().length() > 0) {
					toastUtil.show("取消成功");
					list.remove(row);
					adapter.notifyDataSetChanged();
				} else {
					toastUtil.show("取消失败");

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
