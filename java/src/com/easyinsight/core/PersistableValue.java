package com.easyinsight.core;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 16, 2008
 * Time: 12:43:14 AM
 */
@Entity
@Table(name="value")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class PersistableValue implements Cloneable, Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="value_id")
    private long valueID;

    public long getValueID() {
        return valueID;
    }

    public void setValueID(long valueID) {
        this.valueID = valueID;
    }

    public abstract Value toValue();

    @Override
    public PersistableValue clone() throws CloneNotSupportedException {
        PersistableValue persistableValue = (PersistableValue) super.clone();
        persistableValue.setValueID(0);
        return persistableValue;
    }

    public void truncate() {
    }
}
