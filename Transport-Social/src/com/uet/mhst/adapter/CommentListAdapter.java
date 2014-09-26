package com.uet.mhst.adapter;

import java.util.ArrayList;
import java.util.List;

import com.facebook.widget.ProfilePictureView;
import com.uet.mhst.R;
import com.uet.mhst.model.Cmt;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter {

	private Context context;
	private List<Cmt> commentItems;

	public CommentListAdapter(Context context, List<Cmt> commentItems) {
		this.commentItems = new ArrayList<Cmt>();
		this.context = context;
		if (commentItems != null) {
			this.commentItems = commentItems;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commentItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.comment_list_item, null);
		}
		ProfilePictureView pictureFb = (ProfilePictureView) convertView
				.findViewById(R.id.picture_facebook);
		TextView comment = (TextView) convertView
				.findViewById(R.id.txt_comment);
		TextView nameFacebook = (TextView) convertView
				.findViewById(R.id.nameFacebook);
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView vitri = (TextView) convertView.findViewById(R.id.vitri);

		Cmt commentItem = commentItems.get(position);

		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(commentItem
				.getTime().getValue(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		time.setText(timeAgo);
		vitri.setText(commentItem.getAddress());
//		time.setVisibility(View.GONE);
//		vitri.setVisibility(View.GONE);
		nameFacebook.setText(commentItem.getName());
		pictureFb.setProfileId(commentItem.getIdfb());
		comment.setText(commentItem.getContent());


		return convertView;
	}
}
