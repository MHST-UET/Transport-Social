package com.uet.mhst.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.os.StrictMode;
import com.facebook.*;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.ProfilePictureView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.uet.mhst.R;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.*;
import com.uet.mhst.model.ListComment;
import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.ConnectionDetector;

public class FeedListAdapter extends BaseAdapter {
	private Activity activity;
	private ConnectionDetector cd;
	Boolean isInternetPresent = false;
	private LayoutInflater inflater;
	private List<Item> Items = new ArrayList<Item>();
	private Item item;
	private ViewHold viewHold;
	private DatabaseHandler dataUser;
	private UiLifecycleHelper uiHelper;
	List<Vote> votes;
	boolean checkVote;
	boolean typeVote;

	public FeedListAdapter(Activity activity, List<Item> Items) {
		this.activity = activity;
		this.Items = Items;
		uiHelper = new UiLifecycleHelper(this.activity, null);
		cd = new ConnectionDetector(activity);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		if (convertView == null) {
			inflater = (LayoutInflater) activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.feed_item, null);
			viewHold = new ViewHold();
			viewHold.pictureFb = (ProfilePictureView) convertView
					.findViewById(R.id.picture_facebook);
			viewHold.name = (TextView) convertView.findViewById(R.id.name);
			viewHold.img_moreaction = (ImageView) convertView
					.findViewById(R.id.img_moreaction);
			viewHold.timestamp = (TextView) convertView
					.findViewById(R.id.timestamp);
			viewHold.status = (TextView) convertView
					.findViewById(R.id.txt_status);
			viewHold.address = (TextView) convertView
					.findViewById(R.id.txt_address);
			viewHold.content = (TextView) convertView
					.findViewById(R.id.txt_content);
			viewHold.txt_like = (TextView) convertView
					.findViewById(R.id.txt_like);
			viewHold.txt_dislike = (TextView) convertView
					.findViewById(R.id.txt_dislike);
			viewHold.txt_comment = (TextView) convertView
					.findViewById(R.id.txt_comment);
			viewHold.btn_comment = (Button) convertView
					.findViewById(R.id.btn_comment);
			viewHold.btn_voteup = (Button) convertView
					.findViewById(R.id.btn_voteup);
			viewHold.btn_votedown = (Button) convertView
					.findViewById(R.id.btn_votedown);
			viewHold.btn_share = (Button) convertView
					.findViewById(R.id.btn_share);
			convertView.setTag(viewHold);
		} else {
			viewHold = (ViewHold) convertView.getTag();
		}

		item = Items.get(position);
		dataUser = new DatabaseHandler(activity.getBaseContext());

		viewHold.btn_voteup.setCompoundDrawablesWithIntrinsicBounds(activity
				.getResources().getDrawable(R.drawable.ufi_icon_voteup), null,
				null, null);
		viewHold.btn_voteup.setTextColor(Color.BLACK);
		viewHold.btn_votedown.setCompoundDrawablesWithIntrinsicBounds(activity
				.getResources().getDrawable(R.drawable.ufi_icon_votedown),
				null, null, null);
		viewHold.btn_votedown.setTextColor(Color.BLACK);

		votes = Items.get(position).getVote();
		int voteup = 0;
		int votedown = 0;
		checkVote = false;
		typeVote = false;
		if (votes != null) {
			for (int i = 0; i < votes.size(); i++) {
				Vote vote = votes.get(i);

				if (vote.getIdfb().equals(dataUser.getUserDetails().get("id"))) {
					checkVote = true;
					if (vote.getUp() == true) {
						typeVote = true;
					}
				}
				if (vote.getUp() == true) {
					voteup++;
				} else {
					votedown++;
				}
			}
		}
		if (checkVote) {
			if (typeVote) {
				viewHold.btn_voteup.setCompoundDrawablesWithIntrinsicBounds(
						activity.getResources().getDrawable(
								R.drawable.ufi_icon_voteup_pressed), null,
						null, null);
				viewHold.btn_voteup.setTextColor(Color.rgb(123, 170, 251));

			} else {
				viewHold.btn_votedown.setCompoundDrawablesWithIntrinsicBounds(
						activity.getResources().getDrawable(
								R.drawable.ufi_icon_votedown_pressed), null,
						null, null);
				viewHold.btn_votedown.setTextColor(Color.rgb(123, 170, 251));
			}
		}
		List<Comment> comments = item.getComment();
		viewHold.txt_comment.setText("0");
		if (comments != null) {
			viewHold.txt_comment.setText(String.valueOf(comments.size()));
		}
		viewHold.txt_like.setText(String.valueOf(voteup));
		viewHold.txt_dislike.setText(String.valueOf(votedown));
		viewHold.pictureFb.setProfileId(item.getIdFB());
		viewHold.name.setText(item.getName());
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(item
				.getTime().getValue(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS);
		viewHold.timestamp.setText(timeAgo);
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
		viewHold.status.setText("Tình trạng: " + var);

		viewHold.address.setText(item.getAddress());

		viewHold.content.setText(item.getContent());
		viewHold.img_moreaction.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent moreAction = new Intent(
						"com.uet.mhst.MoreActionActivity");
				Bundle bundle = new Bundle();
				bundle.putLong("id", Items.get(position).getId().getId());
				bundle.putDouble("lat", Items.get(position).getLatitude());
				bundle.putDouble("lng", Items.get(position).getLongitude());
				bundle.putString("address", Items.get(position).getAddress());
				String var = "";
				switch (Items.get(position).getStatus()) {
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
				bundle.putString("status", var);
				// bundle.("", Items.get(position).);
				// bundle.("", Items.get(position).);
				// bundle.("", Items.get(position).);
				// bundle.("", Items.get(position).);
				moreAction.putExtra("bundle", bundle);
				activity.startActivityForResult(moreAction, 111);
			}
		});
		viewHold.txt_comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListComment listCmt = null;
				List<Comment> list = null;
				try {
					list = Items.get(position).getComment();

				} catch (Exception e) {

				}
				listCmt = new ListComment(list, Items.get(position).getId()
						.getId());

				Intent comment = new Intent("com.uet.mhst.StatusDetailActivity");
				Bundle bundle = new Bundle();
				bundle.putLong("id", Items.get(position).getId().getId());
				bundle.putSerializable("listCmt", listCmt);
				comment.putExtra("comment", bundle);

				activity.startActivity(comment);

			}
		});
		viewHold.btn_comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListComment listCmt = null;
				List<Comment> list = null;
				try {
					list = Items.get(position).getComment();

				} catch (Exception e) {

				}
				listCmt = new ListComment(list, Items.get(position).getId()
						.getId());

				Intent comment = new Intent("com.uet.mhst.StatusDetailActivity");
				Bundle bundle = new Bundle();
				bundle.putLong("id", Items.get(position).getId().getId());
				bundle.putSerializable("listCmt", listCmt);
				comment.putExtra("comment", bundle);

				activity.startActivity(comment);
			}
		});

		viewHold.btn_voteup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				votes = Items.get(position).getVote();

				checkVote = false;
				typeVote = false;
				if (votes != null) {
					for (int i = 0; i < votes.size(); i++) {
						Vote vote = votes.get(i);

						if (vote.getIdfb().equals(
								dataUser.getUserDetails().get("id"))) {
							checkVote = true;
							if (vote.getUp() == true) {
								typeVote = true;
							}
						}
					}
				}

				if (checkVote == false) {
					String idf = dataUser.getUserDetails().get("id");
					Long id = Items.get(position).getId().getId();
					boolean up = true;
					Vote vote = new Vote();
					vote.setIdfb(idf);
					vote.setUp(up);
					List<Vote> va = new ArrayList<Vote>();
					va.add(vote);
					if (Items.get(position).getVote() == null) {
						Items.get(position).setVote(va);
					} else {
						List<Vote> v1 = Items.get(position).getVote();
						v1.add(vote);
						Items.get(position).setVote(v1);
					}
					new VoteThread(idf, id, up).start();

				} else if (checkVote == true) {
					String idf = dataUser.getUserDetails().get("id");
					Long id = Items.get(position).getId().getId();
					List<Vote> v1 = Items.get(position).getVote();
					for (int i = 0; i < v1.size(); i++) {
						if (v1.get(i).getIdfb()
								.equals(dataUser.getUserDetails().get("id"))) {
							v1.remove(i);
						}
					}
					new VoteThread(idf, id, true).start();
					if (typeVote == true) {

						Items.get(position).setVote(v1);

					} else if (typeVote == false) {

						boolean up = true;
						Vote vote = new Vote();
						vote.setIdfb(idf);
						vote.setUp(up);
						v1.add(vote);
						Items.get(position).setVote(v1);
						new VoteThread(idf, id, true).start();
					}
				}
				notifyDataSetChanged();
			}
		});
		viewHold.btn_votedown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				votes = Items.get(position).getVote();

				checkVote = false;
				typeVote = false;
				if (votes != null) {
					for (int i = 0; i < votes.size(); i++) {
						Vote vote = votes.get(i);

						if (vote.getIdfb().equals(
								dataUser.getUserDetails().get("id"))) {
							checkVote = true;
							if (vote.getUp() == true) {
								typeVote = true;
							}
						}
					}
				}

				if (checkVote == false) {
					String idf = dataUser.getUserDetails().get("id");
					Long id = Items.get(position).getId().getId();
					boolean up = false;
					Vote vote = new Vote();
					vote.setIdfb(idf);
					vote.setUp(up);
					List<Vote> va = new ArrayList<Vote>();
					va.add(vote);
					if (Items.get(position).getVote() == null) {
						Items.get(position).setVote(va);
					} else {
						List<Vote> v1 = Items.get(position).getVote();
						v1.add(vote);
						Items.get(position).setVote(v1);
					}
					new VoteThread(idf, id, up).start();
				} else if (checkVote == true) {
					String idf = dataUser.getUserDetails().get("id");
					Long id = Items.get(position).getId().getId();
					List<Vote> v1 = Items.get(position).getVote();
					for (int i = 0; i < v1.size(); i++) {
						if (v1.get(i).getIdfb()
								.equals(dataUser.getUserDetails().get("id"))) {
							v1.remove(i);
						}
					}
					new VoteThread(idf, id, true).start();
					if (typeVote == false) {

						Items.get(position).setVote(v1);
					} else if (typeVote == true) {

						boolean up = false;
						Vote vote = new Vote();
						vote.setIdfb(idf);
						vote.setUp(up);
						v1.add(vote);
						Items.get(position).setVote(v1);
						new VoteThread(idf, id, false).start();
					}
				}
				notifyDataSetChanged();

			}
		});
		viewHold.btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
						activity)
						.setDescription(Items.get(position).getContent())
						.setLink(
								"https://www.facebook.com/pages/Transport-Social/1465380390395999")
						.build();
				uiHelper.trackPendingDialogCall(shareDialog.present());
			}
		});

		return convertView;
	}

	public class VoteThread extends Thread {
		String idf;
		Long id;
		boolean up;

		public VoteThread(String idf, Long id, boolean up) {
			this.idf = idf;
			this.id = id;
			this.up = up;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			isInternetPresent = cd.isConnectingToInternet();
			if (isInternetPresent) {

				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				Vote vote = new Vote();
				vote.setName(dataUser.getUserDetails().get("name"));
				vote.setIdfb(idf);
				vote.setUp(up);

				try {
					service.vote(id, vote).execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
		}
	}

	public class ViewHold {
		public Button btn_comment, btn_voteup, btn_votedown, btn_share;
		public TextView txt_like, txt_dislike, timestamp, status, address,
				content, txt_comment, name;
		public ImageView img_moreaction;
		ProfilePictureView pictureFb;
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}