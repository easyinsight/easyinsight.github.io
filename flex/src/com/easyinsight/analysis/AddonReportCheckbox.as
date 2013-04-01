/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/13/12
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.util.GridCheckbox;

public class AddonReportCheckbox extends GridCheckbox {

    override protected function updateSelectedOnObject(selected:Boolean):void {
        AddonReport(data).selected = selected;
    }

    override protected function isSelected():Boolean {
        return AddonReport(data).selected;
    }
}
}
