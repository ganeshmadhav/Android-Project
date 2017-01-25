package com.examples.csci571.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.csci571.homework9.LegislatorA;
import com.example.csci571.homework9.LegislatorB;
import com.example.csci571.homework9.LegislatorC;

/**
 * Created by Ganesh on 17-11-2016.
 */

public class LgPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public LgPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LegislatorA tab1 = new LegislatorA();
                return tab1;
            case 1:
                LegislatorB tab2 = new LegislatorB();
                return tab2;
            case 2:
                LegislatorC tab3 = new LegislatorC();
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