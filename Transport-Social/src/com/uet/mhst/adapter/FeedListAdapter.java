package com.uet.mhst.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.uet.mhst.R;
import com.uet.mhst.itemendpoint.model.Item;

public class FeedListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Item> Items;
	int i = 0;

	public FeedListAdapter(Activity activity, List<Item> Items) {
		this.activity = activity;
		this.Items = Items;
	}

	@Override
	public int getCount() {
		return Items.size();
	}

	@Override
	public Object getItem(int location) {
		return Items.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);
		Item item = Items.get(position);
		ProfilePictureView pictureFb = (ProfilePictureView) convertView
				.findViewById(R.id.picture_facebook);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		TextView status = (TextView) convertView.findViewById(R.id.txt_status);
		TextView address = (TextView) convertView
				.findViewById(R.id.txt_address);
		TextView content = (TextView) convertView
				.findViewById(R.id.txt_content);

		pictureFb.setProfileId(item.getIdFB());
		name.setText(item.getName());
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(item
				.getTime().getValue(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);
		String var = "";
		switch (item.getStatus()) {
		case 1:
			var = "Tắc đường";
			status.setTextColor(Color.BLUE);

			break;
		case 2:
			var = "Đường đông";
			status.setTextColor(Color.MAGENTA);
			break;
		case 3:
			var = "Tai nạn";
			status.setTextColor(Color.RED);
			break;
		case 4:
			var = "Bình thường";
			status.setTextColor(Color.rgb(225, 209, 223));
			break;
		}
		status.setText("Tình trạng: " + var);
		address.setText(item.getAddress());
		content.setText(item.getContent());
		return convertView;
	}
}