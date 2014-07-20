package com.uet.mhst;

import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.GPSTracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
	private String id, name, lat, lag, content;
	private int voteup, votedw, status;
	private Button photoButton, postButton;
	private EditText contentEditText;
	private RadioGroup statusRadio;

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
				id = dataUser.getUserDetails().get("id");
				name = dataUser.getUserDetails().get("name");
				lat = String.valueOf(myLocation.getLatitude());
				lag = String.valueOf(myLocation.getLongitude());
				content = contentEditText.getText().toString();
				voteup = 0;
				votedw = 0;
				int check = statusRadio.getCheckedRadioButtonId();
				switch (check) {
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
				// Code cho nay
				// Up date len datastore voi cac truong nhu tren
				
				Toast.makeText(
						getBaseContext(),
						id + "\n" + name + "\n" + lat + "\n" + lag + "\n"
								+ content + "\n" + String.valueOf(status),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(photo);
		}
	}
}
