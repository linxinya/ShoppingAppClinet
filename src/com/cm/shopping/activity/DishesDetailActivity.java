package com.cm.shopping.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.shopping.adapter.CommentAdapter;
import com.cm.shopping.bean.comments;
import com.cm.utils.AsyncImageLoader;
import com.cm.utils.BaseActivity;
import com.cm.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author zlus
 * 
 */
public class DishesDetailActivity extends BaseActivity {
	private int id = 0;
	private ImageView imageView1;
	private AsyncImageLoader asyncImageLoader;
	private String serverUrl;

	private TextView tvIntro;

	private Button btnTopTitleRight, btnTopTitleLeft;
	private com.cm.shopping.activity.CommonApplication application;
	private ListView listview1;
	private CommentAdapter adapter;
	private Button btnAddCar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dishedetail);
		application = (com.cm.shopping.activity.CommonApplication) getApplicationContext();
		findview();
		asyncImageLoader = new AsyncImageLoader(BitmapFactory.decodeResource(
				getResources(), R.drawable.pc_loading_fali));
		serverUrl = AppConstant.getRootUrl(this);
		if (getIntent() != null) {
			id = getIntent().getIntExtra("id", 0);
			query();
		}

	}

	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("详情");
		imageView1 = (ImageView) findViewById(R.id.imageView1);

		tvIntro = (TextView) findViewById(R.id.tvIntro);

		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setText("抢单");
		btnTopTitleRight.setVisibility(View.VISIBLE);
		btnTopTitleRight.setOnClickListener(this);

		btnAddCar = (Button) findViewById(R.id.btnAddCar);
		btnAddCar.setOnClickListener(this);

		listview1 = (ListView) findViewById(R.id.listview1);
	}

	private void query() {
		showProgressDialog("获取中,请稍后..");
		buildGetOneRowMap("dishes", id);
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (result.trim().length() > 0) {
					try {
						jsonArray = new JSONArray(result);
						jsonObject = jsonArray.getJSONObject(0);

						if (!TextUtils.isEmpty(jsonObject.getString("img_url"))) {
							asyncImageLoader.loadBitmap(
									serverUrl + "UploadFile/"
											+ jsonObject.getString("img_url"),
									imageView1);
						}
						String intro = "名称:" + jsonObject.getString("title")
								+ "\n";
						intro += "单价:¥" + jsonObject.getString("price") + "\n";
						intro += "库存:" + jsonObject.getString("amount") + "\n";
						intro += "简介:" + jsonObject.getString("intro") + "\n";
						intro += "评论:";
						tvIntro.setText(intro);

						queryComment();
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}

		});
	}

	private void submit() {
		mParamMaps.clear();
		mParamMaps.put("Action", "addcar");
		mParamMaps.put("proid", id);
		mParamMaps.put("userid", user.getId());
		dialog = ProgressDialog.show(this, "提示", "处理中,请稍后..");
		AsyncRequestUtils.newInstance().post(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (result.trim().equals("1")) {
					toastUtil.show("添加成功");
				} else if (result.trim().equals("-1")) {
					toastUtil.show("该商品已经存在购物车,不用重复添加");
				} else {
					toastUtil.show("添加失败");
				}

			}

		});
	}

	private void queryComment() {
		mParamMaps.clear();
		mParamMaps.put("Action", "getComment");
		mParamMaps.put("dishesid", id);

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().length() > 0) {
					List<comments> list = new Gson().fromJson(result,
							new TypeToken<List<comments>>() {
							}.getType());
					adapter = new CommentAdapter(DishesDetailActivity.this,
							list);
					listview1.setAdapter(adapter);
					UIUtils.setListViewHeightBasedOnChildren(listview1);
				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:
			intent = new Intent(DishesDetailActivity.this,
					CreateOrderActivity.class);
			intent.putExtra("id", id);
			startActivityForResult(intent, 1);
			break;
		case R.id.btnAddCar:
			submit();
			break;
		default:
			break;
		}

	}

}
