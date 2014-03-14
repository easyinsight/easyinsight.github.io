package com.easyinsight.analysis;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.io.Serializable;

import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.*;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.Pipeline;
import com.easyinsight.tag.Tag;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 11:09:03 PM
 */
@Entity
@Table(name = "analysis_item")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AnalysisItem implements Cloneable, Serializable {

    public static final int ORDER_DEFAULT = 0;
    public static final int ORDER_REPORT_LEVEL = 1;
    public static final int ORDER_DATA_SOURCE_LEVEL = 2;
    public static final int ORDER_KPI = 3;

    @Column(name="custom_flag")
    private int customFlag;

    // if we fold out key into its own class...

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_key_id")
    private Key key;

    @Transient
    private List<Tag> tags;

    @Transient
    private String defaultDate;

    @Transient
    private FieldDataSourceOrigin origin;

    @Transient
    private transient boolean loaded;

    @Transient
    private transient long dataSourceID;

    @Column(name = "concrete")
    private boolean concrete = true;

    @Column(name = "lookup_table_id")
    private Long lookupTableID;

    @Column(name="parent_item_id")
    private Long parentItemID;

    @Column(name="reload_from_data_source")
    private boolean reloadFromDataSource;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_item_id")
    private long analysisItemID;

    @Column(name = "original_display_name")
    private String originalDisplayName;

    @Column(name="formatting_type")
    private int formattingType = FormattingConfiguration.NUMBER;

    @Column(name = "hidden")
    private boolean hidden = false;

    @Column(name = "display_name")
    private String displayName;

    @Column(name="based_on_report_field")
    private Long basedOnReportField;

    @Column(name="unqualified_display_name")
    private String unqualifiedDisplayName;

    @Column(name = "sort_sequence")
    private int sortSequence;

    @Column(name = "tooltip")
    private String tooltip;

    @Column(name = "sort")
    private int sort;

    @Column(name = "width")
    private int width;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "analysis_item_to_filter",
            joinColumns = @JoinColumn(name = "analysis_item_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "filter_id", nullable = false))
    private List<FilterDefinition> filters = new ArrayList<FilterDefinition>();

    @Column(name = "key_column")
    private boolean keyColumn;

    @Column(name = "item_position")
    private int itemPosition;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sort_item_id")
    private AnalysisItem sortItem;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "analysis_item_to_link",
            joinColumns = @JoinColumn(name = "analysis_item_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "link_id", nullable = false))
    private List<Link> links = new ArrayList<Link>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_field_extension_id")
    private ReportFieldExtension reportFieldExtension;

    @Transient
    private AnalysisItem reportTemporaryItem;

    // field connection

    @Transient
    private transient String folder;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_field_id")
    private AnalysisItem fromField;

    @Column(name = "kpi")
    private boolean kpi;

    @Column(name = "field_type")
    private int fieldType;

    @Column(name="flex_id")
    private long flexID;

    /*@Transient
    private transient Set<String> pipelineSections;*/

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

    public int getFormattingType() {
        return formattingType;
    }

    public void setFormattingType(int formattingType) {
        this.formattingType = formattingType;
    }

    public String getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(String defaultDate) {
        this.defaultDate = defaultDate;
    }

    public FieldDataSourceOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(FieldDataSourceOrigin origin) {
        this.origin = origin;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getUnqualifiedDisplayName() {
        return unqualifiedDisplayName;
    }

    public void setUnqualifiedDisplayName(String unqualifiedDisplayName) {
        this.unqualifiedDisplayName = unqualifiedDisplayName;
    }

    public Long getBasedOnReportField() {
        return basedOnReportField;
    }

    public void setBasedOnReportField(Long basedOnReportField) {
        this.basedOnReportField = basedOnReportField;
    }

    public int getCustomFlag() {
        return customFlag;
    }

    public void setCustomFlag(int customFlag) {
        this.customFlag = customFlag;
    }

    public long getFlexID() {
        return flexID;
    }

    public void setFlexID(long flexID) {
        this.flexID = flexID;
    }

    public AnalysisItem getReportTemporaryItem() {
        return reportTemporaryItem;
    }

    public void setReportTemporaryItem(AnalysisItem reportTemporaryItem) {
        this.reportTemporaryItem = reportTemporaryItem;
    }

    public boolean isReloadFromDataSource() {
        return reloadFromDataSource;
    }

    public void setReloadFromDataSource(boolean reloadFromDataSource) {
        this.reloadFromDataSource = reloadFromDataSource;
    }

    public Long getParentItemID() {
        return parentItemID;
    }

    public void setParentItemID(Long parentItemID) {
        this.parentItemID = parentItemID;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public boolean isKpi() {
        return kpi;
    }

    public void setKpi(boolean kpi) {
        this.kpi = kpi;
    }

    public AnalysisItem getFromField() {
        return fromField;
    }

    public void setFromField(AnalysisItem fromField) {
        this.fromField = fromField;
    }

    public void setReady(boolean ready) {

    }

    public AnalysisItem getSortItem() {
        return sortItem;
    }

    /*public Set<String> getPipelineSections() {
        if (pipelineSections == null) {
            pipelineSections = new HashSet<String>();
        }
        return pipelineSections;
    }

    public void setPipelineSections(Set<String> pipelineSections) {
        this.pipelineSections = pipelineSections;
    }*/

    public boolean isKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(boolean keyColumn) {
        this.keyColumn = keyColumn;
    }

    public void setSortItem(AnalysisItem sortItem) {
        this.sortItem = sortItem;
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

    @Transient
    private transient String qualifiedName;

    public String qualifiedName() {
        if (qualifiedName == null) {
            qualifiedName = key.internalString() + getQualifiedSuffix();
        }
        return qualifiedName;
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

    public String toUnqualifiedDisplay() {
        if (unqualifiedDisplayName == null) {
            return toDisplay();
        } else {
            return unqualifiedDisplayName;
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
        if (unqualifiedDisplayName == null) {
            unqualifiedDisplayName = displayName;
        }
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

    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata, boolean timezoneShift, Calendar calendar) {
        return value;
    }

    public Value renameMeLater(Value value) {
        return value;
    }

    public abstract int getType();

    public abstract int actualType();

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

    public AnalysisItem clone() throws CloneNotSupportedException {
        AnalysisItem clonedItem = (AnalysisItem) super.clone();
        clonedItem.cachedKey = null;
        clonedItem.setAnalysisItemID(0);
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
        return lookupTableID != null && lookupTableID > 0;
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
        if (sortItem != null) {
            sortItem = replacementMap.getField(sortItem);
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

    protected List<AnalysisItem> measureFilters(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        if (includeFilters && getFilters().size() > 0) {
            for (FilterDefinition filterDefinition : getFilters()) {
                items.addAll(filterDefinition.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));
            }
        }
        return items;
    }

    private AnalysisItem findMatch(AnalysisItem sourceItem, Map<String, List<AnalysisItem>> displayMap, Map<String, List<AnalysisItem>> keyMap) {

        List<AnalysisItem> items = displayMap.get(sourceItem.toDisplay());
        if (items == null) {
            items = keyMap.get(sourceItem.toDisplay());
        }
        if (items == null) {
            return null;
        } else {
            return items.get(0);
        }
    }

    public void validate(Set<Long> sourceIDs) {
        if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            if (!sourceIDs.contains(derivedKey.getFeedID())) {
                throw new RuntimeException("Bad ID of " + derivedKey.getFeedID() + " on " + toDisplay());
            }
        }
        if (getFilters() != null) {
            for (FilterDefinition filter : getFilters()) {
                if (filter.getField() != null) {
                    filter.getField().validate(sourceIDs);
                }
            }
        }
        if (sortItem != null) {
            sortItem.validate(sourceIDs);
        }
        if (fromField != null) {
            fromField.validate(sourceIDs);
        }
        if (reportFieldExtension != null) {
            reportFieldExtension.validate(sourceIDs);
        }
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {

        if (analysisItemSet.contains(this)) {
            return new ArrayList<AnalysisItem>(analysisItemSet);
        }
        List<AnalysisItem> analysisItemList = new ArrayList<AnalysisItem>();
        analysisItemList.add(this);
        analysisItemSet.add(this);
        analysisItemList.addAll(measureFilters(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));

        if (getLookupTableID() != null && getLookupTableID() > 0 && includeFilters) {
            LookupTable lookupTable = new FeedService().getLookupTable(getLookupTableID());
            if (lookupTable != null) {
                Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
                Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
                for (AnalysisItem analysisItem : insightItems) {
                    List<AnalysisItem> pipelineItems = keyMap.get(analysisItem.getKey().toKeyString());
                    if (pipelineItems == null) {
                        pipelineItems = new ArrayList<AnalysisItem>(1);
                        keyMap.put(analysisItem.getKey().toKeyString(), pipelineItems);
                    }
                    pipelineItems.add(analysisItem);
                    List<AnalysisItem> displayMapItems = displayMap.get(analysisItem.toDisplay());
                    if (displayMapItems == null) {
                        displayMapItems = new ArrayList<AnalysisItem>(1);
                        displayMap.put(analysisItem.toDisplay(), displayMapItems);
                    }
                    displayMapItems.add(analysisItem);
                }
                for (AnalysisItem analysisItem : allItems) {
                    List<AnalysisItem> pipelineItems = keyMap.get(analysisItem.getKey().toKeyString());
                    if (pipelineItems == null) {
                        pipelineItems = new ArrayList<AnalysisItem>(1);
                        keyMap.put(analysisItem.getKey().toKeyString(), pipelineItems);
                    }
                    pipelineItems.add(analysisItem);
                    List<AnalysisItem> displayMapItems = displayMap.get(analysisItem.toDisplay());
                    if (displayMapItems == null) {
                        displayMapItems = new ArrayList<AnalysisItem>(1);
                        displayMap.put(analysisItem.toDisplay(), displayMapItems);
                    }
                    displayMapItems.add(analysisItem);
                }
                AnalysisItem analysisItem = findMatch(lookupTable.getSourceField(), displayMap, keyMap);
                if (analysisItem != null && !analysisItemSet.contains(analysisItem)) {
                    analysisItemList.addAll(analysisItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));
                }
            }
        }
        if (reportFieldExtension != null) {
            analysisItemList.addAll(reportFieldExtension.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));
        }
        if (sortItem != null) {
            analysisItemList.addAll(sortItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));
        }
        if (fromField != null) {
            if (structure.getInsightRequestMetadata().getPipelines(fromField).isEmpty()) {
                structure.getInsightRequestMetadata().getPipelines(fromField).add(Pipeline.BEFORE);
            }
            if (structure.onOrAfter(Pipeline.BEFORE)) {
                analysisItemList.add(fromField);
            }

        }
        if (structure.onOrAfter(Pipeline.BEFORE) && links != null) {
            for (Link link : links) {
                analysisItemList.addAll(link.getFields(allItems));
            }
        }
        return analysisItemList;
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
                if (!analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                    continue;
                }
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

    public void clearCachedKey() {
        cachedKey = null;
    }

    public AggregateKey createAggregateKey() {
        if (cachedKey == null) {
            if (keyColumn) {
                cachedKey = new AggregatePrimaryKey(getKey(), getType(), getFilters());
            } else {
                cachedKey = new AggregateKey(getKey(), getType(), getFilters());
            }
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
        if (parentItemID != null && parentItemID == 0) {
            parentItemID = null;
        }
        if (basedOnReportField != null && basedOnReportField == 0) {
            basedOnReportField = null;
        }
    }

    public boolean isValid() {
        return true;
    }

    public void afterLoad() {
        afterLoad(false);
    }

    public void afterLoad(boolean optimized) {
        /*if (virtualDimension != null) {
            for (VirtualTransform transform : virtualDimension.getVirtualTransforms()) {
                transform.toRemote();
            }
        }*/
        if (!loaded) {
            if (!optimized) {
                for (FilterDefinition filterDefinition : getFilters()) {
                    filterDefinition.afterLoad();
                }
                setLinks(new ArrayList<Link>(getLinks()));
                key = (Key) Database.deproxy(key);
                key.afterLoad();
            }
            /**/
            /*setFilters(new ArrayList<FilterDefinition>());
            setLinks(new ArrayList<Link>());*/

            /**/
            loaded = true;
        }
        if (reportFieldExtension != null) {
            reportFieldExtension = (ReportFieldExtension) Database.deproxy(reportFieldExtension);
            reportFieldExtension.afterLoad();
        }
        if (sortItem != null) {
            sortItem = (AnalysisItem) Database.deproxy(sortItem);
            sortItem.afterLoad();
        }
        if (fromField != null) {
            fromField = (AnalysisItem) Database.deproxy(fromField);
            fromField.afterLoad();
        }
        if (links != null) {
            for (Link link : links) {
                link.afterLoad();
            }
        }
    }

    public String toKeySQL() {
        if (isDerived()) {
            throw new RuntimeException("Attempt made to retrieve SQL for a derived analysis item.");
        }
        if (pkKeyName != null) {
            return pkKeyName;
        }
        return getKey().toBaseKey().toSQL();
    }

    @Transient
    private transient String pkKeyName;

    public String getPkKeyName() {
        return pkKeyName;
    }

    public void setPkKeyName(String pkKeyName) {
        this.pkKeyName = pkKeyName;
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
        if (reportFieldExtension != null && reportFieldExtension.getFromFieldRuleID() > 0) {
            reportFieldExtension = null;
        }
        if (reportFieldExtension != null) {
            reportFieldExtension.reportSave(session);
            session.saveOrUpdate(reportFieldExtension);
        }
        if (getLinks() != null) {
            Iterator<Link> iter = getLinks().iterator();
            while (iter.hasNext()) {
                Link link = iter.next();
                if (link.isDefinedByRule()) {
                    iter.remove();
                } else {
                    link.beforeSave(session);
                }
            }
        }
        if (sortItem != null) {
            sortItem.reportSave(session);
            session.saveOrUpdate(sortItem);
        }
        if (fromField != null) {
            fromField.reportSave(session);
            session.saveOrUpdate(fromField);
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

    public Element toXML(XMLMetadata xmlMetadata) {
        Element root = new Element("analysisItem");
        root.addAttribute(new Attribute("display", getDisplayName() != null ? getDisplayName() : ""));
        root.addAttribute(new Attribute("type", String.valueOf(actualType())));
        root.addAttribute(new Attribute("key", key.urlKeyString(xmlMetadata)));
        root.addAttribute(new Attribute("concrete", String.valueOf(concrete)));
        Element filters = new Element("filters");
        root.appendChild(filters);
        for (FilterDefinition filterDefinition : getFilters()) {
            filters.appendChild(filterDefinition.toXML(xmlMetadata));
        }
        Element links = new Element("links");
        root.appendChild(links);
        for (Link link : getLinks()) {
            links.appendChild(link.toXML(xmlMetadata));
        }
        if (reportFieldExtension != null) {
            root.appendChild(reportFieldExtension.toXML(xmlMetadata));
        }

        return root;
    }

    public void timeshift(Feed dataSource, Collection<FilterDefinition> filters) {
        for (FilterDefinition filterDefinition : getFilters()) {
            filterDefinition.timeshift(dataSource, filters);
        }
    }

    public boolean persistable() {
        return isConcrete() && !isDerived();
    }

    public static AnalysisItem fromXML(Element fieldNode, XMLImportMetadata xmlImportMetadata) {
        int type = Integer.parseInt(fieldNode.getAttribute("type").getValue());
        AnalysisItem analysisItem;
        switch (type) {
            case AnalysisItemTypes.DIMENSION:
                analysisItem = new AnalysisDimension();
                break;
            case AnalysisItemTypes.CALCULATION:
                analysisItem = new AnalysisCalculation();
                break;
            case AnalysisItemTypes.DATE_DIMENSION:
                analysisItem = new AnalysisDateDimension();
                break;
            case AnalysisItemTypes.DERIVED_DATE:
                analysisItem = new DerivedAnalysisDateDimension();
                break;
            case AnalysisItemTypes.DERIVED_DIMENSION:
                analysisItem = new DerivedAnalysisDimension();
                break;
            case AnalysisItemTypes.HIERARCHY:
                analysisItem = new AnalysisHierarchyItem();
                break;
            case AnalysisItemTypes.LISTING:
                analysisItem = new AnalysisList();
                break;
            case AnalysisItemTypes.MEASURE:
                analysisItem = new AnalysisMeasure();
                break;
            case AnalysisItemTypes.STEP:
                analysisItem = new AnalysisStep();
                break;
            case AnalysisItemTypes.TEXT:
                analysisItem = new AnalysisText();
                break;
            default:
                throw new RuntimeException("Unknown type " + type);
        }

        String displayName = fieldNode.getAttribute("display").getValue();
        if (!"".equals(displayName)) {
            analysisItem.setDisplayName(displayName);
        }
        analysisItem.setConcrete(Boolean.parseBoolean(fieldNode.getAttribute("concrete").getValue()));

        // so a derived key is going to be XXXXX:Blah or XXXXX:YYYYY:Blah

        String keyString = fieldNode.getAttribute("key").getValue();
        String[] keyParts = keyString.split("\\|");
        String baseKey = keyParts[keyParts.length - 1];

        List<FeedDefinition> dataSources = new ArrayList<FeedDefinition>();
        for (int i = 0; i < keyParts.length; i++) {
            String keyPart = keyParts[i];
            if (i < (keyParts.length - 1)) {
                dataSources.add(xmlImportMetadata.dataSourceForURLKey(keyPart));
            } else {

            }
        }
        dataSources.add(0, xmlImportMetadata.getDataSource());
        FeedDefinition baseSource = dataSources.get(dataSources.size() - 1);
        AnalysisItem baseItem = baseSource.findAnalysisItemByKey(baseKey);
        if (baseItem == null) {
            boolean found = false;
            for (AnalysisItem analysisItem1 : xmlImportMetadata.getAdditionalReportItems()) {
                if (analysisItem1.getKey().toKeyString().equals(baseKey)) {
                    analysisItem.setKey(analysisItem1.getKey());
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (analysisItem.hasType(AnalysisItemTypes.CALCULATION) || analysisItem.hasType(AnalysisItemTypes.DERIVED_DATE) ||
                        analysisItem.hasType(AnalysisItemTypes.DERIVED_DIMENSION) || analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                    analysisItem.setKey(new NamedKey(baseKey));
                } else {
                    xmlImportMetadata.addUnknownField(baseKey);
                }
            }
        } else {
            //Key parentKey = baseItem.getKey();
            if (dataSources.size() > 1) {
                for (int i = dataSources.size() - 2; i >= 0; i--) {
                    FeedDefinition parentSource = dataSources.get(i);
                    for (AnalysisItem item : parentSource.getFields()) {
                        if (item.getKey() instanceof DerivedKey) {
                            DerivedKey derivedKey = (DerivedKey) item.getKey();
                            if (derivedKey.getParentKey().equals(baseItem.getKey())) {
                                baseItem = item;
                                break;
                            }
                        }
                    }
                }
            }

            analysisItem.setKey(baseItem.getKey());
        }
        if (analysisItem.getKey() == null) {
            analysisItem.setKey(new NamedKey(baseKey));
        }
        Nodes filters = fieldNode.query("filters/filter");
        for (int i = 0; i < filters.size(); i++) {
            Element filterNode = (Element) filters.get(i);
            FilterDefinition filterDefinition = FilterDefinition.fromXML(filterNode, xmlImportMetadata);
            analysisItem.getFilters().add(filterDefinition);
        }
        Nodes links = fieldNode.query("links/link");
        for (int i = 0; i < links.size(); i++) {
            Element filterNode = (Element) links.get(i);
            for (int j = 0; j < filterNode.getChildCount(); j++) {
                Node linkNode = filterNode.getChild(j);
                if (linkNode instanceof Element) {
                    Link link = Link.fromXML((Element) linkNode, xmlImportMetadata);
                    analysisItem.getLinks().add(link);
                }
            }
        }
        Nodes extensionNodes = fieldNode.query("fieldExtension");
        if (extensionNodes.size() > 0) {
            Element extensionElement = (Element) extensionNodes.get(0);
            analysisItem.setReportFieldExtension(ReportFieldExtension.fromXML(extensionElement, xmlImportMetadata));

        }
        analysisItem.subclassFromXML(fieldNode, xmlImportMetadata);
        return analysisItem;
    }

    protected void subclassFromXML(Element fieldNode, XMLImportMetadata xmlImportMetadata) {

    }

    public Link defaultLink() {
        if (getLinks() != null)
            for (Link l : getLinks()) {
                if (l.isDefaultLink())
                    return l;
            }

        return null;
    }

    public void populateNamedFilters(Collection<FilterDefinition> filters) {
        if (getFilters() != null) {
            for (FilterDefinition filter : getFilters()) {
                if (filter instanceof NamedFilterReference) {
                    NamedFilterReference namedFilterReference = (NamedFilterReference) filter;
                    namedFilterReference.populateNamedFilters(filters);
                }
            }
        }
    }

    @Transient
    private transient boolean rowIDField;

    public boolean isRowIDField() {
        return rowIDField;
    }

    public void setRowIDField(boolean rowIDField) {
        this.rowIDField = rowIDField;
    }
}
