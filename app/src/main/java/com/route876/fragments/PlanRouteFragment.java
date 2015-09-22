package com.route876.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.route876.R;
import com.route876.activities.HomeActivity;

/**
 * Created by Howard on 9/13/2015.
 */
public class PlanRouteFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // TODO: Hide TabLayout when opened
        ((HomeActivity)getActivity()).getmTabLayout().setVisibility(View.GONE);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan_route, container, false);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.plan_route_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plan_route_search:
                Toast.makeText(getActivity(), "Do some database checking based on what's selected", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                ((HomeActivity)getActivity()).getmTabLayout().setVisibility(View.VISIBLE);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
