package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * User: James Boe
 * Date: Sep 8, 2008
 * Time: 8:54:12 PM
 */
@Entity
@Table(name="list_limits_metadata")
@PrimaryKeyJoinColumn(name="limits_metadata_id")
public class ListLimitsMetadata extends LimitsMetadata implements Serializable, Cloneable {
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem analysisItem;

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public void afterLoad() {
        if (getAnalysisItem() != null) {
            setAnalysisItem((AnalysisItem) Database.deproxy(getAnalysisItem()));
            getAnalysisItem().afterLoad();
        }
    }

    @Override
    public ListLimitsMetadata clone() throws CloneNotSupportedException {
        return (ListLimitsMetadata) super.clone();
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        if (analysisItem != null) {
            analysisItem = replacementMap.get(analysisItem.getAnalysisItemID());
        }
    }

    public void beforeSave(Session session) {
        if (analysisItem != null) {
            analysisItem.reportSave(session);
            session.save(analysisItem);
        }
    }
}
