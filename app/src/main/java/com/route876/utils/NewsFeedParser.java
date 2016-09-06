package com.route876.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.route876.objects.RSSFeed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Howard on 1/15/2016.
 */
public class NewsFeedParser {
    public static final String ITEM = "item";
    public static final String CHANNEL = "channel";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String DESCRIPTION = "description";
    public static final String CREATOR = "dc:creator";
    public static final String PUBLISHEDDATE = "pubDate";
    public static final String GUID = "guid";

    private List<RSSFeed> rssFeedList;
    private String urlString;
    private String title;
    private String link;
    private String description;
    private String creator;
    private String pubDate;
    private String guid;

    public NewsFeedParser(String urlString) {
        this.urlString = urlString;
    }

    public List<RSSFeed> parse() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            InputStream urlStream = downloadUrl(urlString);
            parser.setInput(urlStream, null);
            int eventType = parser.getEventType();
            boolean done = false;
            RSSFeed rssFeed;
            rssFeedList = new ArrayList<>();
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (tagName.equals(ITEM)) {
                            rssFeed = new RSSFeed();
                        }
                        if (tagName.equals(TITLE)) {
                            title = parser.nextText();
                        }
                        if (tagName.equals(LINK)) {
                            link = parser.nextText();
                        }
                        if (tagName.equals(DESCRIPTION)) {
                            description = parser.nextText().trim();
                        }
                        if (tagName.equals(CREATOR)) {
                            creator = parser.nextText();
                        }
                        if (tagName.equals(PUBLISHEDDATE)) {
                            pubDate = parser.nextText();
                        }
                        if (tagName.equals(GUID)) {
                            guid = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equals(CHANNEL)) {
                            done = true;
                        } else if (tagName.equals(ITEM)) {
                            rssFeed = new RSSFeed(title, link, description, creator, pubDate, guid);
                            rssFeedList.add(rssFeed);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssFeedList;
    }

    @Nullable
    public static InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        Log.d("Response Code", String.valueOf(conn.getResponseCode()));
        return conn.getInputStream();
    }
}