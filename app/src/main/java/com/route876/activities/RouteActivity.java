package com.route876.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.route876.R;
import com.route876.classes.objects.Route;
import com.route876.fragments.travelfragments.CoasterFragment;
import com.route876.fragments.travelfragments.JutcFragment;
import com.route876.fragments.travelfragments.PrivateTaxiFragment;
import com.route876.fragments.travelfragments.RouteTaxiFragment;
import com.route876.utils.ParseRoute;

import java.util.ArrayList;

/**
 * Created by Howard on 9/12/2015.
 */
public class RouteActivity extends AppCompatActivity {

    private boolean openJutc;
    private boolean openCoaster;
    private boolean openPrivateTaxi;
    private boolean openRouteTaxi;

    private ArrayList<Route> routeItems;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(R.layout.activity_route);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ParseRoute parseRoute = new ParseRoute(this);
        routeItems = parseRoute.getRouteList();

        Intent intent = getIntent();
        if (intent.getAction() != null) {
            openTravelFragment(intent.getAction());
        }
    }

    private void openTravelFragment(String travelOption) {
        Fragment fragment = null;
        switch (travelOption) {
            case "jutc":
                openJutc = true;
                fragment = new JutcFragment();
                break;
            case "coaster":
                openCoaster = true;
                fragment = new CoasterFragment();
                break;
            case "privatetaxi":
                openPrivateTaxi = true;
                fragment = new PrivateTaxiFragment();
                break;
            case "routetaxi":
                openRouteTaxi = true;
                fragment = new RouteTaxiFragment();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        setUpRouteSearch(searchView);

        return super.onCreateOptionsMenu(menu);
    }

    private void setUpRouteSearch(SearchView searchView) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                JutcFragment.JutcStartLocationFragment.getCustomRouteAdapter().getFilter().filter("");
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                JutcFragment.JutcStartLocationFragment.getCustomRouteAdapter().getFilter().filter(newText);
                System.out.println("OnQueryTextChangeText: " + newText);
                if (TextUtils.isEmpty(newText)) {
                    JutcFragment.JutcStartLocationFragment.getRouteListView().clearTextFilter();
                } else {
                    JutcFragment.JutcStartLocationFragment.getRouteListView().setFilterText(newText);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_search:
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }

    private void showResults(String query) {
        // Query data set and show results
    }

    public ArrayList<Route> getRouteItems() {
        return routeItems;
    }

    public boolean isOpenJutc() {
        return openJutc;
    }

    public boolean isOpenCoaster() {
        return openCoaster;
    }

    public boolean isOpenPrivateTaxi() {
        return openPrivateTaxi;
    }

    public boolean isOpenRouteTaxi() {
        return openRouteTaxi;
    }
}
