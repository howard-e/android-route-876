package com.route876.fragments.jutcstartlocationfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.route876.fragments.routeinfofragments.RouteInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 9/22/2015.
 */
public class JutcStartLocationFragment extends Fragment {

    String startLocationString;
    ArrayList<Route> routesList = new ArrayList<>();
    ArrayList<Route> rList = new ArrayList<>();
    ArrayList<String> routeStops = new ArrayList<>();

    ListView routeListView;
    RouteInfoFragment routeInfoFragment = new RouteInfoFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String routeTag = "";
        startLocationString = getArguments().getString("StartLocation");
        if (startLocationString.equals("Kingston"))
            routeTag = "K";
        else if (startLocationString.equals("Portmore"))
            routeTag = "P";
        else if (startLocationString.equals("Spanish Town"))
            routeTag = "S";
        setUpRoutes(routeTag);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.route_listview, container, false);
        routeListView = (ListView) rootView.findViewById(R.id.route_listview);
        routeInfoFragment = new RouteInfoFragment();
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
                routeInfoFragment.setArguments(args);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.y_enter, R.anim.y_exit, R.anim.y_pop_enter, R.anim.y_pop_exit);
                fragmentTransaction.replace(R.id.container, routeInfoFragment)
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
