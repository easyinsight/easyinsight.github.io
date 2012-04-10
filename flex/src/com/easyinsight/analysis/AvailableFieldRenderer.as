package com.easyinsight.analysis {
import flash.display.DisplayObject;

import flash.events.MouseEvent;

import mx.controls.advancedDataGridClasses.AdvancedDataGridGroupItemRenderer;
import mx.core.IUITextField;

public class AvailableFieldRenderer extends AdvancedDataGridGroupItemRenderer {
    
    public function AvailableFieldRenderer() {
        super();
        mouseEnabled = true;
        addEventListener(MouseEvent.DOUBLE_CLICK, onDoubleClick);
    }

    private var _dataSourceID:int;

    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
    }

    private var _calcRefactor:Boolean;

    public function set calcRefactor(value:Boolean):void {
        _calcRefactor = value;
    }

    private function onDoubleClick(event:MouseEvent):void {
        dispatchEvent(new FieldDoubleClickEvent(data as AnalysisItemWrapper));
    }

    protected override function commitProperties():void {
        super.commitProperties();
        this.data = data;
        AvailableFieldTextRenderer(label).data = data;
        AvailableFieldTextRenderer(label).dataSourceID = _dataSourceID;
        AvailableFieldTextRenderer(label).calcRefactor = _calcRefactor;
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