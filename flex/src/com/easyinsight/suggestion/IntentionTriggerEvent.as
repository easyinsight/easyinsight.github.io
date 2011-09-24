/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/23/11
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.AnalysisItem;

import flash.events.Event;

public class IntentionTriggerEvent extends Event {

    public static const INTENTION_TRIGGER:String = "intentionTrigger";

    public var admin:Boolean;
    public var newField:AnalysisItem;
    public var newHierarchy:Boolean;

    public function IntentionTriggerEvent(type:String, admin:Boolean = false, newField:AnalysisItem = null,
            newHierarchy:Boolean = false) {
        super(type);
        this.admin = admin;
        this.newField = newField;
        this.newHierarchy = newHierarchy;
    }

    override public function clone():Event {
        return new IntentionTriggerEvent(type, admin, newField, newHierarchy);
    }
}
}
