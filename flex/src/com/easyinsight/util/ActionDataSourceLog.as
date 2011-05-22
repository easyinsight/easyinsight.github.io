/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/16/11
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
[Bindable]
[RemoteClass(alias="com.easyinsight.audit.ActionDataSourceLog")]
public class ActionDataSourceLog extends ActionLog {

    public static const NEW_REPORT:int = 1;
    public static const ADMIN:int = 2;

    public var dataSourceID:int;
    public var dataSourceName:String;

    public function ActionDataSourceLog(actionType:int = 0, dataSourceID:int = 0) {
        this.actionType = actionType;
        this.dataSourceID = dataSourceID;
    }

    override public function get display():String {
        if (actionType == NEW_REPORT) {
            return "Create a new report on " + dataSourceName;
        } else if (actionType == ADMIN) {
            return "Administer " + dataSourceName;
        }
        return null;
    }
}
}
