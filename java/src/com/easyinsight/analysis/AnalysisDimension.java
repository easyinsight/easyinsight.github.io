package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.analysis.AnalysisDimensionResultMetadata;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

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
    private boolean group;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="analysis_dimension_id")
    private long analysisDimensionID;

    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="key_dimension_id")
    private AnalysisDimension keyDimension;

    public AnalysisDimension() {
    }

    public AnalysisDimension getKeyDimension() {
        return keyDimension;
    }

    public void setKeyDimension(AnalysisDimension keyDimension) {
        this.keyDimension = keyDimension;
    }

    public long getAnalysisDimensionID() {
        return analysisDimensionID;
    }

    public void setAnalysisDimensionID(long analysisDimensionID) {
        this.analysisDimensionID = analysisDimensionID;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, List<AnalysisItem> insightItems) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(this);
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

    public Value transformValue(Value value) {
        if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            try {
                NumericValue newNumericValue = new NumericValue(Integer.parseInt(stringValue.toString()));
                value = newNumericValue;
            } catch (NumberFormatException e) {
            }
        }
        return value;
    }

    public void resetIDs() {
        super.resetIDs();
        this.analysisDimensionID = 0;
    }

    public AnalysisDimension clone() {
        AnalysisDimension clonedItem = (AnalysisDimension) super.clone();
        clonedItem.setAnalysisDimensionID(0);
        return clonedItem;
    }

    public boolean equals(Object o) {
        return this == o || o instanceof AnalysisDimension && super.equals(o);

    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (group ? 1 : 0);
        result = 31 * result + (int) (analysisDimensionID ^ (analysisDimensionID >>> 32));
        return result;
    }
}
