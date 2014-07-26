package com.uet.mhst.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.uet.mhst.R;
import com.uet.mhst.model.NavDrawerItem;
import com.uet.mhst.sqlite.DatabaseHandler;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
		ProfilePictureView profilePictureView = (ProfilePictureView) convertView
				.findViewById(R.id.profilePicture);
		if (position == 0) {
			DatabaseHandler db = new DatabaseHandler(context);
			imgIcon.setVisibility(View.GONE);
			profilePictureView.setProfileId(db.getUserDetails().get("id"));
			txtTitle.setText(db.getUserDetails().get("name"));
			txtTitle.setTextSize(30);
			txtTitle.setTextColor(Color.BLUE);
		} else {
			profilePictureView.setVisibility(ProfilePictureView.GONE);

			imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
			txtTitle.setText(navDrawerItems.get(position).getTitle());
			if (navDrawerItems.get(position).getCounterVisibility()) {
				txtCount.setText(navDrawerItems.get(position).getCount());
			} else {
				// hide the counter view
				txtCount.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

}