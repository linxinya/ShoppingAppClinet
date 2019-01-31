package com.cm.shopping.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.BaseActivity;
import com.cm.utils.BaseUtil;

public class CommentActivity extends BaseActivity {

	private TextView tvTopTitleCenter;
	private Button btnOK;
	private EditText etBody;

	private int dishesid = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		dishesid = getIntent().getIntExtra("dishesid", 0);
		findview();

	}

	private void findview() {
		tvTopTitleCenter = (TextView) findViewById(R.id.tvTopTitleCenter);
		tvTopTitleCenter.setText("评论");
		etBody = (EditText) findViewById(R.id.etBody);
		btnOK = (Button) findViewById(R.id.btnOK);
		btnOK.setOnClickListener(this);
	}

	private void submit() {
		if (etBody.getText().length() == 0) {
			toastUtil.show("请输入内容");
			return;
		}

		BaseUtil.HideKeyboard(this);

		mParamMaps.clear();

		mParamMaps.put("Action", "createcomment");
		mParamMaps.put("dishesid", dishesid);
		mParamMaps.put("body", etBody.getText());
		mParamMaps.put("userid", user.getId());
		mParamMaps.put("username", user.getName());
		showProgressDialog("处理中...");
		AsyncRequestUtils.newInstance().post(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().equals("1")) {
					toastUtil.show("评论成功");
					setResult(RESULT_OK);
					finish();
				} else {
					toastUtil.show("评论失败");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOK:
			submit();
			break;

		default:
			break;
		}
	}

}
