package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 11:08:54 PM
 */
@Entity
@Table(name="analysis_dimension")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisDimension extends AnalysisItem {
    @Column(name="group_by")
    private boolean group = true;

    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="key_dimension_id")
    private AnalysisDimension keyDimension;

    @Column(name="summary")
    private boolean summary = false;

    public AnalysisDimension() {
    }

    

    public AnalysisDimension getKeyDimension() {
        return keyDimension;
    }

    public void setKeyDimension(AnalysisDimension keyDimension) {
        this.keyDimension = keyDimension;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        if (getVirtualDimension() != null) {
            analysisItems.add(getVirtualDimension().getBaseDimension());
        } else {
            analysisItems.add(this);
        }
        if (keyDimension != null) {
            analysisItems.add(keyDimension);
        }
        return analysisItems;
    }

    public int getType() {
        return AnalysisItemTypes.DIMENSION;
    }

    public AnalysisItemResultMetadata createResultMetadata() {
        return new AnalysisDimensionResultMetadata();
    }

    public AnalysisDimension(Key key, boolean group) {
        super(key);
        this.group = group;
    }

    public AnalysisDimension(Key key, String displayName) {
        super(key, displayName);
    }

    public AnalysisDimension(String key, boolean group) {
        super(key);
        this.group = group;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (keyDimension != null) {
            keyDimension.afterLoad();
        }
    }

    public Value polishValue(Value value) {
        if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            try {
                value = new NumericValue(Integer.parseInt(stringValue.toString()));
            } catch (NumberFormatException e) {
            }
        }
        return value;
    }

    public void resetIDs() {
        super.resetIDs();
    }

    @Override
    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        super.updateIDs(replacementMap);
        if (getKeyDimension() != null) {
            setKeyDimension((AnalysisDimension) replacementMap.get(getKeyDimension().getAnalysisItemID()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AnalysisDimension that = (AnalysisDimension) o;

        return group == that.group;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (group ? 1 : 0);
        result = 31 * result + (keyDimension != null ? keyDimension.hashCode() : 0);
        return result;
    }

    public void reportSave(Session session) {
        if (keyDimension != null && keyDimension.getAnalysisItemID() == 0) {
            session.save(keyDimension);
        }
    }

    @Override
    public String toString() {
        return getKey().toKeyString() + " - " + getAnalysisItemID();
    }
}
