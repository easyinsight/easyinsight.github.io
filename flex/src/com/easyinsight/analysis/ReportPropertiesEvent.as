/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/29/11
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class ReportPropertiesEvent extends Event {

    public static const REPORT_PROPERTIES:String = "reportProperties";

    public var startIndex:int;

    public function ReportPropertiesEvent(startIndex:int = 0) {
        super(REPORT_PROPERTIES, true);
        this.startIndex = startIndex;
    }

    override public function clone():Event {
        return new ReportPropertiesEvent(startIndex);
    }
}
}
