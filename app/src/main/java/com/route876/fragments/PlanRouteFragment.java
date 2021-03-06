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
import android.widget.CheckBox;
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
//        Animation bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
//        ((HomeActivity) getActivity()).getmTabLayout().startAnimation(bottomUp);
        ((HomeActivity) getActivity()).getmTabLayout().setVisibility(View.GONE);

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan_route, container, false);
        CheckBox jutcCheckBox = (CheckBox) rootView.findViewById(R.id.checkbox_jutc);
        CheckBox coasterCheckBox = (CheckBox) rootView.findViewById(R.id.checkbox_coaster);
        CheckBox taxiCheckBox = (CheckBox) rootView.findViewById(R.id.checkbox_taxi);

        if (isChecked(jutcCheckBox)) {

        }

        if (isChecked(coasterCheckBox)) {

        }

        if (isChecked(taxiCheckBox)) {

        }

        return rootView;
    }

    public boolean isChecked(CheckBox checkBox) {
        return checkBox.isChecked();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_plan_route, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plan_route:
                Toast.makeText(getActivity(), "Do some database checking based on what's selected", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
//                Animation bottomDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
//                ((HomeActivity)getActivity()).getmTabLayout().startAnimation(bottomDown);
                ((HomeActivity) getActivity()).getmTabLayout().setVisibility(View.VISIBLE);
                ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
