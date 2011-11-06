/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/14/11
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.EmbeddedResults;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.EmbeddedVerticalResults")]
public class EmbeddedVerticalDataResults extends EmbeddedResults {

    public var list:ArrayCollection;
    public var report:AnalysisDefinition;

    public function EmbeddedVerticalDataResults() {
    }
}
}
