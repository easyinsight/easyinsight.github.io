/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/6/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;

import mx.collections.ArrayCollection;

public class AddonReportEvent extends Event {

    public static const ADD_REPORT:String = "addonAddReport";
    public static const NEW_ADDONS:String = "newAddons";

    public var report:InsightDescriptor;

    public var addonReports:ArrayCollection;
    public var removedReports:ArrayCollection;

    public function AddonReportEvent(type:String, report:InsightDescriptor, addonReports:ArrayCollection = null, removedReports:ArrayCollection = null) {
        super(type);
        this.report = report;
        this.addonReports = addonReports;
        this.removedReports = removedReports;
    }

    override public function clone():Event {
        return new AddonReportEvent(type, report, addonReports, removedReports);
    }
}
}
