package com.uet.mhst.utility;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class ReverseGeocodingTask {
	private Context mContext;

	public ReverseGeocodingTask(Context context) {
		super();
		mContext = context;
	}

	public String getAddressText(LatLng latLng) {
		Geocoder geocoder = new Geocoder(mContext);
		double latitude = latLng.latitude;
		double longitude = latLng.longitude;

		List<Address> addresses = null;
		String addressText = "";

		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);

			addressText = String.format("%s %s %s", address
					.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0)
					: "", address.getLocality() == "" ? address.getLocality()
					: "", address.getCountryName());

		}
		return addressText;
	}

}
