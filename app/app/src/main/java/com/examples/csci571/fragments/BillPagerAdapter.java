package com.examples.csci571.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.csci571.homework9.Active;
import com.example.csci571.homework9.NewBill;

/**
 * Created by Ganesh on 18-11-2016.
 */

public class BillPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public BillPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Active tabactive = new Active();
                return tabactive;
            case 1:
                NewBill tabnew = new NewBill();
                return tabnew;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
