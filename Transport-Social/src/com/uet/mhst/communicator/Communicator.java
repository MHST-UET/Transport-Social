package com.uet.mhst.communicator;

import android.database.Cursor;

import com.uet.mhst.itemendpoint.model.Item;

public class Communicator {

	public interface MainMapCommunicator {
		public void MainPassToMap(Cursor c);
	}
}
