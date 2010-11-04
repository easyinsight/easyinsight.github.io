package com.easyinsight.guest {
import mx.containers.VBox;
import mx.controls.Label;
import mx.controls.List;
import mx.controls.listClasses.BaseListData;
import mx.controls.listClasses.IDropInListItemRenderer;

public class GuestScenarioRenderer extends VBox implements IDropInListItemRenderer {

    private var topLabel:Label;
    private var bottomLabel:Label;
    private var scenario:Scenario;

    public function GuestScenarioRenderer() {
        super();
        topLabel = new Label();
        topLabel.setStyle("fontWeight", "bold");
        bottomLabel = new Label();
        this.width = 300;
        this.height = 60;
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(topLabel);
        addChild(bottomLabel);
    }

    override public function set data(val:Object):void {
        scenario = val as Scenario;
        if (scenario != null) {
            topLabel.text = scenario.name;
            bottomLabel.text = scenario.summary;
        }
    }

    override public function get data():Object {
        return scenario;
    }

    protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        blah();
    }

    private function blah():void {
        if (this.owner is List && ((this.owner as List).isItemSelected(scenario))) {
            setStyle("backgroundColor", 0x8C8C8C);
        } else if (this.owner is List && ((this.owner as List).isItemHighlighted(scenario))) {
            setStyle("backgroundColor", 0xACACAC);
        } else {
            if (rowIndex % 2 == 0) {
                setStyle("backgroundColor", 0xCCCCCC);
            } else {
                setStyle("backgroundColor", 0xDCDCDC);
            }
        }
    }

    public function get listData():BaseListData {
        return null;
    }

    private var rowIndex:int = 0;

    public function set listData(value:BaseListData):void {
        if (value != null) {
            rowIndex = value.rowIndex;
            blah();
        }
    }
}
}