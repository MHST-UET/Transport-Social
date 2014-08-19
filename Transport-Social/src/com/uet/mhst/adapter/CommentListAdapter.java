package com.uet.mhst.adapter;

import java.util.List;

import com.facebook.widget.ProfilePictureView;
import com.uet.mhst.R;
import com.uet.mhst.itemendpoint.model.Comment;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CommentListAdapter extends BaseAdapter {

	private Context context;
	private List<Comment> commentItems;

	public CommentListAdapter(Context context, List<Comment> commentItems) {
		this.context = context;
		this.commentItems = commentItems;

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
			ProfilePictureView pictureFb = (ProfilePictureView) convertView
					.findViewById(R.id.picture_facebook);
			TextView comment = (TextView) convertView
					.findViewById(R.id.txt_comment);
			Comment commentItem = commentItems.get(position);
			pictureFb.setProfileId(commentItem.getIdfb());
			comment.setText(commentItem.getContent());
			Toast.makeText(context, commentItem.getContent(), Toast.LENGTH_LONG)
					.show();

		}
		return convertView;
	}
}
