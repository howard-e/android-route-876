package com.route876.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.route876.R;
import com.route876.fragments.homefragments.MyRouteFragment;
import com.route876.fragments.homefragments.TravelFragment;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        setupTabs();
    }

    private void setupTabs() {
        mTabLayout.addTab(mTabLayout.newTab().setText("Travel"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Route"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("News"));

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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END))
            mDrawerLayout.closeDrawer(GravityCompat.END);
        else
            super.onBackPressed();
//        if (mTabLayout.getVisibility() == View.GONE) {
////            Animation bottomDown = AnimationUtils.loadAnimation(this, R.anim.bottom_down);
////            mTabLayout.startAnimation(bottomDown);
//            mTabLayout.setVisibility(View.VISIBLE);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                startActivity(new Intent(this, SettingsActivity.class));
//                break;
            case R.id.action_news:
                mDrawerLayout.openDrawer(GravityCompat.END);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public TabLayout getmTabLayout() {
        return mTabLayout;
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
//                case 2:
//                    fragment = new NewsFragment();
//                    break;
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
}
