package com.easyinsight.reportpackage {
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class ReportPackageControls extends HBox {

    [Bindable]
    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var deleteButton:Button;

    private var insightDescriptor:InsightDescriptor;

    public function ReportPackageControls() {
        super();
        setStyle("horizontalAlign", "center");
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Remove this report from the package";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new PackageReportEvent(PackageReportEvent.REMOVE_REPORT, insightDescriptor));
    }

    override public function set data(val:Object):void {
        insightDescriptor = val as InsightDescriptor;
    }

    override public function get data():Object {
        return insightDescriptor;
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }
}
}