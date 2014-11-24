package com.easyinsight.analysis {
public class DerivedDateConfigWindow extends CalculationConfigWindow {

    private var derivedGrouping:DerivedAnalysisDateDimension;

    public function DerivedDateConfigWindow() {
        super();
    }

    override public function set analysisItem(analysisItem:AnalysisItem):void {
        derivedGrouping = analysisItem as DerivedAnalysisDateDimension;
    }

    override protected function commitProperties():void {
        super.commitProperties();
        showMeasures = false;
        if (derivedGrouping != null) {
            calcText = derivedGrouping.derivationCode;
            calcName = derivedGrouping.unqualifiedDisplay;
            dateTime = !derivedGrouping.dateOnlyField;
            dateLevel = derivedGrouping.dateLevel;
            dateApplyBeforeAggregation = derivedGrouping.applyBeforeAggregation;
        } else {
            dateLevel = AnalysisItemTypes.DAY_LEVEL;
        }
        detailIndex = 2;
        detailItemLabel = "Date Level:";
        example1 = "namedbracketdate([Deal Description], \"Close Date\", \"yyyy-MM-dd\")";
        example1Explanation = "";
        example2 = "now() - [Due On]";
        example2Explanation = "";
    }

    override protected function get calculationItem():AnalysisItem {
        return derivedGrouping;
    }

    override public function save(dataSourceID:int):void {
        derivedGrouping.dateLevel = int(dateBox.selectedItem.data);
        derivedGrouping.dateOnlyField = !dateTimeCheckbox.selected;
        derivedGrouping.applyBeforeAggregation = dateApplyBeforeAggregationCheckbox.selected;
    }
}
}