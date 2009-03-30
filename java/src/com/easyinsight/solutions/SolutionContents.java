package com.easyinsight.solutions;

import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.goals.GoalTreeDescriptor;
import com.easyinsight.core.InsightDescriptor;

import java.util.List;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 11:22:19 PM
 */
public class SolutionContents {
    private List<FeedDescriptor> feedDescriptors;
    private List<GoalTreeDescriptor> goalTreeDescriptors;
    private List<InsightDescriptor> insightDescriptors;

    public List<FeedDescriptor> getFeedDescriptors() {
        return feedDescriptors;
    }

    public void setFeedDescriptors(List<FeedDescriptor> feedDescriptors) {
        this.feedDescriptors = feedDescriptors;
    }

    public List<GoalTreeDescriptor> getGoalTreeDescriptors() {
        return goalTreeDescriptors;
    }

    public void setGoalTreeDescriptors(List<GoalTreeDescriptor> goalTreeDescriptors) {
        this.goalTreeDescriptors = goalTreeDescriptors;
    }

    public List<InsightDescriptor> getInsightDescriptors() {
        return insightDescriptors;
    }

    public void setInsightDescriptors(List<InsightDescriptor> insightDescriptors) {
        this.insightDescriptors = insightDescriptors;
    }
}
