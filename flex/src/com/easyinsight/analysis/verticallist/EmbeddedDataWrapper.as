/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/19/11
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;

public class EmbeddedDataWrapper {

    public var dataSet:ArrayCollection;
    public var report:AnalysisDefinition;

    public function EmbeddedDataWrapper(dataSet:ArrayCollection,  report:AnalysisDefinition) {
        this.dataSet = dataSet;
        this.report = report;
    }
}
}
