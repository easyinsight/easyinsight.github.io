/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/30/11
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.ReportFault;

import flash.events.Event;

import mx.collections.ArrayCollection;

public class ReportComponentEvent extends Event {

    public static const GOT_DATA:String = "gotData";

    public var report:AnalysisDefinition;
    public var data:ArrayCollection;
    public var reportFault:ReportFault;

    public function ReportComponentEvent(type:String, report:AnalysisDefinition = null, data:ArrayCollection = null, reportFault:ReportFault = null) {
        super(type);
        this.report = report;
        this.data = data;
        this.reportFault = reportFault;
    }

    override public function clone():Event {
        return new ReportComponentEvent(type, report, data, reportFault);
    }
}
}
