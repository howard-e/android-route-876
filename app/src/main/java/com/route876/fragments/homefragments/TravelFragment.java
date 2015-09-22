package com.route876.fragments.homefragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.route876.R;
import com.route876.activities.RouteActivity;

/**
 * Created by Howard on 9/12/2015.
 */
public class TravelFragment extends Fragment {

    static final String[] TRAVEL_OPTIONS = new String[]{"JUTC", "Coasters", "Private Taxis", "Route Taxis"};
    GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_travel_section, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        gridView.setSelector(R.color.white);
        gridView.setAdapter(new TravelFragmentGridAdapter(getActivity(), TRAVEL_OPTIONS));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String action = null;
                switch (position) {
                    case 0:
                        action = "jutc";
                        break;
                    case 1:
                        action = "coaster";
                        break;
                    case 2:
                        action = "privatetaxi";
                        break;
                    case 3:
                        action = "routetaxi";
                        break;
                }
                Intent intent = new Intent(getActivity(), RouteActivity.class);
                if (action != null) {
                    intent.setAction(action);
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    private class TravelFragmentGridAdapter extends BaseAdapter {
        private final String[] travelOptions;
        private Context context;

        public TravelFragmentGridAdapter(Context context, String[] travelOptions) {
            this.context = context;
            this.travelOptions = travelOptions;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;
            if (convertView == null) {
                gridView = inflater.inflate(R.layout.item_travel_card, null);

                TextView textView = (TextView) gridView.findViewById(R.id.card_title);
                textView.setText(travelOptions[position]);
                ImageView imageView = (ImageView) gridView.findViewById(R.id.card_image);

                String travelOption = travelOptions[position];
                switch (travelOption) {
                    case "JUTC":
                        imageView.setImageResource(R.mipmap.ic_jutc);
                        break;
                    case "Coasters":
                        imageView.setImageResource(R.mipmap.ic_coaster);
                        break;
                    case "Private Taxis":
                        imageView.setImageResource(R.mipmap.ic_private_taxi);
                        break;
                    case "Route Taxis":
                        imageView.setImageResource(R.mipmap.ic_route_taxi);
                        break;
                    default:
                        break;
                }
            } else {
                gridView = convertView;
            }
            return gridView;
        }

        @Override
        public int getCount() {
            return travelOptions.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
