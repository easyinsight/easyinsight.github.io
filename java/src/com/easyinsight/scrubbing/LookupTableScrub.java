package com.easyinsight.scrubbing;

import com.easyinsight.core.Key;
import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.*;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

import org.hibernate.annotations.Cascade;

/**
 * User: James Boe
 * Date: Jul 30, 2008
 * Time: 1:20:48 PM
 */
@Entity
@Table(name="lookup_table_scrub")
@PrimaryKeyJoinColumn(name="data_scrub_id")
public class LookupTableScrub extends DataScrub {
    @OneToMany
    @Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name="data_scrub_id")
    private List<LookupTablePair> lookupTablePairs;
    @OneToOne
    @JoinColumn(name="source_key")
    private Key sourceKey;
    @OneToOne
    @JoinColumn(name="target_key")
    private Key targetKey;

    public Key getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(Key sourceKey) {
        this.sourceKey = sourceKey;
    }

    public Key getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(Key targetKey) {
        this.targetKey = targetKey;
    }

    public List<LookupTablePair> getLookupTablePairs() {
        return lookupTablePairs;
    }

    public void setLookupTablePairs(List<LookupTablePair> lookupTablePairs) {
        this.lookupTablePairs = lookupTablePairs;
    }

    public IScrubber createScrubber() {
        return new LookupTableScrubber(this);
    }

    public Collection<Key> createNeededKeys(List<AnalysisItem> analysisItems) {
        Collection<Key> keys = new ArrayList<Key>();        
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.getKey().getKeyID().equals(targetKey.getKeyID())) {
                keys.add(sourceKey);
            }
        }
        return keys;
    }

    public void hateHibernate() {
        for (LookupTablePair pair : lookupTablePairs) {
            
        }
    }
}
