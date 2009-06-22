package com.easyinsight.solutions {
import flash.events.MouseEvent;

import mx.containers.Box;
import mx.controls.Label;

public class SolutionCategoryButton extends Box {

    private var _category:String;
    private var _selected:Boolean;

    private var categoryLabel:Label;

    public function SolutionCategoryButton() {
        super();
        addEventListener(MouseEvent.CLICK, onClick);
        this.percentWidth = 100;
        this.percentHeight = 100;
        this.setStyle("horizontalAlign", "center");
        categoryLabel = new Label();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(categoryLabel);
    }

    private function onClick(event:MouseEvent):void {
        if (_selected) {
            setStyle("backgroundColor", 0xFFFFFF);
        } else {
            setStyle("backgroundColor", 0x3370ce);
        }
        _selected = !_selected;
        if (_selected) {
            dispatchEvent(new SolutionCategoryEvent(SolutionCategoryEvent.CATEGORY_SELECTED, _category));
        } else {
            dispatchEvent(new SolutionCategoryEvent(SolutionCategoryEvent.CATEGORY_DESELECTED, _category));
        }
    }
}
}