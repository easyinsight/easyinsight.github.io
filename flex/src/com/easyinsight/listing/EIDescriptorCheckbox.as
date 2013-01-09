/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/17/12
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.util.GridCheckbox;

public class EIDescriptorCheckbox extends GridCheckbox  {
    public function EIDescriptorCheckbox() {
    }

    override protected function updateSelectedOnObject(selected:Boolean):void {
        EIDescriptor(data).selected = selected;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (checkbox != null) {
            checkbox.move(12,0);
            checkbox.setActualSize(16, 16);
        }
    }

    override protected function isSelected():Boolean {
        return EIDescriptor(data).selected;
    }
}
}
