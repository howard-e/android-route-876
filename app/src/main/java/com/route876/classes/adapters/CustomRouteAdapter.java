package com.route876.classes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.route876.R;
import com.route876.classes.objects.Route;

import java.util.ArrayList;

/**
 * Created by Howard on 9/13/2015.
 */
public class CustomRouteAdapter extends ArrayAdapter<Route> {
    public CustomRouteAdapter(Context context, ArrayList<Route> routes) {
        super(context, 0, routes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Route route = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.route_item, parent, false);

        TextView routeTo = (TextView) convertView.findViewById(R.id.route_to);
        TextView routeFrom = (TextView) convertView.findViewById(R.id.route_from);
        TextView routeVia = (TextView) convertView.findViewById(R.id.route_via);
//        routeVia.setSelected(true);
        TextView routeNum = (TextView) convertView.findViewById(R.id.route_num);

        routeTo.setText(route.routeTo);
        routeFrom.setText(route.routeFrom);
        routeVia.setText(route.routeVia);
        routeNum.setText(route.routeNum);
        return convertView;
    }
}