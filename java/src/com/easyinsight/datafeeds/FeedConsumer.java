package com.easyinsight.datafeeds;

/**
 * User: James Boe
 * Date: Oct 3, 2008
 * Time: 6:11:03 PM
 */
public class FeedConsumer {
    private String name;

    public FeedConsumer() {
    }

    public FeedConsumer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
