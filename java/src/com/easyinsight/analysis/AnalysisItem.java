package com.easyinsight.analysis;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.io.Serializable;

import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedNode;
import com.easyinsight.datafeeds.AnalysisItemNode;
import com.easyinsight.calculations.Resolver;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.pipeline.IComponent;
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

    @Transient
    private transient boolean loaded;

    @Column(name="concrete")
    private boolean concrete = true;

    @Column(name="lookup_table_id")
    private Long lookupTableID;

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

    @Column(name="sort_sequence")
    private int sortSequence;

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

    /*@OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="virtual_dimension_id")
    private VirtualDimension virtualDimension;*/

    @Transient
    private transient String folder;

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

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public Long getLookupTableID() {
        return lookupTableID;
    }

    public void setLookupTableID(Long lookupTableID) {
        this.lookupTableID = lookupTableID;
    }

    public int getSortSequence() {
        return sortSequence;
    }

    public void setSortSequence(int sortSequence) {
        this.sortSequence = sortSequence;
    }

    public String qualifiedName() {
        return key.internalString() + getQualifiedSuffix();
    }

    protected String getQualifiedSuffix() {
        return String.valueOf(getType());
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
    
    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public boolean isConcrete() {
        return concrete;
    }

    public void setConcrete(boolean concrete) {
        this.concrete = concrete;
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

    protected boolean hasCriteria(int criteria, int particular) {
        return (criteria & particular) == particular;
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
        clonedItem.cachedKey = null;
        clonedItem.setAnalysisItemID(0);
        clonedItem.setFormattingConfiguration(formattingConfiguration.clone());
        List<Link> clonedLinks = new ArrayList<Link>();
        for (Link link : getLinks()) {
            clonedLinks.add(link.clone());
        }
        List<FilterDefinition> clonedFilters = new ArrayList<FilterDefinition>();
        for (FilterDefinition filterDefinition : getFilters()) {
            //filterDefinition.afterLoad();
            clonedFilters.add(filterDefinition.clone());
        }
        clonedItem.setFilters(clonedFilters);
        clonedItem.setLinks(clonedLinks);
        return clonedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalysisItem that = (AnalysisItem) o;

        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    public boolean isDerived() {
        return false;
    }

    public boolean isVirtual() {
        return false;
    }

    @Override
    public String toString() {
        return toDisplay() + " - " + getAnalysisItemID();
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        for (FilterDefinition filter : getFilters()) {
            filter.updateIDs(replacementMap);
        }
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, boolean completelyShallow, int criteria) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        if (includeFilters && getFilters().size() > 0) {
            for (FilterDefinition filterDefinition : getFilters()) {
                items.addAll(filterDefinition.getField().getAnalysisItems(allItems, insightItems, getEverything, includeFilters, false, criteria));
            }
        }
        if (getLookupTableID() != null && getLookupTableID() > 0 && includeFilters) {
            LookupTable lookupTable = new FeedService().getLookupTable(getLookupTableID());
            if (lookupTable != null) {
                items.addAll(lookupTable.getSourceField().getAnalysisItems(allItems, insightItems, getEverything, includeFilters, completelyShallow, criteria));
            }
        }
        return items;
    }

    public List<AnalysisItem> addLinkItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        if (getLinks().size() > 0) {
            Key myKey = getKey();
            DerivedKey myDerivedKey = null;
            if (myKey instanceof DerivedKey) {
                myDerivedKey = (DerivedKey) myKey;
            }
            Map<Key, AnalysisItem> map = new HashMap<Key, AnalysisItem>();
            for (AnalysisItem analysisItem : allItems) {
                Key key = analysisItem.getKey();
                if (key instanceof DerivedKey) {
                    DerivedKey derivedKey = (DerivedKey) key;
                    if (myDerivedKey != null && derivedKey.getFeedID() != myDerivedKey.getFeedID()) {
                        continue;
                    }
                }
                map.put(analysisItem.getKey(), analysisItem);
            }
            Resolver resolver = new Resolver(new ArrayList<AnalysisItem>(map.values()));
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

    private transient AggregateKey cachedKey;

    public AggregateKey createAggregateKey() {
        if (cachedKey == null) {
            cachedKey = new AggregateKey(getKey(), getType(), getFilters());
        }
        return cachedKey;
    }

    public AggregateKey createAggregateKey(boolean measure) {
        return createAggregateKey();
    }

    public void beforeSave() {
        if (lookupTableID != null && lookupTableID == 0) {
            lookupTableID = null;
        }
    }

    public boolean isValid() {
        return true;
    }

    public void afterLoad() {
        /*if (virtualDimension != null) {
            for (VirtualTransform transform : virtualDimension.getVirtualTransforms()) {
                transform.toRemote();
            }
        }*/
        if (!loaded) {
                for (FilterDefinition filterDefinition : getFilters()) {
                    filterDefinition.afterLoad();
                }

            setLinks(new ArrayList<Link>(getLinks()));
            loaded = true;
        }
    }

    public String toKeySQL() {
        if (isDerived()) {
            throw new RuntimeException("Attempt made to retrieve SQL for a derived analysis item.");
        }
        return getKey().toBaseKey().toSQL();
    }    

    public boolean isCalculated() {
        return false;
    }

    public void reportSave(Session session) {
        beforeSave();
        if (getKey().getKeyID() == 0) {
            session.save(getKey());
        } else {
            session.merge(getKey());
        }
        for (FilterDefinition filterDefinition : getFilters()) {
            filterDefinition.beforeSave(session);
        }
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

    public List<IComponent> createComponents() {
        return new ArrayList<IComponent>();
    }

    public String toXML() {
        String xml = "<analysisItem key=\""+key.toDisplayName()+"\" display=\""+toDisplay()+"\" id=\""+analysisItemID+"\">";
        for (FilterDefinition filterDefinition : getFilters()) {
            xml += filterDefinition.toXML();
        }
        for (Link link : getLinks()) {
            xml += link.toXML();
        }
        xml += "</analysisItem>";
        return xml;
    }
}
