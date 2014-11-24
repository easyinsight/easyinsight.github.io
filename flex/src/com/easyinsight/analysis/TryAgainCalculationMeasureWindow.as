package com.easyinsight.analysis {
import com.easyinsight.analysis.formatter.FormattingConfiguration;

import mx.controls.Alert;

public class TryAgainCalculationMeasureWindow extends TryAgainCalcWindow {

    private var analysisCalculation:AnalysisCalculation;

    public function TryAgainCalculationMeasureWindow() {
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
        example2Explanation = "Extracts a Probability custom field from the Deal Description";
        example3 = "nowdate() - [Task - Created At]";
        example3Explanation = "Calculates the age of a task based on the current time minus the task creation date";
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

        analysisCalculation.calculationString = calculationInput.text;
        return analysisCalculation;
    }
}
}