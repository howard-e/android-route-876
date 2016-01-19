package com.route876.fragments.travelfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.route876.R;
import com.route876.activities.RouteActivity;
import com.route876.classes.adapters.CustomRouteAdapter;
import com.route876.classes.objects.Route;
import com.route876.fragments.routeinfofragments.RouteMapFragment;

import java.util.ArrayList;
import java.util.List;
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
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
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

    public static class JutcStartLocationFragment extends Fragment {

        String startLocationString;
        ArrayList<Route> routesList = new ArrayList<>();
        ArrayList<Route> rList = new ArrayList<>();
        ArrayList<String> routeStops = new ArrayList<>();

        ListView routeListView;
        RouteMapFragment routeMapFragment = new RouteMapFragment();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            String routeTag = "";
            startLocationString = getArguments().getString("StartLocation");
            if (startLocationString != null) {
                switch (startLocationString) {
                    case "Kingston":
                        routeTag = "K";
                        break;
                    case "Portmore":
                        routeTag = "P";
                        break;
                    case "Spanish Town":
                        routeTag = "S";
                        break;
                }
            }
            setUpRoutes(routeTag);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.route_listview, container, false);
            routeListView = (ListView) rootView.findViewById(R.id.route_listview);
            routeMapFragment = new RouteMapFragment();
            CustomRouteAdapter customRouteAdapter = new CustomRouteAdapter(getActivity(), rList);

            routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle args = new Bundle();
                    TextView bus_textview_num = (TextView) view.findViewById(R.id.route_num);
                    String busNum = bus_textview_num.getText().toString();

                    List<String> routeStopsList = rList.get(position).getRouteStops();
                    if (routeStopsList != null) {
                        System.out.println("Route List right here: " + routeStopsList.toString());
                        for (int i = 0; i < routeStopsList.size(); i++) {
                            routeStops.add(routeStopsList.get(i));
                        }
                    } else {
                        Log.d("Empty", "no stops available for this route");
                    }

                    args.putString("BusNum", busNum);
                    args.putStringArrayList("BusVias", routeStops);
                    routeMapFragment.setArguments(args);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.y_enter, R.anim.y_exit, R.anim.y_pop_enter, R.anim.y_pop_exit);
                    fragmentTransaction.replace(R.id.container, routeMapFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            routeListView.setHeaderDividersEnabled(false);
            routeListView.setAdapter(customRouteAdapter);
            return rootView;
        }

        private void setUpRoutes(String routeTag) {
            if (!routeTag.equals("")) {
                routesList = (ArrayList<Route>) ((RouteActivity) getActivity()).getRouteItems();
                for (Route route : routesList) {
                    if (route.getRouteTag().equals(routeTag)) {
                        rList.add(route);
                    }
                }
            }
        }
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
                    args.putString("StartLocation", "Kingston");
                    fragment = new JutcStartLocationFragment();
                    fragment.setArguments(args);
                    break;
                case 1:
                    args.putString("StartLocation", "Portmore");
                    fragment = new JutcStartLocationFragment();
                    fragment.setArguments(args);
                    break;
                case 2:
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