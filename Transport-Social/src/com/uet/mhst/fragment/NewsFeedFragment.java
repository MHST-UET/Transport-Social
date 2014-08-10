package com.uet.mhst.fragment;

import com.uet.mhst.MainActivity;
import com.uet.mhst.R;
import java.util.ArrayList;
import java.util.List;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import com.uet.mhst.adapter.FeedListAdapter;
import com.uet.mhst.communicator.Communicator;
import com.uet.mhst.itemendpoint.model.*;
import com.uet.mhst.itemendpoint.*;
import com.uet.mhst.model.ItemSerializable;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class NewsFeedFragment extends Fragment implements IXListViewListener {
	private FeedListAdapter listAdapter;
	private XListView listView;
	private ArrayList<Item> feedItems;
	private int pos;
	private Activity activity;
	private Handler mHandler;
	public static final int NUMBER_ITEM = 10;
	public static final int REQUEST_CODE_INPUT = 113;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news_feed,
				container, false);
		listView = (XListView) rootView.findViewById(R.id.list);
		feedItems = new ArrayList<Item>();
		listAdapter = new FeedListAdapter(activity, feedItems);
		listView.setAdapter(listAdapter);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				ItemSerializable itemSer = new ItemSerializable(feedItems
						.get(position));
				pos = position;
				Intent upstatus = new Intent(
						"com.uet.mhst.StatusDetailActivity");
				upstatus.putExtra("detail", itemSer);

				startActivityForResult(upstatus, REQUEST_CODE_INPUT);
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
				// Toast.makeText(getActivity(), String.valueOf(pos),
				// Toast.LENGTH_SHORT).show();
				((MainActivity) getActivity()).selectTab(1);
				Item i = feedItems.get(pos);
				((Communicator.ActivityCommunicator) activity)
						.passDataToActivity(i);
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
				items = service.listItem().setLimit(NUMBER_ITEM).execute();
			} catch (Exception e) {
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		protected void onPostExecute(CollectionResponseItem items) {
			super.onPostExecute(items);
			List<Item> _list = items.getItems();
			for (Item item : _list) {
				feedItems.add(item);
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
						.setTime(feedItems.get(feedItems.size() - 1).getTime())
						.setLimit(NUMBER_ITEM).execute();
			} catch (Exception e) {
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		@Override
		protected void onPostExecute(CollectionResponseItem items) {
			super.onPostExecute(items);

			if (items.getItems() != null) {
				List<Item> _list = items.getItems();
				for (Item item : _list) {
					feedItems.add(item);
				}
			}
			listAdapter.notifyDataSetChanged();

		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished

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

			List<Item> _list = items.getItems();
			feedItems.clear();
			for (Item item : _list) {
				feedItems.add(item);
			}
			listAdapter.notifyDataSetChanged();

		}

		@Override
		protected void onCancelled() {

		}
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new PullToRefreshDataTask().execute();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new LoadMoreDataTask().execute();
		onLoad();
	}

}
