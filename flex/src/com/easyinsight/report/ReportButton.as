package com.easyinsight.report {

import com.easyinsight.quicksearch.EIDescriptor;

import mx.containers.Box;
import mx.controls.Label;

public class ReportButton extends Box {

    private var report:EIDescriptor;

    private var _reportSelected:Boolean;

    private var reportLabel:Label;

    private var box:Box;
    
    public function ReportButton() {
        super();
        box = new Box();
        box.setStyle("horizontalAlign", "center");
        box.setStyle("borderStyle", "solid");
        box.setStyle("borderThickness", 1);
        box.setStyle("cornerRadius", 8);
        box.setStyle("dropShadowEnabled", true);
        box.setStyle("backgroundColor", 0xFFFFFF);
        box.percentWidth = 100;
        reportLabel = new Label();
        reportLabel.setStyle("fontSize", 14);
        reportLabel.maxWidth = 130;
        setStyle("paddingLeft", 5);
        setStyle("paddingRight", 5);
        setStyle("paddingTop", 5);
        setStyle("paddingBottom", 5);
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        percentWidth = 100;
        percentHeight = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        box.addChild(reportLabel);
        addChild(box);
    }

    override public function set data(val:Object):void {
        this.report = val as EIDescriptor;
        reportLabel.text = this.report.name;
    }

    override public function get data():Object {
        return report;
    }
}
}