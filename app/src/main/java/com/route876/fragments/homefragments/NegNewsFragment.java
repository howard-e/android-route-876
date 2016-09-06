package com.route876.fragments.homefragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.route876.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 9/12/2015.
 */
public class NegNewsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new NewsHolderFragment())
                    .commit();
        }
    }

    public static class NewsHolderFragment extends ListFragment {

        private List<String> rssLinks = new ArrayList<>();

        @Override
        public void onStart() {
            super.onStart();
            new GetGleanerRssFeedAsyncTask(this).execute();
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Uri uri = Uri.parse(rssLinks.get(position));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getActivity().startActivity(intent);
        }

        private String getGleanerRssFeed() throws IOException {
            InputStream in = null;
            String rssFeed = null;
            try {
                URL url = new URL("http://www.jamaica-gleaner.com/feed/rss.xml");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10 * 1000);
                conn.setConnectTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int responseCode = conn.getResponseCode();
                Log.d("debug", "The response is: " + responseCode);

                in = conn.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                for (int count; (count = in.read(buffer)) != -1; ) {
                    out.write(buffer, 0, count);
                }
                byte[] response = out.toByteArray();
                rssFeed = new String(response, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return rssFeed;
        }

        private class GetGleanerRssFeedAsyncTask extends AsyncTask<Void, Void, List<String>> {

            private WeakReference<NewsHolderFragment> mNewsHolderFragment = null;

            public GetGleanerRssFeedAsyncTask(NewsHolderFragment newsHolderFragment) {
                mNewsHolderFragment = new WeakReference<NewsHolderFragment>(newsHolderFragment);
            }

            @Override
            protected List<String> doInBackground(Void... params) {
                List<String> result = null;
                try {
                    String feed = getGleanerRssFeed();
                    result = parse(feed);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<String> rssFeed) {
                if (rssFeed != null) {
                    setListAdapter(new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            rssFeed));
                }
            }

            private List<String> parse(String rssFeed) throws XmlPullParserException, IOException {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(rssFeed));
                xpp.nextTag();
                return readRss(xpp);
            }

            private List<String> readRss(XmlPullParser parser) throws XmlPullParserException, IOException {
                List<String> items = new ArrayList<>();
                parser.require(XmlPullParser.START_TAG, null, "rss");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    if (name.equals("channel")) {
                        items.addAll(readChannel(parser));
                    } else {
                        skip(parser);
                    }
                }
                return items;
            }

            private List<String> readChannel(XmlPullParser parser) throws IOException, XmlPullParserException {
                List<String> items = new ArrayList<>();
                parser.require(XmlPullParser.START_TAG, null, "channel");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    if (name.equals("item")) {
                        items.add(readItem(parser));
                    } else {
                        skip(parser);
                    }
                }
                return items;
            }

            private String readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
                String result = null;
                parser.require(XmlPullParser.START_TAG, null, "item");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    switch (name) {
                        case "title":
                            result = readTitle(parser);
                            break;
                        case "link":
                            mNewsHolderFragment.get().rssLinks.add(readLink(parser));
                            break;
                        default:
                            skip(parser);
                            break;
                    }
                }
                return result;
            }

            // Processes title tags in the feed.
            private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
                parser.require(XmlPullParser.START_TAG, null, "title");
                String title = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "title");
                return title;
            }

            // Processes link tags in feed
            private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
                parser.require(XmlPullParser.START_TAG, null, "link");
                String link = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "link");
                return link;
            }

            private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
                String result = "";
                if (parser.next() == XmlPullParser.TEXT) {
                    result = parser.getText();
                    parser.nextTag();
                }
                return result;
            }

            private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    throw new IllegalStateException();
                }
                int depth = 1;
                while (depth != 0) {
                    switch (parser.next()) {
                        case XmlPullParser.END_TAG:
                            depth--;
                            break;
                        case XmlPullParser.START_TAG:
                            depth++;
                            break;
                    }
                }
            }
        }
    }
}
