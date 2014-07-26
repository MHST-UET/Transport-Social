package com.uet.mhst;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.GPSTracker;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UpNewsFeedActivity extends Activity {
	private static final int CAMERA_REQUEST = 1888;
	private ImageView imageView;
	private GPSTracker myLocation;
	private DatabaseHandler dataUser;
	private Button photoButton, postButton;
	private EditText contentEditText;
	private RadioGroup statusRadio;
	private GoogleAccountCredential credential;
	private SharedPreferences settings;
	private String accountName;
	static final int REQUEST_ACCOUNT_PICKER = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_news_feed);
		this.imageView = (ImageView) this.findViewById(R.id.imageView1);
		photoButton = (Button) this.findViewById(R.id.btn_photo);
		postButton = (Button) this.findViewById(R.id.btn_post);
		contentEditText = (EditText) this.findViewById(R.id.editText_content);
		statusRadio = (RadioGroup) this.findViewById(R.id.radio_status);
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
		
		photoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
		postButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String id = dataUser.getUserDetails().get("id");
				String name = dataUser.getUserDetails().get("name");
				String lat = String.valueOf(myLocation.getLatitude());
				String lag = String.valueOf(myLocation.getLongitude());
				String content = contentEditText.getText().toString();
				int status = 0;
				switch (statusRadio.getCheckedRadioButtonId()) {
				case R.id.radio_tac:
					status = 1;
					break;
				case R.id.radio_dong:
					status = 2;
					break;
				case R.id.radio_tai_nan:
					status = 3;
					break;
				case R.id.radio_bing_thuong:
					status = 4;
					break;
				}
				Item item = new Item();
				item.setIdFB(id);
				item.setName(name);
				item.setLat(lat);
				item.setLag(lag);
				item.setTime(new DateTime(System.currentTimeMillis()));
				item.setStatus(status);
				item.setImg("");
				item.setContent(content);
				Item [] params = {item};
				new AddItemAsyncTask().execute(params);
			}
		});
	}
	
	private class AddItemAsyncTask extends AsyncTask<Item, Void, Void>{

		  protected Void doInBackground(Item ... params) {
		    try {
		    	Itemendpoint.Builder builder = new Itemendpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential);
				Itemendpoint service =  builder.build();
				service.insertItem(params[0]).execute();
		    } catch (Exception e) {
		      Log.d("Could not Add Item", e.getMessage(), e);
		    }
		    return null;
		  }

		  protected void onPostExecute(Void unused) {
			  //Clear the progress dialog and the fields
			  contentEditText.setText("");
			  contentEditText.setHint("Write Something");
			  //Display success message to user
			  Toast.makeText(getBaseContext(), "Item added succesfully", Toast.LENGTH_SHORT).show();
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
		
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(photo);
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
