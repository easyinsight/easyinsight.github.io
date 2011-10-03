/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/1/11
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import mx.rpc.remoting.RemoteObject;

public class UserAudit {

    private static var singleton:UserAudit;

    private var adminService:RemoteObject;

    public function UserAudit() {
        adminService = new RemoteObject();
        adminService.destination = "admin";
        adminService.endpoint = "https://www.easy-insight.com/app/messagebroker/amfsecure";
    }

    public static function initialize():void {
        singleton = new UserAudit();
    }

    public static function instance():UserAudit {
        if (singleton == null) {
            initialize();
        }
        return singleton;
    }

    public function log(actionLog:ActionLog):void {
        adminService.logAction.send(actionLog);
    }
}
}
