/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/20/14
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
public class FilterRuleDefinition {

    public static const DASHBOARD_TO_REPORT_LINK:int = 1;
    public static const REPORT_TO_DASHBOARD_LINK:int = 2;

    public var data:Object;
    public var label:String;
    public var type:int;
    public var selected:Boolean;

    public function FilterRuleDefinition() {
    }


    public function clearRule(generalFilterEditSettings:GeneralFilterEditSettings):void {
        if (type == DASHBOARD_TO_REPORT_LINK) {
            generalFilterEditSettings.parentChild = null;
        } else if (type == REPORT_TO_DASHBOARD_LINK) {
            generalFilterEditSettings.childParent = null;
        }
    }
}
}
