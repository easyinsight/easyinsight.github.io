/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/13/12
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {

public class ReportTagCheckbox extends GridCheckbox {

    override protected function updateSelectedOnObject(selected:Boolean):void {
        data["report"] = selected;
    }

    override protected function isSelected():Boolean {
        return data["report"];
    }
}
}
