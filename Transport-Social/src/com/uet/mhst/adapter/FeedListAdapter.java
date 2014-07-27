package com.uet.mhst.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.widget.ProfilePictureView;

import com.uet.mhst.R;
import com.uet.mhst.itemendpoint.model.*;
import com.uet.mhst.volley.AppController;
import com.uet.mhst.volley.FeedImageView;

public class FeedListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Item> Items;
	int i = 0;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

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
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		TextView status = (TextView) convertView.findViewById(R.id.txtStatus);
		TextView content = (TextView) convertView.findViewById(R.id.txtContent);
		final TextView voteUp = (TextView) convertView
				.findViewById(R.id.txtView_vote_up);
		TextView voteDown = (TextView) convertView
				.findViewById(R.id.txtView_vote_down);
		ProfilePictureView profilePic = (ProfilePictureView) convertView
				.findViewById(R.id.profilePic);
		FeedImageView feedImageView = (FeedImageView) convertView
				.findViewById(R.id.feedImage1);
		Button voteUpBtn = (Button) convertView.findViewById(R.id.btn_vote_up);
		Button voteDownBtn = (Button) convertView
				.findViewById(R.id.btn_vote_down);

		voteUpBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		voteDownBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		Item item = Items.get(position);
		name.setText(item.getName());
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(item
				.getTime().getValue(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);
		switch (item.getStatus()) {
		case 1:
			status.setText("Tắc đường");
			break;
		case 2:
			status.setText("Đường đông");
			break;
		case 3:
			status.setText("Tại nạn");
			break;
		case 4:
			status.setText("Bình thường");
			break;
		}
		content.setText(item.getContent());
		profilePic.setProfileId(item.getIdFB());
		if (item.getImg() != null) {
			feedImageView.setImageUrl(item.getImg(), imageLoader);
			feedImageView.setVisibility(View.VISIBLE);
			feedImageView
					.setResponseObserver(new FeedImageView.ResponseObserver() {
						@Override
						public void onError() {
						}

						@Override
						public void onSuccess() {
						}
					});
		} else {
			feedImageView.setVisibility(View.GONE);
		}

		voteUp.setText(String.valueOf(item.getVoteUp()) + " vote up");
		Log.e("VOTE", String.valueOf(item.getVoteUp()));
		voteDown.setText(String.valueOf(item.getVoteDw()) + " vote down");
		return convertView;
	}
}