/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/24/11
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.AnalysisItem;

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.AddReportFieldIntention")]
public class AddReportFieldIntention extends Intention {

    public var analysisItem:AnalysisItem;

    public function AddReportFieldIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        dispatchEvent(new IntentionTriggerEvent(IntentionTriggerEvent.INTENTION_TRIGGER, false, analysisItem));
    }
}
}
