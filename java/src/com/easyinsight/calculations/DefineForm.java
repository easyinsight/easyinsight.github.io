package com.easyinsight.calculations;

import com.easyinsight.analysis.ActualRowLayoutItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.MaterializedFilterPatternDefinition;
import com.easyinsight.core.Value;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User: jamesboe
 * Date: 4/4/12
 * Time: 10:47 AM
 */
public class DefineForm extends Function {
    public Value evaluate() {
        FormCalculationMetadata formCalculationMetadata = (FormCalculationMetadata) calculationMetadata;
        List<AnalysisItem> pool = formCalculationMetadata.getAnalysisItemPool();
        int numberColumns = params.get(0).toDouble().intValue();
        int sort = params.get(1).toDouble().intValue();
        int columnWidth = params.get(2).toDouble().intValue();
        int formLabelWidth = params.get(3).toDouble().intValue();
        ActualRowLayoutItem form = new ActualRowLayoutItem();
        form.setColumns(numberColumns);
        form.setColumnWidth(columnWidth);
        form.setFormLabelWidth(formLabelWidth);
        for (int i = 4; i < params.size(); i++) {
            String pattern = minusQuotes(i);
            String wildcardPattern = MaterializedFilterPatternDefinition.createWildcardPattern(pattern).toLowerCase();
            Pattern p = Pattern.compile(wildcardPattern);
            Iterator<AnalysisItem> iter = pool.iterator();
            while (iter.hasNext()) {
                AnalysisItem analysisItem = iter.next();
                if (p.matcher(analysisItem.toDisplay().toLowerCase()).matches()) {
                    iter.remove();
                    form.getAnalysisItems().add(analysisItem);
                }
            }
        }
        if (sort == 1) {
            Collections.sort(form.getAnalysisItems(), new Comparator<AnalysisItem>() {

                public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                    return analysisItem.toDisplay().compareTo(analysisItem1.toDisplay());
                }
            });
        }
        formCalculationMetadata.getForms().add(form);
        return null;
    }

    public int getParameterCount() {
        return -1;
    }
}
