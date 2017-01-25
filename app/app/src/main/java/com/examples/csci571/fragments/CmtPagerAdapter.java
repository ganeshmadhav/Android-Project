package com.examples.csci571.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.csci571.homework9.CmtHouse;
import com.example.csci571.homework9.CmtJoint;
import com.example.csci571.homework9.CmtSenate;

/**
 * Created by Ganesh on 18-11-2016.
 */

public class CmtPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CmtPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CmtHouse tab1 = new CmtHouse();
                return tab1;
            case 1:
                CmtSenate tab2 = new CmtSenate();
                return tab2;
            case 2:
                CmtJoint tab3 = new CmtJoint();
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
