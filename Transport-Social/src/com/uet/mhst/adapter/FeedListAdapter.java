package com.uet.mhst.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.os.Bundle;
import android.os.StrictMode;
import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.ProfilePictureView;
import com.uet.mhst.R;
import com.uet.mhst.itemendpoint.model.*;

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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);
		Item item = Items.get(position);
		ProfilePictureView pictureFb = (ProfilePictureView) convertView
				.findViewById(R.id.picture_facebook);
		final TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		TextView status = (TextView) convertView.findViewById(R.id.txt_status);
		TextView address = (TextView) convertView
				.findViewById(R.id.txt_address);
		TextView content = (TextView) convertView
				.findViewById(R.id.txt_content);
		TextView txt_like = (TextView) convertView.findViewById(R.id.txt_like);
		TextView txt_dislike = (TextView) convertView
				.findViewById(R.id.txt_dislike);
		TextView txt_comment = (TextView) convertView
				.findViewById(R.id.txt_comment);
		List<Vote> votes = item.getVote();
		int voteup = 0;
		int votedown = 0;
		if (votes != null) {
			for (int i = 0; i < votes.size(); i++) {
				Vote vote = votes.get(i);
				if (vote.getUp() == true) {
					voteup++;
				} else {
					votedown++;
				}
			}
		}
		List<Comment> comments = item.getComment();
		txt_comment.setText("0");
		if (comments != null) {
			txt_comment.setText(String.valueOf(comments.size()));
		}
		txt_like.setText(String.valueOf(voteup));
		txt_dislike.setText(String.valueOf(votedown));
		pictureFb.setProfileId(item.getIdFB());
		Session session = Session.getActiveSession();
		Bundle params = new Bundle();
		params.putString("fields", "name,picture");
		try {
			new Request(session, "/" + String.valueOf(item.getIdFB()), params,
					HttpMethod.GET, new Request.Callback() {
						public void onCompleted(Response response) {
							/* handle the result */
							GraphObject g = response.getGraphObject();
							g.getProperty("name");
							name.setText(g.getProperty("name").toString());
						}
					}).executeAsync();
		} catch (Exception e) {

		}
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(item
				.getTime().getValue(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);
		String var = "";
		switch (item.getStatus()) {
		case 3:
			var = "Tắc đường";
			break;
		case 2:
			var = "Đường đông";
			break;
		case 4:
			var = "Tai nạn";
			break;
		case 1:
			var = "Bình thường";
			break;
		}
		status.setText("Tình trạng: " + var);
		address.setText(item.getAddress());
		content.setText(item.getContent());
		return convertView;
	}
}