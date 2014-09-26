package com.uet.mhst;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.uet.mhst.adapter.CommentListAdapter;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.Comment;
import com.uet.mhst.model.ListComment;
import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.GPSTracker;
import com.uet.mhst.utility.ReverseGeocodingTask;

public class StatusDetailActivity extends Activity {
	private ListView listComment;
	private EditText edit_comment;
	private DatabaseHandler dataUser;
	private GPSTracker myLocation;
	private CommentListAdapter adapter;
	private Comment cmt;
	private ImageView img_close;
	private ListComment listCmt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_detail);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("comment");
		listCmt = (ListComment) bundle.getSerializable("listCmt");
		dataUser = new DatabaseHandler(getBaseContext());
		myLocation = new GPSTracker(getBaseContext());

		listComment = (ListView) findViewById(R.id.listComment);
		edit_comment = (EditText) findViewById(R.id.edit_comment);
		img_close = (ImageView) findViewById(R.id.img_close);
		img_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		adapter = new CommentListAdapter(getBaseContext(),
				listCmt.getListComments());

		listComment.setAdapter(adapter);

		Button btn_comment = (Button) findViewById(R.id.btn_comment);

		btn_comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cmt = new Comment();
				cmt.setContent(edit_comment.getText().toString());
				cmt.setTime(new DateTime(System.currentTimeMillis()));
				String id = dataUser.getUserDetails().get("id");
				cmt.setIdfb(id);
				cmt.setLatitude(myLocation.getLatitude());
				cmt.setLongitude(myLocation.getLongitude());
				cmt.setName(dataUser.getUserDetails().get("name"));
				LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation
						.getLongitude());
				cmt.setAddress(new ReverseGeocodingTask(getBaseContext())
						.getAddressText(latLng));

				listCmt.add(cmt);
				adapter.notifyDataSetChanged();
				edit_comment.setText("");

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Itemendpoint.Builder builder = new Itemendpoint.Builder(
								AndroidHttp.newCompatibleTransport(),
								new GsonFactory(), null);
						Itemendpoint service = builder.build();

						try {
							service.comment(listCmt.getId(), cmt).execute();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}).start();
			}
		});
	}

}
