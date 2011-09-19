/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/18/11
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.filtering.TransformContainer;

import mx.collections.ArrayCollection;

public class SuggestionMetadata {

    public var report:AnalysisDefinition;
    public var transformContainer:TransformContainer;
    public var allFields:ArrayCollection;
    public var wrappers:ArrayCollection;

    public function SuggestionMetadata(report:AnalysisDefinition, transformContainer:TransformContainer, allFields:ArrayCollection,
            wrappers:ArrayCollection) {
        this.report = report;
        this.transformContainer = transformContainer;
        this.allFields = allFields;
        this.wrappers = wrappers;
    }
}
}
