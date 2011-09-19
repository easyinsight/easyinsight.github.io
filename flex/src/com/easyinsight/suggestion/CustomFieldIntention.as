/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/18/11
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemNode;
import com.easyinsight.analysis.AnalysisItemWrapper;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.CustomFieldIntention")]
public class CustomFieldIntention extends Intention {

    public var field:AnalysisItem;

    public function CustomFieldIntention() {
    }


    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        var node:AnalysisItemNode = new AnalysisItemNode();
        node.analysisItem = field;
        var wrapper:AnalysisItemWrapper = new AnalysisItemWrapper(node);
        suggestionMetadata.allFields.addItem(wrapper);
        suggestionMetadata.wrappers.addItem(wrapper);
        if (suggestionMetadata.report.addedItems == null) {
            suggestionMetadata.report.addedItems = new ArrayCollection();
        }
        suggestionMetadata.report.addedItems.addItem(field);
    }
}
}
