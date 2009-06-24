package com.easyinsight.goals {
import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Button;
public class AssociatedFeedsAdminControls extends HBox{

    private var goalFeed:GoalFeed;

    [Bindable]
    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var deleteButton:Button;

    public function AssociatedFeedsAdminControls() {
        super();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Delete Association";
        deleteButton.addEventListener(MouseEvent.CLICK, deleteAssociation);
    }

    private function deleteAssociation(event:MouseEvent):void {
        dispatchEvent(new DeleteAssociationEvent(DeleteAssociationEvent.DELETE_DATA_SOURCE, goalFeed));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        goalFeed = val as GoalFeed;
    }

    override public function get data():Object {
        return goalFeed;
    }
}
}