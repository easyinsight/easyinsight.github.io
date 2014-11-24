package com.easyinsight.analysis {
public class TryAgainDerivedDateWindow extends TryAgainCalcWindow {

    private var derivedGrouping:DerivedAnalysisDateDimension;

    public function TryAgainDerivedDateWindow() {
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

    override protected function actualSave():AnalysisItem {
        if (derivedGrouping == null) {
            derivedGrouping = new DerivedAnalysisDateDimension();
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