package com.easyinsight.dashboard {

import com.easyinsight.util.AutoSizeTextArea;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.controls.TextArea;

public class DashboardTextViewComponent extends Box implements IDashboardViewComponent  {

    public var dashboardText:DashboardTextElement;

    private var textArea:TextArea;

    public function DashboardTextViewComponent() {
        super();
        percentWidth = 100;
        percentHeight = 100;
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo();
    }

    protected override function createChildren():void {
        super.createChildren();
        textArea = new AutoSizeTextArea();
        textArea.percentWidth = 100;
        textArea.editable = false;
        textArea.text = dashboardText.text;
        addChild(textArea);
    }

    public function refresh():void {
    }

    public function updateAdditionalFilters(filters:Object):void {
    }

    public function initialRetrieve():void {
    }

    public function reportCount():ArrayCollection {
        return new ArrayCollection();
    }

    public function toggleFilters(showFilters:Boolean):void {
    }
}
}