package com.easyinsight.admin;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 9/21/12
 * Time: 4:35 PM
 */
public class NewsEntry {
    private String news;
    private String title;
    private Date date;
    private long id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
