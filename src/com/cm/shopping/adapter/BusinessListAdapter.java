package com.cm.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cm.shopping.activity.R;
import com.cm.shopping.activity.AppConstant;
import com.cm.shopping.bean.business;
import com.cm.utils.AsyncImageLoader;

public class BusinessListAdapter extends BaseAdapter {
	private List<business> list = null;
	private final Context context;
	private LayoutInflater infater = null;

	private final AsyncImageLoader asyncImageLoader;
	private final String serverUrl;

	public BusinessListAdapter(Context context, List<business> list) {
		this.infater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		asyncImageLoader = new AsyncImageLoader(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.pc_loading_fali));
		serverUrl = AppConstant.getRootUrl(context);
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertview == null) {
			holder = new ViewHolder();
			convertview = infater.inflate(R.layout.listview_item_dishes, null);
			holder.imageView1 = (ImageView) convertview
					.findViewById(R.id.imageView1);
			holder.tvTitle = (TextView) convertview.findViewById(R.id.tvTitle);
			holder.tvPrice = (TextView) convertview.findViewById(R.id.tvPrice);
			holder.tvIntro = (TextView) convertview.findViewById(R.id.tvIntro);

			holder.tvPrice.setVisibility(View.GONE);

			LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) holder.imageView1
					.getLayoutParams();
			lParams.height = (int) (lParams.width * 0.9);
			lParams.width = lParams.height;
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		holder.tvTitle.setText(list.get(position).getName());
		holder.tvIntro.setText("ΩÈ…‹:" + list.get(position).getIntro());
		asyncImageLoader.loadBitmap(
				serverUrl + "UploadFile/" + list.get(position).getImg_url(),
				holder.imageView1);
		return convertview;
	}

	class ViewHolder {
		private ImageView imageView1;
		private TextView tvTitle;
		private TextView tvPrice;
		private TextView tvIntro;

	}

}
