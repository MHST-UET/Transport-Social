package com.uet.mhst;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.impl.cookie.DateUtils;

import com.facebook.Session;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.GeoPt;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.GPSTracker;
import com.uet.mhst.utility.ReverseGeocodingTask;

import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpNewsFeedActivity extends Activity {

	private GPSTracker myLocation;
	private DatabaseHandler dataUser;
	private GoogleAccountCredential credential;
	private SharedPreferences settings;
	static final int REQUEST_ACCOUNT_PICKER = 2;
	private TextView txt_address, txt_time;
	private Spinner statusSpinner;
	private int status;
	private String content;
	private EditText contentEdit;

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
		LatLng latLng = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());
		txt_address.setText(new ReverseGeocodingTask(getBaseContext())
				.getAddressText(latLng));
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
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#81a3d0"));
		ab.setBackgroundDrawable(colorDrawable);

		myLocation = new GPSTracker(getBaseContext());
		dataUser = new DatabaseHandler(getBaseContext());
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

	}

	private class AddItemAsyncTask extends AsyncTask<Item, Void, Void> {

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
			Toast.makeText(getBaseContext(), "Item added succesfully",
					Toast.LENGTH_SHORT).show();

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
			GeoPt point = new GeoPt();
			point.setLatitude((float) myLocation.getLatitude());
			point.setLongitude((float) myLocation.getLongitude());
			statusItem.setPoint(point);
			Item[] params = { statusItem };
			new AddItemAsyncTask().execute(params);
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
