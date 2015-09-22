package com.route876.fragments.travelfragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.route876.R;
import com.route876.fragments.jutcstartlocationfragments.JutcStartLocationFragment;

import java.util.Locale;

/**
 * Created by Howard on 3/6/2015.
 */
public class JutcFragment extends Fragment {
    StartLocationPagerAdapter mStartLocationPagerAdapter;
    ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jutc, container, false);
        mTabLayout = (TabLayout)rootView.findViewById(R.id.tabLayout);
        mStartLocationPagerAdapter = new StartLocationPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        setUpTabs();
        mViewPager.setAdapter(mStartLocationPagerAdapter);
        return rootView;
    }

    private void setUpTabs() {
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_kingston).toUpperCase(Locale.getDefault())));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_portmore).toUpperCase(Locale.getDefault())));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_spanish_town).toUpperCase(Locale.getDefault())));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class StartLocationPagerAdapter extends FragmentPagerAdapter {
        public StartLocationPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle args = new Bundle();
            switch (position) {
                case 0:
//                    fragment = new KingstonFragment();
                    args.putString("StartLocation", "Kingston");
                    fragment = new JutcStartLocationFragment();
                    fragment.setArguments(args);
                    break;
                case 1:
//                    fragment = new PortmoreFragment();
                    args.putString("StartLocation", "Portmore");
                    fragment = new JutcStartLocationFragment();
                    fragment.setArguments(args);
                    break;
                case 2:
//                    fragment = new SpanishTownFragment();
                    args.putString("StartLocation", "Spanish Town");
                    fragment = new JutcStartLocationFragment();
                    fragment.setArguments(args);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}