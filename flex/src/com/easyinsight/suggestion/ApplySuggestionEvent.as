/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/24/11
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import flash.events.Event;

public class ApplySuggestionEvent extends Event {

    public static const APPLY_SUGGESTION:String = "applySuggestion";

    public var suggestion:IntentionSuggestion;

    public function ApplySuggestionEvent(suggestion:IntentionSuggestion) {
        super(APPLY_SUGGESTION);
        this.suggestion = suggestion;
    }

    override public function clone():Event {
        return new ApplySuggestionEvent(suggestion);
    }
}
}
