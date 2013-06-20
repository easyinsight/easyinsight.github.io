/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/24/11
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
[Bindable]
[RemoteClass(alias="com.easyinsight.intention.CustomizeJoinsIntention")]
public class CustomizeJoinsIntention extends Intention {
    public function CustomizeJoinsIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        dispatchEvent(new IntentionTriggerEvent(IntentionTriggerEvent.INTENTION_TRIGGER, false, null, false, true));
    }
}
}
