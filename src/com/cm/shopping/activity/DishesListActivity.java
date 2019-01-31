package com.cm.shopping.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.shopping.adapter.DishesAdapter;
import com.cm.shopping.bean.dishes;
import com.cm.shopping.bean.types;
import com.cm.utils.ActivityUtils;
import com.cm.utils.BaseActivity;
import com.cm.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 
 * @author zlus
 * 
 */
public class DishesListActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private List<dishes> list;
	private DishesAdapter adapter;
	private ListView listview1;
	private TextView tvTopTitleCenter;
	private String keyword = "";
	private final Gson gson = new Gson();
	private List<types> typelist;
	private int typeid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findview();
		query();
	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText(getString(R.string.app_name));

		btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
		btnTopTitleLeft.setVisibility(View.VISIBLE);
		btnTopTitleLeft.setOnClickListener(this);
		btnTopTitleLeft.setText("个人");

		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setVisibility(View.VISIBLE);
		btnTopTitleRight.setOnClickListener(this);
		btnTopTitleRight.setText("搜索");

		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DishesListActivity.this,
						DishesDetailActivity.class);
				intent.putExtra("id", list.get(position).getId());
				startActivity(intent);

			}
		});

	}

	private void query() {
		showProgressDialog("获取中,请稍后..");
		mParamMaps.clear();
		mParamMaps.put("Action", "getdisheslist");
		mParamMaps.put("keyword", keyword);
		mParamMaps.put("typeid", typeid);
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					list = gson.fromJson(result, new TypeToken<List<dishes>>() {
					}.getType());
					adapter = new DishesAdapter(DishesListActivity.this, list);
					listview1.setAdapter(adapter);
				} else {
					toastUtil.show("没有数据");
				}

				querytype();
			}

		});
	}

	private void querytype() {
		mParamMaps.clear();
		mParamMaps.put("Action", "getOneRow");
		mParamMaps.put("Table", "types");
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					typelist = gson.fromJson(result,
							new TypeToken<List<types>>() {
							}.getType());

				} else {
					typelist = new ArrayList<types>();
				}

			}
		});
	}

	private void dialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.layout_search,
				null);
		final EditText etName = (EditText) view.findViewById(R.id.etName);
		final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
		String[] strs = new String[typelist.size() + 1];
		strs[0] = "所有";
		for (int i = 0; i < typelist.size(); i++) {
			strs[i + 1] = typelist.get(i).getTypename();
		}
		UIUtils.bindSpinner(this, spinner1, strs);
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("搜索")
				.setIcon(android.R.drawable.ic_dialog_info).setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						keyword = etName.getText().toString();

						if (spinner1.getSelectedItemPosition() != 0) {
							typeid = typelist.get(
									spinner1.getSelectedItemPosition() - 1)
									.getId();
						} else {
							typeid = 0;
						}
						query();
					}

				}).setNegativeButton("取消", null).create();
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:
			dialog();
			break;
		case R.id.btnTopTitleLeft:

			ActivityUtils.startActivity(PersonCenterActivity.class);
			break;

		default:
			break;
		}

	}

}
