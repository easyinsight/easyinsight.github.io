/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/24/11
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.report {

import flash.events.Event;

public class ReportSetupEvent extends Event {

    public static const REPORT_SETUP:String = "reportSetup";

    public var reportInfo:ReportInfo;

    public function ReportSetupEvent(reportInfo:ReportInfo) {
        super(REPORT_SETUP);
        this.reportInfo = reportInfo;
    }

    override public function clone():Event {
        return new ReportSetupEvent(reportInfo);
    }
}
}
