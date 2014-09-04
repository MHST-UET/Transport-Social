package com.uet.mhst;

import java.util.ArrayList;
import java.util.List;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.widget.ProfilePictureView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.uet.mhst.adapter.CommentListAdapter;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.Comment;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.itemendpoint.model.Vote;
import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.GPSTracker;

public class StatusDetailActivity extends Activity {
	private Long id;
	private Item item;
	private ProfilePictureView profilePicture;
	private TextView name;
	private TextView timestamp;
	private TextView content;
	private TextView address;
	private TextView status;
	private ListView listComment;
	private EditText edit_comment;
	private GoogleAccountCredential credential;
	private SharedPreferences settings;
	private String accountName;
	static final int REQUEST_ACCOUNT_PICKER = 2;
	private DatabaseHandler dataUser;
	private GPSTracker myLocation;
	private CommentListAdapter adapter;
	TextView txt_like;
	TextView txt_dislike;
	TextView txt_comment;
	Button btn_voteup;
	Button btn_votedown;
	private List<Comment> commentItems = new ArrayList<Comment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_detail);
		Intent intent = getIntent();
		id = intent.getLongExtra("id", 0);
		dataUser = new DatabaseHandler(getBaseContext());
		myLocation = new GPSTracker(getBaseContext());
		listComment = (ListView) findViewById(R.id.listComment);
		profilePicture = (ProfilePictureView) findViewById(R.id.picture_facebook);
		name = (TextView) findViewById(R.id.name);
		address = (TextView) findViewById(R.id.txt_address);
		timestamp = (TextView) findViewById(R.id.timestamp);
		content = (TextView) findViewById(R.id.txt_content);
		status = (TextView) findViewById(R.id.txt_status);
		edit_comment = (EditText) findViewById(R.id.edit_comment);

		txt_like = (TextView) findViewById(R.id.txt_like);
		txt_dislike = (TextView) findViewById(R.id.txt_dislike);
		txt_comment = (TextView) findViewById(R.id.txt_comment);

		settings = getSharedPreferences("Transport Social", 0);
		credential = GoogleAccountCredential.usingAudience(this,
				"server:client_id:" + Ids.WEB_CLIENT_ID);
		setAccountName(settings.getString("ACCOUNT_NAME", null));

		if (credential.getSelectedAccountName() != null) {
			// Already signed in, begin app!
			Toast.makeText(getBaseContext(),
					"Logged in with : " + credential.getSelectedAccountName(),
					Toast.LENGTH_SHORT).show();
		} else {
			// Not signed in, show login window or request an account.
			chooseAccount();
		}

		new NewsFeedAsyncTask().execute();

		btn_voteup = (Button) findViewById(R.id.btn_voteup);
		btn_votedown = (Button) findViewById(R.id.btn_votedown);
		Button btn_comment = (Button) findViewById(R.id.btn_comment);
		btn_voteup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new VoteAsyncTask().execute(1);
			}
		});
		btn_votedown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				new VoteAsyncTask().execute(0);
			}
		});
		btn_comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new CommentAsyncTask().execute();
			}
		});
	}

	private class NewsFeedAsyncTask extends AsyncTask<Void, Void, Item> {

		protected Item doInBackground(Void... unused) {
			item = null;
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				item = service.getItem(id).execute();

			} catch (Exception e) {
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return item;
		}

		protected void onPostExecute(Item item) {
			profilePicture.setProfileId(item.getIdFB());
			Session session = Session.getActiveSession();
			Bundle params = new Bundle();
			params.putString("fields", "name,picture");
			new Request(session, "/" + String.valueOf(item.getIdFB()), params,
					HttpMethod.GET, new Request.Callback() {
						public void onCompleted(Response response) {
							/* handle the result */
							GraphObject g = response.getGraphObject();
							g.getProperty("name");
							name.setText(g.getProperty("name").toString());
						}
					}).executeAsync();

			CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(item
					.getTime().getValue(), System.currentTimeMillis(),
					DateUtils.SECOND_IN_MILLIS);
			timestamp.setText(timeAgo);
			content.setText(item.getContent());
			address.setText(item.getAddress());
			String var = "";
			switch (item.getStatus()) {
			case 1:
				var = "Tắc đường";
				break;
			case 2:
				var = "Đường đông";
				break;
			case 3:
				var = "Tai nạn";
				break;
			case 4:
				var = "Bình thường";
				break;
			}
			status.setText("Tình trạng: " + var);
			try {

				commentItems = item.getComment();

			} catch (Exception e) {
				Toast.makeText(getBaseContext(), "Comment null",
						Toast.LENGTH_LONG).show();
			}
			List<Vote> votes = item.getVote();
			int voteup = 0;
			int votedown = 0;
			btn_votedown.setBackground(getResources().getDrawable(
					R.drawable.bg_selector));
			btn_voteup.setBackground(getResources().getDrawable(
					R.drawable.bg_selector));
			if (votes != null) {
				for (int i = 0; i < votes.size(); i++) {
					Vote vote = votes.get(i);
					if (vote.getUp() == true) {
						voteup++;
					} else {
						votedown++;
					}

					if (vote.getIdfb().equals(
							dataUser.getUserDetails().get("id"))) {

						if (vote.getUp() == true) {
							btn_voteup.setBackground(getResources()
									.getDrawable(R.drawable.bg_click));

						} else {
							btn_votedown.setBackground(getResources()
									.getDrawable(R.drawable.bg_click));
						}
					}
				}
			}
			List<Comment> comments = item.getComment();
			if (comments != null) {
				txt_comment.setText(String.valueOf(comments.size()));
			}
			txt_like.setText(String.valueOf(voteup));
			txt_dislike.setText(String.valueOf(votedown));
			adapter = new CommentListAdapter(getBaseContext(), commentItems);

			listComment.setAdapter(adapter);
		}

	}

	private class VoteAsyncTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), credential);
				Itemendpoint service = builder.build();
				Vote vote = new Vote();
				vote.setIdfb(item.getIdFB());

				if (params[0] == 1) {
					vote.setUp(true);
				} else {
					vote.setUp(false);
				}
				service.vote(item.getId().getId(), vote).execute();
			} catch (Exception e) {
				Log.d("Could not Add Item", e.getMessage(), e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			new NewsFeedAsyncTask().execute();
		}

	}

	private class CommentAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), credential);
				Itemendpoint service = builder.build();
				Comment cmt = new Comment();
				cmt.setContent(edit_comment.getText().toString());
				cmt.setTime(new DateTime(System.currentTimeMillis()));
				String id = dataUser.getUserDetails().get("id");
				cmt.setIdfb(id);
				cmt.setLatitude(myLocation.getLatitude());
				cmt.setLongitude(myLocation.getLongitude());
				service.comment(item.getId().getId(), cmt).execute();
			} catch (Exception e) {
				Log.d("Could not Add Item", e.getMessage(), e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			edit_comment.setText("");
			new NewsFeedAsyncTask().execute();
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (data != null && data.getExtras() != null) {
				String accountName = data.getExtras().getString(
						AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					setAccountName(accountName);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("ACCOUNT_NAME", accountName);
					editor.commit();
					// User is authorized.
				}
			}
			break;
		}

	}

	private void chooseAccount() {
		startActivityForResult(credential.newChooseAccountIntent(),
				REQUEST_ACCOUNT_PICKER);
	}

	private void setAccountName(String accountName) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("ACCOUNT_NAME", accountName);
		editor.commit();
		credential.setSelectedAccountName(accountName);
		this.accountName = accountName;
	}
}
