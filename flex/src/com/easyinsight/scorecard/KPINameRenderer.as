package com.easyinsight.scorecard {
import mx.controls.Alert;
import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;

public class KPINameRenderer extends KPIRenderer implements IListItemRenderer {

    private var kpiLabel:Label;

    public function KPINameRenderer() {
        super();
        kpiLabel = new Label();
        kpiLabel.setStyle("fontSize", 14);
        kpiLabel.x = 5;
        kpiLabel.y = 8;
        /*kpiLabel.mouseEnabled = false; */
    }

    override protected function measure():void {
        super.measure();
        //Alert.show("grr");
        measuredWidth = 360;
        measuredHeight = 40;
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(kpiLabel);
    }

    [Bindable("dataChange")]
    override public function set data(val:Object):void {
        super.data = val;
        kpiLabel.text = kpi.name;
    }

    /*override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        kpiLabel.move(5,8);
        kpiLabel.setActualSize(unscaledWidth, unscaledHeight);
    }*/
}
}