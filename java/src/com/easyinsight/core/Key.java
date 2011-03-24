package com.easyinsight.core;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jun 12, 2008
 * Time: 10:16:45 AM
 */
@Entity
@Table(name="item_key")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class Key implements Comparable<Key>, Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="item_key_id")
    private long keyID;

    public abstract String toSQL();

    public abstract Key toBaseKey();

    public boolean hasDataSource(long dataSourceID) {
        return false;
    }

    public long getKeyID() {
        return keyID;
    }

    public void setKeyID(long keyID) {
        this.keyID = keyID;
    }

    public abstract String toDisplayName();

    public abstract boolean indexed();

    public abstract String toKeyString();

    public abstract String internalString();

    public int compareTo(Key o) {
        return toDisplayName().compareTo(o.toDisplayName());
    }
    
    public Key clone() throws CloneNotSupportedException {
        Key key = (Key) super.clone();
        key.keyID = 0;
        return key;
    }
}
