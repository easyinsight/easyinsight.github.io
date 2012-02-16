/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/14/12
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class AdditionalActionEvent extends Event {

    public static const NEW_REPORT:String = "newReportAction";
    public static const REPORT_VIEW:String = "reportViewAction";
    public static const NEW_DASHBOARD:String = "newDashboardAction";
    public static const SEARCH:String = "searchAction";
    public static const CONFIGURE_DATA_SOURCE:String = "configureDataSourceAction";

    public function AdditionalActionEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new AdditionalActionEvent(type);
    }
}
}
