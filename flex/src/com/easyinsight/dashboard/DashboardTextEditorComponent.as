package com.easyinsight.dashboard {

import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.TextArea;

public class DashboardTextEditorComponent extends VBox implements IDashboardEditorComponent {

    public var dashboardText:DashboardTextElement;

    private var textArea:TextArea;

    public function DashboardTextEditorComponent() {
        super();
        setStyle("verticalAlign", "middle");
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo();
    }

    protected override function createChildren():void {
        super.createChildren();
        textArea = new TextArea();
        textArea.editable = true;
        addChild(textArea);
    }

    public function save():void {
        dashboardText.text = textArea.text;
    }

    public function validate():Boolean {
        return true;
    }

    public function edit():void {
    }

    public function refresh():void {
    }

    public function updateAdditionalFilters(filterMap:Object):void {
    }

    public function initialRetrieve():void {
    }

    public function reportCount():ArrayCollection {
        return null;
    }

    public function toggleFilters(showFilters:Boolean):void {
    }
}
}