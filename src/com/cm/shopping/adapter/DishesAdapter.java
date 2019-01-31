package com.cm.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cm.shopping.activity.R;
import com.cm.shopping.bean.dishes;
import com.cm.utils.AsyncImageLoader;

public class DishesAdapter extends BaseAdapter {
	private List<dishes> list = null;
	private final Context context;
	private LayoutInflater infater = null;

	public DishesAdapter(Context context, List<dishes> list) {
		this.infater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
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
	public View getView(final int position, View convertview, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertview == null) {
			holder = new ViewHolder();
			convertview = infater.inflate(R.layout.listview_item_dishes, null);
			holder.imageView1 = (ImageView) convertview
					.findViewById(R.id.imageView1);
			holder.tvTitle = (TextView) convertview.findViewById(R.id.tvTitle);
			holder.tvPrice = (TextView) convertview.findViewById(R.id.tvPrice);
			holder.tvIntro = (TextView) convertview.findViewById(R.id.tvIntro);
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		holder.tvTitle.setText(list.get(position).getTitle());
		holder.tvPrice.setText("单价：￥" + list.get(position).getPrice() + "\n类型:"
				+ list.get(position).getTypename());
		holder.tvIntro.setText("介绍：" + list.get(position).getIntro());

		holder.tvIntro.setText("销量：【" + list.get(position).getSales() + "】\n"
				+ holder.tvIntro.getText());

		AsyncImageLoader.getInstance().loadBitmap(
				list.get(position).getImg_url(), holder.imageView1);

		return convertview;
	}

	class ViewHolder {
		private ImageView imageView1;
		private TextView tvTitle;
		private TextView tvPrice;
		private TextView tvIntro;

	}

}
