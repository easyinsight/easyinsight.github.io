package test.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;

/**
* User: jamesboe
* Date: 7/19/12
* Time: 1:29 PM
*/
public class FieldWrapper {
    private AnalysisItem analysisItem;

    FieldWrapper(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void addFilter(FilterDefinition filterDefinition) {
        analysisItem.getFilters().add(filterDefinition);
    }
}
