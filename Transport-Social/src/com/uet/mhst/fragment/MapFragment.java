package com.uet.mhst.fragment;

import java.util.List;

import com.uet.mhst.MainActivity;
import com.uet.mhst.R;

import com.uet.mhst.communicator.*;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.CollectionResponseItem;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.utility.GPSTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;

public class MapFragment extends Fragment implements
		Communicator.MainMapCommunicator {
	private GoogleMap googleMap;
	public Context context;
	private GPSTracker myLocation;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = getActivity();
		((MainActivity) context).mapCommunicator = this;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);
		googleMap = ((SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		myLocation = new GPSTracker(context);
		ImageView checkin = (ImageView) rootView.findViewById(R.id.checkin);
		checkin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent upstatus = new Intent("com.uet.mhst.UpNewsFeedActivity");

				startActivity(upstatus);

			}
		});
		ImageView place = (ImageView) rootView.findViewById(R.id.place);
		place.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new PlaceAsyncTask().execute();
			}
		});
		googleMap.setMyLocationEnabled(true);

		return rootView;
	}

	@Override
	public void MainPassToMap(Cursor c) {
		// TODO Auto-generated method stub
		((MainActivity) getActivity()).selectTab(1);
		MarkerOptions markerOptions = null;
		LatLng position = null;
		googleMap.clear();
		while (c.moveToNext()) {
			markerOptions = new MarkerOptions();
			position = new LatLng(Double.parseDouble(c.getString(1)),
					Double.parseDouble(c.getString(2)));
			markerOptions.position(position);
			markerOptions.title(c.getString(0));
			googleMap.addMarker(markerOptions);
		}
		if (position != null) {
			CameraUpdate cameraPosition = CameraUpdateFactory
					.newLatLng(position);
			googleMap.animateCamera(cameraPosition);
		}
	}

	private class PlaceAsyncTask extends AsyncTask<Void, Void, Void> {
		CollectionResponseItem items = null;

		protected Void doInBackground(Void... unused) {

			try {
				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();

				items = service.listItem().setLon(myLocation.getLongitude())
						.setLat(myLocation.getLatitude()).setDistance(100.0)
						.execute();

			} catch (Exception e) {
				Log.d("Could not retrieve News Feed", e.getMessage(), e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			List<Item> list = items.getItems();
			Toast.makeText(context, String.valueOf(list.size()),
					Toast.LENGTH_LONG).show();
			for (Item tem : list) {

				String var = "";
				switch (tem.getStatus()) {
				case 3:
					var = "Tắc đường";
					break;
				case 2:
					var = "Đường đông";
					break;
				case 4:
					var = "Tai nạn";
					break;
				case 1:
					var = "Bình thường";
					break;
				}

				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(tem.getLatitude(), tem.getLongitude()))
						.title(var + " - " + tem.getAddress());

				googleMap.addMarker(marker);

			}

		}
	}

	@Override
	public void PassTypeMaptoMap(int type) {
		// TODO Auto-generated method stub
		if (type == 1) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		} else if (type == 2) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		} else if (type == 3) {
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}
	}

}
