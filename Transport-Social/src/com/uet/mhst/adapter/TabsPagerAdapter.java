package com.uet.mhst.adapter;

import com.uet.mhst.fragment.FriendFragment;
import com.uet.mhst.fragment.MapFragment;
import com.uet.mhst.fragment.NewsFeedFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:

			return new NewsFeedFragment();
		case 1:

			return new MapFragment();

		case 2:

			return new FriendFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
