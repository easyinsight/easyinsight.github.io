package com.easyinsight.exchange;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 14, 2009
 * Time: 7:08:30 PM
 */
public class ExchangeItem {
    private String name;
    private long id;
    private int installs;
    private Date dateAdded;
    private String description;
    private List<String> tags;
    private String author;

    public ExchangeItem() {
    }

    public ExchangeItem(String name, long id, int installs, Date dateAdded,
                        String description, String author) {
        this.name = name;
        this.id = id;
        this.installs = installs;
        this.dateAdded = dateAdded;
        this.description = description;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getInstalls() {
        return installs;
    }

    public void setInstalls(int installs) {
        this.installs = installs;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
