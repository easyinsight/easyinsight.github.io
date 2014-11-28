package com.easyinsight.analysis {
public class TryAgainDerivedGroupingWindow extends TryAgainCalcWindow {

    private var derivedGrouping:DerivedAnalysisDimension;

    public function TryAgainDerivedGroupingWindow() {
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

    override protected function actualSave():AnalysisItem {
        if (derivedGrouping == null) {
            derivedGrouping = new DerivedAnalysisDimension();
            derivedGrouping.concrete = false;
            var namedKey:NamedKey = new NamedKey();
            namedKey.name = nameInput.text;
            derivedGrouping.key = namedKey;
            derivedGrouping.displayName = nameInput.text;
            derivedGrouping.unqualifiedDisplayName = nameInput.text;
        } else {
            if (derivedGrouping.key is NamedKey) {
                NamedKey(derivedGrouping.key).name = nameInput.text;
            }
            derivedGrouping.displayName = nameInput.text;
            derivedGrouping.unqualifiedDisplayName = nameInput.text;
        }

        derivedGrouping.derivationCode = calculationInput.text;
        return derivedGrouping;
    }
}
}