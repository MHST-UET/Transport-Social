package com.uet.mhst.fragment;

import com.uet.mhst.MainActivity;
import com.uet.mhst.R;
import com.uet.mhst.communicator.*;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.utility.GPSTracker;
import com.uet.mhst.utility.ReverseGeocodingTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements
		Communicator.FragmentCommunicator {
	private GoogleMap googleMap;
	public Context context;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = getActivity();
		// activityCommunicator = (ActivityCommunicator) context;
		((MainActivity) context).fragmentCommunicator = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);
		googleMap = ((SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		Button checkin = (Button) rootView.findViewById(R.id.btn_checkin);
		checkin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent upstatus = new Intent("com.uet.mhst.UpNewsFeedActivity");

				startActivity(upstatus);
			}
		});
		googleMap.setMyLocationEnabled(true);

		return rootView;
	}

	@Override
	public void passDataToFragment(Item item) {
		// TODO Auto-generated method stub

		// LatLng locationMarker = new LatLng(Double.valueOf(item.getLat()),
		// Double.valueOf(item.getLag()));
		// MarkerOptions marker = new MarkerOptions();
		// marker.position(locationMarker);
		// marker.icon(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		// marker.title(item.getContent());
		// googleMap.addMarker(marker);
		// googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
		// locationMarker, 15));
	}
}
