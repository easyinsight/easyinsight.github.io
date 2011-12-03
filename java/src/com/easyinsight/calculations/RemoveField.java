package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemFilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.definitions.WSCompareYearsDefinition;
import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.analysis.definitions.WSYTDDefinition;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.Iterator;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 3:42 PM
 */
public class RemoveField extends Function {
    public Value evaluate() {
        if (calculationMetadata.getReport() != null && calculationMetadata.getReport().getReportType() == WSAnalysisDefinition.VERTICAL_LIST) {
            WSVerticalListDefinition verticalListDefinition = (WSVerticalListDefinition) calculationMetadata.getReport();
            String fieldName = minusQuotes(params.get(0)).toString().toLowerCase();
            Iterator<AnalysisItem> iter = verticalListDefinition.getMeasures().iterator();
            while (iter.hasNext()) {
                AnalysisItem item = iter.next();
                if (item.toDisplay().toLowerCase().equals(fieldName)) {
                    iter.remove();
                }
            }
        } else if (calculationMetadata.getReport() != null && calculationMetadata.getReport().getReportType() == WSAnalysisDefinition.YTD) {
            WSYTDDefinition verticalListDefinition = (WSYTDDefinition) calculationMetadata.getReport();
            String fieldName = minusQuotes(params.get(0)).toString().toLowerCase();
            Iterator<AnalysisItem> iter = verticalListDefinition.getMeasures().iterator();
            while (iter.hasNext()) {
                AnalysisItem item = iter.next();
                if (item.toDisplay().toLowerCase().equals(fieldName)) {
                    iter.remove();
                }
            }
        } else if (calculationMetadata.getReport() != null && calculationMetadata.getReport().getReportType() == WSAnalysisDefinition.COMPARE_YEARS) {
            WSCompareYearsDefinition verticalListDefinition = (WSCompareYearsDefinition) calculationMetadata.getReport();
            String fieldName = minusQuotes(params.get(0)).toString().toLowerCase();
            Iterator<AnalysisItem> iter = verticalListDefinition.getMeasures().iterator();
            while (iter.hasNext()) {
                AnalysisItem item = iter.next();
                if (item.toDisplay().toLowerCase().equals(fieldName)) {
                    iter.remove();
                }
            }
        }else if (calculationMetadata.getFilterDefinition() != null && calculationMetadata.getFilterDefinition() instanceof AnalysisItemFilterDefinition) {
            String fieldName = minusQuotes(params.get(0)).toString().toLowerCase();
            AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) calculationMetadata.getFilterDefinition();
            Iterator<AnalysisItem> iter = analysisItemFilterDefinition.getAvailableItems().iterator();
            while (iter.hasNext()) {
                AnalysisItem analysisItem = iter.next();
                if (analysisItem.toDisplay().toLowerCase().equals(fieldName)) {
                    iter.remove();
                }
            }
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return -1;
    }
}
