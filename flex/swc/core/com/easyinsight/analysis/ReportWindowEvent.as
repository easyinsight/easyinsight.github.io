package com.easyinsight.analysis {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class ReportWindowEvent extends Event {

    public static const REPORT_WINDOW:String = "reportWindow";

    public var reportID:int;    
    public var dataSourceID:int;
    public var x:int;
    public var y:int;
    public var filters:ArrayCollection;
    public var reportType:int;

    public function ReportWindowEvent(reportID:int, x:int, y:int, filters:ArrayCollection, dataSourceID:int, reportType:int) {
        super(REPORT_WINDOW, true);
        this.reportID = reportID;
        this.x = x;
        this.y = y;
        this.filters = filters;
        this.dataSourceID = dataSourceID;
        this.reportType = reportType;
    }

    override public function clone():Event {
        return new ReportWindowEvent(reportID, x, y, filters, dataSourceID, reportType);
    }
}
}