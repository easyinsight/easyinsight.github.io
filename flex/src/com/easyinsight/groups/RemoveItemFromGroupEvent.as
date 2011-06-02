package com.easyinsight.groups {
import flash.events.Event;
public class RemoveItemFromGroupEvent extends Event{

    public static const REMOVE_REPORT_FROM_GROUP:String = "removeReportFromGroup";
    public static const REMOVE_DATA_SOURCE_FROM_GROUP:String = "removeDataSourceFromGroup";
    public static const REMOVE_DASHBOARD_FROM_GROUP:String = "removeDashboardFromGroup";
    public static const REMOVE_SCORECARD_FROM_GROUP:String = "removeScorecardFromGroup";

    public var id:int;

    public function RemoveItemFromGroupEvent(type:String, id:int) {
        super(type, true);
        this.id = id;
    }

    override public function clone():Event {
        return new RemoveItemFromGroupEvent(type, id);
    }
}
}