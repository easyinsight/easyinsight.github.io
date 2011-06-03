package com.easyinsight.util {
import mx.rpc.remoting.RemoteObject;

public class UserAudit {

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

    public function log(actionLog:ActionLog):void {
        adminService.logAction.send(actionLog);
    }
}
}