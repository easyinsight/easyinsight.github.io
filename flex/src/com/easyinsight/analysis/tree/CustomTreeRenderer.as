package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisItem;

import flash.display.DisplayObject;

import mx.controls.advancedDataGridClasses.AdvancedDataGridGroupItemRenderer;
import mx.core.IUITextField;

public class CustomTreeRenderer extends AdvancedDataGridGroupItemRenderer {

    private var _analysisItem:AnalysisItem;

    public function CustomTreeRenderer() {
        super();
        mouseEnabled = true;
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
    }

    protected override function commitProperties():void {
        super.commitProperties();
        CustomTreeTextRenderer(label).analysisItem = analysisItem;
        CustomTreeTextRenderer(label).data = data;
    }


    override public function set data(value:Object):void {
        super.data = value;
        invalidateProperties();
    }

    override protected function createLabel(childIndex:int):void {
        if (!label)
        {
            label = IUITextField(new CustomTreeTextRenderer());
            label.styleName = this;

            if (childIndex == -1)
                addChild(DisplayObject(label));
            else
                addChildAt(DisplayObject(label), childIndex);
        }
    }
}
}