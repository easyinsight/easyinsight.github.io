/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/18/11
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import flash.events.Event;

public class IntentionEvent extends Event {

    public static const SUGGESTION_CHOICE:String = "suggestionChoice";

    public var suggestion:IntentionSuggestion;

    public function IntentionEvent(type:String, suggestion:IntentionSuggestion) {
        super(type, true);
        this.suggestion = suggestion;
    }

    override public function clone():Event {
        return new IntentionEvent(type, suggestion);
    }
}
}
