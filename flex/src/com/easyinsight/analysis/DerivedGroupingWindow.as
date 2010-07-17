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
    }

    override protected function get calculationItem():AnalysisItem {
        return derivedGrouping;
    }

    override public function save(dataSourceID:int):AnalysisItem {
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
        analysisService.validateCalculation.send(derivedGrouping.derivationCode, dataSourceID, items);
        return derivedGrouping;
    }
}
}