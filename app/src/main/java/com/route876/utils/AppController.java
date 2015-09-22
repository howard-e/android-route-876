package com.route876.utils;

import android.app.Application;

import com.parse.Parse;
import com.route876.R;

/**
 * Created by Howard on 9/13/2015.
 */
public class AppController extends Application {

    @Override public void onCreate() {
        super.onCreate();

        String ApplicationID = getResources().getString(R.string.application_id);
        String ClientKey = getResources().getString(R.string.client_key);

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, ApplicationID, ClientKey);
    }
}
