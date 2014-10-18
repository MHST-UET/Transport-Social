package com.uet.mhst.fragment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.uet.mhst.MainActivity;
import com.uet.mhst.R;

import com.uet.mhst.communicator.*;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.CollectionResponseItem;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.utility.GPSTracker;
import com.uet.mhst.utility.GoogleMapUtis;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

public class MapFragment extends Fragment implements
		Communicator.MainMapCommunicator {
	private static final HttpTransport HTTP_TRANSPORT = AndroidHttp
			.newCompatibleTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private GoogleMap googleMap;
	public Context context;
	private GPSTracker myLocation;
	private List<Marker> markers = new ArrayList<Marker>();
	private ProgressDialog progress;
	private List<LatLng> latLngs = new ArrayList<LatLng>();
	private Animator animator = new Animator();
	private final Handler mHandler = new Handler();
	private LinearLayout layout_infor;
	private TextView txt_from, txt_to;
	private ImageView img_direction;

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
		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");
		progress.setIndeterminate(true);
		layout_infor = (LinearLayout) rootView.findViewById(R.id.layout_infor);
		txt_from = (TextView) rootView.findViewById(R.id.txt_from);
		txt_to = (TextView) rootView.findViewById(R.id.txt_to);
		img_direction = (ImageView) rootView.findViewById(R.id.img_direction);
		layout_infor.setVisibility(View.INVISIBLE);

		googleMap = ((SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		myLocation = new GPSTracker(context);
		ImageView img_compass = (ImageView) rootView
				.findViewById(R.id.img_compass);
		ImageView place = (ImageView) rootView.findViewById(R.id.place);
		place.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new PlaceAsyncTask().execute();
			}
		});
		img_compass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent("com.uet.mhst.CompassActivity"));
			}
		});
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.getUiSettings().setRotateGesturesEnabled(true);
		googleMap.getUiSettings().setCompassEnabled(true);
		return rootView;
	}

	@Override
	public void MainPassToMap(Cursor c) {
		// TODO Auto-generated method stub
		((MainActivity) getActivity()).selectTab(0);
		MarkerOptions markerOptions = null;
		LatLng position = null;
		googleMap.clear();
		layout_infor.setVisibility(View.INVISIBLE);
		while (c.moveToNext()) {
			markerOptions = new MarkerOptions();
			position = new LatLng(Double.parseDouble(c.getString(1)),
					Double.parseDouble(c.getString(2)));
			markerOptions.position(position);
			markerOptions.title(c.getString(0));

			googleMap.addMarker(markerOptions);
		}
		if (position != null) {

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(position).zoom(15).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		}
	}

	private class PlaceAsyncTask extends AsyncTask<Void, Void, Void> {
		CollectionResponseItem items = null;
		SharedPreferences pre = context.getSharedPreferences("dataSetting",
				context.MODE_PRIVATE);
		private int num = 0;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.show();
		}

		protected Void doInBackground(Void... unused) {
			try {

				Itemendpoint.Builder builder = new Itemendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Itemendpoint service = builder.build();

				items = service
						.listItem()
						.setLon(myLocation.getLongitude())
						.setLat(myLocation.getLatitude())
						.setDistance(Double.valueOf(pre.getString("rad", "0")))
						.setLimit(Integer.valueOf(pre.getString("number", "0")))
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
			googleMap.clear();
			try {

				List<Item> list = items.getItems();
				num = list.size();
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

					MarkerOptions marker = new MarkerOptions()
							.position(
									new LatLng(tem.getLatitude(), tem
											.getLongitude())).title(var)
							.snippet(tem.getAddress());

					googleMap.addMarker(marker);

				}

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(list.get(0).getLatitude(), list.get(
								0).getLongitude())).zoom(15).build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
			} catch (Exception e) {

			}
			progress.dismiss();
			Toast.makeText(context, String.valueOf(num) + " places",
					Toast.LENGTH_SHORT).show();
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

	@Override
	public void PassAPlaceToMap(Bundle bundle) {
		// TODO Auto-generated method stub
		layout_infor.setVisibility(View.INVISIBLE);
		googleMap.clear();
		MarkerOptions marker = new MarkerOptions()
				.position(
						new LatLng(bundle.getDouble("lat"), bundle
								.getDouble("lng")))
				.title(bundle.getString("status"))
				.snippet(bundle.getString("address"));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(bundle.getDouble("lat"), bundle
						.getDouble("lng"))).zoom(15).build();

		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		googleMap.addMarker(marker);
	}

	@Override
	public void PassPlaceDirectionToMap(String from, String to) {
		((MainActivity) getActivity()).selectTab(0);
		// TODO Auto-generated method stub
		txt_from.setText("From: " + from);
		txt_to.setText("To: " + to);
		layout_infor.setVisibility(View.VISIBLE);
		new DirectionsFetcher(from, to).execute();
	}

	public void clearMarkers() {
		googleMap.clear();
		markers.clear();
	}

	public static class DirectionsResult {

		@Key("routes")
		public List<Route> routes;

	}

	public static class Route {
		@Key("overview_polyline")
		public OverviewPolyLine overviewPolyLine;

	}

	public static class OverviewPolyLine {
		@Key("points")
		public String points;

	}

	public void addPolylineToMap(List<LatLng> latLngs) {
		PolylineOptions options = new PolylineOptions();
		for (LatLng latLng : latLngs) {
			options.add(latLng);
		}
		googleMap.addPolyline(options);
	}

	private class DirectionsFetcher extends AsyncTask<URL, Integer, Void> {

		private String origin;
		private String destination;
		private boolean check = true;

		public DirectionsFetcher(String origin, String destination) {
			this.origin = origin;
			this.destination = destination;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			clearMarkers();
			getActivity().setProgressBarIndeterminateVisibility(Boolean.TRUE);

		}

		protected Void doInBackground(URL... urls) {
			try {
				HttpRequestFactory requestFactory = HTTP_TRANSPORT
						.createRequestFactory(new HttpRequestInitializer() {
							@Override
							public void initialize(HttpRequest request) {
								request.setParser(new JsonObjectParser(
										JSON_FACTORY));
							}
						});

				GenericUrl url = new GenericUrl(
						"http://maps.googleapis.com/maps/api/directions/json");
				url.put("origin", origin);
				url.put("destination", destination);
				url.put("sensor", false);

				HttpRequest request = requestFactory.buildGetRequest(url);
				HttpResponse httpResponse = request.execute();
				DirectionsResult directionsResult = httpResponse
						.parseAs(DirectionsResult.class);

				String encodedPoints = directionsResult.routes.get(0).overviewPolyLine.points;
				latLngs = PolyUtil.decode(encodedPoints);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;

		}

		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Void result) {

			addPolylineToMap(latLngs);

			GoogleMapUtis.fixZoomForLatLngs(googleMap, latLngs);

			img_direction.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (check == true) {
						check = !check;
						animator.startAnimation(false, latLngs);
					} else {
						check = !check;
						animator.stopAnimation();

					}

				}
			});

		}
	}

	public class Animator implements Runnable {

		private static final int ANIMATE_SPEEED = 1500;
		private static final int ANIMATE_SPEEED_TURN = 1500;
		private static final int BEARING_OFFSET = 20;

		private final Interpolator interpolator = new LinearInterpolator();

		private boolean animating = false;

		private List<LatLng> latLngs = new ArrayList<LatLng>();

		int currentIndex = 0;

		float tilt = 90;
		float zoom = 15.5f;
		boolean upward = true;

		long start = SystemClock.uptimeMillis();

		LatLng endLatLng = null;
		LatLng beginLatLng = null;

		boolean showPolyline = false;

		private Marker trackingMarker;

		public void reset() {
			resetMarkers();
			start = SystemClock.uptimeMillis();
			currentIndex = 0;
			endLatLng = getEndLatLng();
			beginLatLng = getBeginLatLng();

		}

		public void stopAnimation() {
			animating = false;
			mHandler.removeCallbacks(animator);

		}

		public void initialize(boolean showPolyLine) {
			reset();
			this.showPolyline = showPolyLine;

			highLightMarker(0);

			if (showPolyLine) {
				polyLine = initializePolyLine();
			}

			// We first need to put the camera in the correct position for the
			// first run (we need 2 markers for this).....
			LatLng markerPos = latLngs.get(0);
			LatLng secondPos = latLngs.get(1);

			setInitialCameraPosition(markerPos, secondPos);

		}

		private void setInitialCameraPosition(LatLng markerPos, LatLng secondPos) {

			float bearing = GoogleMapUtis.bearingBetweenLatLngs(markerPos,
					secondPos);

			trackingMarker = googleMap.addMarker(new MarkerOptions().position(
					markerPos).title("Direction"));

			float mapZoom = googleMap.getCameraPosition().zoom >= 16 ? googleMap
					.getCameraPosition().zoom : 16;

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(markerPos).bearing(bearing + BEARING_OFFSET)
					.tilt(90).zoom(mapZoom).build();

			googleMap.animateCamera(
					CameraUpdateFactory.newCameraPosition(cameraPosition),
					ANIMATE_SPEEED_TURN, new CancelableCallback() {

						@Override
						public void onFinish() {

							animator.reset();
							Handler handler = new Handler();
							handler.post(animator);
						}

						@Override
						public void onCancel() {

						}
					});
		}

		private Polyline polyLine;
		private PolylineOptions rectOptions = new PolylineOptions();

		private Polyline initializePolyLine() {
			// polyLinePoints = new ArrayList<LatLng>();
			rectOptions.add(latLngs.get(0));
			return googleMap.addPolyline(rectOptions);
		}

		/**
		 * Add the marker to the polyline.
		 */
		private void updatePolyLine(LatLng latLng) {
			List<LatLng> points = polyLine.getPoints();
			points.add(latLng);
			polyLine.setPoints(points);
		}

		public void startAnimation(boolean showPolyLine, List<LatLng> latLngs) {
			if (trackingMarker != null) {
				trackingMarker.remove();
			}
			this.animating = true;
			this.latLngs = latLngs;
			if (latLngs.size() > 2) {
				initialize(showPolyLine);
			}

		}

		public boolean isAnimating() {
			return this.animating;
		}

		@Override
		public void run() {

			long elapsed = SystemClock.uptimeMillis() - start;
			double t = interpolator.getInterpolation((float) elapsed
					/ ANIMATE_SPEEED);
			LatLng intermediatePosition = SphericalUtil.interpolate(
					beginLatLng, endLatLng, t);

			Double mapZoomDouble = 18.5 - (Math.abs((0.5 - t)) * 5);
			float mapZoom = mapZoomDouble.floatValue();

			trackingMarker.setPosition(intermediatePosition);

			if (showPolyline) {
				updatePolyLine(intermediatePosition);
			}

			if (t < 1) {
				mHandler.postDelayed(this, 16);
			} else {

				// imagine 5 elements - 0|1|2|3|4 currentindex must be smaller
				// than 4
				if (currentIndex < latLngs.size() - 2) {

					currentIndex++;

					endLatLng = getEndLatLng();
					beginLatLng = getBeginLatLng();

					start = SystemClock.uptimeMillis();

					Double heading = SphericalUtil.computeHeading(beginLatLng,
							endLatLng);

					highLightMarker(currentIndex);

					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(endLatLng)
							.bearing(heading.floatValue() /* + BEARING_OFFSET */)
							// .bearing(bearingL + BEARING_OFFSET)
							.tilt(tilt)
							.zoom(googleMap.getCameraPosition().zoom).build();

					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition),
							ANIMATE_SPEEED_TURN, null);

					// start = SystemClock.uptimeMillis();
					mHandler.postDelayed(this, 16);

				} else {
					currentIndex++;
					highLightMarker(currentIndex);
					stopAnimation();
				}

			}
		}

		private LatLng getEndLatLng() {
			return latLngs.get(currentIndex + 1);
		}

		private LatLng getBeginLatLng() {
			return latLngs.get(currentIndex);
		}

	};

	private void highLightMarker(int index) {
		if (markers.size() >= index + 1) {
			highLightMarker(markers.get(index));
		}
	}

	/**
	 * Highlight the marker by marker.
	 */
	private void highLightMarker(Marker marker) {

		if (marker != null) {
			marker.setIcon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
			marker.showInfoWindow();
		}

	}

	private void resetMarkers() {
		for (Marker marker : this.markers) {
			marker.setIcon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		}
	}
}
