package com.uet.mhst;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingActivity extends Activity {
	private CheckBox checkBox_noti, checkBox_audio;
	private SeekBar seekBar_rad, seekBar_number;
	private TextView txt_rad, txt_number;
	SharedPreferences pre;
	private int progressRad = 0;
	private int progressNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		ActionBar actionBar = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#990000"));
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		pre = getSharedPreferences("dataSetting", MODE_PRIVATE);
		checkBox_noti = (CheckBox) findViewById(R.id.checkBox_noti);
		checkBox_audio = (CheckBox) findViewById(R.id.checkBox_audio);
		seekBar_rad = (SeekBar) findViewById(R.id.seekBar_rad);
		seekBar_number = (SeekBar) findViewById(R.id.seekBar_number);
		checkBox_noti.setChecked(pre.getBoolean("noti", false));
		checkBox_audio.setChecked(pre.getBoolean("audio", false));
		txt_rad = (TextView) findViewById(R.id.txt_rad);
		txt_number = (TextView) findViewById(R.id.txt_number);

		seekBar_rad.setProgress(Integer.valueOf(pre.getString("rad", "3")));
		seekBar_number.setProgress(Integer.valueOf(pre
				.getString("number", "10")));

		txt_rad.setText("Tìm trong bán kính: " + seekBar_rad.getProgress()
				+ "/" + seekBar_rad.getMax() + " km");

		seekBar_rad.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue,
					boolean fromUser) {
				progressRad = progresValue;
				txt_rad.setText("Tìm trong bán kinh: " + progressRad + "/"
						+ seekBar.getMax() + " km");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		txt_number.setText("Số lượng địa điểm: " + seekBar_number.getProgress()
				+ "/" + seekBar_number.getMax());
		seekBar_number
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progresValue, boolean fromUser) {
						progressNumber = progresValue;
						txt_number.setText("Số lượng địa điểm: "
								+ progressNumber + "/" + seekBar.getMax());
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}
				});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences.Editor edit = pre.edit();
		edit.putString("rad", "100");
		edit.putString("number", "20");
		edit.putBoolean("noti", checkBox_noti.isChecked());
		edit.putBoolean("audio", checkBox_audio.isChecked());
		edit.putString("rad", String.valueOf(seekBar_rad.getProgress()));
		edit.putString("number", String.valueOf(seekBar_number.getProgress()));

		edit.commit();
		if (!checkBox_noti.isChecked()) {
			stopService(new Intent(this, MyService.class));
		} else {
			startService(new Intent(this, MyService.class));
		}

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
