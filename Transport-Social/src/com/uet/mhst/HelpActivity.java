package com.uet.mhst;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#3b5998"));
		getActionBar().setBackgroundDrawable(colorDrawable);
	}

}
