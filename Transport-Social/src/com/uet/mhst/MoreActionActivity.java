package com.uet.mhst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MoreActionActivity extends Activity {
	private Button btn_showMap, btn_delete;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_action);

		Intent intent = getIntent();
		bundle = intent.getBundleExtra("bundle");

		btn_showMap = (Button) findViewById(R.id.btn_showMap);
		btn_delete = (Button) findViewById(R.id.btn_delete);

		btn_showMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();

				intent.putExtra("bundle", bundle);
				setResult(111, intent);
				finish();
			}
		});

		btn_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
