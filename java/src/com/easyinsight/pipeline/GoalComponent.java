package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 5/23/13
 * Time: 10:16 AM
 */
public class GoalComponent implements IComponent {

    private AnalysisItem goal;
    private Value goalValue;

    public GoalComponent(AnalysisItem goal) {
        this.goal = goal;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        WSListDefinition goalReport = new WSListDefinition();
        goalReport.setFilterDefinitions(new ArrayList<FilterDefinition>());
        goalReport.setColumns(Arrays.asList(goal));
        goalReport.setDataFeedID(pipelineData.getReport().getDataFeedID());
        DataSet goalSet = DataService.listDataSet(goalReport, new InsightRequestMetadata(), pipelineData.getConn());
        if (goalSet.getRows().size() > 0) {
            IRow row = goalSet.getRows().get(0);
            goalValue = row.getValue(goal);
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
        listDataResults.getAdditionalProperties().put("goal" + goal.qualifiedName(), goalValue);
    }
}
