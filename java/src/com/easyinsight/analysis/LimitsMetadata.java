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
public class LimitsMetadata implements Serializable, Cloneable {
    @Column(name="top_items")
    private boolean top;

    @Column(name="number_items")
    private int number;

    @Column(name="limit_enabled")
    private boolean limitEnabled;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="limits_metadata_id")
    private long limitsMetadataID;

    public long getLimitsMetadataID() {
        return limitsMetadataID;
    }

    public void setLimitsMetadataID(long limitsMetadataID) {
        this.limitsMetadataID = limitsMetadataID;
    }

    public boolean isLimitEnabled() {
        return limitEnabled;
    }

    public void setLimitEnabled(boolean limitEnabled) {
        this.limitEnabled = limitEnabled;
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

    public LimitsMetadata clone() throws CloneNotSupportedException {
        LimitsMetadata limitsMetadata = (LimitsMetadata) super.clone();
        limitsMetadata.setLimitsMetadataID(0);
        return limitsMetadata;
    }
}
