package com.route876.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.route876.R;
import com.route876.objects.Route;

import java.util.ArrayList;

/**
 * Created by Howard on 9/13/2015.
 */
public class CustomRouteAdapter extends BaseAdapter implements Filterable {
//public class CustomRouteAdapter extends ArrayAdapter<Route> implements Filterable {

    private ArrayList<Route> filteredRouteArrayList;
    private ArrayList<Route> originalRouteArrayList;

    private RouteFilter routeFilter;

    private LayoutInflater mInflater;

    public CustomRouteAdapter(Context context, ArrayList<Route> filteredRouteArrayList) {
//        super(context, 0, filteredRouteArrayList);
        mInflater = LayoutInflater.from(context);
        this.filteredRouteArrayList = filteredRouteArrayList;
        originalRouteArrayList = filteredRouteArrayList;

        routeFilter = new RouteFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Route route = getItem(position);

        if (convertView == null)
            convertView = mInflater.inflate(R.layout.item_route_listview, null);
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_route_listview, parent, false);

        TextView routeTo = (TextView) convertView.findViewById(R.id.route_to);
        TextView routeFrom = (TextView) convertView.findViewById(R.id.route_from);
        TextView routeVia = (TextView) convertView.findViewById(R.id.route_via);
        TextView routeNum = (TextView) convertView.findViewById(R.id.route_num);
//        routeVia.setSelected(true);

        routeTo.setText(route.getRouteTo());
        routeFrom.setText(route.getRouteFrom());
        routeVia.setText(route.getRouteVia());
        routeNum.setText(route.getRouteNum());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return routeFilter;
    }

    private class RouteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Route> results = new ArrayList<Route>();

            if (originalRouteArrayList == null)
                originalRouteArrayList = filteredRouteArrayList;
            if (constraint != null) {
                if (originalRouteArrayList != null && originalRouteArrayList.size() > 0) {
                    for (Route route : originalRouteArrayList) {
                        if (route.getRouteFrom().toLowerCase().contains(constraint))
                            results.add(route);
                        if (route.getRouteTo().toLowerCase().contains(constraint))
                            results.add(route);
                    }
                }
                filterResults.values = results;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredRouteArrayList = (ArrayList<Route>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return filteredRouteArrayList.size();
    }

    @Override
    public Route getItem(int position) {
        return filteredRouteArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}