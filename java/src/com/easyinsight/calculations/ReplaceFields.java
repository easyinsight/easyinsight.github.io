package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.MaterializedFilterPatternDefinition;
import com.easyinsight.analysis.ReaggregateAnalysisMeasure;
import com.easyinsight.analysis.definitions.WSCompareYearsDefinition;
import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.analysis.definitions.WSYTDDefinition;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 1:14 PM
 */
public class ReplaceFields extends Function {
    public Value evaluate() {

        String fieldToReplace = minusQuotes(params.get(0)).toString();
        List<AnalysisItem> measures;
        if (calculationMetadata.getReport() instanceof WSVerticalListDefinition) {
            WSVerticalListDefinition verticalListDefinition = (WSVerticalListDefinition) calculationMetadata.getReport();
            measures = verticalListDefinition.getMeasures();
        } else if (calculationMetadata.getReport() instanceof WSYTDDefinition) {
            WSYTDDefinition verticalListDefinition = (WSYTDDefinition) calculationMetadata.getReport();
            measures = verticalListDefinition.getMeasures();
        } else if (calculationMetadata.getReport() instanceof WSCompareYearsDefinition) {
            WSCompareYearsDefinition verticalListDefinition = (WSCompareYearsDefinition) calculationMetadata.getReport();
            measures = verticalListDefinition.getMeasures();
        } else {
            throw new RuntimeException();
        }
        AnalysisItem template = null;
        for (AnalysisItem analysisItem : measures) {
            if (analysisItem.toDisplay().toLowerCase().equals(fieldToReplace.toLowerCase())) {
                template = analysisItem;
            }
        }
        int index = measures.indexOf(template);
        measures.remove(template);
        List<AnalysisItem> toAdd = new ArrayList<AnalysisItem>();
        if (template != null) {
            for (int i = 1; i < params.size(); i++) {
                String patternString = minusQuotes(params.get(i)).toString();
                Pattern pattern = Pattern.compile(MaterializedFilterPatternDefinition.createWildcardPattern(patternString.toLowerCase()));
                for (AnalysisItem field : calculationMetadata.getDataSourceFields()) {
                    Matcher matcher = pattern.matcher(field.toDisplay().toLowerCase());
                    if (matcher.matches()) {
                        toAdd.add(fromTemplate(template, field));
                    }
                }
            }
            Collections.sort(toAdd, new Comparator<AnalysisItem>() {

                public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                    return analysisItem1.toDisplay().compareTo(analysisItem.toDisplay());
                }
            });
            for (AnalysisItem analysisItem : toAdd) {
                measures.add(index, analysisItem);
            }
        }
        return new EmptyValue();
    }

    private AnalysisItem fromTemplate(AnalysisItem template, AnalysisItem target) {
        try {
            AnalysisItem clone = template.clone();
            if (clone instanceof ReaggregateAnalysisMeasure) {
                ReaggregateAnalysisMeasure reaggregateAnalysisMeasure = (ReaggregateAnalysisMeasure) clone;
                reaggregateAnalysisMeasure.setWrappedMeasure(target);
            }
            clone.setDisplayName(target.getDisplayName());
            clone.setKey(target.getKey());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
