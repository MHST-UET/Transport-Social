package com.uet.mhst;

import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.model.ItemSerializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StatusDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_detail);
		Intent intent = getIntent();

		ItemSerializable itemSer = (ItemSerializable) intent
				.getSerializableExtra("detail");

		TextView t1 = (TextView) findViewById(R.id.textView1);
		TextView t2 = (TextView) findViewById(R.id.textView2);
		TextView t3 = (TextView) findViewById(R.id.textView3);
		TextView t4 = (TextView) findViewById(R.id.textView4);
		TextView t5 = (TextView) findViewById(R.id.textView5);
		TextView t6 = (TextView) findViewById(R.id.textView6);
		TextView t7 = (TextView) findViewById(R.id.textView7);
		TextView t8 = (TextView) findViewById(R.id.textView8);
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				setResult(1, intent);
				finish();
			}
		});
		t1.setText(itemSer.getAddress());
		t2.setText(itemSer.getContent());
		t3.setText(itemSer.getName());
		t4.setText(String.valueOf(itemSer.getStatus()));
		t5.setText(String.valueOf(itemSer.getVoteUp()));
		t6.setText(String.valueOf(itemSer.getVoteUp()));
		t7.setText(String.valueOf(itemSer.getLat()));
		t8.setText(String.valueOf(itemSer.getLag()));
	}
}
