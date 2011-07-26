/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/10/11
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;

public interface IReportView {
    function renderReport(data:ArrayCollection, report:AnalysisDefinition):void;
    function preserveValues():Boolean;
}
}
