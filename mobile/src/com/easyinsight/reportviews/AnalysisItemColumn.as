/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/27/11
 * Time: 10:38 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisItem;

import mx.formatters.Formatter;

import spark.components.gridClasses.GridColumn;

public class AnalysisItemColumn extends GridColumn {

    public var analysisItem:AnalysisItem;
    public var itemFormatter:Formatter;

    public function AnalysisItemColumn(analysisItem:AnalysisItem, formatter:Formatter) {
        this.analysisItem = analysisItem;
        this.itemFormatter = formatter;
    }
}
}
