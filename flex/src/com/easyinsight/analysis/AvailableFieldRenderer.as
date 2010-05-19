package com.easyinsight.analysis {
import flash.display.DisplayObject;

import mx.controls.advancedDataGridClasses.AdvancedDataGridGroupItemRenderer;
import mx.core.IUITextField;

public class AvailableFieldRenderer extends AdvancedDataGridGroupItemRenderer {

    private var wrapper:Object;

    public function AvailableFieldRenderer() {
        super();
        mouseEnabled = true;
    }

    protected override function commitProperties():void {
        super.commitProperties();
        AvailableFieldTextRenderer(label).data = data;
    }

    override protected function createLabel(childIndex:int):void {
        if (!label)
        {
            label = IUITextField(new AvailableFieldTextRenderer());
            label.styleName = this;

            if (childIndex == -1)
                addChild(DisplayObject(label));
            else
                addChildAt(DisplayObject(label), childIndex);
        }
    }
}
}