package com.uet.mhst;

import java.util.ArrayList;

import com.facebook.Session;
import com.uet.mhst.adapter.NavDrawerListAdapter;
import com.uet.mhst.adapter.TabsPagerAdapter;
import com.uet.mhst.model.NavDrawerItem;
import com.uet.mhst.sqlite.DatabaseHandler;
import com.uet.mhst.utility.GPSTracker;
import com.uet.mhst.utility.PlaceProvider;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.uet.mhst.communicator.*;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, LoaderCallbacks<Cursor> {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private DatabaseHandler db;
	boolean up = true;
	FrameLayout.LayoutParams parms;
	LinearLayout.LayoutParams par;
	float dx = 0;
	float dy = 0;
	float x = 0;
	float y = 0;

	// Communicator
	public Communicator.MainMapCommunicator mapCommunicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DatabaseHandler(getBaseContext());
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#990000"));
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.white)));

		// Adding Tabs
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.tabbar_icon_notifications_white)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.tabbar_icon_feed_white)
				.setTabListener(this));

		viewPager.setOffscreenPageLimit(1);

		this.selectTab(1);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons
				.getResourceId(8, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons
				.getResourceId(9, -1)));
		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		handleIntent(getIntent());

		// ImageView update status
		ImageView imageViewUpStatus = (ImageView) findViewById(R.id.imageView_upstatus);
		imageViewUpStatus.setOnTouchListener(new View.OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					parms = (android.widget.FrameLayout.LayoutParams) v
							.getLayoutParams();

					dx = event.getRawX() - parms.leftMargin;
					dy = event.getRawY() - parms.topMargin;

					break;
				}

				case MotionEvent.ACTION_MOVE: {

					x = event.getRawX();
					y = event.getRawY();

					parms.leftMargin = (int) (x - dx);
					parms.topMargin = (int) (y - dy);

					if (parms.leftMargin < 0) {
						parms.leftMargin = 0;
					}
					if (parms.topMargin < 0) {
						parms.topMargin = 0;
					}
					DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout);

					if (parms.leftMargin > layout.getWidth() - 150) {
						parms.leftMargin = layout.getWidth() - 150;
					}
					if (parms.topMargin > layout.getHeight() - 150) {
						parms.topMargin = layout.getHeight() - 150;
					}
					v.setLayoutParams(parms);
					up = false;
					break;
				}

				case MotionEvent.ACTION_UP: {
					if (up == true) {
						Intent upstatus = new Intent(
								"com.uet.mhst.UpNewsFeedActivity");

						startActivity(upstatus);
					} else {
						up = true;
					}
					break;
				}
				}
				return true;
			}
		});
		SharedPreferences pre = getSharedPreferences("dataSetting",
				MODE_PRIVATE);

		if (!pre.getBoolean("check", false)) {
			SharedPreferences.Editor edit = pre.edit();
			edit.putBoolean("check", true);
			edit.putString("rad", "100");
			edit.putString("number", "20");
			edit.putBoolean("noti", true);
			edit.putBoolean("audio", true);
			edit.commit();
		}

		displayView(0);

		if (pre.getBoolean("noti", false)) {
			startService(new Intent(this, MyService.class));
		}

		GPSTracker gps = new GPSTracker(MainActivity.this);
		if (gps.canGetLocation()) {
		} else {
			gps.showSettingsAlert();
		}
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			doSearch(intent.getStringExtra(SearchManager.QUERY));
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));

		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}

	private void doSearch(String query) {
		Bundle data = new Bundle();
		data.putString("query", query);
		getSupportLoaderManager().restartLoader(0, data, this);
	}

	private void getPlace(String query) {
		Bundle data = new Bundle();
		data.putString("query", query);

		getSupportLoaderManager().restartLoader(1, data, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
		CursorLoader cLoader = null;
		if (arg0 == 0)
			cLoader = new CursorLoader(getBaseContext(),
					PlaceProvider.SEARCH_URI, null, null,
					new String[] { query.getString("query") }, null);
		else if (arg0 == 1)
			cLoader = new CursorLoader(getBaseContext(),
					PlaceProvider.DETAILS_URI, null, null,
					new String[] { query.getString("query") }, null);
		return cLoader;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		showLocations(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
	}

	private void showLocations(Cursor c) {
		mapCommunicator.MainPassToMap(c);
		// Toast.makeText(getBaseContext(), c.getString(0).toString(),
		// Toast.LENGTH_SHORT).show();
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {

			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_directions:
			startActivityForResult(
					new Intent("com.uet.mhst.DirectionActivity"), 112);

		default:

			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments

		switch (position) {
		case 0:

			break;
		case 2:
			mapCommunicator.PassTypeMaptoMap(1);
			selectTab(0);
			break;
		case 3:
			mapCommunicator.PassTypeMaptoMap(2);
			selectTab(0);
			break;
		case 4:
			mapCommunicator.PassTypeMaptoMap(3);
			selectTab(0);
			break;
		case 6:
			startActivity(new Intent("com.uet.mhst.AboutActivity"));
			break;
		case 7:
			startActivity(new Intent("com.uet.mhst.HelpActivity"));
			break;
		case 8:
			startActivity(new Intent("com.uet.mhst.SettingActivity"));
			break;
		case 9:
			Session session = Session.getActiveSession();
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
			}
			Intent login = new Intent(getApplicationContext(),
					LoginFacebookActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			db.resetTables();
			startActivity(login);
			this.finish();
			break;
		default:
			break;
		}

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		if (tab.getPosition() == 0) {
			tab.setIcon(R.drawable.tabbar_icon_notifications_pressed_white);

		} else if (tab.getPosition() == 1) {
			tab.setIcon(R.drawable.tabbar_icon_feed_pressed_white);
		}
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		if (tab.getPosition() == 0) {
			tab.setIcon(R.drawable.tabbar_icon_notifications_white);

		} else if (tab.getPosition() == 1) {
			tab.setIcon(R.drawable.tabbar_icon_feed_white);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void selectTab(int position) {
		actionBar.setSelectedNavigationItem(position);
		viewPager.setCurrentItem(position);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == 111) {
				Bundle bundle = data.getBundleExtra("bundle");
				mapCommunicator.PassAPlaceToMap(bundle);
				selectTab(0);
			}
			if (requestCode == 112) {
				if (resultCode == DirectionActivity.RESULT_CODE) {
					String from = data.getExtras().getString("from");
					String to = data.getExtras().getString("to");
					mapCommunicator.PassPlaceDirectionToMap(from, to);
				}

			}
		}
	}
}
