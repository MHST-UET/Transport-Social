package com.uet.mhst;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		ActionBar actionBar = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#990000"));
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1
				.setText(Html
						.fromHtml(" <a href=\"https://github.com/googlemaps/android-maps-utils\">Google Maps Android API utility library</a>"));
		textView1.setMovementMethod(android.text.method.LinkMovementMethod
				.getInstance());

		TextView textView3 = (TextView) findViewById(R.id.textView3);
		textView3
				.setText(Html
						.fromHtml("<a href=\"https://github.com/chrisbanes/Android-PullToRefresh\">Pull to refresh listview library</a>"));
		textView3.setMovementMethod(android.text.method.LinkMovementMethod
				.getInstance());

		TextView textView4 = (TextView) findViewById(R.id.textView4);
		textView4
				.setText(Html
						.fromHtml("<a href=\"https://developers.facebook.com/docs/android/change-log-3.x\">Facebook SDK</a>"));
		textView4.setMovementMethod(android.text.method.LinkMovementMethod
				.getInstance());

		TextView textView7 = (TextView) findViewById(R.id.textView7);
		textView7
				.setText(Html
						.fromHtml("<a href=\"http://ddewaele.github.io/GoogleMapsV2WithActionBarSherlock/\">Maps V2 Android</a>"));
		textView7.setMovementMethod(android.text.method.LinkMovementMethod
				.getInstance());

		TextView textView8 = (TextView) findViewById(R.id.textView8);
		textView8
				.setText(Html
						.fromHtml("<a href=\"https://developers.facebook.com/docs/android\">Facebook SDK for Android</a>"));
		textView8.setMovementMethod(android.text.method.LinkMovementMethod
				.getInstance());

		TextView textView6 = (TextView) findViewById(R.id.textView6);
		textView6
				.setText(Html
						.fromHtml("<a href=\"https://developers.google.com/appengine/\">Google App Engine</a>"));
		textView6.setMovementMethod(android.text.method.LinkMovementMethod
				.getInstance());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}
