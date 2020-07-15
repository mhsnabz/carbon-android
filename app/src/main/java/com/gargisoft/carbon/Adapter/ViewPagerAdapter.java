package com.gargisoft.carbon.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gargisoft.carbon.Home.DiscoverFragment;
import com.gargisoft.carbon.Home.FriendListFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FriendListFragment listFragment = new FriendListFragment();
                return listFragment;
            case 1:
                DiscoverFragment discoverFragment = new DiscoverFragment();
                return discoverFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
