/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/30/11
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class ReportPreferencesEvent extends Event {

    public static const REPORT_PREFERENCES:String = "reportPreferences";

    public var refreshData:Boolean;

    public function ReportPreferencesEvent(refreshData:Boolean = false) {
        super(REPORT_PREFERENCES);
        this.refreshData = refreshData;
    }

    override public function clone():Event {
        return new ReportPreferencesEvent(refreshData);
    }
}
}
