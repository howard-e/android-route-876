package com.route876.fragments.homefragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.route876.R;
import com.route876.activities.WebViewActivity;
import com.route876.objects.RSSFeed;
import com.route876.utils.NewsFeedParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 1/15/2016.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private String RSSFEEDSOURCE;

    private Spinner mRssSpinner;
    private ListView mRssListView;
    private ImageView mRssUnavailablePoster;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private NewsFeedParser mNewsFeeder;
    private RssAdapter mRssAdapter;
    private List<RSSFeed> mRssFeedList;

    private List<String> rssLinks = new ArrayList<>();
    private List<String> rssTitles = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        System.out.println("RSS Op Code: " + sharedPreferences.getInt("rss_op_index", 0));
        setRSSFeedSource(sharedPreferences.getInt("rss_op_index", 0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rss_feed_list_view, container, false);
        mRssSpinner = (Spinner) rootView.findViewById(R.id.rss_spinner);
        mRssListView = (ListView) rootView.findViewById(R.id.rss_list_view);
        mRssUnavailablePoster = (ImageView) rootView.findViewById(R.id.service_unavailable_poster);

        mRssFeedList = new ArrayList<>();
        new LoadRssFeedAsyncTask().execute(RSSFEEDSOURCE);

        setUpRssSpinner();

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRssFeed();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(R.color.red, R.color.blue,
                R.color.yellow, R.color.green);

        mRssListView.setOnItemClickListener(this);
        mRssListView.setOnScrollListener(this);
        return rootView;
    }

    private void setUpRssSpinner() {
        sharedPreferencesEditor = sharedPreferences.edit();

        ArrayAdapter<String> rssOptionsAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.rss_feed_options));
        rssOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRssSpinner.setAdapter(rssOptionsAdapter);
        mRssSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String rssOption = parent.getItemAtPosition(position).toString();
                int rssIndex = parent.getSelectedItemPosition();

                sharedPreferencesEditor.putString("rss_op", rssOption);
                sharedPreferencesEditor.putInt("rss_op_index", rssIndex);
                sharedPreferencesEditor.apply();

                setRSSFeedSource(rssIndex);
                refreshRssFeed();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void refreshRssFeed() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new LoadRssFeedAsyncTask().execute(RSSFEEDSOURCE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setRSSFeedSource(int rssOpIndex) {
        switch (rssOpIndex) {
            case 0:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/rss.xml";
                break;
            case 1:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/news.xml";
                break;
            case 2:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/sports.xml";
                break;
            case 3:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/business.xml";
                break;
            case 4:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/ent.xml";
                break;
            case 5:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/letters.xml";
                break;
            case 6:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/cleisure.xml";
                break;
            case 7:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/arts.xml";
                break;
            case 8:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/focus.xml";
                break;
            case 9:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/flair.xml";
                break;
            case 10:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/social.xml";
                break;
            case 11:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/carib.xml";
                break;
            case 12:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/int.xml";
                break;
            case 13:
                RSSFEEDSOURCE = "http://www.sportsjamaica.com/feed/sj2.rss";
                break;
            case 14:
                RSSFEEDSOURCE = "http://www.jamaicastar.com/feed/news.xml";
                break;
            case 15:
                RSSFEEDSOURCE = "http://www.jamaicastar.com/feed/ent.xml";
                break;
            default:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/rss.xml";
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle newsUrl = new Bundle();
        newsUrl.putString("newsUrl", rssLinks.get(position));
        newsUrl.putString("newsTitle", rssTitles.get(position));
        getActivity().startActivity(new Intent(getActivity(), WebViewActivity.class).putExtras(newsUrl));
    }

    public List<String> getRssLinks() {
        return rssLinks;
    }

    public List<String> getRssTitles() {
        return rssTitles;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Do nothing
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRowPosition = (mRssListView == null || mRssListView.getChildCount() == 0) ? 0 : mRssListView.getChildAt(0).getTop();
        mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowPosition >= 0);
    }

    private static class RssHolder {
        public TextView rssTitleView;
        public TextView rssDescripView;
        public TextView rssAuthorView;
    }

    private class RssAdapter extends ArrayAdapter<RSSFeed> {
        private List<RSSFeed> rssFeedLst;

        public RssAdapter(Context context, int textViewResourceId, List<RSSFeed> rssFeedLst) {
            super(context, textViewResourceId, rssFeedLst);
            this.rssFeedLst = rssFeedLst;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            RssHolder rssHolder;
            if (convertView == null) {
                view = View.inflate(getActivity(), R.layout.rss_list_item, null);
                rssHolder = new RssHolder();
                rssHolder.rssTitleView = (TextView) view.findViewById(R.id.rss_title_view);
                rssHolder.rssDescripView = (TextView) view.findViewById(R.id.rss_descrip_view);
                rssHolder.rssAuthorView = (TextView) view.findViewById(R.id.rss_author_view);
                view.setTag(rssHolder);
            } else {
                rssHolder = (RssHolder) view.getTag();
            }
            RSSFeed rssFeed = rssFeedLst.get(position);
            rssHolder.rssTitleView.setText(rssFeed.getTitle());
            rssHolder.rssDescripView.setText(rssFeed.getDescription());
            rssHolder.rssAuthorView.setText(rssFeed.getCreator());

            getRssTitles().add(rssFeed.getTitle());
            getRssLinks().add(rssFeed.getLink());
            return view;
        }
    }

    public class LoadRssFeedAsyncTask extends AsyncTask<String, Void, List<RSSFeed>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading....");
            progressDialog.show();
        }

        @Override
        protected List<RSSFeed> doInBackground(String... params) {
            for (String url : params) {
                mNewsFeeder = new NewsFeedParser(url);
            }
            mRssFeedList = mNewsFeeder.parse();
            return mRssFeedList;
        }

        @Override
        protected void onPostExecute(List<RSSFeed> result) {
            progressDialog.dismiss();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRssFeedList != null) {
                        mRssUnavailablePoster.setVisibility(View.GONE);
                        mRssListView.setVisibility(View.VISIBLE);
                        mRssAdapter = new RssAdapter(getActivity(), R.layout.rss_list_item, mRssFeedList);
                        int count = mRssAdapter.getCount();
                        if (count != 0 && mRssAdapter != null) {
                            mRssListView.setAdapter(mRssAdapter);
                        }
                    } else {
                        mRssListView.setVisibility(View.GONE);
                        mRssUnavailablePoster.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
