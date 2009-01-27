package com.easyinsight.goals {
import flash.events.MouseEvent;
import mx.controls.Button;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionEvent;
import mx.containers.HBox;
public class InstallSolutionToGoalControls extends HBox {

    [Embed(source="../../../../assets/component_blue_add.png")]
    private var installIcon:Class;

    private var solution:Solution;

    override public function set data(data:Object):void {
        this.solution = data as Solution;
    }

    override public function get data():Object {
        return this.solution;
    }

    override protected function createChildren():void {
        super.createChildren();
        var button:Button = new Button();
        button.setStyle("icon", installIcon);
        button.toolTip = "Install Solution into Goal";
        button.addEventListener(MouseEvent.CLICK, installSolution);
        addChild(button);
    }

    private function installSolution(event:MouseEvent):void {
        dispatchEvent(new GoalSolutionInstallEvent(solution));
    }
}
}