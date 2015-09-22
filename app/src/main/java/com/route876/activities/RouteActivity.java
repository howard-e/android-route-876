package com.route876.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.route876.R;
import com.route876.classes.ParseRoute;
import com.route876.classes.objects.Route;
import com.route876.fragments.travelfragments.CoasterFragment;
import com.route876.fragments.travelfragments.JutcFragment;
import com.route876.fragments.travelfragments.PrivateTaxiFragment;
import com.route876.fragments.travelfragments.RouteTaxiFragment;

import java.util.List;

/**
 * Created by Howard on 9/12/2015.
 */
public class RouteActivity extends AppCompatActivity {

    boolean openJutc;
    boolean openCoaster;
    boolean openPrivateTaxi;
    boolean openRouteTaxi;
    private List<Route> routeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ParseRoute parseRoute = new ParseRoute(this);
        routeItems = parseRoute.getRouteList();

        Intent intent = getIntent();
        if (intent.getAction() != null) {
            if (intent.getAction().equals("jutc")) {
                openJutc = true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new JutcFragment())
                        .commit();
            }
            if (intent.getAction().equals("coaster")) {
                openCoaster = true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new CoasterFragment())
                        .commit();
            }
            if (intent.getAction().equals("privatetaxi")) {
                openPrivateTaxi = true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new PrivateTaxiFragment())
                        .commit();
            }
            if (intent.getAction().equals("routetaxi")) {
                openRouteTaxi = true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new RouteTaxiFragment())
                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<Route> getRouteItems() {
        return routeItems;
    }
}
