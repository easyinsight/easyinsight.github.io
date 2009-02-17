package com.easyinsight.goals {
import mx.containers.HBox;
import mx.controls.Image;
public class GoalIconRenderer extends HBox{

    private var image:Image;

    public function GoalIconRenderer() {
        super();
        image = new Image();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        if (this._goalTreeNodeData.iconImage != null) {
            image.load("/DMS/assets/icons/16x16/" + this._goalTreeNodeData.iconImage);
        }
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}