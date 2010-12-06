package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisTypes;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:51:51 PM
 */
public class WSGanttChartDefinition extends WSAnalysisDefinition {

    private AnalysisItem startTime;
    private AnalysisItem endTime;
    private AnalysisItem grouping;
    private AnalysisItem taskGrouping;
    private long ganttDefinitionID;

    public AnalysisItem getTaskGrouping() {
        return taskGrouping;
    }

    public void setTaskGrouping(AnalysisItem taskGrouping) {
        this.taskGrouping = taskGrouping;
    }

    public long getGanttDefinitionID() {
        return ganttDefinitionID;
    }

    public void setGanttDefinitionID(long ganttDefinitionID) {
        this.ganttDefinitionID = ganttDefinitionID;
    }

    public AnalysisItem getStartTime() {
        return startTime;
    }

    public void setStartTime(AnalysisItem startTime) {
        this.startTime = startTime;
    }

    public AnalysisItem getEndTime() {
        return endTime;
    }

    public void setEndTime(AnalysisItem endTime) {
        this.endTime = endTime;
    }

    public AnalysisItem getGrouping() {
        return grouping;
    }

    public void setGrouping(AnalysisItem grouping) {
        this.grouping = grouping;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.GANTT;
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> items = new HashSet<AnalysisItem>();
        items.add(startTime);
        items.add(endTime);
        items.add(grouping);
        items.add(taskGrouping);
        return items;
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("startTime", Arrays.asList(startTime), structure);
        addItems("endTime", Arrays.asList(endTime), structure);
        addItems("grouping", Arrays.asList(grouping), structure);
        addItems("taskGrouping", Arrays.asList(taskGrouping), structure);
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        startTime = firstItem("startTime", structure);
        endTime = firstItem("endTime", structure);
        grouping = firstItem("grouping", structure);
        taskGrouping = firstItem("taskGrouping", structure);
    }

}
