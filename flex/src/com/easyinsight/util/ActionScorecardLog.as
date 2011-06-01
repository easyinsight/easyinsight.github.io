/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/16/11
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import com.easyinsight.scorecard.ScorecardDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.audit.ActionScorecardLog")]
public class ActionScorecardLog extends ActionLog {

    public static const EDIT:int = 1;
    public static const VIEW:int = 2;

    public var scorecardID:int;

    public var scorecardDescriptor:ScorecardDescriptor;

    public function ActionScorecardLog(actionType:int = 0, scorecardID:int = 0) {
        this.actionType = actionType;
        this.scorecardID = scorecardID;
    }

    override public function get display():String {
        if (actionType == EDIT) {
            return "Edit " + scorecardDescriptor.name;
        } else if (actionType == VIEW) {
            return "View " + scorecardDescriptor.name;
        }
        return null;
    }
}
}
