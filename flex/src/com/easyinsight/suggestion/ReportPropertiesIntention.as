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
import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.filtering.FilterDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.ReportPropertiesIntention")]
public class ReportPropertiesIntention extends Intention {

    public var fullJoins:Boolean;
    public var summaryRow:Boolean;
    public var trendSetup:Boolean;

    public function ReportPropertiesIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        if (fullJoins) {
            suggestionMetadata.report.fullJoins = true;
        }
        if (summaryRow) {
            ListDefinition(suggestionMetadata.report).summaryTotal = true;
        }
        if (trendSetup) {
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
        }
    }
}
}
