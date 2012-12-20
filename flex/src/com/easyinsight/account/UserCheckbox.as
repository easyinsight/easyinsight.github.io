/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/13/12
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.account {
import com.easyinsight.framework.UserTransferObject;
import com.easyinsight.util.GridCheckbox;

public class UserCheckbox extends GridCheckbox {

    override protected function updateSelectedOnObject(selected:Boolean):void {
        data["selected"] = selected;
    }

    override protected function isSelected():Boolean {
        return data["selected"];
    }
}
}
