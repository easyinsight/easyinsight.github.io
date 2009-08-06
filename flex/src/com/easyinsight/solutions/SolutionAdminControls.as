package com.easyinsight.solutions {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;
public class SolutionAdminControls extends HBox{

    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var editButton:Button;
    private var deleteButton:Button;

    private var solution:Solution;

    public function SolutionAdminControls() {
        super();
        editButton = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.addEventListener(MouseEvent.CLICK, editSolution);
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, deleteSolution);
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }

    private function editSolution(event:MouseEvent):void {
        var window:CreateSolutionWindow = new CreateSolutionWindow();
        window.solution = solution;
        PopUpManager.addPopUp(window, this.parent.parent, true);
        PopUpUtil.centerPopUp(window);
    }

    override public function set data(val:Object):void {
        this.solution = val as Solution;
    }

    override public function get data():Object {
        return this.solution;
    }

    private function deleteSolution(event:MouseEvent):void {
        dispatchEvent(new DeleteSolutionEvent(solution.solutionID));
    }
}
}