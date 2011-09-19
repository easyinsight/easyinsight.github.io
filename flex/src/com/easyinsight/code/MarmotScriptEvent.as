/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/17/11
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.code {
import flash.events.Event;

public class MarmotScriptEvent extends Event {

    public static const SAVE_SCRIPT:String = "saveScript";

    public var script:String;

    public function MarmotScriptEvent(type:String, script:String) {
        super(type);
        this.script = script;
    }

    override public function clone():Event {
        return new MarmotScriptEvent(type, script);
    }
}
}
