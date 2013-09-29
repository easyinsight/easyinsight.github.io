/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/23/13
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import flash.events.Event;

public class ExperimentEvent extends Event {

    public static const EXPERIMENT_COLLAPSE:String = "experimentCollapse";

    public function ExperimentEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new ExperimentEvent(type);
    }
}
}
