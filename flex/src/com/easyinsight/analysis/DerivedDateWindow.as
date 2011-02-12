package com.easyinsight.analysis {
public class DerivedDateWindow extends CalculationWindow {

    private var derivedGrouping:DerivedAnalysisDateDimension;

    public function DerivedDateWindow() {
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
            calcName = derivedGrouping.display;
            dateLevel = derivedGrouping.dateLevel;
        }
        detailIndex = 1;
        detailItemLabel = "Date Level:";
        example1 = "namedbracketdate([Deal Description], \"Close Date\", \"yyyy-MM-dd\")";
        example1Explanation = "";
        example2 = "now() - [Due On]";
        example2Explanation = "";
    }

    override protected function get calculationItem():AnalysisItem {
        return derivedGrouping;
    }

    override protected function actualSave():AnalysisItem {
        if (derivedGrouping == null) {
            derivedGrouping = new DerivedAnalysisDateDimension();
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