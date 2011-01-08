/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/7/11
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class PromptEvent extends Event {

    public static const PROMPT_SAVE:String = "promptSave";
    public static const PROMPT_DISCARD:String = "promptDiscard";
    public static const PROMPT_CANCEL:String = "promptCancel";

    public function PromptEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new PromptEvent(type);
    }
}
}
