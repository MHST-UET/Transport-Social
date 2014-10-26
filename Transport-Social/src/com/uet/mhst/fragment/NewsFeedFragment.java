package com.uet.mhst.fragment;

import com.uet.mhst.R;
import java.util.ArrayList;
import java.util.List;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import android.widget.ListView;
import com.uet.mhst.adapter.FeedListAdapter;
import com.uet.mhst.itemendpoint.model.*;
import com.uet.mhst.itemendpoint.*;
import com.uet.mhst.utility.ConnectionDetector;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFeedFragment extends Fragment {

	private FeedListAdapter listAdapter;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayList<Item> feedItems;
	private Activity activity;
	public static final int NUMBER_ITEM = 10;
	private ProgressDialog progress;
	private ConnectionDetector cd;
	Boolean isInternetPresent = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news_feed,
				container, false);
		mPullRefreshListView = (PullToRefreshListView) rootView
				.findViewById(R.id.pull_refresh_list);
		feedItems = new ArrayList<Item>();
		listAdapter = new FeedListAdapter(activity, feedItems);
		mPullRefreshListView.setAdapter(listAdapter);
		cd = new ConnectionDetector(activity);
		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");
		progress.setIndeterminate(true);

		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {

						// Do work to refresh the list here.
						isInternetPresent = cd.isConnectingToInternet();
						if (isInternetPresent) {
							new PullToRefreshDataTask().execute();
						} else {

							showAlertDialog(activity, "No Internet",
									"You don't have internet connection.",
									false);
						}

					}
				});

		// Add an end-of-list listener
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						// new LoadMoreDataTask().execute();

						isInternetPresent = cd.isConnectingToInternet();
						if (isInternetPresent) {
							new LoadMoreDataTask().execute();
						} else {

							showAlertDialog(activity, "No Internet",
									"You don't have internet connection.",
									false);
						}
					}
				});

		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			new NewsFeedAsyncTask().execute();
		} else {

			showAlertDialog(activity, "No Internet",
					"You don't have internet connection.", false);
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}

	private class NewsFeedAsyncTask extends
			AsyncTask<Void, Void, CollectionResponseItem> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.show();
		}

		protected CollectionResponseItem doInBackground(Void... unused) {
			CollectionResponseItem items = null;
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				items = service.listItem().setLimit(NUMBER_ITEM).execute();

			} catch (Exception e) {
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		protected void onPostExecute(CollectionResponseItem items) {
			super.onPostExecute(items);
			try {
				List<Item> _list = items.getItems();
				for (Item item : _list) {
					feedItems.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			listAdapter.notifyDataSetChanged();
			progress.dismiss();
		}
	}

	private class LoadMoreDataTask extends
			AsyncTask<Void, Void, CollectionResponseItem> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.show();
		}

		@Override
		protected CollectionResponseItem doInBackground(Void... params) {
			CollectionResponseItem items = null;
			if (isCancelled()) {
				return null;
			}
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				items = service
						.listItem()
						.setTimeAfter(
								feedItems.get(feedItems.size() - 1).getTime())
						.setLimit(NUMBER_ITEM).execute();
			} catch (Exception e) {

				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		@Override
		protected void onPostExecute(CollectionResponseItem items) {
			super.onPostExecute(items);

			try {
				List<Item> _list = items.getItems();
				for (Item item : _list) {

					feedItems.add(item);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			listAdapter.notifyDataSetChanged();
			progress.dismiss();
		}

	}

	private class PullToRefreshDataTask extends
			AsyncTask<Void, Void, CollectionResponseItem> {

		@Override
		protected CollectionResponseItem doInBackground(Void... params) {
			CollectionResponseItem items = null;
			if (isCancelled()) {
				return null;
			}
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				items = service.listItem().setLimit(NUMBER_ITEM).execute();
			} catch (Exception e) {
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		@Override
		protected void onPostExecute(CollectionResponseItem items) {
			super.onPostExecute(items);

			try {
				List<Item> _list = items.getItems();
				feedItems.clear();
				for (Item item : _list) {
					feedItems.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			listAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}

	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
