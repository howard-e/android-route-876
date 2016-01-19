package com.route876.classes.objects;

/**
 * Created by Howard on 1/15/2016.
 */
public class RSSFeed {

    private String title;
    private String link;
    private String description;
    private String creator;
    private String pubDate;
    private String guid;

    public RSSFeed() {
    }

    public RSSFeed(String title, String link, String description, String creator, String pubDate, String guid) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.creator = creator;
        this.pubDate = pubDate;
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() {
        return creator;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getGuid() {
        return guid;
    }
}
