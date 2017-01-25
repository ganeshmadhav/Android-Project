package com.examples.csci571.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.csci571.homework9.R;

/**
 * Created by Ganesh on 17-11-2016.
 */

public class LegislatorsFrame extends Fragment {
    TabLayout legislatorTabs;
    ViewPager lgViewPage;
    LgPagerAdapter lgView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.legislators, container, false);
        /*legislatorTabs = (TabLayout) rootView.findViewById(R.id.legislatortab);
        lgViewPage = (ViewPager) rootView.findViewById(R.id.lgviewpager);
        lgView = new LgPagerAdapter(getActivity().getSupportFragmentManager());
        lgView.addFragments(new LegislatorA(), "Senate");
        lgView.addFragments(new LegislatorB(), "House");
        lgView.addFragments(new LegislatorC(), "State");
        lgViewPage.setAdapter(lgView);
        legislatorTabs.setupWithViewPager(lgViewPage);*/
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        /*setSupportActionBar(toolbar);*/

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.legislatortab);
        tabLayout.addTab(tabLayout.newTab().setText("State"));
        tabLayout.addTab(tabLayout.newTab().setText("House"));
        tabLayout.addTab(tabLayout.newTab().setText("Senate"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.lgviewpager);
        final PagerAdapter adapter = new LgPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return rootView;
    }
}
