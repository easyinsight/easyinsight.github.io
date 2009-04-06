package com.easyinsight.core;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Jun 12, 2008
 * Time: 10:30:38 AM
 */
@Entity
@Table(name="derived_item_key")
@PrimaryKeyJoinColumn(name="item_key_id")
public class DerivedKey extends Key {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="parent_item_key_id")
    private Key parentKey;
    @Column(name="feed_id")
    private long feedID;

    public DerivedKey() {
    }

    public DerivedKey(Key parentKey, long feedID) {
        this.parentKey = parentKey;
        this.feedID = feedID;
    }

    public Key getParentKey() {
        return parentKey;
    }

    public void setParentKey(Key parentKey) {
        this.parentKey = parentKey;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    @Override
    public String toSQL() {
        return getParentKey().toSQL();
    }

    @Override
    public Key toBaseKey() {
        return getParentKey().toBaseKey();
    }

    public String toDisplayName() {
        return getParentKey().toDisplayName();
    }

    public String toKeyString() {
        return getParentKey().toKeyString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DerivedKey that = (DerivedKey) o;

        if (feedID != that.feedID) return false;
        if (!parentKey.equals(that.parentKey)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = parentKey.hashCode();
        result = 31 * result + (int) (feedID ^ (feedID >>> 32));
        return result;
    }
}
