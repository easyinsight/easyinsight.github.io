package com.easyinsight.goals {
import com.easyinsight.solutions.Solution;
import mx.containers.HBox;
import mx.controls.Image;
public class SolutionGoalTreeIcon extends HBox{

    private var solution:Solution;

    [Bindable]
    [Embed(source="../../../../assets/branchx16.png")]
    private var branchIcon:Class;

    private var image:Image;

    public function SolutionGoalTreeIcon() {
        super();
        percentWidth = 100;
        setStyle("horizontalAlign", "center");
        image = new Image();
        image.source = branchIcon;
        image.visible = false;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    override public function set data(val:Object):void {
        this.solution = val as Solution;
        image.visible = solution.goalTreeID > 0;
    }

    override public function get data():Object {
        return this.solution;
    }
}
}