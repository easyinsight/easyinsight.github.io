/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/18/11
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemEditEvent;
import com.easyinsight.analysis.DerivedAnalysisDateDimension;
import com.easyinsight.analysis.NamedKey;
import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.filtering.FilterDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.ReportPropertiesIntention")]
public class ReportPropertiesIntention extends Intention {

    public var intentionType:int;



    public function ReportPropertiesIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        if (intentionType == IntentionSuggestion.FULL_JOINS) {
            suggestionMetadata.report.fullJoins = true;
        } else if (intentionType == IntentionSuggestion.SUMMARY_ROW) {
            ListDefinition(suggestionMetadata.report).summaryTotal = true;
        } else if (intentionType == IntentionSuggestion.CONFIGURE_TRENDING) {
            var o:Object = suggestionMetadata.report.newFilters(suggestionMetadata.transformContainer.getFilterDefinitions());
            if (o != null) {
                var newFilters:ArrayCollection = o["filters"];
                for each (var filterDefinition:FilterDefinition in newFilters) {
                    suggestionMetadata.transformContainer.addFilterDefinition(filterDefinition);
                }
                var newFields:ArrayCollection = o["fields"];
                for each (var item:AnalysisItem in newFields) {
                    suggestionMetadata.reportEditor.onAnalysisItemEdit(new AnalysisItemEditEvent(item, null));
                }
            }
        } else if (intentionType == IntentionSuggestion.TURN_OFF_AGGREGATE_QUERY) {
            suggestionMetadata.report.aggregateQueryIfPossible = false;
        } else if (intentionType == IntentionSuggestion.CONFIGURE_DATE_COMPARISON) {
            suggestionMetadata.report.baseDate = "Comparison Date";
            var date:DerivedAnalysisDateDimension = new DerivedAnalysisDateDimension();
            date.dateOnlyField = true;
            date.concrete = false;
            date.applyBeforeAggregation = true;
            date.derivationCode = "nowdate()";
            var key:NamedKey = new NamedKey();
            key.name = "Comparison Date";
            date.key = key;
            suggestionMetadata.reportEditor.onAnalysisItemEdit(new AnalysisItemEditEvent(date, null));
        }
    }
}
}
