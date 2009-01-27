package com.easyinsight.scrubbing;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 30, 2008
 * Time: 1:27:30 PM
 */
@Entity
@Table(name="lookup_table_scrub_pair")
public class LookupTablePair implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="lookup_table_scrub_pair_id")
    private long pairID;
    @Column(name="source_value")
    private String sourceValue;
    @Column(name="target_value")
    private String replaceValue;

    public long getPairID() {
        return pairID;
    }

    public void setPairID(long pairID) {
        this.pairID = pairID;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getReplaceValue() {
        return replaceValue;
    }

    public void setReplaceValue(String replaceValue) {
        this.replaceValue = replaceValue;
    }
}
