/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/9/11
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import flash.events.Event;

public class JoinStartEvent extends Event {

    public static const CREATE_SOURCE:String = "createSource";
    public static const NEED_CHOICE:String = "needChoice";

    public var joinSuggestion:JoinSuggestion;

    public function JoinStartEvent(type:String, joinSuggestion:JoinSuggestion) {
        super(type);
        this.joinSuggestion = joinSuggestion;
    }

    override public function clone():Event {
        return new JoinStartEvent(type, joinSuggestion);
    }
}
}
