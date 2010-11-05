package com.easyinsight.analysis;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.core.Key;

import java.util.*;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "analysis_to_tag",
            joinColumns = @JoinColumn(name = "analysis_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_tags_id", nullable = false))
    private List<Tag> tags = new ArrayList<Tag>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "report_state_id")
    private AnalysisDefinitionState analysisDefinitionState;

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

    @Column(name = "marketplace_visible")
    private boolean marketplaceVisible;

    @Column(name = "publicly_visible")
    private boolean publiclyVisible;

    @Column(name = "feed_visibility")
    private boolean visibleAtFeedLevel;

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

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public AnalysisDefinition clone(Map<Key, Key> keyMap, List<AnalysisItem> allFields) throws CloneNotSupportedException {
        AnalysisDefinition analysisDefinition = (AnalysisDefinition) super.clone();
        analysisDefinition.setAnalysisDefinitionState(analysisDefinitionState.clone(keyMap, allFields));
        analysisDefinition.setUrlKey(null);
        analysisDefinition.setAnalysisID(null);
        Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();

        List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();

        if (getAddedItems() != null) {
            for (AnalysisItem analysisItem : getAddedItems()) {
                AnalysisItem clonedItem;
                if (replacementMap.get(analysisItem.getAnalysisItemID()) == null) {
                    clonedItem = analysisItem.clone();
                    replacementMap.put(analysisItem.getAnalysisItemID(), clonedItem);
                } else {
                    clonedItem = replacementMap.get(analysisItem.getAnalysisItemID());
                }
                addedItems.add(clonedItem);
            }
        }

        allFields = new ArrayList<AnalysisItem>(allFields);
        allFields.addAll(addedItems);

        Collection<AnalysisItem> reportItems = createBlazeDefinition().getAllAnalysisItems();
        reportItems.remove(null);
        for (AnalysisItem baseItem : reportItems) {
            if (replacementMap.get(baseItem.getAnalysisItemID()) == null) {
                AnalysisItem clonedItem = baseItem.clone();
                replacementMap.put(baseItem.getAnalysisItemID(), clonedItem);
            }
            List<AnalysisItem> items = baseItem.getAnalysisItems(allFields, reportItems, true, true, false);
            for (AnalysisItem item : items) {
                if (replacementMap.get(item.getAnalysisItemID()) == null) {
                    AnalysisItem clonedItem = item.clone();
                    replacementMap.put(item.getAnalysisItemID(), clonedItem);
                }
            }
        }
        List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();
        if (this.filterDefinitions != null) {
            for (FilterDefinition persistableFilterDefinition : this.filterDefinitions) {
                filterDefinitions.add(persistableFilterDefinition.clone());
                List<AnalysisItem> filterItems = persistableFilterDefinition.getAnalysisItems(allFields, reportItems, true, true);
                for (AnalysisItem item : filterItems) {
                    if (replacementMap.get(item.getAnalysisItemID()) == null) {
                        AnalysisItem clonedItem = item.clone();
                        replacementMap.put(item.getAnalysisItemID(), clonedItem);
                    }
                }
            }
        }
        analysisDefinition.setFilterDefinitions(filterDefinitions);

        Map<String, AnalysisItem> clonedStructure = new HashMap<String, AnalysisItem>(getReportStructure());
        for (Map.Entry<String, AnalysisItem> entry : clonedStructure.entrySet()) {
            if (replacementMap.get(entry.getValue().getAnalysisItemID()) == null) {
                AnalysisItem clonedItem = entry.getValue().clone();
                replacementMap.put(entry.getValue().getAnalysisItemID(), clonedItem);
                clonedStructure.put(entry.getKey(), clonedItem);
                List<AnalysisItem> items = entry.getValue().getAnalysisItems(new ArrayList<AnalysisItem>(), new ArrayList<AnalysisItem>(), false, true, false);
                for (AnalysisItem item : items) {
                    if (replacementMap.get(item.getAnalysisItemID()) == null) {
                        AnalysisItem clonedChildItem = item.clone();
                        replacementMap.put(item.getAnalysisItemID(), clonedChildItem);
                    }
                }
            } else {
                clonedStructure.put(entry.getKey(), replacementMap.get(entry.getValue().getAnalysisItemID()));
            }
        }

        for (AnalysisItem analysisItem : replacementMap.values()) {
            if (keyMap != null) {
                Key key = keyMap.get(analysisItem.getKey());
                if (key != null) {
                    analysisItem.setKey(key);
                } else {
                    analysisItem.setKey(analysisItem.getKey().clone());
                }
            }
            analysisItem.updateIDs(replacementMap);
        }
        for (FilterDefinition filter : filterDefinitions) {
            filter.updateIDs(replacementMap);
        }
        analysisDefinition.getAnalysisDefinitionState().updateIDs(replacementMap, keyMap);
        analysisDefinition.setReportStructure(clonedStructure);
        analysisDefinition.setAddedItems(addedItems);
        analysisDefinition.setUserBindings(new ArrayList<UserToAnalysisBinding>());
        List<Tag> clonedTags = new ArrayList<Tag>();
        for (Tag tag : tags) {
            clonedTags.add(tag.clone());
        }
        analysisDefinition.setTags(clonedTags);
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
            analysisDefinition.setAddedItems(new ArrayList<AnalysisItem>(getAddedItems()));
            for (AnalysisItem analysisItem : analysisDefinition.getAddedItems()) {
                analysisItem.afterLoad();
            }
        }
        analysisDefinition.setName(title);
        analysisDefinition.setFilterDefinitions(filterDefinitions);
        analysisDefinition.setFilterDefinitions(FilterDefinitionConverter.fromPersistableFilters(filterDefinitions));
        analysisDefinition.setPolicy(analysisPolicy);
        analysisDefinition.setRootDefinition(rootDefinition);
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
        analysisDefinition.setDateUpdated(getDateUpdated());
        analysisDefinition.setUrlKey(urlKey);
        analysisDefinition.setTagCloud(new ArrayList<Tag>(getTags()));
        analysisDefinition.setMarketplaceVisible(marketplaceVisible);
        analysisDefinition.setPubliclyVisible(publiclyVisible);
        analysisDefinition.setVisibleAtFeedLevel(visibleAtFeedLevel);
        analysisDefinition.setSolutionVisible(solutionVisible);
        analysisDefinition.setAccountVisible(accountVisible);
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

    /*protected List<AnalysisItem> processItems(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
            }
        }
        return analysisItems;
    }*/

    public List<AnalysisDefinition> containedReports(Session session) {
        List<AnalysisDefinition> reports = new ArrayList<AnalysisDefinition>();
        reports.addAll(analysisDefinitionState.containedReports(session));
        return reports;
    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        analysisDefinitionState.updateReportIDs(reportReplacementMap);
        for (AnalysisItem analysisItem : reportStructure.values()) {
            for (Link link : analysisItem.getLinks()) {
                link.updateReportIDs(reportReplacementMap);    
            }
        }
    }

    public Set<Long> containedReportIDs() {
        Set<Long> drillIDs = new HashSet<Long>();
        for (AnalysisItem analysisItem : reportStructure.values()) {
            List<Link> links = analysisItem.getLinks();
            if (links != null) {
                for (Link link : links) {
                    if (link instanceof DrillThrough) {
                        DrillThrough drillThrough = (DrillThrough) link;
                        drillIDs.add(drillThrough.getReportID());
                    }
                }
            }
        }
        return drillIDs;
    }
}
