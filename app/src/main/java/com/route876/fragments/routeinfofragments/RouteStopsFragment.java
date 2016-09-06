package com.route876.fragments.routeinfofragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.route876.R;

import java.util.ArrayList;

/**
 * Created by Howard on 9/14/2015.
 */
public class RouteStopsFragment extends Fragment {

    // TODO: Evaluate the creation of a new list so other selected routes will not add to a previous list displayed in this fragment
    private ArrayList<String> busVias;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route_stops, container, false);
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);

        busVias = getArguments().getStringArrayList("BusVias");
        if (busVias != null) {
            for (int i = 0; i < busVias.size(); i++) {
                TextView routeStopTextView = new TextView(getActivity());
                routeStopTextView.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                routeStopTextView.setPadding(20, 20, 20, 20);
                routeStopTextView.setText(busVias.get(i));
                linearLayout.addView(routeStopTextView);
            }
        }
        // TODO: Display error message if no routes are available for the user
//        else {
//            LayoutParams layoutParams = new LayoutParams(
//                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            layoutParams.weight = 1.0f;
//            layoutParams.gravity = Gravity.CENTER;
//            linearLayout.setLayoutParams(layoutParams);
//            TextView noRouteStopsAvailableTextView = new TextView(getActivity());
//            noRouteStopsAvailableTextView.setLayoutParams(new LayoutParams(
//                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//            noRouteStopsAvailableTextView.setText("Sorry, no stops found for this route ;(");
//            noRouteStopsAvailableTextView.setTextSize(24.0f);
//            noRouteStopsAvailableTextView.setTextColor(Color.BLACK);
//            linearLayout.addView(noRouteStopsAvailableTextView);
//        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        busVias = new ArrayList<>();
    }

    @Override
    public void onPause() {
        super.onPause();
        busVias = new ArrayList<>();
    }
}
