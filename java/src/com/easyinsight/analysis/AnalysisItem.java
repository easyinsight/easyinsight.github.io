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
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedNode;
import com.easyinsight.datafeeds.AnalysisItemNode;
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

    @Column(name="original_display_name")
    private String originalDisplayName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="formatting_configuration_id")
    private FormattingConfiguration formattingConfiguration = new FormattingConfiguration();

    @Column(name="hidden")
    private boolean hidden = false;

    @Column(name="display_name")
    private String displayName;

    @Column(name="sort_sequence")
    private int sortSequence;

    @Column(name="tooltip")
    private String tooltip;

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

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "report_field_extension_id")
    private ReportFieldExtension reportFieldExtension;

    @Column(name="marmotscript")
    private String marmotScript;

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

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public ReportFieldExtension getReportFieldExtension() {
        return reportFieldExtension;
    }

    public void setReportFieldExtension(ReportFieldExtension reportFieldExtension) {
        this.reportFieldExtension = reportFieldExtension;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getOriginalDisplayName() {
        return originalDisplayName;
    }

    public void setOriginalDisplayName(String originalDisplayName) {
        this.originalDisplayName = originalDisplayName;
    }

    public String toOriginalDisplayName() {
        if (originalDisplayName == null) {
            return toDisplay();
        }
        return originalDisplayName;
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

    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata, boolean timezoneShift) {
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
            FilterDefinition clonedFilter = filterDefinition.clone();
            clonedFilters.add(clonedFilter);
            if (clonedFilter.getField() != null) {
                clonedFilter.setField(clonedFilter.getField().clone());
            }
        }
        clonedItem.setFilters(clonedFilters);
        clonedItem.setLinks(clonedLinks);
        if (reportFieldExtension != null) {
            clonedItem.setReportFieldExtension(reportFieldExtension.clone());
        }
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

    public void updateIDs(ReplacementMap replacementMap) {
        for (FilterDefinition filter : getFilters()) {
            filter.updateIDs(replacementMap);
        }
        if (reportFieldExtension != null) {
            reportFieldExtension.updateIDs(replacementMap);
        }
    }
    
    private AnalysisItem findMatch(Key key, Collection<AnalysisItem> analysisItems) {
        if (key instanceof DerivedKey) {
            for (AnalysisItem analysisItem : analysisItems) {
                Key analysisItemKey = analysisItem.getKey();
                if (analysisItemKey.matchesOrContains(key)) {
                    return analysisItem;
                }
            }
        }
        return null;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, int criteria) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        if (includeFilters && getFilters().size() > 0) {
            for (FilterDefinition filterDefinition : getFilters()) {
                items.addAll(filterDefinition.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria));
            }
        }
        if (getLookupTableID() != null && getLookupTableID() > 0 && includeFilters) {
            LookupTable lookupTable = new FeedService().getLookupTable(getLookupTableID());
            if (lookupTable != null) {
                Key key = lookupTable.getSourceField().getKey();
                AnalysisItem analysisItem = findMatch(key, allItems);
                if (analysisItem != null) {
                    items.addAll(analysisItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria));
                }
            }
        }
        if (reportFieldExtension != null) {
            items.addAll(reportFieldExtension.getAnalysisItems(getEverything));
        }
        return items;
    }

    public List<AnalysisItem> addLinkItems(List<AnalysisItem> allItems) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        if (getLinks().size() > 0) {
            Key myKey = getKey();
            DerivedKey myDerivedKey = null;
            if (myKey instanceof DerivedKey) {
                myDerivedKey = (DerivedKey) myKey;
            }
            Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
            Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : allItems) {
                List<AnalysisItem> myItems = displayMap.get(analysisItem.toDisplay());
                if (myItems == null) {
                    myItems = new ArrayList<AnalysisItem>(1);
                    displayMap.put(analysisItem.toDisplay(), myItems);
                }
                myItems.add(analysisItem);
                Key key = analysisItem.getKey();
                if (key instanceof DerivedKey) {
                    DerivedKey derivedKey = (DerivedKey) key;
                    if (myDerivedKey != null && derivedKey.getFeedID() != myDerivedKey.getFeedID()) {
                        continue;
                    }
                }
                keyMap.put(analysisItem.getKey().toDisplayName(), Arrays.asList(analysisItem));
            }
            for (Link link : getLinks()) {
                List<AnalysisItem> keys = link.neededKeys(keyMap, displayMap);
                //Set<Key> keys = variableVisitor.getVariableList();
                for (AnalysisItem key : keys) {
                    items.add(key);
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
        if (reportFieldExtension != null) {
            reportFieldExtension = (ReportFieldExtension) Database.deproxy(reportFieldExtension);
            reportFieldExtension.afterLoad();
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
        if (reportFieldExtension != null) {
            reportFieldExtension.reportSave(session);
            session.saveOrUpdate(reportFieldExtension);
        }
        if (getLinks() != null) {
            for (Link link : getLinks()) {
                link.beforeSave();
            }
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

    public void timeshift(Feed dataSource, Collection<FilterDefinition> filters) {
        for (FilterDefinition filterDefinition : getFilters()) {
            filterDefinition.timeshift(dataSource, filters);
        }
    }
}
