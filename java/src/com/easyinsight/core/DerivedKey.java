package com.easyinsight.core;

import com.easyinsight.database.Database;

import javax.persistence.*;
import java.util.List;

/**
 * User: James Boe
 * Date: Jun 12, 2008
 * Time: 10:30:38 AM
 */
@Entity
@Table(name="derived_item_key")
@PrimaryKeyJoinColumn(name="item_key_id")
public class DerivedKey extends Key {
    @OneToOne (fetch = FetchType.LAZY)
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

    public void afterLoad() {
        super.afterLoad();
        parentKey = (Key) Database.deproxy(parentKey);
        parentKey.afterLoad();
    }

    @Override
    public List<EIDescriptor> getDescriptors() {
        List<EIDescriptor> descs = super.getDescriptors();
        descs.addAll(parentKey.getDescriptors());
        return descs;
    }

    public boolean hasDataSource(long dataSourceID) {
        return feedID == dataSourceID || parentKey.hasDataSource(dataSourceID);
    }

    @Override
    public boolean matchesOrContains(Key key) {
        if (key instanceof NamedKey) {
            return parentKey.matchesOrContains(key);
        } else if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            if (derivedKey.feedID == this.feedID) {
                return parentKey.matchesOrContains(((DerivedKey) key).getParentKey());
            } else {
                return parentKey.matchesOrContains(key);
            }
        }
        return false;
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
        return toBaseKey().toSQL();
    }

    @Override
    public Key toBaseKey() {
        return getParentKey().toBaseKey();
    }

    public String toDisplayName() {
        return toBaseKey().toDisplayName();
    }

    @Override
    public boolean indexed() {
        return toBaseKey().indexed();
    }

    public String toKeyString() {
        return toBaseKey().toKeyString();
    }

    public String urlKeyString(XMLMetadata xmlMetadata) {
        return xmlMetadata.urlKeyForDataSourceID(feedID) + "-" + parentKey.urlKeyString(xmlMetadata);
    }

    @Override
    public String internalString() {
        return feedID + "-" + parentKey.internalString();
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

    @Override
    public Key clone() throws CloneNotSupportedException {
        DerivedKey derivedKey = (DerivedKey) super.clone();
        return derivedKey;
    }

    @Override
    public String toString() {
        return internalString();
    }
}
