package com.easyinsight.analysis;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.io.Serializable;

import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.datafeeds.FeedNode;
import com.easyinsight.datafeeds.AnalysisItemNode;
import com.easyinsight.calculations.Resolver;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "analysis_item_to_filter",
            joinColumns = @JoinColumn(name = "analysis_item_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "filter_id", nullable = false))
    private List<FilterDefinition> filters = new ArrayList<FilterDefinition>();

    @Column(name="high_is_good")
    private boolean highIsGood;

    @Column(name="item_position")
    private int itemPosition;

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

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public String toDisplay() {
        if (displayName == null) {
            return getKey().toDisplayName();
        } else {
            return displayName;
        }
    }

    public boolean isHighIsGood() {
        return highIsGood;
    }

    public void setHighIsGood(boolean highIsGood) {
        this.highIsGood = highIsGood;
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
        List<FilterDefinition> clonedFilters = new ArrayList<FilterDefinition>();
        for (FilterDefinition filterDefinition : getFilters()) {
            clonedFilters.add(filterDefinition.clone());
        }
        setFilters(clonedFilters);
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
        for (FilterDefinition filter : getFilters()) {
            filter.updateIDs(replacementMap);
        }
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        if (getFilters().size() > 0) {
            for (FilterDefinition filterDefinition : getFilters()) {
                items.add(filterDefinition.getField());
            }
        }
        return items;
    }

    public List<AnalysisItem> addLinkItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        if (getLinks().size() > 0) {
            Map<Key, AnalysisItem> map = new HashMap<Key, AnalysisItem>();
            for (AnalysisItem analysisItem : allItems) {
                map.put(analysisItem.getKey(), analysisItem);
            }
            Resolver resolver = new Resolver(allItems);
            for (Link link : getLinks()) {
                List<Key> keys = link.neededKeys(resolver);
                //Set<Key> keys = variableVisitor.getVariableList();
                for (Key key : keys) {
                    AnalysisItem analysisItem = map.get(key);
                    boolean alreadyInInsight = false;
                    for (AnalysisItem insightItem : insightItems) {
                        if (insightItem.getKey().equals(analysisItem.getKey())) {
                            alreadyInInsight = true;
                        }
                    }
                    if (!alreadyInInsight) items.add(analysisItem);
                }
            }
        }
        return items;
    }

    public List<AnalysisItem> getDerivedItems() {
        return new ArrayList<AnalysisItem>();
    }

    public AggregateKey createAggregateKey() {
        return new AggregateKey(getKey(), getType());
    }

    public void beforeSave() {
        for (FilterDefinition filterDefinition : getFilters()) {
            filterDefinition.beforeSave();
        }
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
        for (FilterDefinition filterDefinition : getFilters()) {
            filterDefinition.afterLoad();
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

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public boolean blocksDBAggregation() {
        return false;
    }
}
