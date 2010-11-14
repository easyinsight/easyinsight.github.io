package com.easyinsight.kpi;

import com.easyinsight.analysis.*;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.goals.GoalTreeDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Jan 18, 2010
 * Time: 2:38:30 PM
 */
public class KPI implements Cloneable {

    public static final int GOOD = 2;
    public static final int BAD = 1;

    private long kpiID;

    private long coreFeedID;
    private String coreFeedName;
    private String coreFeedUrlKey;
    private AnalysisMeasure analysisMeasure;
    private AnalysisDateDimension dateDimension;
    private List<FilterDefinition> filters = new ArrayList<FilterDefinition>();

    private List<Tag> tags = new ArrayList<Tag>();
    
    private boolean goalDefined;

    private int dayWindow;
    private double threshold;
    
    private List<FilterDefinition> problemConditions = new ArrayList<FilterDefinition>();
    private int highIsGood;
    private double goalValue;

    private String name;
    private String description;
    private String iconImage;

    private boolean connectionVisible;

    private boolean temporary;

    private long connectionID;

    private KPIOutcome kpiOutcome;

    private List<InsightDescriptor> reports = new ArrayList<InsightDescriptor>();
    private List<GoalTreeDescriptor> kpiTrees = new ArrayList<GoalTreeDescriptor>();

    private List<KPIUser> kpiUsers = new ArrayList<KPIUser>();

    public List<KPIUser> getKpiUsers() {
        return kpiUsers;
    }

    public void setKpiUsers(List<KPIUser> kpiUsers) {
        this.kpiUsers = kpiUsers;
    }

    public long getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(long connectionID) {
        this.connectionID = connectionID;
    }

    public List<InsightDescriptor> getReports() {
        return reports;
    }

    public void setReports(List<InsightDescriptor> reports) {
        this.reports = reports;
    }

    public boolean isGoalDefined() {
        return goalDefined;
    }

    public void setGoalDefined(boolean goalDefined) {
        this.goalDefined = goalDefined;
    }

    public AnalysisDateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(AnalysisDateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    public List<FilterDefinition> getProblemConditions() {
        return problemConditions;
    }

    public void setProblemConditions(List<FilterDefinition> problemConditions) {
        this.problemConditions = problemConditions;
    }

    public int getDayWindow() {
        return dayWindow;
    }

    public void setDayWindow(int dayWindow) {
        this.dayWindow = dayWindow;
    }

    public List<GoalTreeDescriptor> getKpiTrees() {
        return kpiTrees;
    }

    public void setKpiTrees(List<GoalTreeDescriptor> kpiTrees) {
        this.kpiTrees = kpiTrees;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public int getHighIsGood() {
        return highIsGood;
    }

    public void setHighIsGood(int highIsGood) {
        this.highIsGood = highIsGood;
    }

    public double getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(double goalValue) {
        this.goalValue = goalValue;
    }

    public boolean isConnectionVisible() {
        return connectionVisible;
    }

    public void setConnectionVisible(boolean connectionVisible) {
        this.connectionVisible = connectionVisible;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public KPI clone() {
        try {
            KPI clonedKPI = (KPI) super.clone();
            clonedKPI.setKpiID(0);
            clonedKPI.setKpiOutcome(null);
            List<FilterDefinition> copyFilters = new ArrayList<FilterDefinition>();
            for (FilterDefinition filter : filters) {
                //filter.beforeSave(session);
                FilterDefinition clonedFilter = filter.clone();
                clonedFilter.setField(clonedFilter.getField().clone());
                copyFilters.add(clonedFilter);
            }
            clonedKPI.setFilters(copyFilters);
            clonedKPI.setAnalysisMeasure((AnalysisMeasure) getAnalysisMeasure().clone());
            return clonedKPI;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        for (FilterDefinition filter : filters) {
            filter.updateIDs(replacementMap);
        }
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public KPIOutcome getKpiOutcome() {
        return kpiOutcome;
    }

    public void setKpiOutcome(KPIOutcome kpiOutcome) {
        this.kpiOutcome = kpiOutcome;
    }

    public long getKpiID() {
        return kpiID;
    }

    public void setKpiID(long kpiID) {
        this.kpiID = kpiID;
    }

    public long getCoreFeedID() {
        return coreFeedID;
    }

    public void setCoreFeedID(long coreFeedID) {
        this.coreFeedID = coreFeedID;
    }

    public String getCoreFeedName() {
        return coreFeedName;
    }

    public void setCoreFeedName(String coreFeedName) {
        this.coreFeedName = coreFeedName;
    }

    public AnalysisMeasure getAnalysisMeasure() {
        return analysisMeasure;
    }

    public void setAnalysisMeasure(AnalysisMeasure analysisMeasure) {
        this.analysisMeasure = analysisMeasure;
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

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String createIconText() {
        if(getKpiOutcome().isProblemEvaluated()) {
            return "bullet_square_glass_red.png";
        } else if (getProblemConditions().size() > 0) {
            return "bullet_ball_green.png";
        }
        switch(getKpiOutcome().getOutcomeState()) {
            case KPIOutcome.EXCEEDING_GOAL:
            case KPIOutcome.POSITIVE:
                if (getKpiOutcome().getDirection() == KPIOutcome.UP_DIRECTION) {
                    return "arrow2_up_green.png";
                } else if (getKpiOutcome().getDirection() == KPIOutcome.DOWN_DIRECTION) {
                    return "arrow2_down_green.png";
                } else {
                    return "bullet_ball_green.png";
                }
            case KPIOutcome.NEGATIVE:
                if (getKpiOutcome().getDirection() == KPIOutcome.UP_DIRECTION) {
                    return "arrow2_up_red.png";
                } else if (getKpiOutcome().getDirection() == KPIOutcome.DOWN_DIRECTION) {
                    return "arrow2_down_red.png";
                } else {
                    return "bullet_square_glass_red.png";
                }
            case KPIOutcome.NEUTRAL:
                if (getKpiOutcome().getDirection() == KPIOutcome.UP_DIRECTION) {
                    return "arrow2_up_blue.png";
                } else if (getKpiOutcome().getDirection() == KPIOutcome.DOWN_DIRECTION) {
                    return "arrow2_down_blue.png";
                } else {
                    return "bullet_ball_blue.png";
                }
            case KPIOutcome.NO_DATA:
            default:
                return "bullet_square_grey.png";
        }
    }

    public String getCoreFeedUrlKey() {
        return coreFeedUrlKey;
    }

    public void setCoreFeedUrlKey(String coreFeedUrlKey) {
        this.coreFeedUrlKey = coreFeedUrlKey;
    }
}
