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

            formattingConfiguration = analysisCalculation.formattingConfiguration;
            applyBefore = analysisCalculation.applyBeforeAggregation;
        }
    }

    override protected function get calculationItem():AnalysisItem {
        return analysisCalculation;
    }

    override public function save(dataSourceID:int):AnalysisItem {
        if (analysisCalculation == null) {
            analysisCalculation = new AnalysisCalculation();
            analysisCalculation.concrete = false;
            var namedKey:NamedKey = new NamedKey();
            namedKey.name = nameInput.text;
            analysisCalculation.key = namedKey;
        } else {
            analysisCalculation.displayName = nameInput.text;
        }
        //analysisCalculation.applyBeforeAggregation = applyBeforeCheckbox.selected;
        analysisCalculation.calculationString = calculationInput.text;
        analysisCalculation.formattingConfiguration = formattingSetup.formattingConfiguration;
        analysisService.validateCalculation.send(analysisCalculation.calculationString, dataSourceID, items);
        return analysisCalculation;
    }
}
}