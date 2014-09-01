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
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class NewsFeedFragment extends Fragment {
	private FeedListAdapter listAdapter;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayList<Item> feedItems;
	private Activity activity;
	public static final int NUMBER_ITEM = 5;
	public static final int REQUEST_CODE_INPUT = 113;

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
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent upstatus = new Intent(
						"com.uet.mhst.StatusDetailActivity");
				upstatus.putExtra("id", feedItems.get(position - 1).getId()
						.getId());

				startActivityForResult(upstatus, REQUEST_CODE_INPUT);
			}
		});

		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {

						// Do work to refresh the list here.
						new PullToRefreshDataTask().execute();
					}
				});

		// Add an end-of-list listener
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						new LoadMoreDataTask().execute();
						Toast.makeText(activity, "End of List!",
								Toast.LENGTH_SHORT).show();
					}
				});
		ImageView imageViewUpStatus = (ImageView) rootView
				.findViewById(R.id.imageView_upstatus);

		imageViewUpStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent upstatus = new Intent("com.uet.mhst.UpNewsFeedActivity");

				startActivity(upstatus);
			}
		});
		new NewsFeedAsyncTask().execute();
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_INPUT) {
			if (resultCode == 1) {

			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}

	private class NewsFeedAsyncTask extends
			AsyncTask<Void, Void, CollectionResponseItem> {

		protected CollectionResponseItem doInBackground(Void... unused) {
			CollectionResponseItem items = null;
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				items = service.listItem().setLimit(NUMBER_ITEM).setLat(21.0369).setLon(105.782).setDistance(10.0).execute();

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
		}
	}

	private class LoadMoreDataTask extends
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
				items = service.listItem()
						.setTimeAfter(feedItems.get(feedItems.size() - 1).getTime())
						.setLimit(NUMBER_ITEM).setLat(21.0369).setLon(105.782).setDistance(10.0).execute();
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
				items = service.listItem().setLimit(NUMBER_ITEM).setLat(21.0369).setLon(105.782).setDistance(10.0).execute();
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

}
