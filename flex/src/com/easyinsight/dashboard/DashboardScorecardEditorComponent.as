package com.easyinsight.dashboard {
import mx.containers.VBox;
import mx.controls.Image;
import mx.controls.Label;
import mx.managers.PopUpManager;

public class DashboardScorecardEditorComponent extends VBox implements IDashboardEditorComponent {

    [Embed(source="../../../../assets/arrow2_up_green.png")]
    private var reportIcon:Class;

    public var scorecard:DashboardScorecard;

    public function DashboardScorecardEditorComponent() {
        super();
        setStyle("verticalAlign", "middle");
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    protected override function createChildren():void {
        super.createChildren();
        var image:Image = new Image();
        image.source = reportIcon;
        addChild(image);
        var label:Label = new Label();
        label.text = scorecard.scorecard.name;
        addChild(label);
    }

    public function save():void {
    }

    public function validate():Boolean {
        return true;
    }

    public function edit():void {
        var window:DashboardEditWindow = new DashboardEditWindow();
        window.dashboardElement = scorecard;
        PopUpManager.addPopUp(window, this, true);
    }
}
}