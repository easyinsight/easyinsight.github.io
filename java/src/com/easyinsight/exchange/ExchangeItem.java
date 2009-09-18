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
    private String attribution;
    private double ratingAverage;
    private double ratingCount;
    private Date dateAdded;
    private String description;
    private List<String> tags;
    private String author;

    public ExchangeItem() {
    }

    public ExchangeItem(String name, long id, String attribution, double ratingAverage, double ratingCount, Date dateAdded,
                        String description, String author) {
        this.name = name;
        this.id = id;
        this.attribution = attribution;
        this.ratingAverage = ratingAverage;
        this.ratingCount = ratingCount;
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

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public double getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(double ratingCount) {
        this.ratingCount = ratingCount;
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
