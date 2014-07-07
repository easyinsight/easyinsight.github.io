package com.easyinsight.analysis;

import com.easyinsight.analysis.gauge.GaugeDefinitionState;
import com.easyinsight.core.*;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.easyinsight.tag.Tag;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import org.hibernate.Session;
import org.hibernate.annotations.MapKey;

import javax.persistence.*;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 11:40:41 AM
 */
@Entity
@Table(name = "analysis")
public class AnalysisDefinition implements Cloneable {
    @Column(name = "title")
    private String title;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long analysisID;
    @Column(name = "data_feed_id")
    private long dataFeedID;

    @Column(name="data_source_field_report")
    private boolean dataSourceFieldReport;

    @Column(name = "root_definition")
    private boolean rootDefinition;

    @Column(name = "report_type")
    private int reportType;

    @Column(name = "temporary_report")
    private boolean temporaryReport;

    @Column(name = "marmotscript")
    private String marmotScript;

    @Column(name = "report_run_marmotscript")
    private String reportRunMarmotScript;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "analysis_to_filter_join",
            joinColumns = @JoinColumn(name = "analysis_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "filter_id", nullable = false))
    private List<FilterDefinition> filterDefinitions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "additional_items",
            joinColumns = @JoinColumn(name = "analysis_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_id", nullable = false))
    private List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "analysis_id", nullable = false)
    private List<UserToAnalysisBinding> userBindings = new ArrayList<UserToAnalysisBinding>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "report_state_id")
    private AnalysisDefinitionState analysisDefinitionState;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "analysis_to_join_override",
            joinColumns = @JoinColumn(name = "analysis_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "join_override_id", nullable = false))
    private List<JoinOverride> joinOverrides;

    @Column(name = "folder")
    private int folder;

    @Column(name = "policy")
    private int analysisPolicy;

    @Column(name = "url_key")
    private String urlKey;

    @Column(name = "create_date")
    private Date dateCreated;

    @Column(name="public_with_key")
    private boolean publicWithKey;

    @Column(name = "update_date")
    private Date dateUpdated;

    @Column(name = "solution_visible")
    private boolean solutionVisible;

    @Column(name = "recommended_exchange")
    private boolean recommendedExchange;

    @Column(name = "marketplace_visible")
    private boolean marketplaceVisible;

    @Column(name = "publicly_visible")
    private boolean publiclyVisible;

    @Column(name="auto_setup_delivery")
    private boolean autoSetupDelivery;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "description")
    private String description;

    // one to many report adapters
    // add report, remove report from report editor
    // in retrieving fields, recognize and handle fields appropriately (QueryStateNode)

    // what if there's a date...

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "report_to_report_stub",
            joinColumns = @JoinColumn(name = "report_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "report_stub_id", nullable = false))
    private List<ReportStub> reportStubs = new ArrayList<ReportStub>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "report_to_filter_set_stub",
            joinColumns = @JoinColumn(name = "report_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "filter_set_stub_id", nullable = false))
    private List<FilterSetStub> filterSets = new ArrayList<FilterSetStub>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "report_to_report_property",
            joinColumns = @JoinColumn(name = "analysis_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "report_property_id", nullable = false))
    private List<ReportProperty> properties = new ArrayList<ReportProperty>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(columns = @Column(name = "structure_key"))
    @JoinTable(name = "report_structure",
            joinColumns = @JoinColumn(name = "analysis_id"),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_id"))
    private Map<String, AnalysisItem> reportStructure;

    @Column(name = "account_visible")
    private boolean accountVisible;

    public void validate() {
        Iterator<Map.Entry<String, AnalysisItem>> iter = reportStructure.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, AnalysisItem> entry = iter.next();
            if (entry.getValue() != null) {
                entry.getValue().afterLoad();
            }
            if (entry.getValue() != null && entry.getValue().getKey().getClass().getName().equals("com.easyinsight.core.Key")) {
                iter.remove();
            }
        }
        if(joinOverrides != null) {
            Iterator<JoinOverride> joinIter = joinOverrides.iterator();
            while (joinIter.hasNext()) {
                JoinOverride joinOverride = joinIter.next();
                if (joinOverride.getSourceItem() != null && joinOverride.getSourceItem().getKey().getClass().getName().equals("com.easyinsight.core.Key")) {
                    joinIter.remove();
                } else if (joinOverride.getTargetItem() != null && joinOverride.getTargetItem().getKey().getClass().getName().equals("com.easyinsight.core.Key")) {
                    joinIter.remove();
                }
            }
        }
    }

    public List<FilterSetStub> getFilterSets() {
        return filterSets;
    }

    public void setFilterSets(List<FilterSetStub> filterSets) {
        this.filterSets = filterSets;
    }

    public boolean isPublicWithKey() {
        return publicWithKey;
    }

    public void setPublicWithKey(boolean publicWithKey) {
        this.publicWithKey = publicWithKey;
    }

    public List<ReportStub> getReportStubs() {
        return reportStubs;
    }

    public void setReportStubs(List<ReportStub> reportStubs) {
        this.reportStubs = reportStubs;
    }

    public boolean isAutoSetupDelivery() {
        return autoSetupDelivery;
    }

    public void setAutoSetupDelivery(boolean autoSetupDelivery) {
        this.autoSetupDelivery = autoSetupDelivery;
    }

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }

    public boolean isDataSourceFieldReport() {
        return dataSourceFieldReport;
    }

    public void setDataSourceFieldReport(boolean dataSourceFieldReport) {
        this.dataSourceFieldReport = dataSourceFieldReport;
    }

    public boolean isRecommendedExchange() {
        return recommendedExchange;
    }

    public void setRecommendedExchange(boolean recommendedExchange) {
        this.recommendedExchange = recommendedExchange;
    }

    public List<JoinOverride> getJoinOverrides() {
        return joinOverrides;
    }

    public void setJoinOverrides(List<JoinOverride> joinOverrides) {
        this.joinOverrides = joinOverrides;
    }

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public String getReportRunMarmotScript() {
        return reportRunMarmotScript;
    }

    public void setReportRunMarmotScript(String reportRunMarmotScript) {
        this.reportRunMarmotScript = reportRunMarmotScript;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public AnalysisDefinitionState getAnalysisDefinitionState() {
        return analysisDefinitionState;
    }

    public void setAnalysisDefinitionState(AnalysisDefinitionState analysisDefinitionState) {
        this.analysisDefinitionState = analysisDefinitionState;
    }

    public boolean isTemporaryReport() {
        return temporaryReport;
    }

    public void setTemporaryReport(boolean temporaryReport) {
        this.temporaryReport = temporaryReport;
    }

    public List<ReportProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ReportProperty> properties) {
        this.properties = properties;
    }

    public Map<String, AnalysisItem> getReportStructure() {
        return reportStructure;
    }

    public void setReportStructure(Map<String, AnalysisItem> reportStructure) {
        this.reportStructure = reportStructure;
    }

    public boolean isRootDefinition() {
        return rootDefinition;
    }

    public void setRootDefinition(boolean rootDefinition) {
        this.rootDefinition = rootDefinition;
    }

    public List<AnalysisItem> getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(List<AnalysisItem> addedItems) {
        this.addedItems = addedItems;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isSolutionVisible() {
        return solutionVisible;
    }

    public void setSolutionVisible(boolean solutionVisible) {
        this.solutionVisible = solutionVisible;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public int getAnalysisPolicy() {
        return analysisPolicy;
    }

    public void setAnalysisPolicy(int analysisPolicy) {
        this.analysisPolicy = analysisPolicy;
    }

    public List<UserToAnalysisBinding> getUserBindings() {
        return userBindings;
    }

    public void setUserBindings(List<UserToAnalysisBinding> userBindings) {
        this.userBindings = userBindings;
    }

    public List<FilterDefinition> getFilterDefinitions() {
        return filterDefinitions;
    }

    public void setFilterDefinitions(List<FilterDefinition> filterDefinitions) {
        this.filterDefinitions = filterDefinitions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAnalysisID() {
        return analysisID;
    }

    public void setAnalysisID(Long analysisID) {
        this.analysisID = analysisID;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public boolean isMarketplaceVisible() {
        return marketplaceVisible;
    }

    public void setMarketplaceVisible(boolean marketplaceVisible) {
        this.marketplaceVisible = marketplaceVisible;
    }

    public boolean isPubliclyVisible() {
        return publiclyVisible;
    }

    public void setPubliclyVisible(boolean publiclyVisible) {
        this.publiclyVisible = publiclyVisible;
    }

    private void cleanup(AnalysisItem analysisItem, boolean changingDataSource) {
        if (changingDataSource) {
            // TODO: validate calculations and lookup tables--if necessary to create, should emit something with the report
            analysisItem.setLookupTableID(null);
        }
    }

    public SaveMetadata clone(List<AnalysisItem> allFields, boolean changingDataSource) throws CloneNotSupportedException {
        return clone(allFields, changingDataSource, null);
    }

    public Set<Long> findTags() {
        Set<Long> tags = new HashSet<Long>();
        for (FilterDefinition filterDefinition : filterDefinitions) {
            if (filterDefinition instanceof AnalysisItemFilterDefinition ||
                    filterDefinition instanceof MultiFieldFilterDefinition) {
                IFieldChoiceFilter fieldChoiceFilter = (IFieldChoiceFilter) filterDefinition;
                for (WeNeedToReplaceHibernateTag tag : fieldChoiceFilter.getAvailableTags()) {
                    tags.add(tag.getTagID());
                }
            }
        }
        return tags;
    }

    public SaveMetadata clone(List<AnalysisItem> allFields, boolean changingDataSource, Map<Long, Tag> tagMap) throws CloneNotSupportedException {
        return clone(allFields, changingDataSource, tagMap, new ReplacementMapFactory());
    }

    public SaveMetadata clone(List<AnalysisItem> allFields, boolean changingDataSource, Map<Long, Tag> tagMap, ReplacementMapFactory factory) throws CloneNotSupportedException {
        AnalysisDefinition analysisDefinition = (AnalysisDefinition) super.clone();

        analysisDefinition.setAnalysisDefinitionState(analysisDefinitionState.clone(allFields));
        analysisDefinition.setUrlKey(null);
        analysisDefinition.setAnalysisID(null);
        //Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
        ReplacementMap replacementMap = factory.createMap();
        replacementMap.setTagReplacementMap(tagMap);

        allFields = new ArrayList<AnalysisItem>(allFields);
        List<AnalysisItem> added = new ArrayList<AnalysisItem>();

        if (analysisDefinition.getAddedItems() != null) {
            allFields.addAll(analysisDefinition.getAddedItems());
            added.addAll(analysisDefinition.getAddedItems());
        }
        if (analysisDefinition.getReportStubs() != null) {
            for (ReportStub reportStub : analysisDefinition.getReportStubs()) {
                Map<Long, AnalysisItem> xReplacementMap = new HashMap<Long, AnalysisItem>();
                List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
                WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportStub.getReportID());
                Map<String, AnalysisItem> structure = report.createStructure();
                for (AnalysisItem item : structure.values()) {
                    AnalysisItem clone;
                    if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        AnalysisDateDimension baseDate = (AnalysisDateDimension) item;
                        AnalysisDateDimension date = new AnalysisDateDimension();
                        date.setDateLevel(baseDate.getDateLevel());
                        date.setOutputDateFormat(baseDate.getOutputDateFormat());
                        date.setDateOnlyField(baseDate.isDateOnlyField() || baseDate.hasType(AnalysisItemTypes.DERIVED_DATE));
                        clone = date;
                    } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                        AnalysisMeasure baseMeasure = (AnalysisMeasure) item;
                        AnalysisMeasure measure = new AnalysisMeasure();
                        measure.setFormattingType(item.getFormattingType());
                        if (report.isPersistedCache()) {
                            measure.setAggregation(AggregationTypes.SUM);
                        } else {
                            measure.setAggregation(baseMeasure.getAggregation());
                        }
                        measure.setPrecision(baseMeasure.getPrecision());
                        measure.setMinPrecision(baseMeasure.getMinPrecision());
                        clone = measure;
                    } else {
                        clone = new AnalysisDimension();
                    }
                    clone.setOriginalDisplayName(item.toDisplay());
                    clone.setDisplayName(report.getName() + " - " + item.toDisplay());
                    clone.setUnqualifiedDisplayName(item.toUnqualifiedDisplay());
                    clone.setBasedOnReportField(item.getAnalysisItemID());
                    ReportKey reportKey = new ReportKey();
                    reportKey.setParentKey(item.getKey());
                    reportKey.setReportID(reportStub.getReportID());
                    clone.setKey(reportKey);
                    xReplacementMap.put(item.getAnalysisItemID(), clone);
                    fields.add(clone);
                }
                ReplacementMap replacements = ReplacementMap.fromMap(xReplacementMap);
                for (AnalysisItem clone : fields) {
                    clone.updateIDs(replacements);
                    allFields.add(clone);
                    System.out.println("Adding report level clone " + clone.toDisplay());
                    added.add(clone);
                }
            }
        }

        List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();

        Collection<AnalysisItem> reportItems = getReportStructure().values();

        AnalysisItemRetrievalStructure structure = new AnalysisItemRetrievalStructure(null);
        structure.setBaseReport(this);
        structure.setNoCalcs(true);

        analysisDefinition.setReportStubs(new ArrayList<ReportStub>());

        if (getAddedItems() != null) {

            for (AnalysisItem analysisItem : getAddedItems()) {
                AnalysisItem clonedItem = replacementMap.addField(analysisItem, changingDataSource);
                addedItems.add(clonedItem);
                List<AnalysisItem> items = analysisItem.getAnalysisItems(allFields, reportItems, true, true, new HashSet<AnalysisItem>(), structure);
                for (AnalysisItem item : items) {
                    /*if (set.contains(item)  && !addedItems.contains(item)) {
                        addedItems.add(item);
                    }*/
                    replacementMap.addField(item, changingDataSource);
                }
            }
        }

        allFields = new ArrayList<AnalysisItem>(allFields);
        allFields.addAll(addedItems);


        reportItems.remove(null);
        for (AnalysisItem baseItem : reportItems) {
            replacementMap.addField(baseItem, changingDataSource);
            List<AnalysisItem> items = baseItem.getAnalysisItems(allFields, reportItems, true, true, new HashSet<AnalysisItem>(), structure);
            for (AnalysisItem item : items) {
                /*if (set.contains(item)  && !addedItems.contains(item)) {
                    addedItems.add(item);
                }*/
                replacementMap.addField(item, changingDataSource);
            }
        }
        List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();
        if (this.filterDefinitions != null) {
            for (FilterDefinition persistableFilterDefinition : this.filterDefinitions) {
                filterDefinitions.add(persistableFilterDefinition.clone());
                List<AnalysisItem> filterItems = persistableFilterDefinition.getAnalysisItems(allFields, reportItems, true, true, new HashSet<AnalysisItem>(), structure);
                for (AnalysisItem item : filterItems) {
                    /*if (set.contains(item) && !addedItems.contains(item)) {
                        addedItems.add(item);
                    }*/
                    replacementMap.addField(item, changingDataSource);
                }
            }
        }
        analysisDefinition.setFilterDefinitions(filterDefinitions);

        if (factory instanceof TemplateReplacementMapFactory) {
            analysisDefinition.setJoinOverrides(null);
        } else {
            if (getJoinOverrides() != null) {
                List<JoinOverride> clones = new ArrayList<JoinOverride>();
                if (joinOverrides.size() > 0) {
                    System.out.println("Copying multiple join overrides for " + getTitle());
                }
                for (JoinOverride joinOverride : joinOverrides) {
                    System.out.println("\t" + joinOverride.getSourceItem() + " to " + joinOverride.getTargetItem());
                    replacementMap.addField(joinOverride.getSourceItem(), changingDataSource);
                    replacementMap.addField(joinOverride.getTargetItem(), changingDataSource);
                    clones.add(joinOverride.clone());
                }
                analysisDefinition.setJoinOverrides(clones);
            }
        }

        /*
        for (Map.Entry<String, AnalysisItem> entry : clonedStructure.entrySet()) {
            replacementMap.addField(entry.getValue(), changingDataSource);
            List<AnalysisItem> items = entry.getValue().getAnalysisItems(allFields, reportItems, true, true, CleanupComponent.AGGREGATE_CALCULATIONS);
            for (AnalysisItem item : items) {
                replacementMap.addField(item, changingDataSource);
            }
        }*/

        List<ReportStub> clonedStubs = new ArrayList<ReportStub>();
        for (ReportStub reportStub : reportStubs) {
            clonedStubs.add(reportStub.clone());
        }

        analysisDefinition.setReportStubs(clonedStubs);
        if (filterSets != null) {
            List<FilterSetStub> filterSetStubs = new ArrayList<FilterSetStub>();
            for (FilterSetStub stub : this.filterSets) {
                filterSetStubs.add(stub.clone());
            }
            analysisDefinition.setFilterSets(filterSetStubs);
        }


        analysisDefinition.getAnalysisDefinitionState().updateIDs(replacementMap);

        analysisDefinition.setAddedItems(addedItems);
        analysisDefinition.setUserBindings(new ArrayList<UserToAnalysisBinding>());
        List<ReportProperty> clonedProperties = new ArrayList<ReportProperty>();
        for (ReportProperty reportProperty : this.properties) {
            clonedProperties.add(reportProperty.clone());
        }
        analysisDefinition.setProperties(clonedProperties);
        analysisDefinition.setTemporaryReport(temporaryReport);

        Map<String, AnalysisItem> clonedStructure = new HashMap<String, AnalysisItem>();
        for (Map.Entry<String, AnalysisItem> entry : getReportStructure().entrySet()) {
            clonedStructure.put(entry.getKey(), replacementMap.getField(entry.getValue()));
        }

        analysisDefinition.setReportStructure(clonedStructure);
        SaveMetadata saveMetadata = new SaveMetadata();
        saveMetadata.added = added;
        saveMetadata.replacementMap = replacementMap;
        saveMetadata.analysisDefinition = analysisDefinition;
        return saveMetadata;
    }

    public static class SaveMetadata {
        public ReplacementMap replacementMap;
        public AnalysisDefinition analysisDefinition;
        public List<AnalysisItem> added;

    }

    public void validateForInstall() {

    }

    private static boolean originValid(AnalysisItem dataSourceItem, AnalysisDefinition analysisDefinition) {
        boolean valid = true;
        if (dataSourceItem.getOrigin() != null) {
            if (dataSourceItem.getOrigin().getReport() == analysisDefinition.getAnalysisID()) {
                valid = false;
            } else if (dataSourceItem.getOrigin().getAdditionalReports() != null &&
                    dataSourceItem.getOrigin().getAdditionalReports().contains(analysisDefinition.getAnalysisID())) {
                valid = false;
            }
        }
        return valid;
    }

    public static void copyToAlternateType(FeedDefinition target, Map<String, AnalysisItem> targetFieldMap,
                                           AnalysisDefinition analysisDefinition, ReplacementMap replacementMap) throws CloneNotSupportedException {

        for (AnalysisItem analysisItem : replacementMap.getFields()) {
            if (target != null) {
                target.updateLinks(analysisItem);
            }
            analysisItem.updateIDs(replacementMap);
        }
        for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
            filter.updateIDs(replacementMap);
        }
    }

    public static void updateFromMetadata(FeedDefinition target, ReplacementMap replacementMap,
                                          AnalysisDefinition analysisDefinition, List<AnalysisItem> allFields, List<AnalysisItem> added) throws CloneNotSupportedException {
        Map<String, AnalysisItem> clonedStructure = analysisDefinition.getReportStructure();

        Map<String, List<AnalysisItem>> targetFieldMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        for (AnalysisItem item : allFields) {
            List<AnalysisItem> targetItems = targetFieldMap.get(item.toDisplay());
            if (targetItems == null) {
                targetItems = new ArrayList<AnalysisItem>(1);
                targetFieldMap.put(item.toDisplay(), targetItems);
            }
            targetItems.add(item);
            List<AnalysisItem> items = keyMap.get(item.getKey().toKeyString());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                keyMap.put(item.getKey().toKeyString(), items);
            }
            items.add(item);
        }
        /*for (AnalysisItem item : added) {
            targetFieldMap.put(item.toDisplay(), item);
        }*/
        List<AnalysisItem> addedItems = analysisDefinition.getAddedItems();
        if (target != null) {

            // here is where if it's a semantic link, we find the appropriate source and field

            // so the challenge from there, dealing with partials/joins

            // how do we handle that...

            // other thing is that we really want to be able to join individual tables

            //

            for (AnalysisItem analysisItem : replacementMap.getFields()) {
                analysisItem.setBasedOnReportField(null);
                //analysisItem.afterLoad();
                Key key = null;
                Key deproxiedKey = (Key) Database.deproxy(analysisItem.getKey());
                if (deproxiedKey instanceof ReportKey) {
                    System.out.println("\t\treport key of " + analysisItem.toDisplay() + ", skipping");
                } else {
                    List<AnalysisItem> targetItems = targetFieldMap.get(analysisItem.toDisplay());
                    AnalysisItem dataSourceItem = null;
                    if (targetItems == null) {
                    } else if (targetItems.size() == 1) {
                        dataSourceItem = targetItems.get(0);
                    } else if (targetItems.size() > 1) {
                        throw new RuntimeException("Ambiguous reference to " + analysisItem.toDisplay());
                    }

                    if (dataSourceItem != null && originValid(dataSourceItem, analysisDefinition)) {
                        System.out.println("\t\tFound key for " + analysisItem.toDisplay() + " via display name of " + analysisItem.toDisplay());
                        key = dataSourceItem.getKey();
                    } else {

                        targetItems = targetFieldMap.get(analysisItem.toOriginalDisplayName());

                        if (targetItems == null) {
                        } else if (targetItems.size() == 1) {
                            dataSourceItem = targetItems.get(0);
                        } else if (targetItems.size() > 1) {
                            LogClass.error("Ambiguous reference to " + analysisItem.toDisplay() + " by original display name of " + analysisItem.toOriginalDisplayName());
                        }

                        if (dataSourceItem != null && originValid(dataSourceItem, analysisDefinition)) {
                            System.out.println("\t\tFound key for " + analysisItem.toDisplay() + " via original display name of " + analysisItem.toOriginalDisplayName());
                            key = dataSourceItem.getKey();
                        } else {
                            dataSourceItem = null;
                            List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                            if (items != null) {
                                int i = 0;
                                for (AnalysisItem item : items) {
                                    if (item.getOrigin() != null && item.getOrigin().getReport() == analysisDefinition.getAnalysisID()) {

                                    } else {
                                        i++;
                                    }
                                }
                                if (i == 1) {
                                    for (AnalysisItem item : items) {
                                        if (item.getOrigin() != null && item.getOrigin().getReport() == analysisDefinition.getAnalysisID()) {

                                        } else {
                                            dataSourceItem = item;
                                            break;
                                        }
                                    }
                                } else if (i > 1) {
                                    throw new RuntimeException("Ambiguous reference to " + analysisItem.toDisplay() + " by key of " + analysisItem.getKey().toKeyString());
                                }
                                if (dataSourceItem != null) {
                                    System.out.println("\t\tFound key for " + analysisItem.toDisplay() + " via key for " + analysisItem.getKey().toKeyString());
                                    key = dataSourceItem.getKey();
                                }
                            }
                        }
                    }
                    if (key != null) {
                        //System.out.println("\t\tFound key for " + analysisItem.toDisplay());
                        analysisItem.setKey(key);
                        /*if (set.containsKey(analysisItem.toDisplay()) && !addedItems.contains(analysisItem)) {
                            addedItems.add(analysisItem);
                        }*/
                    } else {
                        if (analysisItem.getKey() instanceof DerivedKey) {
                            System.out.println("*** THIS IS BAAAAAAAAD ***");
                        }
                        System.out.println("\t\tCould not find " + analysisItem.toDisplay() + ", cloning key");
                        Key clonedKey = analysisItem.getKey().clone();
                        analysisItem.setKey(clonedKey);
                        if (!addedItems.contains(analysisItem)) {
                            analysisItem.setConcrete(false);
                            addedItems.add(analysisItem);
                        }
                    }
                }
            }
        }


        for (AnalysisItem analysisItem : replacementMap.getFields()) {
            if (target != null) {
                target.updateLinks(analysisItem);
            }
            analysisItem.updateIDs(replacementMap);
        }
        for (Map.Entry<String, AnalysisItem> entry : analysisDefinition.getReportStructure().entrySet()) {
            clonedStructure.put(entry.getKey(), replacementMap.getField(entry.getValue()));
        }
        for (FilterDefinition filter : analysisDefinition.getFilterDefinitions()) {
            filter.updateIDs(replacementMap);
        }
        if (analysisDefinition.getJoinOverrides() != null) {
            for (JoinOverride joinOverride : analysisDefinition.getJoinOverrides()) {
                joinOverride.updateIDs(replacementMap);
                if (target != null) {
                    joinOverride.setDataSourceID(target.getDataFeedID());
                }
            }
        }


    }

    private AnalysisDefinitionState migrationHandler() {
        return new ListDefinitionState();
    }

    public WSAnalysisDefinition createBlazeDefinition() {
        WSAnalysisDefinition analysisDefinition;
        AnalysisDefinitionState analysisDefinitionState = this.analysisDefinitionState;
        if (analysisDefinitionState == null) {
            analysisDefinitionState = migrationHandler();
        }
        analysisDefinitionState.afterLoad();
        analysisDefinition = analysisDefinitionState.createWSDefinition();
        analysisDefinition.setReportStateID(analysisDefinitionState.getId());
        analysisDefinition.setReportType(reportType);
        analysisDefinition.setAnalysisID(analysisID);
        analysisDefinition.setDataFeedID(dataFeedID);
        if (getAddedItems() != null) {
            List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();
            for (AnalysisItem analysisItem : getAddedItems()) {
                analysisItem = (AnalysisItem) Database.deproxy(analysisItem);
                analysisItem.afterLoad();
                addedItems.add(analysisItem);
            }
            analysisDefinition.setAddedItems(addedItems);
        }
        analysisDefinition.setName(title);
        analysisDefinition.setFilterDefinitions(FilterDefinitionConverter.fromPersistableFilters(filterDefinitions));
        analysisDefinition.setPolicy(analysisPolicy);
        analysisDefinition.populateProperties(properties);
        for (AnalysisItem analysisItem : reportStructure.values()) {
            analysisItem.afterLoad();
            /*if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
            }*/
        }
        analysisDefinition.populateFromReportStructure(reportStructure);
        analysisDefinition.setDescription(getDescription());
        analysisDefinition.setDataSourceFieldReport(isDataSourceFieldReport());
        analysisDefinition.setCanSaveDirectly(isOwner(SecurityUtil.getUserID(false)));
        analysisDefinition.setAuthorName(getAuthorName());
        analysisDefinition.setDateCreated(getDateCreated());
        analysisDefinition.setFolder(getFolder());
        analysisDefinition.setDateUpdated(getDateUpdated());
        analysisDefinition.setUrlKey(urlKey);
        analysisDefinition.setMarketplaceVisible(marketplaceVisible);
        analysisDefinition.setPubliclyVisible(publiclyVisible);
        analysisDefinition.setSolutionVisible(solutionVisible);
        analysisDefinition.setAccountVisible(accountVisible);
        analysisDefinition.setMarmotScript(marmotScript);
        analysisDefinition.setPublicWithKey(publicWithKey);
        analysisDefinition.setReportRunMarmotScript(reportRunMarmotScript);
        analysisDefinition.setRecommendedExchange(recommendedExchange);
        analysisDefinition.setAutoSetupDelivery(autoSetupDelivery);
        if (joinOverrides != null) {
            List<JoinOverride> joins = new ArrayList<JoinOverride>();
            for (JoinOverride joinOverride : joinOverrides) {
                joinOverride.afterLoad();
                joins.add(joinOverride);
            }
            analysisDefinition.setJoinOverrides(joins);
        }
        List<AddonReport> addonReports = new ArrayList<AddonReport>();
        if (reportStubs != null && reportStubs.size() > 0) {
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT TITLE FROM ANALYSIS WHERE ANALYSIS_ID = ?");
                for (ReportStub reportStub : reportStubs) {
                    AddonReport addonReport = new AddonReport();
                    stmt.setLong(1, reportStub.getReportID());
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    addonReport.setReportName(rs.getString(1));
                    addonReport.setReportID(reportStub.getReportID());
                    addonReports.add(addonReport);
                }
                stmt.close();
            } catch (Exception e) {
                LogClass.error(e);
            } finally {
                Database.closeConnection(conn);
            }
        }
        analysisDefinition.setAddonReports(addonReports);
        List<FilterSetDescriptor> filterSetList = new ArrayList<FilterSetDescriptor>();
        if (filterSets != null && filterSets.size() > 0) {
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT FILTER_SET_NAME FROM FILTER_SET WHERE FILTER_SET_ID = ?");
                for (FilterSetStub filterStub : filterSets) {
                    FilterSetDescriptor filterSetDescriptor = new FilterSetDescriptor();
                    filterSetDescriptor.setId(filterStub.getFilterSetID());
                    stmt.setLong(1, filterStub.getFilterSetID());
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    filterSetDescriptor.setName(rs.getString(1));
                    filterSetList.add(filterSetDescriptor);
                }
                stmt.close();
            } catch (Exception e) {
                LogClass.error(e);
            } finally {
                Database.closeConnection(conn);
            }
        }
        analysisDefinition.setFilterSets(filterSetList);
        return analysisDefinition;
    }

    private boolean isOwner(Long userID) {
        for (UserToAnalysisBinding binding : getUserBindings()) {
            if (binding.getUserID() == userID && binding.getRelationshipType() == UserPermission.OWNER) {
                return true;
            }
        }
        return false;
    }

    public List<AnalysisDefinition> containedReports(Session session) {
        List<AnalysisDefinition> reports = new ArrayList<AnalysisDefinition>();
        reports.addAll(analysisDefinitionState.containedReports(session));
        return reports;
    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap, Map<Long, Dashboard> dashboardReplacementMap, Session session) {
        analysisDefinitionState.updateReportIDs(reportReplacementMap);
        for (AnalysisItem analysisItem : reportStructure.values()) {
            for (Link link : analysisItem.getLinks()) {
                link.updateReportIDs(reportReplacementMap, dashboardReplacementMap);
            }
        }
        for (AnalysisItem analysisItem : reportStructure.values()) {
            cleanupField(analysisItem, reportReplacementMap, session);
        }
        for (FilterDefinition filterDefinition : getFilterDefinitions()) {
            if (filterDefinition.getField() != null) {
                cleanupField(filterDefinition.getField(), reportReplacementMap, session);
            }
        }
        for (AnalysisItem addedItem : getAddedItems()) {
            cleanupField(addedItem, reportReplacementMap, session);
        }
        if (getJoinOverrides() != null) {
            for (JoinOverride joinOverride : getJoinOverrides()) {
                AnalysisItem sourceItem = joinOverride.getSourceItem();
                AnalysisItem targetItem = joinOverride.getTargetItem();
                cleanupField(sourceItem, reportReplacementMap, session);
                cleanupField(targetItem, reportReplacementMap, session);
            }
        }
        for (ReportStub reportStub : reportStubs) {
            if (reportReplacementMap.containsKey(reportStub.getReportID())) {
                reportStub.setReportID(reportReplacementMap.get(reportStub.getReportID()).getAnalysisID());
            }
        }
    }

    private void cleanupField(AnalysisItem analysisItem, Map<Long, AnalysisDefinition> reportReplacementMap, Session session) {
            //Key key = (Key) Database.deproxy(analysisItem.getKey());
        Key deproxiedKey = (Key) Database.deproxy(analysisItem.getKey());
        if (deproxiedKey instanceof ReportKey) {
                ReportKey reportKey = (ReportKey) deproxiedKey;
                //reportKey.afterLoad();
                if (reportReplacementMap.containsKey(reportKey.getReportID())) {
                    ReportKey cloneKey = new ReportKey();
                    cloneKey.setReportID(reportKey.getReportID());
                    cloneKey.setParentKey(reportKey.getParentKey());
                    cloneKey.updateIDs(reportReplacementMap);
                    analysisItem.setKey(cloneKey);
                    AnalysisDefinition targetDef = reportReplacementMap.get(reportKey.getReportID());
                    for (AnalysisItem targetItem : targetDef.reportStructure.values()) {
                        if (analysisItem.toDisplay().equals(targetItem.toDisplay())) {
                            cloneKey.setParentKey(targetItem.getKey());
                        }
                    }
                    analysisItem.getKey().updateIDs(reportReplacementMap);
                    session.save(cloneKey);
                }
            }
    }

    public Set<ValidationID>  populateValidationIDs(Set<Long> validFeedIDs) {
        Set<ValidationID> validationIDs = new HashSet<ValidationID>();
        for (AnalysisItem item : reportStructure.values()) {
            validationIDs.add(new ValidationID(ValidationID.KEY, item.getKey().getKeyID()));
            validationIDs.add(new ValidationID(ValidationID.FIELD, item.getAnalysisItemID()));
            item.validate(validFeedIDs);
        }
        if (joinOverrides != null) {
            for (JoinOverride joinOverride : joinOverrides) {
                if (joinOverride.getSourceItem() != null) {
                    joinOverride.getSourceItem().validate(validFeedIDs);
                    validationIDs.add(new ValidationID(ValidationID.FIELD, joinOverride.getSourceItem().getAnalysisItemID()));
                }
                if (joinOverride.getTargetItem() != null) {
                    joinOverride.getTargetItem().validate(validFeedIDs);
                    validationIDs.add(new ValidationID(ValidationID.FIELD, joinOverride.getTargetItem().getAnalysisItemID()));
                }
            }
        }
        if (filterDefinitions != null) {
            for (FilterDefinition filter : filterDefinitions) {
                validationIDs.add(new ValidationID(ValidationID.FILTER, filter.getFilterID()));
                if (filter.getField() != null) {
                    filter.getField().validate(validFeedIDs);
                    validationIDs.add(new ValidationID(ValidationID.KEY, filter.getField().getKey().getKeyID()));
                    validationIDs.add(new ValidationID(ValidationID.FIELD, filter.getField().getAnalysisItemID()));
                }
            }
        }
        if (addedItems != null) {
            for (AnalysisItem item : addedItems) {
                item.validate(validFeedIDs);
                validationIDs.add(new ValidationID(ValidationID.KEY, item.getKey().getKeyID()));
                validationIDs.add(new ValidationID(ValidationID.FIELD, item.getAnalysisItemID()));
            }
        }
        return validationIDs;
    }

    public Set<EIDescriptor> containedReportIDs() {
        Set<EIDescriptor> drillIDs = new HashSet<EIDescriptor>();
        for (AnalysisItem analysisItem : reportStructure.values()) {
            List<Link> links = analysisItem.getLinks();
            if (links != null) {
                for (Link link : links) {
                    if (link instanceof DrillThrough) {
                        DrillThrough drillThrough = (DrillThrough) link;
                        if (drillThrough.getReportID() != null && drillThrough.getReportID() > 0) {
                            drillIDs.add(new InsightDescriptor(drillThrough.getReportID(), null, 0, 0, null, 0, false));
                        } else if (drillThrough.getDashboardID() != null && drillThrough.getDashboardID() > 0) {
                            drillIDs.add(new DashboardDescriptor(null, drillThrough.getDashboardID(), null, 0, 0, null, false));
                        }
                    }
                }
            }
        }
        if (reportStubs != null) {
            for (ReportStub reportStub : reportStubs) {
                drillIDs.add(new InsightDescriptor(reportStub.getReportID(), null, 0, 0, null, 0, false));
            }
        }
        return drillIDs;
    }

    public static AnalysisDefinition fromXML(Element root, XMLImportMetadata xmlImportMetadata) {
        int reportType = Integer.parseInt(root.getAttribute("type").getValue());
        AnalysisDefinition report = new AnalysisDefinition();
        report.setTitle(root.getAttribute("name").getValue());
        report.setUrlKey(root.getAttribute("urlKey").getValue() + "2");
        String dataSourceURLKey = root.getAttribute("dataSourceUrlKey").getValue();
        FeedDefinition dataSource = xmlImportMetadata.dataSourceForURLKey(dataSourceURLKey);
        report.setDataFeedID(dataSource.getDataFeedID());
        xmlImportMetadata.setDataSource(dataSource);
        AnalysisDefinitionState state = createReport(reportType);
        state.subclassFromXML((Element) root.query("reportState").get(0), xmlImportMetadata);
        report.setAnalysisDefinitionState(state);
        report.setReportType(reportType);
        report.setDataFeedID(dataSource.getDataFeedID());
        report.setAccountVisible(Boolean.parseBoolean(root.getAttribute("accountVisible").getValue()));
        report.setPubliclyVisible(Boolean.parseBoolean(root.getAttribute("publiclyVisible").getValue()));
        report.setMarketplaceVisible(Boolean.parseBoolean(root.getAttribute("exchangeVisible").getValue()));

        Nodes additionalFieldNodes = root.query("additionalFields/analysisItem");
        List<AnalysisItem> additionalFields = new ArrayList<AnalysisItem>();
        for (int i = 0; i < additionalFieldNodes.size(); i++) {
            Element additionalFieldNode = (Element) additionalFieldNodes.get(i);
            AnalysisItem analysisItem = AnalysisItem.fromXML(additionalFieldNode, xmlImportMetadata);
            additionalFields.add(analysisItem);
        }
        report.setAddedItems(additionalFields);

        xmlImportMetadata.setAdditionalReportItems(additionalFields);

        Nodes fieldNodes = root.query("fields/analysisItem");
        Map<String, AnalysisItem> reportStructure = new HashMap<String, AnalysisItem>();
        for (int i = 0; i < fieldNodes.size(); i++) {
            Element fieldNode = (Element) fieldNodes.get(i);
            AnalysisItem analysisItem = AnalysisItem.fromXML(fieldNode, xmlImportMetadata);
            String structureID = fieldNode.getAttribute("structureID").getValue();
            reportStructure.put(structureID, analysisItem);
        }

        report.setMarmotScript(xmlImportMetadata.getValue(root, "marmotScript/text()"));
        report.setDescription(xmlImportMetadata.getValue(root, "description/text()"));
        report.setReportRunMarmotScript(xmlImportMetadata.getValue(root, "reportRunMarmotScript/text()"));
        Nodes filterNodes = root.query("filters/filter");
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (int i = 0; i < filterNodes.size(); i++) {
            Element filterNode = (Element) filterNodes.get(i);
            filters.add(FilterDefinition.fromXML(filterNode, xmlImportMetadata));
        }
        List<JoinOverride> joinOverrides = new ArrayList<JoinOverride>();
        Nodes joinNodes = root.query("/joinOverrides/joinOverride");
        for (int i = 0; i < joinNodes.size(); i++) {
            JoinOverride joinOverride = new JoinOverride();
            joinOverride.fromXML((Element) joinNodes.get(i), xmlImportMetadata);
            joinOverrides.add(joinOverride);
        }
        report.setJoinOverrides(joinOverrides);
        Node propertyRoot = root.query("reportProperties").get(0);
        for (int i = 0; i < propertyRoot.getChildCount(); i++) {
            Node propertyChild = propertyRoot.getChild(i);
            if (propertyChild instanceof Element) {
                ReportProperty property = ReportProperty.fromXML((Element) propertyChild);
                report.getProperties().add(property);
            }
        }
        report.setFilterDefinitions(filters);
        report.setReportStructure(reportStructure);
        if (!xmlImportMetadata.getUnknownFields().isEmpty()) {
            throw new ReportException(new MissingFieldReportFault(xmlImportMetadata.getUnknownFields()));
        }
        return report;
    }



    public String toXML(XMLMetadata xmlMetadata) {
        Element root = new Element("report");
        DateFormat df = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        root.addAttribute(new Attribute("type", String.valueOf(getReportType())));
        root.addAttribute(new Attribute("name", xmlMetadata.value(getTitle())));
        root.addAttribute(new Attribute("urlKey", xmlMetadata.value(getUrlKey())));
        root.addAttribute(new Attribute("dataSourceUrlKey", xmlMetadata.urlKeyForDataSourceID(getDataFeedID())));
        root.addAttribute(new Attribute("dateCreated", df.format(dateCreated)));
        root.addAttribute(new Attribute("dateUpdated", df.format(dateUpdated)));
        root.addAttribute(new Attribute("publiclyVisible", String.valueOf(isPubliclyVisible())));
        root.addAttribute(new Attribute("accountVisible", String.valueOf(isAccountVisible())));
        root.addAttribute(new Attribute("exchangeVisible", String.valueOf(isMarketplaceVisible())));
        Element fields = new Element("fields");
        root.appendChild(fields);
        for (Map.Entry<String, AnalysisItem> entry : reportStructure.entrySet()) {
            Element field = entry.getValue().toXML(xmlMetadata);
            fields.appendChild(field);
            field.addAttribute(new Attribute("structureID", entry.getKey()));
        }
        Element filters = new Element("filters");
        root.appendChild(filters);
        for (FilterDefinition filterDefinition : getFilterDefinitions()) {
            filters.appendChild(filterDefinition.toXML(xmlMetadata));
        }
        Element additionalFields = new Element("additionalFields");
        root.appendChild(additionalFields);
        if (getAddedItems() != null) {
            for (AnalysisItem additionalField : getAddedItems()) {
                additionalFields.appendChild(additionalField.toXML(xmlMetadata));
            }
        }
        Element marmotScript = new Element("marmotScript");
        root.appendChild(marmotScript);
        marmotScript.appendChild(this.marmotScript != null ? this.marmotScript : "");
        Element reportRunMarmotScript = new Element("reportRunMarmotScript");
        root.appendChild(reportRunMarmotScript);
        reportRunMarmotScript.appendChild(this.reportRunMarmotScript != null ? this.reportRunMarmotScript : "");
        Element stateElement = analysisDefinitionState.toXML(xmlMetadata);
        root.appendChild(stateElement);
        Element joinOverridesElement = new Element("joinOverrides");
        if (joinOverrides != null) {
            for (JoinOverride joinOverride : joinOverrides) {
                joinOverridesElement.appendChild(joinOverride.toXML(xmlMetadata));
            }
        }
        Element descriptionElement = new Element("description");
        root.appendChild(descriptionElement);
        descriptionElement.appendChild(description);
        Element reportPropertiesElement = new Element("reportProperties");
        root.appendChild(reportPropertiesElement);
        for (ReportProperty reportProperty : properties) {
            Element propertyXML = reportProperty.toXML();
            if (propertyXML != null) {
                reportPropertiesElement.appendChild(propertyXML);
            }
        }
        return root.toXML();
    }

    private static AnalysisDefinitionState createReport(int reportType) {
        switch (reportType) {
            case WSAnalysisDefinition.LIST:
                return new ListDefinitionState();
            case WSAnalysisDefinition.CROSSTAB:
                return new CrosstabDefinitionState();
            case WSAnalysisDefinition.TREE:
                return new TreeDefinitionState();
            case WSAnalysisDefinition.SUMMARY:
                return new SummaryDefinitionState();
            case WSAnalysisDefinition.AREA:
            case WSAnalysisDefinition.BAR:
            case WSAnalysisDefinition.COLUMN:
            case WSAnalysisDefinition.LINE:
            case WSAnalysisDefinition.PIE:
            case WSAnalysisDefinition.BUBBLE:
            case WSAnalysisDefinition.PLOT:
            case WSAnalysisDefinition.STACKED_BAR:
            case WSAnalysisDefinition.STACKED_COLUMN:
                return new ChartDefinitionState();
            case WSAnalysisDefinition.GAUGE:
                return new GaugeDefinitionState();
            case WSAnalysisDefinition.TREE_MAP:
                return new TreeMapDefinitionState();
            case WSAnalysisDefinition.TREND:
                return new TrendDefinitionState();
            case WSAnalysisDefinition.TREND_GRID:
                return new TrendGridDefinitionState();
            case WSAnalysisDefinition.VERTICAL_LIST:
                return new VerticalListDefinitionState();
            case WSAnalysisDefinition.HEATMAP:
                return new HeatMapDefinitionState();
            case WSAnalysisDefinition.DIAGRAM:
                return new DiagramDefinitionState();
            case WSAnalysisDefinition.FORM:
                return new FormDefinitionState();
        }

        return null;
    }
}
