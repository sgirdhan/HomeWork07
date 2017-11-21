package com.example.sharangirdhani.homework07;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sharangirdhani on 11/20/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ManageFriendsFragment tab1 = new ManageFriendsFragment();
                return tab1;
            case 1:
                AddNewFriendFragment tab2 = new AddNewFriendFragment();
                return tab2;
            case 2:
                PendingRequestFragment tab3 = new PendingRequestFragment();
                return tab3;
            default:
                return getItem(0);
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
