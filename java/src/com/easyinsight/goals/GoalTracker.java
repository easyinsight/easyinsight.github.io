package com.easyinsight.goals;

import com.easyinsight.IDataService;
import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.analysis.AnalysisStorage;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.util.List;

/**
 * User: James Boe
 * Date: Oct 27, 2008
 * Time: 11:11:38 AM
 */
public class GoalTracker {

    private IDataService dataService;
    private AnalysisStorage analysisStorage;

    private void run(Integer interval) {
       /* List<GoalTreeNode> nodes = retrieveGoalsForInterval(interval);
        for (GoalTreeNode node : nodes) {
            WSAnalysisDefinition analysisDefinition = analysisStorage.getAnalysisDefinition(node.getCoreInsightID()).createBlazeDefinition();
            // need to add in time at this point...
            
            // retrieve the data set...
            dataService.list(analysisDefinition);
            node.getGoalCondition().
        }*/
    }
}
