package com.uet.mhst;

import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.uet.mhst.itemendpoint.Itemendpoint;
import com.uet.mhst.itemendpoint.model.CollectionResponseItem;
import com.uet.mhst.itemendpoint.model.Item;
import com.uet.mhst.utility.ConnectionDetector;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class MyService extends Service {

	private ConnectionDetector cd;
	Boolean isInternetPresent = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		cd = new ConnectionDetector(getBaseContext());
	}

	@Override
	public void onStart(Intent intent, int startId) {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		MyThread myThread = new MyThread();
		myThread.start();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {

	}

	private class MyThread extends Thread {
		private Long check = 0l;

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			while (true) {

				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {

					Item item = null;
					CollectionResponseItem items = null;
					try {
						Itemendpoint.Builder builder = new Itemendpoint.Builder(
								AndroidHttp.newCompatibleTransport(),
								new GsonFactory(), null);
						Itemendpoint service = builder.build();
						items = service.listItem().setLimit(1).execute();

						List<Item> _list = items.getItems();
						item = _list.get(0);

						if (!check.equals(item.getId().getId())) {
							check = item.getId().getId();

							NotificationManager notificationManager;
							int notificationId = 1;
							String serName = Context.NOTIFICATION_SERVICE;
							notificationManager = (NotificationManager) getSystemService(serName);
							Notification notification = new Notification(
									R.drawable.ic_launcher, "Transport Social",
									System.currentTimeMillis());
							SharedPreferences pre = getSharedPreferences(
									"dataSetting", MODE_PRIVATE);
							if (pre.getBoolean("audio", false)) {
								Uri alarmSound = RingtoneManager
										.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
								notification.sound = alarmSound;
							}

							String var = "";
							switch (item.getStatus()) {
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

							RemoteViews contentView = new RemoteViews(
									getPackageName(),
									R.layout.notification_layout);
							contentView.setImageViewResource(R.id.image,
									R.drawable.ic_launcher);
							contentView.setTextViewText(R.id.title, var);
							contentView.setTextViewText(R.id.text,
									item.getAddress());
							notification.contentView = contentView;

							Intent intent = new Intent(getApplicationContext(),
									LoginFacebookActivity.class);
							PendingIntent launchIntent = PendingIntent
									.getActivity(getApplicationContext(), 0,
											intent, 0);
							notification.contentIntent = launchIntent;

							Intent not = new Intent(getApplicationContext(),
									UpNewsFeedActivity.class);
							PendingIntent launch = PendingIntent.getActivity(
									getApplicationContext(), 0, not, 0);
							notification.contentView.setOnClickPendingIntent(
									R.id.btn_save, launch);
							notification.flags |= Notification.FLAG_ONGOING_EVENT;
							notificationId = 1;
							notificationManager.notify(notificationId,
									notification);
						}
					} catch (Exception e) {
						Log.d("Could not retrieve News Feed", e.getMessage(), e);
					}
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	}

}