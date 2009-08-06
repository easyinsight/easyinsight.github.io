package com.easyinsight.goals {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;
public class MilestoneControls extends HBox{

    [Embed(source="../../../../assets/calendar.png")]
    private var editIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var editButton:Button;
    private var deleteButton:Button;

    private var milestone:GoalTreeMilestone;

    public function MilestoneControls() {
        super();
        editButton = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
    }

    override public function set data(val:Object):void {
        this.milestone = val as GoalTreeMilestone;
    }

    override public function get data():Object {
        return this.milestone;
    }

    private function onEdit(event:MouseEvent):void {
        var window:MilestoneEditWindow = new MilestoneEditWindow();
        window.goalTreeMilestone = milestone;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new DeleteMilestoneEvent(milestone));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }
}
}