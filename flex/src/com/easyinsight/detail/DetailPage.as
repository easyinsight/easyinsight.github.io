package com.easyinsight.detail {

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.containers.Box;
import mx.containers.Form;
import mx.containers.FormItem;
import mx.containers.HBox;

public class DetailPage extends Box {

    private var _detailItem:Object;
    private var layout:HBox;

    public function DetailPage() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("backgroundAlpha", 1);
    }

    public function set detailItem(val:Object):void {
        _detailItem = val;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (layout == null) {
            layout = new HBox();
            layout.percentWidth = 100;
            layout.percentHeight = 100;

            var keys:ArrayCollection = new ArrayCollection();
            for (var sortKey:String in _detailItem) {
                keys.addItem(sortKey);
            }
            keys.sort = new Sort();
            keys.refresh();

            var leftForm:Form = new Form();
            leftForm.setStyle("paddingLeft", 0);
            leftForm.setStyle("paddingRight", 0);
            leftForm.setStyle("paddingTop", 0);
            leftForm.setStyle("paddingBottom", 0);
            leftForm.percentWidth = 50;
            leftForm.percentHeight = 100;
            layout.addChild(leftForm);
            var rightForm:Form = new Form();
            rightForm.setStyle("paddingLeft", 0);
            rightForm.setStyle("paddingRight", 0);
            rightForm.setStyle("paddingTop", 0);
            rightForm.setStyle("paddingBottom", 0);
            rightForm.percentWidth = 50;
            rightForm.percentHeight = 100;
            layout.addChild(rightForm);

            var mid:int = keys.length / 2 + 1;

            for (var i:int = 0; i < mid; i++) {
                var leftKey:String = keys.getItemAt(i) as String;
                var leftRenderer:DetailRenderer = new DetailRenderer();
                leftRenderer.value = _detailItem[leftKey];
                var leftItem:FormItem = new FormItem();
                leftItem.direction = "horizontal";
                leftItem.addChild(leftRenderer);
                leftItem.label = leftKey + ":";
                leftForm.addChild(leftItem);
                if (i + mid < (keys.length)) {
                    var rightKey:String = keys.getItemAt(i + mid) as String;
                    var rightRenderer:DetailRenderer = new DetailRenderer();
                    rightRenderer.value = _detailItem[rightKey];
                    var rightItem:FormItem = new FormItem();
                    rightItem.direction = "horizontal";
                    rightItem.label = rightKey + ":";
                    rightItem.addChild(rightRenderer);
                    rightForm.addChild(rightItem);
                }
            }
        }
        addChild(layout);
    }
}
}