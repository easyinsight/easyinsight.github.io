package com.easyinsight.analysis;

import com.easyinsight.scrubbing.DataScrub;
import com.easyinsight.AnalysisItem;
import com.easyinsight.AnalysisItemTypes;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 11:40:41 AM
 */
@Entity
@Table(name="analysis")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class AnalysisDefinition implements Cloneable {
    @Column(name="title")
    private String title;
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="analysis_id")
    private Long analysisID;
    @Column(name="data_feed_id")
    private long dataFeedID;

    @Column(name="root_definition")
    private boolean rootDefinition;
    
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
    @JoinTable(name="analysis_to_hierarchy_join",
        joinColumns = @JoinColumn(name="analysis_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="analysis_item_id", nullable = false))
    private List<AnalysisItem> hierarchies = new ArrayList<AnalysisItem>();
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="analysis_id", nullable = false)
    private List<UserToAnalysisBinding> userBindings = new ArrayList<UserToAnalysisBinding>();

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="analysis_id", nullable = false)
    private List<DataScrub> dataScrubs = new ArrayList<DataScrub>();

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="analysis_to_tag",
        joinColumns = @JoinColumn(name="analysis_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="analysis_tags_id", nullable = false))
    private List<Tag> tags = new ArrayList<Tag>();

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

    public List<AnalysisItem> getHierarchies() {
        return hierarchies;
    }

    public void setHierarchies(List<AnalysisItem> hierarchies) {
        this.hierarchies = hierarchies;
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
            persistableFilterDefinition.setFilterId(null);
            filterDefinitions.add(persistableFilterDefinition);
        }
        analysisDefinition.setFilterDefinitions(filterDefinitions);
        List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem analysisItem : addedItems) {
            addedItems.add(analysisItem.clone());
        }
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

    public WSAnalysisDefinition createBlazeDefinition() {
        WSAnalysisDefinition analysisDefinition = createWSDefinition();
        analysisDefinition.setAnalysisID(analysisID);
        analysisDefinition.setDataFeedID(dataFeedID);
        if (getAddedItems() != null) {
            analysisDefinition.setAddedItems(new ArrayList<AnalysisItem>(getAddedItems()));
        }
        analysisDefinition.setName(title);
        analysisDefinition.setFilterDefinitions(FilterDefinitionConverter.fromPersistableFilters(filterDefinitions));
        analysisDefinition.setPolicy(analysisPolicy);
        analysisDefinition.setRootDefinition(rootDefinition);
        List<DataScrub> newScrubs = new ArrayList<DataScrub>();
        for (DataScrub dataScrub : dataScrubs) {
            dataScrub.hateHibernate();
            newScrubs.add(dataScrub);
        }
        if (getHierarchies() != null) {
            List<AnalysisItem> hierarchies = new ArrayList<AnalysisItem>();
            for (AnalysisItem analysisItem : getHierarchies()) {
                AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
                hierarchies.add(analysisHierarchyItem);
            }
            analysisDefinition.setHierarchies(hierarchies);
        }
        analysisDefinition.setDataScrubs(newScrubs);
        analysisDefinition.setTagCloud(new ArrayList<Tag>(getTags()));
        analysisDefinition.setMarketplaceVisible(marketplaceVisible);
        analysisDefinition.setPubliclyVisible(publiclyVisible);
        analysisDefinition.setVisibleAtFeedLevel(visibleAtFeedLevel);
        return analysisDefinition;
    }

    protected void processItems(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
            }
        }
    }

    protected abstract WSAnalysisDefinition createWSDefinition();
}
