/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/16/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.filtering.TransformContainer;

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.AddFilterIntention")]
public class AddFilterIntention extends Intention {

    public var filterDefinition:FilterDefinition;

    public function AddFilterIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        suggestionMetadata.transformContainer.addFilterDefinition(filterDefinition);
    }
}
}
