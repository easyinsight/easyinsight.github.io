/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/23/13
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.SimpleReportEditor;
import com.easyinsight.analysis.AnalysisItemCopyEvent;
import com.easyinsight.analysis.AnalysisItemWrapper;
import com.easyinsight.util.PopUpUtil;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.core.Application;
import mx.managers.PopUpManager;

public class SuggestionFactory {

    private var reportEditor:SimpleReportEditor;

    public function SuggestionFactory(reportEditor:SimpleReportEditor) {
        this.reportEditor = reportEditor;
    }

    public function argh(type:int, analysisItems:ArrayCollection, dataSourceID:int):void {
        /*var fields:ArrayCollection = new ArrayCollection();
        for each (var wrapper:AnalysisItemWrapper in analysisItems) {
            fields.addItem(wrapper.analysisItem);
        }*/
        if (type == IntentionSuggestion.FILTERED_FIELD) {
            var generateFilteredFieldWindow:GenerateFilteredFieldWindow = new GenerateFilteredFieldWindow();
            generateFilteredFieldWindow.analysisItems = analysisItems;
            generateFilteredFieldWindow.dataSourceID = dataSourceID;
            generateFilteredFieldWindow.addEventListener(AnalysisItemCopyEvent.ITEM_COPY, reportEditor.onCopy, false, 0, true);
            PopUpManager.addPopUp(generateFilteredFieldWindow, Application(Application.application), true);
            PopUpUtil.centerPopUp(generateFilteredFieldWindow);
        } else if (type == IntentionSuggestion.DISTINCT_COUNT) {
            var distinctCountWindow:GenerateCountDistinctWindow = new GenerateCountDistinctWindow();
            distinctCountWindow.analysisItems = analysisItems;
            distinctCountWindow.dataSourceID = dataSourceID;
            distinctCountWindow.addEventListener(AnalysisItemCopyEvent.ITEM_COPY, reportEditor.onCopy, false, 0, true);
            PopUpManager.addPopUp(distinctCountWindow, Application(Application.application), true);
            PopUpUtil.centerPopUp(distinctCountWindow);
        }
    }
}
}
