package com.route876.classes;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.route876.classes.objects.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 9/13/2015.
 */
public class ParseRoute {

    private boolean localStoreInfo = false;
    private Context context;
    private List<Route> routeList = new ArrayList<>();

    public ParseRoute(Context context) {
        this.context = context;
        getRouteInformation(localStoreInfo);
    }

    public void getRouteInformation(boolean fromLocalDataStore) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("route_info");
            if (fromLocalDataStore)
                query.fromLocalDatastore();

            query.orderByAscending("route_ID");
            List<ParseObject> parseObjectList = query.find();
            for (ParseObject parseObject : parseObjectList) {
                Route route = new Route();
                localStoreInfo = true;
                parseObject.pinInBackground();

                route.setId(parseObject.getInt("route_ID"));
                route.setRouteNum(parseObject.getString("routeNum"));
                route.setRouteFrom(parseObject.getString("routeFrom"));
                route.setRouteTo(parseObject.getString("routeTo"));
                route.setRouteVia(parseObject.getString("routeVia"));
                route.setRouteTag(parseObject.getString("routeTag"));

                List<String> routeStops = parseObject.getList("routeStops");
                route.setRouteStops(routeStops);

                routeList.add(route);
//                parseObject.pinInBackground();
            }
        } catch (ParseException e) {
            Log.e("Parse Error: ", e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Route> getRouteList() {
        return routeList;
    }
}
