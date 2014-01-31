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
            calcName = analysisCalculation.unqualifiedDisplay;
            aggregation = analysisCalculation.aggregation;
            formattingConfiguration = analysisCalculation.formattingType;
            applyBefore = analysisCalculation.applyBeforeAggregation;
            summaryRecalc = analysisCalculation.recalculateSummary;
            underline = analysisCalculation.underline;
            precision = analysisCalculation.precision;
            minPrecision = analysisCalculation.minPrecision;
        }
        detailIndex = 1;
        detailItemLabel = "Aggregation:";
        example1 = "[Revenue] / [Units]";
        example1Explanation = "Produces Revenue Divided by Units";
        example2 = "namedbracketvalue([Deal Description], \"Prob\")";
        example2Explanation = "";
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
            analysisCalculation.displayName = nameInput.text;
            analysisCalculation.unqualifiedDisplayName = nameInput.text;
        } else {
            if (analysisCalculation.key is NamedKey) {
                NamedKey(analysisCalculation.key).name = nameInput.text;
            }
            analysisCalculation.displayName = nameInput.text;
            analysisCalculation.unqualifiedDisplayName = nameInput.text;
        }
        analysisCalculation.applyBeforeAggregation = rowLevelCheckbox.selected;
        analysisCalculation.aggregation = measureAggregationBox.selectedItem.value;
        analysisCalculation.calculationString = calculationInput.text;
        analysisCalculation.formattingType = formattingSetup.formattingConfiguration;
        analysisCalculation.recalculateSummary = summaryCheckbox.selected;
        analysisCalculation.precision = precisionInput.value;
        analysisCalculation.underline = underlineCheckbox.selected;
        analysisCalculation.minPrecision = minPrecisionInput.value;
        return analysisCalculation;
    }
}
}