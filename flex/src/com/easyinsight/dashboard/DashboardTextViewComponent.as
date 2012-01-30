package com.easyinsight.dashboard {

import com.easyinsight.util.AutoSizeTextArea;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.controls.Alert;
import mx.controls.TextArea;

public class DashboardTextViewComponent extends Box implements IDashboardViewComponent  {

    public var dashboardText:DashboardTextElement;

    public var metadata:DashboardEditorMetadata;

    private var textArea:TextArea;

    public function DashboardTextViewComponent() {
        super();
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        /*setStyle("paddingLeft", 10);
        setStyle("paddingTop", 10);
        setStyle("paddingRight", 10);
        setStyle("paddingBottom", 10);*/
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("backgroundAlpha", 1);
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo(dashboardText.preferredWidth, dashboardText.preferredHeight);
    }

    protected override function createChildren():void {
        super.createChildren();
        var canvas:Box = new Box();
        if (metadata.dashboard.borderThickness != 0) {
            setStyle("borderStyle", "inset");
            setStyle("borderThickness", 3);
            setStyle("borderColor", 0x00000);
            canvas.setStyle("borderStyle", "solid");
            canvas.setStyle("borderThickness", 1);
            canvas.setStyle("cornerRadius", 8);
            canvas.setStyle("dropShadowEnabled", true);
            canvas.setStyle("paddingLeft", 10);
            canvas.setStyle("paddingRight", 10);
            canvas.setStyle("paddingBottom", 10);
            canvas.setStyle("paddingTop", 10);
        }
        canvas.percentHeight = 100;
        canvas.percentWidth = 100;
        addChild(canvas);
        textArea = new TextArea();
        textArea.setStyle("textAlign", "center");
        textArea.setStyle("fontSize", 14);
        if (dashboardText.preferredHeight == 0) {
            textArea.percentHeight = 100;
            this.percentHeight = 100;
        } else {
            textArea.height = dashboardText.preferredHeight;
            this.height = dashboardText.preferredHeight;
        }
        if (dashboardText.preferredWidth == 0) {
            textArea.percentWidth = 100;
            this.percentWidth = 100;
        } else {
            textArea.width = dashboardText.preferredWidth;
            this.width = dashboardText.preferredWidth;
        }
        textArea.editable = false;
        textArea.text = dashboardText.text;
        canvas.addChild(textArea);
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