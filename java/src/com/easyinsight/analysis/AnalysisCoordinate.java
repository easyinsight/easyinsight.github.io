package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.database.Database;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 2:07:31 PM
 */
@Entity
@Table(name="analysis_coordinate")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public abstract class AnalysisCoordinate extends AnalysisDimension {

    public AnalysisCoordinate(Key key, boolean group, String displayName) {
        super(key, displayName);
        setGroup(group);
    }

    public AnalysisCoordinate() {
    }

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="analysis_zip_id")
    private AnalysisZipCode analysisZipCode;

    public AnalysisZipCode getAnalysisZipCode() {
        return analysisZipCode;
    }

    public void setAnalysisZipCode(AnalysisZipCode analysisZipCode) {
        this.analysisZipCode = analysisZipCode;
    }

    @Override
    public boolean isDerived() {
        return analysisZipCode != null;
    }

    @Override
    public void reportSave(Session session) {
        super.reportSave(session);
        if (analysisZipCode != null) {
            analysisZipCode.reportSave(session);
        }
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (analysisZipCode != null) {
            setAnalysisZipCode((AnalysisZipCode) Database.deproxy(getAnalysisZipCode()));
            analysisZipCode.afterLoad();
        }
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> items = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        if (analysisZipCode != null) {
            items.add(analysisZipCode);
        }
        return items;
    }
}
