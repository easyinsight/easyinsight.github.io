package com.easyinsight.core;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Jul 16, 2008
 * Time: 12:43:14 AM
 */
@Entity
@Table(name="value")
@Inheritance(strategy= InheritanceType.JOINED)
public class PersistableValue {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="value_id")
    private Long valueID;

    public long getValueID() {
        return valueID;
    }

    public void setValueID(long valueID) {
        this.valueID = valueID;
    }
}
