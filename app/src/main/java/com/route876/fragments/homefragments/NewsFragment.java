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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.route876.R;
import com.route876.activities.WebViewActivity;
import com.route876.classes.objects.RSSFeed;
import com.route876.utils.NewsFeedParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 1/15/2016.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Get rss feed source from PreferenceManager when I properly configure settings
    private String RSSFEEDSOURCE;

    private ListView mRssListView;
    private ImageView mRssUnavailablePoster;

    private List<RSSFeed> mRssFeedList;
    private NewsFeedParser mNewsFeeder;
    private RssAdapter mRssAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<String> rssLinks = new ArrayList<>();
    private List<String> rssTitles = new ArrayList<>();

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        System.out.println("RSS Source: " + sharedPreferences.getString("rss_feed_options", "JGleaner Lead"));
        switch (sharedPreferences.getString("rss_feed_options", "JGleaner Lead")) {
            case "JGLeaner Lead":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/rss.xml";
                break;
            case "JGleaner News":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/news.xml";
                break;
            case "JGleaner Sports":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/sports.xml";
                break;
            case "JGleaner Business":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/business.xml";
                break;
            case "JGleaner Entertainment":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/ent.xml";
                break;
            case "JGleaner Letters":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/letters.xml";
                break;
            case "JGleaner Commentary":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/cleisure.xml";
                break;
            case "JGleaner Arts":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/arts.xml";
                break;
            case "JGleaner Focus":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/focus.xml";
                break;
            case "JGleaner Flair":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/flair.xml";
                break;
            case "JGleaner Social":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/social.xml";
                break;
            case "JGleaner Caribbean":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/carib.xml";
                break;
            case "JGleaner International":
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/int.xml";
                break;
            case "SJamaica Headlines":
                RSSFEEDSOURCE = "http://www.sportsjamaica.com/feed/sj2.rss";
                break;
            case "JStar News":
                RSSFEEDSOURCE = "http://www.jamaicastar.com/feed/news.xml";
                break;
            case "JStar Entertainment":
                RSSFEEDSOURCE = "http://www.jamaicastar.com/feed/ent.xml";
                break;
            default:
                RSSFEEDSOURCE = "http://www.jamaica-gleaner.com/feed/rss.xml";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rss_feed_list_view, container, false);
        mRssListView = (ListView) rootView.findViewById(R.id.rss_list_view);
        mRssUnavailablePoster = (ImageView) rootView.findViewById(R.id.service_unavailable_poster);

        mRssFeedList = new ArrayList<>();
        new LoadRssFeedAsyncTask().execute(RSSFEEDSOURCE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRssFeed();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.red, R.color.blue,
                R.color.yellow, R.color.green);

        View mRssHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.rss_list_header, null);
        TextView mRssHeaderTextView = (TextView) mRssHeaderView.findViewById(R.id.rss_header_text);
        mRssHeaderTextView.setText(sharedPreferences.getString("rss_feed_options", "JGleaner Lead"));

        mRssListView.addHeaderView(mRssHeaderView, null, false);
        mRssListView.setOnItemClickListener(this);
        return rootView;
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

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
