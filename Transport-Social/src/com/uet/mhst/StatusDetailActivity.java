package com.uet.mhst;

import java.util.List;

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
import com.uet.mhst.itemendpoint.model.GeoPt;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.GPSTracker;

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

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StatusDetailActivity extends Activity {
	private Long id;
	private Item item;
	private ProfilePictureView profilePicture;
	private TextView name;
	private TextView timestamp;
	private TextView content;
	private TextView status;
	private ListView listComment;
	private Button btn_comment;
	private EditText edit_comment;
	private GoogleAccountCredential credential;
	private SharedPreferences settings;
	private String accountName;
	static final int REQUEST_ACCOUNT_PICKER = 2;
	private DatabaseHandler dataUser;
	private GPSTracker myLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_detail);
		Intent intent = getIntent();
		id = intent.getLongExtra("id", 0);
		dataUser = new DatabaseHandler(getBaseContext());
		myLocation = new GPSTracker(getBaseContext());
		profilePicture = (ProfilePictureView) findViewById(R.id.picture_facebook);
		name = (TextView) findViewById(R.id.name);
		timestamp = (TextView) findViewById(R.id.timestamp);
		content = (TextView) findViewById(R.id.txt_content);
		status = (TextView) findViewById(R.id.txt_status);
		listComment = (ListView) findViewById(R.id.list_comment);
		btn_comment = (Button) findViewById(R.id.btn_comment);
		edit_comment = (EditText) findViewById(R.id.edit_comment);
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

		btn_comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new CommentAsyncTask().execute();
			}
		});
		new NewsFeedAsyncTask().execute();

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
			// Toast.makeText(getBaseContext(), item.toString(),
			// Toast.LENGTH_LONG)
			// .show();
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
			// if (item.getComment() == null) {
			// Toast.makeText(getBaseContext(),
			// item.getComment().get(0).getContent(), Toast.LENGTH_LONG)
			// .show();
			// }
			List<Comment> comment;
			comment = item.getComment();
			CommentListAdapter adapter = new CommentListAdapter(
					getBaseContext(), comment);
			listComment.setAdapter(adapter);
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
				GeoPt point = new GeoPt();
				point.setLatitude((float) myLocation.getLatitude());
				point.setLongitude((float) myLocation.getLongitude());
				cmt.setPoint(point);
				service.comment(Long.parseLong("5075880531460096"), cmt).execute();
			} catch (Exception e) {
				Log.d("Could not Add Item", e.getMessage(), e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

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
