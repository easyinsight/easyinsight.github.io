package com.easyinsight.sequence;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.database.Database;

import javax.persistence.*;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 10:33:03 AM
 */
@Entity
@Table(name="report_sequence")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class Sequence implements Cloneable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="report_sequence_id")
    private long sequenceID;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem analysisItem;

    public abstract AnalysisItem toAnalysisItem();

    public long getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(long sequenceID) {
        this.sequenceID = sequenceID;
    }

    public void afterLoad() {
        setAnalysisItem((AnalysisItem) Database.deproxy(getAnalysisItem()));
        getAnalysisItem().afterLoad();
    }

    public Sequence clone() throws CloneNotSupportedException {
        Sequence sequence = (Sequence) super.clone();
        sequence.setSequenceID(0);
        return sequence;
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) throws CloneNotSupportedException {
        setAnalysisItem(replacementMap.get(analysisItem.getAnalysisItemID()));
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }
}
