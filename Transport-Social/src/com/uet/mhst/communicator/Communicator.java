package com.uet.mhst.communicator;

import com.uet.mhst.itemendpoint.model.Item;

public class Communicator {
	public interface FragmentCommunicator {
		public void passDataToFragment(Item item);
	}

	public interface ActivityCommunicator {
		public void passDataToActivity(Item item);
	}
}
