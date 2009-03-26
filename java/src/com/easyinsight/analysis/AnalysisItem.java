package com.easyinsight.analysis;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.io.Serializable;

import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 11:09:03 PM
 */
@Entity
@Table(name="analysis_item")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class AnalysisItem implements Cloneable, Serializable {
    // if we fold out key into its own class...

    @OneToOne
    @JoinColumn(name="item_key_id")
    private Key key;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="analysis_item_id")
    private long analysisItemID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="formatting_configuration_id")
    private FormattingConfiguration formattingConfiguration = new FormattingConfiguration();

    @Column(name="hidden")
    private boolean hidden = false;

    @Column(name="display_name")
    private String displayName;

    @Column(name="sort")
    private int sort;

    @Column(name="width")
    private int width;

    public AnalysisItem() {
    }

    public AnalysisItem(String keyName) {
        this.key = new NamedKey(keyName);
    }

    public AnalysisItem(Key key) {
        this.key = key;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key keyObject) {
        this.key = keyObject;
    }

    public long getAnalysisItemID() {
        return analysisItemID;
    }

    public void setAnalysisItemID(long analysisItemID) {
        this.analysisItemID = analysisItemID;
    }

    public Value transformValue(Value value) {
        return value;
    }

    public Value renameMeLater(Value value) {
        return value;
    }

    public abstract int getType();

    public boolean hasType(int type) {
        return (getType() & type) == type;
    }

    public boolean requiresDataEarly() {
        return false;
    }

    public void handleEarlyData(List<IRow> rows) {
        throw new UnsupportedOperationException();
    }

    public Value polishValue(Value value) {
        return value;
    }

    public AnalysisItemResultMetadata createResultMetadata() {
        return new AnalysisItemResultMetadata();
    }

    public boolean isMultipleTransform() {
        return false;
    }

    public Value[] transformToMultiple(Value value) {
        throw new UnsupportedOperationException();
    }

    public void resetIDs() {
        this.analysisItemID = 0;
    }

    public FormattingConfiguration getFormattingConfiguration() {
        return formattingConfiguration;
    }

    public void setFormattingConfiguration(FormattingConfiguration formattingConfiguration) {
        this.formattingConfiguration = formattingConfiguration;
    }

    public AnalysisItem clone() {
        try {
            AnalysisItem clonedItem = (AnalysisItem) super.clone();
            clonedItem.setAnalysisItemID(0);
            return clonedItem;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisItem)) return false;

        AnalysisItem that = (AnalysisItem) o;

        return key.equals(that.key);

    }

    public int hashCode() {
        return key.hashCode();
    }

    public boolean isDerived() {
        return false;
    }

    public List<AnalysisItem> getParentItems(List<AnalysisItem> allFeedItems) {
        throw new UnsupportedOperationException();
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        return items;
    }

    public List<AnalysisItem> getDerivedItems() {
        return new ArrayList<AnalysisItem>();
    }

    public AggregateKey createAggregateKey() {
        return new AggregateKey(getKey(), getType());
    }
}
