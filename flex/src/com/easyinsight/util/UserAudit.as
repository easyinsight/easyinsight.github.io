package com.easyinsight.util {
import mx.rpc.remoting.RemoteObject;

public class UserAudit {

    public static const CONNECTED_TO_DATA:String = "Connected to a Data Source";
    public static const REFRESHED_DATA:String = "Refreshed a Data Source";
    public static const OPEN_REPORT_EDITOR:String = "Open a Report in Editor";
    public static const VALID_REPORT:String = "Retrieved Data With a Valid Report";
    public static const OPEN_REPORT_VIEW:String = "Open a Report in View";
    public static const SAVED_REPORT:String = "Saved a Report";
    public static const EXPORTED_TO_EXCEL:String = "Exported a Report to Excel";
    public static const SET_UP_EMAIL:String = "Set up Email of a Report";
    public static const NEW_REPORT:String = "New Report in Editor";
    public static const VISITED_EXCHANGE:String = "Visited Exchange";
    public static const USED_REPORT_IN_EXCHANGE:String = "Used a Report in the Exchange";
    public static const ADDED_KPI:String = "Defined a KPI";
    public static const EXPORTED_TO_IGOOGLE:String = "Exported a Scorecard to iGoogle";
    public static const USED_EMBEDDED:String = "Used an Embedded Report";
    public static const CREATED_FILTER:String = "Created a Filter";
    public static const CREATED_CALCULATION:String = "Created a Calculation";
    public static const CREATED_HIERARCHY:String = "Created a Hierarchy";
    public static const ADDED_REPORT_TO_EXCHANGE:String = "Added a Report to the Exchange";
    public static const ADDED_USER_TO_ACCOUNT:String = "Added a User to the Account";
    public static const SCHEDULED_REFRESH:String = "Scheduled a Data Source for Refresh";
    public static const EDITED_FIELD:String = "Edited a Field";
    public static const COPIED_FIELD:String = "Copied a Field";
    public static const CREATED_LINK:String = "Created a Link";

    private static var singleton:UserAudit;

    private var adminService:RemoteObject;

    public function UserAudit() {
        adminService = new RemoteObject();
        adminService.destination = "admin";
    }

    public static function initialize():void {
        singleton = new UserAudit();
    }

    public static function instance():UserAudit {
        return singleton;
    }

    public function audit(message:String):void {
        adminService.userAudit.send(message);
    }

    public function log(actionLog:ActionLog):void {
        adminService.logAction.send(actionLog);
    }
}
}