/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/3/11
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.Event;

public class MeasureFilterEvent extends Event {

    public static const MEASURE_FILTER:String = "measureFilter";

    public var object:Object;

    public function MeasureFilterEvent(type:String,  object:Object) {
        super(type);
        this.object = object;
    }

    override public function clone():Event {
        return new MeasureFilterEvent(type, object);
    }
}
}
