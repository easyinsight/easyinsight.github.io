package com.easyinsight.goals {
import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Button;
public class AssociatedInsightsAdminControls extends HBox{

    private var goalInsight:GoalInsight;

    [Bindable]
    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var deleteButton:Button;

    public function AssociatedInsightsAdminControls() {
        super();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Delete Association";
        deleteButton.addEventListener(MouseEvent.CLICK, deleteAssociation);
    }

    private function deleteAssociation():void {
        dispatchEvent(new DeleteAssociationEvent(DeleteAssociationEvent.DELETE_REPORT, goalInsight));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        goalInsight = val as GoalInsight;
    }

    override public function get data():Object {
        return goalInsight;
    }
}
}