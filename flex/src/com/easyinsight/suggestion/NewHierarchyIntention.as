/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/24/11
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
[Bindable]
[RemoteClass(alias="com.easyinsight.intention.NewHierarchyIntention")]
public class NewHierarchyIntention extends Intention {

    public static const  HIERARCHY:int = 1;
    public static const  CUSTOMIZE_JOINS:int = 2;

    public var variant:int;

    public function NewHierarchyIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        if (variant == HIERARCHY) {
            dispatchEvent(new IntentionTriggerEvent(IntentionTriggerEvent.INTENTION_TRIGGER, false, null, true));
        } else {
            dispatchEvent(new IntentionTriggerEvent(IntentionTriggerEvent.INTENTION_TRIGGER, false, null, false, true));
        }
    }
}
}
