package com.uet.mhst;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassActivity extends Activity implements SensorEventListener {

	// define the display assembly compass picture
	private ImageView image;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;

	TextView tvHeading, txt_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);

		// our compass image
		image = (ImageView) findViewById(R.id.imageViewCompass);
		image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// TextView that will tell the user what degree is he heading
		tvHeading = (TextView) findViewById(R.id.tvHeading);
		txt_head = (TextView) findViewById(R.id.txt_head);

		// initialize your android device sensor capabilities
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// for the system's orientation sensor registered listeners
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// to stop the listener and save battery
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);
		float baseAzimuth = degree;
		// Set the field
		String bearingText = "N";

		if ((360 >= baseAzimuth && baseAzimuth >= 337.5)
				|| (0 <= baseAzimuth && baseAzimuth <= 22.5))
			bearingText = "Bắc";
		else if (baseAzimuth > 22.5 && baseAzimuth < 67.5)
			bearingText = "Đông Bắc";
		else if (baseAzimuth >= 67.5 && baseAzimuth <= 112.5)
			bearingText = "Đông";
		else if (baseAzimuth > 112.5 && baseAzimuth < 157.5)
			bearingText = "Đông Nam";
		else if (baseAzimuth >= 157.5 && baseAzimuth <= 202.5)
			bearingText = "Nam";
		else if (baseAzimuth > 202.5 && baseAzimuth < 247.5)
			bearingText = "Tây Nam";
		else if (baseAzimuth >= 247.5 && baseAzimuth <= 292.5)
			bearingText = "Tây";
		else if (baseAzimuth > 292.5 && baseAzimuth < 337.5)
			bearingText = "Tây Bắc";
		else
			bearingText = "?";

		tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
		txt_head.setText(bearingText);
		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		image.startAnimation(ra);
		currentDegree = -degree;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}
}