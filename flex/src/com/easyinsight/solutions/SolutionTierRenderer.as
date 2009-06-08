package com.easyinsight.solutions {
import com.easyinsight.account.Account;

import mx.containers.HBox;
import mx.controls.Label;

public class SolutionTierRenderer extends HBox{

    private var tierLabel:Label;
    private var solution:Solution;

    public function SolutionTierRenderer() {
        super();
        this.percentWidth = 100;
        this.setStyle("horizontalAlign", "center");
        tierLabel = new Label();
    }

    override public function get data():Object {
        return solution;
    }

    override public function set data(obj:Object):void {
        solution = obj as Solution;
        switch (solution.solutionTier) {
            case Account.FREE:
                tierLabel.text = "Free";
                break;
            case Account.INDIVIDUAL:
                tierLabel.text = "Individual";
                break;
            case Account.GROUP:
                tierLabel.text = "Group";
                break;
            case Account.PROFESSIONAL:
                tierLabel.text = "Professional";
                break;
            case Account.ENTERPRISE:
                tierLabel.text = "Enterprise";
                break;
        }
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(tierLabel);
    }
}
}