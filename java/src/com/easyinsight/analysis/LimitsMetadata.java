package com.easyinsight.analysis;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 11, 2008
 * Time: 10:35:13 PM
 */
@Entity
@Table(name="limits_metadata")
@Inheritance(strategy=InheritanceType.JOINED)
public class LimitsMetadata implements Serializable {
    @Column(name="top_items")
    private boolean top;

    @Column(name="number_items")
    private int number;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="limits_metadata_id")
    private long limitsMetadataID;

    public long getLimitsMetadataID() {
        return limitsMetadataID;
    }

    public void setLimitsMetadataID(long limitsMetadataID) {
        this.limitsMetadataID = limitsMetadataID;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
