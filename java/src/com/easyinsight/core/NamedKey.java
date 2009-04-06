package com.easyinsight.core;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * User: James Boe
 * Date: Jun 12, 2008
 * Time: 11:14:16 AM
 */
@Entity
@Table(name="named_item_key")
@PrimaryKeyJoinColumn(name="item_key_id")
public class NamedKey extends Key {
    @Column(name="name")
    private String name;

    public NamedKey() {
    }

    public NamedKey(String name) {
        this.name = name;
    }  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toSQL() {
        return "k" + getKeyID();
    }

    @Override
    public Key toBaseKey() {
        return this;
    }

    public String toDisplayName() {
        return name;
    }

    public String toKeyString() {
        return name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedKey)) return false;

        NamedKey namedKey = (NamedKey) o;

        return name.equals(namedKey.name);

    }

    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
