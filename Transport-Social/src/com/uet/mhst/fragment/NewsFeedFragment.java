package com.uet.mhst.fragment;

import java.util.ArrayList;
import java.util.List;

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

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.uet.mhst.MainActivity;
import com.uet.mhst.R;
import com.uet.mhst.adapter.FeedListAdapter;
import com.uet.mhst.communicator.Communicator;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.CollectionResponseItem;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.model.ItemSerializable;

public class NewsFeedFragment extends Fragment
{
	private FeedListAdapter listAdapter;
	private PullAndLoadListView listView;
	private ArrayList<Item> feedItems;
	private int pos;
	private Activity activity;
	public static final int REQUEST_CODE_INPUT = 113;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_news_feed,
				container, false);

		listView = (PullAndLoadListView) rootView.findViewById(R.id.list);
		feedItems = new ArrayList<Item>();
		listAdapter = new FeedListAdapter(activity, feedItems);
		listView.setAdapter(listAdapter);
		listView.setOnRefreshListener(new OnRefreshListener()
		{

			@Override
			public void onRefresh()
			{
				// TODO Auto-generated method stub
				new PullToRefreshDataTask().execute();
			}
		});
		listView.setOnLoadMoreListener(new OnLoadMoreListener()
		{

			@Override
			public void onLoadMore()
			{
				// TODO Auto-generated method stub
				new LoadMoreDataTask().execute();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
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

		imageViewUpStatus.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				Intent upstatus = new Intent("com.uet.mhst.UpNewsFeedActivity");

				startActivity(upstatus);
			}
		});

		new NewsFeedAsyncTask().execute();

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_INPUT)
		{
			if (resultCode == 1)
			{
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
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}

	private class NewsFeedAsyncTask extends
			AsyncTask<Void, Void, CollectionResponseItem>
	{

		protected CollectionResponseItem doInBackground(Void... unused)
		{
			CollectionResponseItem items = null;
			try
			{
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				items = service.listItem().setLimit(3).execute();
			}
			catch (Exception e)
			{
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		protected void onPostExecute(CollectionResponseItem items)
		{
			super.onPostExecute(items);
			List<Item> _list = items.getItems();
			for (Item item : _list)
			{
				feedItems.add(item);
			}
			listAdapter.notifyDataSetChanged();
		}
	}

	private class LoadMoreDataTask extends
			AsyncTask<Void, Void, CollectionResponseItem>
	{

		@Override
		protected CollectionResponseItem doInBackground(Void... params)
		{
			CollectionResponseItem items = null;
			if (isCancelled()) { return null; }
			try
			{
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				items = service.listItem()
						.setTime(feedItems.get(feedItems.size()).getTime())
						.setLimit(10).execute();
			}
			catch (Exception e)
			{
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		@Override
		protected void onPostExecute(CollectionResponseItem items)
		{
			super.onPostExecute(items);
			List<Item> _list = items.getItems();
			for (Item item : _list)
			{
				feedItems.add(item);
			}
			listAdapter.notifyDataSetChanged();
			listView.onLoadMoreComplete();

		}

		@Override
		protected void onCancelled()
		{
			// Notify the loading more operation has finished
			listView.onLoadMoreComplete();
		}
	}

	private class PullToRefreshDataTask extends
			AsyncTask<Void, Void, CollectionResponseItem>
	{

		@Override
		protected CollectionResponseItem doInBackground(Void... params)
		{
			CollectionResponseItem items = null;
			if (isCancelled()) { return null; }
			try
			{
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				items = service.listItem().setLimit(3).execute();
			}
			catch (Exception e)
			{
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		@Override
		protected void onPostExecute(CollectionResponseItem items)
		{
			super.onPostExecute(items);
			List<Item> _list = items.getItems();
			feedItems = new ArrayList<Item>();
			for (Item item : _list)
			{
				feedItems.add(item);
			}
			listAdapter.notifyDataSetChanged();
			listView.onRefreshComplete();

		}

		@Override
		protected void onCancelled()
		{
			listView.onRefreshComplete();
		}
	}
}
