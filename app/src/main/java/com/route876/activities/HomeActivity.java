package com.route876.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.route876.R;
import com.route876.fragments.homefragments.MyRouteFragment;
import com.route876.fragments.homefragments.NewsFragment;
import com.route876.fragments.homefragments.TravelFragment;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        setupTabs();
    }

    private void setupTabs() {
        mTabLayout.addTab(mTabLayout.newTab().setText("Travel"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Route"));
        mTabLayout.addTab(mTabLayout.newTab().setText("News"));

        ViewPagerHomeSectionAdapter viewPagerHomeSectionAdapter = new ViewPagerHomeSectionAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(viewPagerHomeSectionAdapter);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mTabLayout.getVisibility() == View.GONE) {
            mTabLayout.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public class ViewPagerHomeSectionAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public ViewPagerHomeSectionAdapter(FragmentManager fragmentManager, int NumOfTabs) {
            super(fragmentManager);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new TravelFragment();
                    break;
                case 1:
                    fragment = new MyRouteFragment();
                    break;
                case 2:
                    fragment = new NewsFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    public TabLayout getmTabLayout() {
        return mTabLayout;
    }
}
