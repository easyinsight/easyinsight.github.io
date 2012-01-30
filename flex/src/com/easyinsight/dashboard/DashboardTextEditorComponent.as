package com.easyinsight.dashboard {

import com.easyinsight.util.AutoSizeTextArea;

import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.TextArea;
import mx.managers.PopUpManager;

public class DashboardTextEditorComponent extends VBox implements IDashboardEditorComponent {

    public var dashboardText:DashboardTextElement;

    private var textArea:AutoSizeTextArea;

    public function DashboardTextEditorComponent() {
        super();
        setStyle("verticalAlign", "middle");
        setStyle("horizontalAlign", "center");
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo();
    }

    protected override function createChildren():void {
        super.createChildren();
        textArea = new AutoSizeTextArea();
        if (dashboardText.preferredHeight == 0) {
            textArea.percentHeight = 100;
            this.percentHeight = 100;
        } else {
            textArea.height = dashboardText.preferredHeight - 60;
            this.height = dashboardText.preferredHeight;
        }
        if (dashboardText.preferredWidth == 0) {
            textArea.percentWidth = 100;
            this.percentWidth = 100;
        } else {
            textArea.width = dashboardText.preferredWidth;
            this.width = dashboardText.preferredWidth;
        }
        textArea.editable = true;
        textArea.text = dashboardText.text;
        addChild(textArea);
    }

    public function save():void {
        dashboardText.text = textArea.text;
    }

    public function validate():Boolean {
        return true;
    }

    public function edit():void {
        var window:DashboardEditWindow = new DashboardEditWindow();
        window.dashboardElement = dashboardText;
        PopUpManager.addPopUp(window, this, true);
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