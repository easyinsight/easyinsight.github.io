package com.easyinsight.analysis;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.io.Serializable;

import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.datafeeds.FeedNode;
import com.easyinsight.datafeeds.AnalysisItemNode;
import org.hibernate.Session;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "analysis_item_to_link",
            joinColumns = @JoinColumn(name = "analysis_item_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "link_id", nullable = false))
    private List<Link> links = new ArrayList<Link>();

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="virtual_dimension_id")
    private VirtualDimension virtualDimension;

    public AnalysisItem() {
    }

    public AnalysisItem(String keyName) {
        this.key = new NamedKey(keyName);
    }

    public AnalysisItem(Key key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public AnalysisItem(Key key) {
        this.key = key;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public VirtualDimension getVirtualDimension() {
        return virtualDimension;
    }

    public void setVirtualDimension(VirtualDimension virtualDimension) {
        this.virtualDimension = virtualDimension;
    }

    public String toDisplay() {
        if (displayName == null) {
            return getKey().toDisplayName();
        } else {
            return displayName;
        }
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

    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata) {
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

    public AnalysisItem clone() throws CloneNotSupportedException {
        AnalysisItem clonedItem = (AnalysisItem) super.clone();
        clonedItem.setAnalysisItemID(0);
        clonedItem.setFormattingConfiguration(formattingConfiguration.clone());
        List<Link> clonedLinks = new ArrayList<Link>();
        for (Link link : getLinks()) {
            clonedLinks.add(link.clone());
        }
        setLinks(clonedLinks);
        return clonedItem;
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
        return virtualDimension != null;
    }

    public boolean isVirtual() {
        return virtualDimension != null;
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {

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

    public void beforeSave() {
        
    }

    public boolean isValid() {
        return true;
    }

    public void afterLoad() {
        if (virtualDimension != null) {
            for (VirtualTransform transform : virtualDimension.getVirtualTransforms()) {
                transform.toRemote();
            }
        }
        setLinks(new ArrayList<Link>(getLinks()));        
    }

    public String toKeySQL() {
        if (isDerived()) {
            throw new RuntimeException("Attempt made to retrieve SQL for a derived analysis item.");
        }
        return getKey().toSQL();
    }

    public Value calculate(DataSet dataSet, IRow row) {
        throw new UnsupportedOperationException();
    }

    public boolean isCalculated() {
        return false;
    }

    public void reportSave(Session session) {

    }

    public FeedNode toFeedNode() {
        AnalysisItemNode analysisItemNode = new AnalysisItemNode();
        analysisItemNode.setAnalysisItem(this);
        return analysisItemNode;
    }
}
