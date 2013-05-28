/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/23/13
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class ReportPagingEvent extends Event {

    public static const SHOW_ALL:String = "showAll";

    public var uid:String = null;

    public function ReportPagingEvent(type:String, uid:String = null) {
        super(type, true);
        this.uid = uid;
    }


    override public function clone():Event {
        return new ReportPagingEvent(type, uid);
    }
}
}
