package com.cm.shopping.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.shopping.bean.users;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author zlus
 * 
 */
public class CreateOrderActivity extends BaseActivity {
	private int id = 0;
	private Button btnTopTitleRight, btnTopTitleLeft;
	private EditText etPhone, etAddr, etAmount;
	private final Gson gson = new Gson();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createorder);
		id = getIntent().getIntExtra("id", 0);
		findview();
		query();

	}

	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("�ύ����");

		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setText("提交");
		btnTopTitleRight.setVisibility(View.VISIBLE);
		btnTopTitleRight.setOnClickListener(this);

		btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
		btnTopTitleLeft.setVisibility(View.VISIBLE);
		btnTopTitleLeft.setOnClickListener(this);
		btnTopTitleLeft.setText("返回");

		etAmount = (EditText) findViewById(R.id.etAmount);
		etAddr = (EditText) findViewById(R.id.etAddr);
		etPhone = (EditText) findViewById(R.id.etPhone);
	}

	private void query() {
		showProgressDialog("获取中,请稍后..");
		buildGetOneRowMap("users", user.getId());

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					List<users> list = gson.fromJson(result,
							new TypeToken<List<users>>() {
							}.getType());
					if (list != null && list.size() > 0) {
						etPhone.setText(list.get(0).getPhone());
						etAddr.setText(list.get(0).getAddr());
					}
				}
			}

		});
	}

	private void submit() {
		if (TextUtils.isEmpty(etPhone.getText().toString())) {
			toastUtil.show("请输入联系电话");
			return;
		}
		if (TextUtils.isEmpty(etAddr.getText().toString())) {
			toastUtil.show("请输入送货地址");
			return;
		}
		if (TextUtils.isEmpty(etAmount.getText().toString())) {
			toastUtil.show("请输入抢单数量");
			return;
		}
		submitOrder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:
			submit();
			break;
		case R.id.btnTopTitleLeft:
			finish();
			break;
		default:
			break;
		}

	}

	private void submitOrder() {
		mParamMaps.clear();
		mParamMaps.put("Action", "createorder");
		mParamMaps.put("dishesid", id);
		mParamMaps.put("userid", user.getId());
		mParamMaps.put("username", user.getName());
		mParamMaps.put("amount", etAmount.getText());
		mParamMaps.put("phone", etPhone.getText());
		mParamMaps.put("addr", etAddr.getText());
		showProgressDialog("处理中,请稍后..");
		AsyncRequestUtils.newInstance().post(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().equals("1")) {
					toastUtil.show("订单提交成功");
					intent = new Intent(CreateOrderActivity.this,
							MyOrdersListActivity.class);
					startActivity(intent);
					finish();
				} else {
					if (result.trim().equals("-1")) {
						toastUtil.show("库存不足,订单提交失败");
					} else {
						toastUtil.show("订单提交失败");
					}

				}
			}

		});
	}

}
