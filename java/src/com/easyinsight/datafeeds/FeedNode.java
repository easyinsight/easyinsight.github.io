package com.easyinsight.datafeeds;

/**
 * User: James Boe
 * Date: Apr 27, 2008
 * Time: 9:34:20 PM
 */
public class FeedNode {
    private String name;

    public FeedNode() {
    }

    public FeedNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedNode that = (FeedNode) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }
}
