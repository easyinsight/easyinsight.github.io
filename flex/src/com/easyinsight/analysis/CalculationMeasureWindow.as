package com.easyinsight.analysis {
public class CalculationMeasureWindow extends CalculationWindow {

    private var analysisCalculation:AnalysisCalculation;

    public function CalculationMeasureWindow() {
        super();
    }

    override public function set analysisItem(analysisItem:AnalysisItem):void {
        analysisCalculation = analysisItem as AnalysisCalculation;
    }

    override protected function commitProperties():void {
        super.commitProperties();
        showMeasures = true;
        if (analysisCalculation != null) {
            calcText = analysisCalculation.calculationString;
            calcName = analysisCalculation.display;
            aggregation = analysisCalculation.aggregation;
            formattingConfiguration = analysisCalculation.formattingConfiguration;
            applyBefore = analysisCalculation.applyBeforeAggregation;
        }
        detailIndex = 2;
        detailItemLabel = "Aggregation:";
    }

    override protected function get calculationItem():AnalysisItem {
        return analysisCalculation;
    }

    override protected function actualSave():AnalysisItem {
        if (analysisCalculation == null) {
            analysisCalculation = new AnalysisCalculation();
            analysisCalculation.concrete = false;
            var namedKey:NamedKey = new NamedKey();
            namedKey.name = nameInput.text;
            analysisCalculation.key = namedKey;
        } else {
            analysisCalculation.displayName = nameInput.text;
        }
        analysisCalculation.applyBeforeAggregation = rowLevelCheckbox.selected;
        analysisCalculation.aggregation = measureAggregationBox.selectedItem.value;
        analysisCalculation.calculationString = calculationInput.text;
        analysisCalculation.formattingConfiguration = formattingSetup.formattingConfiguration;
        return analysisCalculation;
    }
}
}