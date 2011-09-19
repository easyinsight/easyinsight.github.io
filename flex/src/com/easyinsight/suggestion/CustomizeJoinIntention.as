/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/16/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.filtering.TransformContainer;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.CustomizeJoinsIntention")]
public class CustomizeJoinIntention extends Intention {

    public var joinOverrides:ArrayCollection;

    public function CustomizeJoinIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        suggestionMetadata.report.joinOverrides = joinOverrides;
    }
}
}
