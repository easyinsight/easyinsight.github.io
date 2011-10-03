/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/3/11
 * Time: 12:50 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.Event;

public class DateChoiceEvent extends Event {

    public static const DATE_CHOICE:String = "dateChoice";

    public var date:Date;

    public function DateChoiceEvent(type:String,  date:Date) {
        super(type);
        this.date = date;
    }

    override public function clone():Event {
        return new DateChoiceEvent(type, date);
    }
}
}
