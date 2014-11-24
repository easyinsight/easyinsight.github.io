package com.easyinsight.analysis {
import com.easyinsight.analysis.formatter.FormattingConfiguration;

import mx.controls.Alert;

public class CalculationMeasureConfigWindow extends CalculationConfigWindow {

    private var analysisCalculation:AnalysisCalculation;

    public function CalculationMeasureConfigWindow() {
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
            defaultDate = analysisCalculation.defaultDate;
            customFormatChoice = analysisCalculation.customFormatChoice;
            if (formattingConfiguration == FormattingConfiguration.MILLISECONDS) {
                formatIndex = 1;
            }
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

    override public function save(dataSourceID:int):void {
        analysisCalculation.applyBeforeAggregation = rowLevelCheckbox.selected;
        analysisCalculation.customFormatChoice = intervalBox.selectedItem.value;
        analysisCalculation.aggregation = measureAggregationBox.selectedItem.value;
        analysisCalculation.formattingType = formattingSetup.formattingConfiguration;
        analysisCalculation.recalculateSummary = summaryCheckbox.selected;
        analysisCalculation.precision = precisionInput.value;
        analysisCalculation.underline = false;
        if (dateComparisonBox.selectedItem is AnalysisItem) {
            analysisCalculation.defaultDate = dateComparisonBox.selectedItem.display;
        } else {
            analysisCalculation.defaultDate = null;
        }
        analysisCalculation.minPrecision = minPrecisionInput.value;
    }
}
}