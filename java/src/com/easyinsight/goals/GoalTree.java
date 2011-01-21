package com.easyinsight.goals;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.datafeeds.FeedConsumer;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 2:44:25 PM
 */
public class GoalTree implements Cloneable {

    private GoalTreeNode rootNode;

    private String urlKey;

    private String name;

    private String description;

    private long dataSourceID;

    private List<FeedConsumer> administrators = new ArrayList<FeedConsumer>();

    private long goalTreeID;

    private List<GoalTreeDescriptor> subTreeParents = new ArrayList<GoalTreeDescriptor>();

    private List<FilterDefinition> filters = new ArrayList<FilterDefinition>();

    private String iconImage;

    private boolean accountVisible;

    private boolean exchangeVisible;

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
    }

    public boolean isExchangeVisible() {
        return exchangeVisible;
    }

    public void setExchangeVisible(boolean exchangeVisible) {
        this.exchangeVisible = exchangeVisible;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public List<GoalTreeDescriptor> getSubTreeParents() {
        return subTreeParents;
    }

    public void setSubTreeParents(List<GoalTreeDescriptor> subTreeParents) {
        this.subTreeParents = subTreeParents;
    }

    public List<FeedConsumer> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<FeedConsumer> administrators) {
        this.administrators = administrators;
    }

    public GoalTreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(GoalTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getGoalTreeID() {
        return goalTreeID;
    }

    public void setGoalTreeID(long goalTreeID) {
        this.goalTreeID = goalTreeID;
    }

    @Override
    public GoalTree clone() throws CloneNotSupportedException {
        GoalTree goalTree = (GoalTree) super.clone();
        goalTree.setRootNode(rootNode.clone());
        goalTree.setGoalTreeID(0);
        goalTree.setAdministrators(new ArrayList<FeedConsumer>());
        goalTree.setUrlKey(null);
        return goalTree;
    }
}
