package com.easyinsight.analysis;

import com.easyinsight.scrubbing.DataScrub;
import com.easyinsight.security.SecurityUtil;

import java.util.*;

import org.hibernate.annotations.MapKey;

import javax.persistence.*;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 11:40:41 AM
 */
@Entity
@Table(name="analysis")
public class AnalysisDefinition implements Cloneable {
    @Column(name="title")
    private String title;
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="analysis_id")
    private Long analysisID;
    @Column(name="data_feed_id")
    private long dataFeedID;

    @Column(name="root_definition")
    private boolean rootDefinition;

    @Column(name="report_type")
    private int reportType;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="analysis_to_filter_join",
        joinColumns = @JoinColumn(name="analysis_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="filter_id", nullable = false))
    private List<PersistableFilterDefinition> filterDefinitions;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="additional_items",
        joinColumns = @JoinColumn(name="analysis_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="analysis_item_id", nullable = false))
    private List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="analysis_id", nullable = false)
    private List<UserToAnalysisBinding> userBindings = new ArrayList<UserToAnalysisBinding>();

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="analysis_to_data_scrub",
        joinColumns = @JoinColumn(name="analysis_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="data_scrub_id", nullable = false))
    private List<DataScrub> dataScrubs = new ArrayList<DataScrub>();

    @OneToMany(cascade= CascadeType.ALL)
    @JoinTable(name="analysis_to_tag",
        joinColumns = @JoinColumn(name="analysis_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="analysis_tags_id", nullable = false))
    private List<Tag> tags = new ArrayList<Tag>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="report_state_id")
    private AnalysisDefinitionState analysisDefinitionState;

    @Column(name="policy")
    private int analysisPolicy;

    @Column(name="genre")
    private String genre;

    @Column(name="create_date")
    private Date dateCreated;

    @Column(name="update_date")
    private Date dateUpdated;

    @Column(name="views")
    private int viewCount;

    @Column(name="rating_count")
    private int ratingCount;

    @Column(name="rating_average")
    private double ratingAverage;

    @Column(name="marketplace_visible")
    private boolean marketplaceVisible;

    @Column(name="publicly_visible")
    private boolean publiclyVisible;

    @Column(name="feed_visibility")
    private boolean visibleAtFeedLevel;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKey(columns = @Column(name = "structure_key"))
    @JoinTable(name = "report_structure",
                joinColumns = @JoinColumn(name = "analysis_id"),
                inverseJoinColumns = @JoinColumn(name = "analysis_item_id"))
    private Map<String, AnalysisItem> reportStructure;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    public List<DataScrub> getDataScrubs() {
        return dataScrubs;
    }

    public void setDataScrubs(List<DataScrub> dataScrubs) {
        this.dataScrubs = dataScrubs;
    }

    public List<UserToAnalysisBinding> getUserBindings() {
        return userBindings;
    }

    public void setUserBindings(List<UserToAnalysisBinding> userBindings) {
        this.userBindings = userBindings;
    }

    public List<PersistableFilterDefinition> getFilterDefinitions() {
        return filterDefinitions;
    }

    public void setFilterDefinitions(List<PersistableFilterDefinition> filterDefinitions) {
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

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        AnalysisDefinition analysisDefinition = (AnalysisDefinition) super.clone();
        analysisDefinition.setAnalysisID(null);
        List<PersistableFilterDefinition> filterDefinitions = new ArrayList<PersistableFilterDefinition>();
        for (PersistableFilterDefinition persistableFilterDefinition : this.filterDefinitions) {
            persistableFilterDefinition.setFilterId(0);
            filterDefinitions.add(persistableFilterDefinition);
        }
        analysisDefinition.setFilterDefinitions(filterDefinitions);
        List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem analysisItem : addedItems) {
            addedItems.add(analysisItem.clone());
        }
        Map<String, AnalysisItem> clonedStructure = new HashMap<String, AnalysisItem>(getReportStructure());
        for (Map.Entry<String, AnalysisItem> entry : clonedStructure.entrySet()) {
            clonedStructure.put(entry.getKey(), entry.getValue().clone());
        }
        analysisDefinition.setReportStructure(clonedStructure);
        analysisDefinition.setAddedItems(addedItems);
        analysisDefinition.setUserBindings(new ArrayList<UserToAnalysisBinding>());
        List<Tag> clonedTags = new ArrayList<Tag>();
        for (Tag tag : tags) {
            clonedTags.add(tag);
        }
        analysisDefinition.setTags(clonedTags);
        List<DataScrub> dataScrubs = new ArrayList<DataScrub>();
        for (DataScrub dataScrub : this.dataScrubs) {
            dataScrub.setDataScrubID(0);
            dataScrubs.add(dataScrub);
        }
        analysisDefinition.setDataScrubs(dataScrubs);
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
        analysisDefinition = analysisDefinitionState.createWSDefinition();
        analysisDefinition.setReportType(reportType);        
        analysisDefinition.setAnalysisID(analysisID);
        analysisDefinition.setDataFeedID(dataFeedID);
        if (getAddedItems() != null) {
            analysisDefinition.setAddedItems(new ArrayList<AnalysisItem>(getAddedItems()));
        }
        analysisDefinition.setName(title);
        analysisDefinition.setFilterDefinitions(FilterDefinitionConverter.fromPersistableFilters(filterDefinitions));
        analysisDefinition.setPolicy(analysisPolicy);
        analysisDefinition.setRootDefinition(rootDefinition);
        for (AnalysisItem analysisItem : reportStructure.values()) {
            analysisItem.afterLoad();
            /*if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
            }*/
        }
        analysisDefinition.populateFromReportStructure(reportStructure);
        List<DataScrub> newScrubs = new ArrayList<DataScrub>();
        for (DataScrub dataScrub : dataScrubs) {
            dataScrub.hateHibernate();
            newScrubs.add(dataScrub);
        }
        analysisDefinition.setCanSaveDirectly(isOwner(SecurityUtil.getUserID(false)));
        analysisDefinition.setDataScrubs(newScrubs);
        analysisDefinition.setTagCloud(new ArrayList<Tag>(getTags()));
        analysisDefinition.setMarketplaceVisible(marketplaceVisible);
        analysisDefinition.setPubliclyVisible(publiclyVisible);
        analysisDefinition.setVisibleAtFeedLevel(visibleAtFeedLevel);
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
}
