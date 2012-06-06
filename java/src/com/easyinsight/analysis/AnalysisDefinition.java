package com.easyinsight.analysis;

import com.easyinsight.analysis.gauge.GaugeDefinitionState;
import com.easyinsight.core.*;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.pipeline.CleanupComponent;
import com.easyinsight.security.SecurityUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Column(name = "update_date")
    private Date dateUpdated;

    @Column(name = "views")
    private int viewCount;

    @Column(name = "rating_count")
    private int ratingCount;

    @Column(name = "solution_visible")
    private boolean solutionVisible;

    @Column(name = "rating_average")
    private double ratingAverage;

    @Column(name = "recommended_exchange")
    private boolean recommendedExchange;

    @Column(name = "marketplace_visible")
    private boolean marketplaceVisible;

    @Column(name = "publicly_visible")
    private boolean publiclyVisible;

    @Column(name = "feed_visibility")
    private boolean visibleAtFeedLevel;

    @Column(name="auto_setup_delivery")
    private boolean autoSetupDelivery;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "description")
    private String description;

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

    public boolean isVisibleAtFeedLevel() {
        return visibleAtFeedLevel;
    }

    public void setVisibleAtFeedLevel(boolean visibleAtFeedLevel) {
        this.visibleAtFeedLevel = visibleAtFeedLevel;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(double ratingAverage) {
        this.ratingAverage = ratingAverage;
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

    public AnalysisDefinition clone(FeedDefinition target, List<AnalysisItem> allFields, boolean changingDataSource) throws CloneNotSupportedException {
        AnalysisDefinition analysisDefinition = (AnalysisDefinition) super.clone();

        analysisDefinition.setAnalysisDefinitionState(analysisDefinitionState.clone(allFields));
        analysisDefinition.setUrlKey(null);
        analysisDefinition.setAnalysisID(null);
        //Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
        ReplacementMap replacementMap = new ReplacementMap();

        allFields = new ArrayList<AnalysisItem>(allFields);
        if (analysisDefinition.getAddedItems() != null) {
            allFields.addAll(analysisDefinition.getAddedItems());
        }

        List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();

        Collection<AnalysisItem> reportItems = getReportStructure().values();

        AnalysisItemRetrievalStructure structure = new AnalysisItemRetrievalStructure();
        structure.setBaseReport(this);

        if (getAddedItems() != null) {

            for (AnalysisItem analysisItem : getAddedItems()) {
                AnalysisItem clonedItem = replacementMap.addField(analysisItem, changingDataSource);
                addedItems.add(clonedItem);
                List<AnalysisItem> items = analysisItem.getAnalysisItems(allFields, reportItems, true, true, CleanupComponent.AGGREGATE_CALCULATIONS, new HashSet<AnalysisItem>(), structure);
                for (AnalysisItem item : items) {
                    replacementMap.addField(item, changingDataSource);
                }
            }
        }

        allFields = new ArrayList<AnalysisItem>(allFields);
        allFields.addAll(addedItems);


        reportItems.remove(null);
        for (AnalysisItem baseItem : reportItems) {
            replacementMap.addField(baseItem, changingDataSource);
            List<AnalysisItem> items = baseItem.getAnalysisItems(allFields, reportItems, true, true, CleanupComponent.AGGREGATE_CALCULATIONS, new HashSet<AnalysisItem>(), structure);
            for (AnalysisItem item : items) {
                replacementMap.addField(item, changingDataSource);
            }
        }
        List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();
        if (this.filterDefinitions != null) {
            for (FilterDefinition persistableFilterDefinition : this.filterDefinitions) {
                filterDefinitions.add(persistableFilterDefinition.clone());
                List<AnalysisItem> filterItems = persistableFilterDefinition.getAnalysisItems(allFields, reportItems, true, true, CleanupComponent.AGGREGATE_CALCULATIONS, new HashSet<AnalysisItem>(), structure);
                for (AnalysisItem item : filterItems) {
                    replacementMap.addField(item, changingDataSource);
                }
            }
        }
        analysisDefinition.setFilterDefinitions(filterDefinitions);

        if (getJoinOverrides() != null) {
            List<JoinOverride> clones = new ArrayList<JoinOverride>();
            for (JoinOverride joinOverride : joinOverrides) {
                replacementMap.addField(joinOverride.getSourceItem(), changingDataSource);
                replacementMap.addField(joinOverride.getTargetItem(), changingDataSource);
                clones.add(joinOverride.clone());
            }
            analysisDefinition.setJoinOverrides(clones);
        }

        /*
        for (Map.Entry<String, AnalysisItem> entry : clonedStructure.entrySet()) {
            replacementMap.addField(entry.getValue(), changingDataSource);
            List<AnalysisItem> items = entry.getValue().getAnalysisItems(allFields, reportItems, true, true, CleanupComponent.AGGREGATE_CALCULATIONS);
            for (AnalysisItem item : items) {
                replacementMap.addField(item, changingDataSource);
            }
        }*/

        Map<String, AnalysisItem> clonedStructure = new HashMap<String, AnalysisItem>(getReportStructure());

        if (target != null) {
            for (AnalysisItem analysisItem : replacementMap.getFields()) {
                Key key = null;

                AnalysisItem dataSourceItem = target.findAnalysisItemByDisplayName(analysisItem.toDisplay());
                if (dataSourceItem != null) {
                    key = dataSourceItem.getKey();
                } else {
                    if (analysisItem.getOriginalDisplayName() != null) {
                        dataSourceItem = target.findAnalysisItemByDisplayName(analysisItem.getOriginalDisplayName());
                    }
                    if (dataSourceItem != null) {
                        key = dataSourceItem.getKey();
                    } else {
                        dataSourceItem = target.findAnalysisItem(analysisItem.getKey().toKeyString());
                        if (dataSourceItem != null) {
                            key = dataSourceItem.getKey();
                        }
                    }
                }
                if (key != null) {
                    analysisItem.setKey(key);
                } else {
                    Key clonedKey = analysisItem.getKey().clone();
                    analysisItem.setKey(clonedKey);
                }
            }
        }
        for (AnalysisItem analysisItem : replacementMap.getFields()) {
            if (target != null) {
                target.updateLinks(analysisItem);
            }
            analysisItem.updateIDs(replacementMap);
        }
        for (Map.Entry<String, AnalysisItem> entry : getReportStructure().entrySet()) {
            clonedStructure.put(entry.getKey(), replacementMap.getField(entry.getValue()));
        }
        for (FilterDefinition filter : filterDefinitions) {
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
        analysisDefinition.getAnalysisDefinitionState().updateIDs(replacementMap);
        analysisDefinition.setReportStructure(clonedStructure);
        analysisDefinition.setAddedItems(addedItems);
        analysisDefinition.setUserBindings(new ArrayList<UserToAnalysisBinding>());
        List<ReportProperty> clonedProperties = new ArrayList<ReportProperty>();
        for (ReportProperty reportProperty : this.properties) {
            clonedProperties.add(reportProperty.clone());
        }
        analysisDefinition.setProperties(clonedProperties);
        analysisDefinition.setTemporaryReport(temporaryReport);
        return analysisDefinition;
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
        analysisDefinition.setCanSaveDirectly(isOwner(SecurityUtil.getUserID(false)));
        analysisDefinition.setAuthorName(getAuthorName());
        analysisDefinition.setDateCreated(getDateCreated());
        analysisDefinition.setFolder(getFolder());
        analysisDefinition.setDateUpdated(getDateUpdated());
        analysisDefinition.setUrlKey(urlKey);
        analysisDefinition.setMarketplaceVisible(marketplaceVisible);
        analysisDefinition.setPubliclyVisible(publiclyVisible);
        analysisDefinition.setVisibleAtFeedLevel(visibleAtFeedLevel);
        analysisDefinition.setSolutionVisible(solutionVisible);
        analysisDefinition.setAccountVisible(accountVisible);
        analysisDefinition.setMarmotScript(marmotScript);
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

    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap, Map<Long, Dashboard> dashboardReplacementMap) {
        analysisDefinitionState.updateReportIDs(reportReplacementMap);
        for (AnalysisItem analysisItem : reportStructure.values()) {
            for (Link link : analysisItem.getLinks()) {
                link.updateReportIDs(reportReplacementMap, dashboardReplacementMap);
            }
        }
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
        return drillIDs;
    }

    public static AnalysisDefinition fromXML(Element root, XMLImportMetadata xmlImportMetadata) {
        int reportType = Integer.parseInt(root.getAttribute("type").getValue());
        AnalysisDefinition report = new AnalysisDefinition();
        report.setTitle(root.getAttribute("name").getValue());
        report.setUrlKey(root.getAttribute("urlKey").getValue() + "2");
        String dataSourceURLKey = root.getAttribute("dataSourceUrlKey").getValue();
        FeedDefinition dataSource = xmlImportMetadata.dataSourceForURLKey(dataSourceURLKey);
        xmlImportMetadata.setDataSource(dataSource);
        report.setReportType(reportType);
        report.setDataFeedID(dataSource.getDataFeedID());
        report.setAnalysisDefinitionState(createReport(reportType));
        report.setAccountVisible(Boolean.parseBoolean(root.getAttribute("accountVisible").getValue()));
        report.setPubliclyVisible(Boolean.parseBoolean(root.getAttribute("publiclyVisible").getValue()));
        report.setMarketplaceVisible(Boolean.parseBoolean(root.getAttribute("exchangeVisible").getValue()));
        Nodes fieldNodes = root.query("fields/analysisItem");
        Map<String, AnalysisItem> reportStructure = new HashMap<String, AnalysisItem>();
        for (int i = 0; i < fieldNodes.size(); i++) {
            Element fieldNode = (Element) fieldNodes.get(i);
            AnalysisItem analysisItem = AnalysisItem.fromXML(fieldNode, xmlImportMetadata);
            String structureID = fieldNode.getAttribute("structureID").getValue();
            reportStructure.put(structureID, analysisItem);
        }
        report.setMarmotScript(root.getAttribute("marmotScript").getValue());
        report.setDescription(root.getAttribute("description").getValue());
        Nodes filterNodes = root.query("filters/filter");
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (int i = 0; i < filterNodes.size(); i++) {
            Element filterNode = (Element) filterNodes.get(i);
            filters.add(FilterDefinition.fromXML(filterNode, xmlImportMetadata));
        }
        List<JoinOverride> joinOverrides = new ArrayList<JoinOverride>();
        Nodes joinNodes = root.query("");
        for (int i = 0; i < joinNodes.size(); i++) {

        }
        report.setFilterDefinitions(filters);
        report.setReportStructure(reportStructure);
        return report;
    }



    public String toXML(XMLMetadata xmlMetadata) {
        Element root = new Element("report");
        DateFormat df = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        root.addAttribute(new Attribute("type", String.valueOf(getReportType())));
        root.addAttribute(new Attribute("name", getTitle()));
        root.addAttribute(new Attribute("urlKey", getUrlKey()));
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
        for (AnalysisItem additionalField : getAddedItems()) {
            additionalFields.appendChild(additionalField.toXML(xmlMetadata));
        }
        Element marmotScript = new Element("marmotScript");
        root.appendChild(marmotScript);
        marmotScript.appendChild(this.marmotScript != null ? this.marmotScript : "");
        Element reportRunMarmotScript = new Element("reportRunMarmotScript");
        root.appendChild(reportRunMarmotScript);
        reportRunMarmotScript.appendChild(this.reportRunMarmotScript != null ? this.reportRunMarmotScript : "");
        Element stateElement = analysisDefinitionState.toXML(xmlMetadata);
        if (stateElement != null) {
            root.appendChild(stateElement);
        }
        Element joinOverridesElement = new Element("joinOverrides");
        for (JoinOverride joinOverride : joinOverrides) {
            joinOverridesElement.appendChild(joinOverride.toXML(xmlMetadata));
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
