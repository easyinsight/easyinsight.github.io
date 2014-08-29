/**
 * Created by jamesboe on 8/29/14.
 */
package com.easyinsight {
public class WindowManagementInstance {

    private static var windowManager:IWindowManagement = new EmptyWindowManagement();

    public function WindowManagementInstance() {
    }

    public static function assign(manager:IWindowManagement):void {
        windowManager = manager;
    }

    public static function getManager():IWindowManagement {
        return windowManager;
    }
}
}
