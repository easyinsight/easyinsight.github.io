package com.easyinsight.dashboard {

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
}
}