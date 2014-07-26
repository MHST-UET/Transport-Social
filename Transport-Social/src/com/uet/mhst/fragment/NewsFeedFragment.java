package com.uet.mhst.fragment;

import com.uet.mhst.R;
import java.util.ArrayList;
import java.util.List;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.uet.mhst.adapter.FeedListAdapter;
import com.uet.mhst.itemendpoint.model.*;
import com.uet.mhst.itemendpoint.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class NewsFeedFragment extends Fragment {
	private FeedListAdapter listAdapter;
	private ListView listView;
	private List<Item> feedItems;
	private ProgressBar progressbar;

	@SuppressWarnings("unused")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news_feed,
				container, false);

		progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		listView = (ListView) rootView.findViewById(R.id.list);
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

	private class NewsFeedAsyncTask extends
			AsyncTask<Void, Void, CollectionResponseItem> {

		protected CollectionResponseItem doInBackground(Void... unused) {
			CollectionResponseItem items = null;
			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();
				items = service.listItem().execute();
			} catch (Exception e) {
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return items;
		}

		protected void onPostExecute(CollectionResponseItem items) {
			progressbar.setVisibility(View.GONE);
			List<Item> _list = items.getItems();
			feedItems = new ArrayList<Item>();
			for (Item item : _list) {
				feedItems.add(item);
			}
			listAdapter = new FeedListAdapter(getActivity(), feedItems);
			listView.setAdapter(listAdapter);

		}
	}
}
