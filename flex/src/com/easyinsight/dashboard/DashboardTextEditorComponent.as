package com.easyinsight.dashboard {

import com.easyinsight.util.AutoSizeTextArea;

import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.TextArea;
import mx.managers.PopUpManager;

public class DashboardTextEditorComponent extends VBox implements IDashboardEditorComponent {

    public var dashboardText:DashboardTextElement;

    private var textArea:TextArea;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    public function DashboardTextEditorComponent() {
        super();
        /*setStyle("verticalAlign", "middle");
        setStyle("horizontalAlign", "center");*/

    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo();
    }

    protected override function createChildren():void {
        super.createChildren();
        textArea = new TextArea();
        var sizeInfo:SizeInfo = obtainPreferredSizeInfo();
        if (sizeInfo.preferredWidth > 0) {
            width = dashboardText.preferredWidth;
            textArea.width = dashboardText.preferredWidth;
        } else {
            percentWidth = 100;
            textArea.percentWidth = 100;
        }
        if (sizeInfo.preferredHeight > 0) {
            height = dashboardText.preferredHeight;
            textArea.height = dashboardText.preferredHeight;
        } else if (dashboardEditorMetadata.dashboard.absoluteSizing) {
            height = 400;
            textArea.height = 400;
        } else {
            height = 400;
            textArea.height = 400;
        }
        textArea.editable = true;
        textArea.text = dashboardText.text;
        addChild(textArea);
    }

    public function save():void {
        dashboardText.text = textArea.text;
    }

    public function validate(results:Array):void {
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