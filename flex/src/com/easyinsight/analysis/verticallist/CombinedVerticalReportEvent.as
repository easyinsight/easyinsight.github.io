/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/21/11
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;

import flash.events.Event;

public class CombinedVerticalReportEvent extends Event {

    public static const REMOVE_REPORT:String = "removeReport";

    public var report:AnalysisDefinition;

    public function CombinedVerticalReportEvent(report:AnalysisDefinition) {
        super(REMOVE_REPORT, true);
        this.report = report;
    }


    override public function clone():Event {
        return new CombinedVerticalReportEvent(report);
    }
}
}
