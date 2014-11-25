package com.easyinsight.analysis {
public class DerivedGroupingConfigWindow extends CalculationConfigWindow {

    private var derivedGrouping:DerivedAnalysisDimension;

    public function DerivedGroupingConfigWindow() {
        super();
    }

    override public function set analysisItem(analysisItem:AnalysisItem):void {
        derivedGrouping = analysisItem as DerivedAnalysisDimension;
    }

    override protected function commitProperties():void {
        super.commitProperties();
        showMeasures = false;
        if (derivedGrouping != null) {
            calcText = derivedGrouping.derivationCode;
            calcName = derivedGrouping.unqualifiedDisplay;
            wordWrap = derivedGrouping.wordWrap;
            groupingApplyBeforeAggregation = derivedGrouping.applyBeforeAggregation;
            html = derivedGrouping.html;
            if (derivedGrouping.sortItem != null) {
                sortDimension = derivedGrouping.sortItem.display;
            }
        }
        detailIndex = 3;
        example1 = "namedbracketvalue([Deal Description], \"Channel\")";
        example1Explanation = "";
        example2 = "[First Name] + [Last Name]";
        example2Explanation = "";
    }

    override protected function get calculationItem():AnalysisItem {
        return derivedGrouping;
    }

    override public function save(dataSourceID:int):void {
        var sortItem:AnalysisItem = sortComboBox.selectedItem as AnalysisItem;
        if (sortItem != null) {
            derivedGrouping.sortItem = sortItem;
        } else {
            derivedGrouping.sortItem = null;
        }

        derivedGrouping.html = htmlCheckbox.selected;
        derivedGrouping.applyBeforeAggregation = groupingApplyBeforeAggregationCheckbox.selected;
        derivedGrouping.wordWrap = wordWrapCheckbox.selected;
    }
}
}