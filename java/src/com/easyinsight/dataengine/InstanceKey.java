package com.easyinsight.dataengine;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 10:25:28 PM
 */
public class InstanceKey implements Serializable {
    private String identity;

    public InstanceKey() {
    }

    public InstanceKey(String identity) {
        this.identity = identity;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceKey that = (InstanceKey) o;

        return identity.equals(that.identity);

    }

    public int hashCode() {
        return identity.hashCode();
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
