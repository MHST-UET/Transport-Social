package com.uet.mhst.fragment;

import com.uet.mhst.MainActivity;
import com.uet.mhst.R;
import com.uet.mhst.communicator.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements
		Communicator.MainMapCommunicator {
	private GoogleMap googleMap;
	public Context context;

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
		ImageView checkin = (ImageView) rootView.findViewById(R.id.checkin);
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

}
