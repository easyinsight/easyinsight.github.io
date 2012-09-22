package com.easyinsight.analysis;

import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.intention.NewHierarchyIntention;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.ListSummaryComponent;

import java.sql.SQLException;
import java.util.*;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:24 PM
 */
public class WSSummaryDefinition extends WSTreeDefinition {

    private long summaryDefinitionID;

    public long getSummaryDefinitionID() {
        return summaryDefinitionID;
    }

    public void setSummaryDefinitionID(long summaryDefinitionID) {
        this.summaryDefinitionID = summaryDefinitionID;
    }

    public String getDataFeedType() {
        return AnalysisTypes.SUMMARY;
    }

    private AnalysisItem hierarchy;
    private List<AnalysisItem> items;

    @Override
    public List<IntentionSuggestion> suggestIntentions(WSAnalysisDefinition report) {
        List<IntentionSuggestion> suggestions = super.suggestIntentions(report);
        WSSummaryDefinition tree = (WSSummaryDefinition) report;
        if (tree.getHierarchy() == null) {
            suggestions.add(new IntentionSuggestion("Create a Hierarchy",
                    "This action will create a new hierarchy of fields.",
                    IntentionSuggestion.SCOPE_REPORT, IntentionSuggestion.NEW_HIERARCHY, IntentionSuggestion.OTHER));

        }
        return suggestions;
    }

    public List<Intention> createIntentions(List<AnalysisItem> fields, int type) throws SQLException {
        List<Intention> intentions = new ArrayList<Intention>();
        if (type == IntentionSuggestion.NEW_HIERARCHY) {
            intentions.add(new NewHierarchyIntention());
        } else {
            throw new RuntimeException("Unrecognized intention type");
        }
        return intentions;
    }
}