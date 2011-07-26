/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/30/11
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.filtering.FilterDefinition;

public class FilterInfo {

    public var filterDefinition:FilterDefinition;
    public var report:AnalysisDefinition;

    public function FilterInfo(filterDefinition:FilterDefinition, report:AnalysisDefinition) {
        this.filterDefinition = filterDefinition;
        this.report = report;
    }
}
}
