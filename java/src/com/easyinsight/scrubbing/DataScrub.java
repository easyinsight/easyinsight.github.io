package com.easyinsight.scrubbing;

import com.easyinsight.core.Key;
import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Feb 18, 2008
 * Time: 11:29:55 AM
 */
@Entity
@Table(name="data_scrub")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class DataScrub implements Serializable {
    @Column(name="data_scrub_id")
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long dataScrubID;

    public DataScrub() {
    }

    public long getDataScrubID() {
        return dataScrubID;
    }

    public void setDataScrubID(long dataScrubID) {
        this.dataScrubID = dataScrubID;
    }
    public abstract IScrubber createScrubber();

    public Collection<Key> createNeededKeys(Collection<AnalysisItem> analysisItems) {
        return new ArrayList<Key>();
    }

    public void hateHibernate() {
        
    }
}
