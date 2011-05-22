/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/16/11
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import com.easyinsight.solutions.InsightDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.audit.ActionReportLog")]
public class ActionReportLog extends ActionLog {

    public static const EDIT:int = 1;
    public static const VIEW:int = 2;
    public static const EXPORT_XLS:int = 3;
    public static const EXPORT_PDF:int = 4;
    public static const EXPORT_PNG:int = 5;

    public var reportID:int;

    public var insightDescriptor:InsightDescriptor;

    public function ActionReportLog(actionType:int = 0, reportID:int = 0) {
        this.actionType = actionType;
        this.reportID = reportID;
    }

    override public function get display():String {
        if (actionType == EDIT) {
            return "Edit " + insightDescriptor.name;
        } else if (actionType == VIEW) {
            return "View " + insightDescriptor.name;
        } else if (actionType == EXPORT_XLS) {
            return "Export " + insightDescriptor.name + " to Excel";
        } else if (actionType == EXPORT_PDF) {
            return "Export " + insightDescriptor.name + " to PDF";
        } else if (actionType == EXPORT_PNG) {
            return "Export " + insightDescriptor.name + " to PNG";
        }
        return null;
    }
}
}
