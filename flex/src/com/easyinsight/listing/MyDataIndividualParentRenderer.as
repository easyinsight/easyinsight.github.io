/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/30/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.display.DisplayObject;

import mx.controls.advancedDataGridClasses.AdvancedDataGridGroupItemRenderer;
import mx.core.IUITextField;

public class MyDataIndividualParentRenderer extends AdvancedDataGridGroupItemRenderer {

    public function MyDataIndividualParentRenderer() {
        super();
        mouseEnabled = true;
    }

    override public function set data(value:Object):void {
        super.data = value;
        invalidateProperties();
    }

    override protected function commitProperties():void {
        super.commitProperties();
        MyDataIndividualNameRenderer(label).data = data;
    }

    override protected function createLabel(childIndex:int):void {
        if (!label)
        {
            label = IUITextField(new MyDataIndividualNameRenderer());
            //label.styleName = this;

            if (childIndex == -1)
                addChild(DisplayObject(label));
            else
                addChildAt(DisplayObject(label), childIndex);
        }
    }
}
}
