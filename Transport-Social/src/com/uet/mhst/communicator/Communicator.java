package com.uet.mhst.communicator;

import android.database.Cursor;
import android.os.Bundle;

public class Communicator {

	public interface MainMapCommunicator {
		public void MainPassToMap(Cursor c);

		public void PassPlaceDirectionToMap(String from, String to);

		public void PassTypeMaptoMap(int type);

		public void PassAPlaceToMap(Bundle bundle);
	}
}
