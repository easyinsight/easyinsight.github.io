package com.easyinsight.solutions {
import com.easyinsight.administration.feed.ConnectionReports;
import com.easyinsight.skin.ConnectionSkinWindow;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;
public class SolutionAdminControls extends HBox{

    private var editButton:Button;
    private var styleButton:Button;
    private var deleteButton:Button;
    private var prebuiltButton:Button;

    private var solution:Solution;

    public function SolutionAdminControls() {
        super();
        editButton = new Button();
        editButton.label = "Edit";
        editButton.addEventListener(MouseEvent.CLICK, editSolution);
        styleButton = new Button();
        styleButton.label = "Styling";
        styleButton.addEventListener(MouseEvent.CLICK, editStyle);
        prebuiltButton = new Button();
        prebuiltButton.label = "Prebuilts";
        prebuiltButton.addEventListener(MouseEvent.CLICK, prebuilts);
        deleteButton = new Button();
        deleteButton.label = "Delete";
        deleteButton.addEventListener(MouseEvent.CLICK, deleteSolution);
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    private function editStyle(event:MouseEvent):void {
        var window:ConnectionSkinWindow = new ConnectionSkinWindow();
        window.connectionType = solution.dataSourceType;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function prebuilts(event:MouseEvent):void {
        var window:ConnectionReports = new ConnectionReports();
        window.dataSourceType = solution.dataSourceType;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(styleButton);
        addChild(prebuiltButton);
        addChild(deleteButton);
    }

    private function editSolution(event:MouseEvent):void {
        var window:CreateSolutionWindow = new CreateSolutionWindow();
        window.addEventListener(SolutionAdminEvent.SOLUTION_ADMIN, onAdminChange, false, 0, true);
        window.solution = solution;
        PopUpManager.addPopUp(window, this.parent.parent, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onAdminChange(event:SolutionAdminEvent):void {
        dispatchEvent(event);
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