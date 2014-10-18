package com.uet.mhst;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.GPSTracker;
import com.uet.mhst.utility.ReverseGeocodingTask;

public class UpNewsFeedActivity extends Activity {

	private GPSTracker myLocation;
	private DatabaseHandler dataUser;
	private GoogleAccountCredential credential;
	private SharedPreferences settings;
	static final int REQUEST_ACCOUNT_PICKER = 2;
	private TextView txt_address, txt_time;
	private Spinner statusSpinner;
	private int status;
	private String content, location;
	private EditText contentEdit;
	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_news_feed);
		myLocation = new GPSTracker(getBaseContext());
		txt_address = (TextView) findViewById(R.id.txt_address);
		txt_time = (TextView) findViewById(R.id.txt_time);
		contentEdit = (EditText) findViewById(R.id.content);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm   dd/MM/yyyy");
		String currentDateandTime = sdf.format(new Date());
		txt_time.setText(currentDateandTime);
		progress = new ProgressDialog(this);
		progress.setMessage("Updating...");
		progress.setIndeterminate(true);
		String arr[] = { "Bình thường", "Đông", "Tắc đường", "Tai nạn" };
		statusSpinner = (Spinner) findViewById(R.id.status);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		statusSpinner.setAdapter(adapter);
		statusSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						status = 1 + position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						status = 1;
					}

				});
		ActionBar actionBar = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#990000"));
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		myLocation = new GPSTracker(getBaseContext());
		dataUser = new DatabaseHandler(getBaseContext());
		settings = getSharedPreferences("Transport Social", 0);
		credential = GoogleAccountCredential.usingAudience(this,
				"server:client_id:" + Ids.WEB_CLIENT_ID);
		setAccountName(settings.getString("ACCOUNT_NAME", null));

		if (credential.getSelectedAccountName() != null) {
			// Already signed in, begin app!

		} else {
			// Not signed in, show login window or request an account.
			chooseAccount();
		}
		new LocationAsyncTask().execute();
	}

	private class LocationAsyncTask extends AsyncTask<Item, Void, Void> {

		protected Void doInBackground(Item... params) {
			LatLng latLng = new LatLng(myLocation.getLatitude(),
					myLocation.getLongitude());
			location = new ReverseGeocodingTask(getBaseContext())
					.getAddressText(latLng);
			return null;
		}

		protected void onPostExecute(Void unused) {

			txt_address.setText(location);

		}
	}

	private class AddItemAsyncTask extends AsyncTask<Item, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.show();
		}

		protected Void doInBackground(Item... params) {
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), credential);
				Itemendpoint service = builder.build();
				service.insertItem(params[0]).execute();
			} catch (Exception e) {
				Log.d("Could not Add Item", e.getMessage(), e);
			}
			return null;
		}

		protected void onPostExecute(Void unused) {

			// Display success message to user
			Toast.makeText(getBaseContext(), "Status updated succesfully",
					Toast.LENGTH_SHORT).show();
			progress.dismiss();
			finish();

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.up_status_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_post:
			Item statusItem = new Item();
			String id = dataUser.getUserDetails().get("id");
			statusItem.setIdFB(id);
			statusItem.setTime(new DateTime(System.currentTimeMillis()));
			statusItem.setStatus(status);
			content = contentEdit.getText().toString();
			statusItem.setContent(content);
			statusItem.setLatitude(myLocation.getLatitude());
			statusItem.setLongitude(myLocation.getLongitude());
			LatLng latLng = new LatLng(myLocation.getLatitude(),
					myLocation.getLongitude());
			statusItem.setAddress(new ReverseGeocodingTask(getBaseContext())
					.getAddressText(latLng));
			String name = dataUser.getUserDetails().get("name");
			statusItem.setName(name);
			Item[] params = { statusItem };
			new AddItemAsyncTask().execute(params);
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
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

	}
}
