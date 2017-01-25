package com.examples.csci571.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.csci571.homework9.FavBills;
import com.example.csci571.homework9.FavCommittees;
import com.example.csci571.homework9.FavLegislators;

/**
 * Created by Ganesh on 18-11-2016.
 */

public class FavPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public FavPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FavLegislators tab1 = new FavLegislators();
                return tab1;
            case 1:
                FavBills tab2 = new FavBills();
                return tab2;
            case 2:
                FavCommittees tab3 = new FavCommittees();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
