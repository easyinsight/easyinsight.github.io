package com.easyinsight.analysis {
public class DerivedGroupingWindow extends CalculationWindow {

    private var derivedGrouping:DerivedAnalysisDimension;

    public function DerivedGroupingWindow() {
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
            calcName = derivedGrouping.display;
        }
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
        } else {
            derivedGrouping.displayName = nameInput.text;
        }
        derivedGrouping.derivationCode = calculationInput.text;
        return derivedGrouping;
    }
}
}