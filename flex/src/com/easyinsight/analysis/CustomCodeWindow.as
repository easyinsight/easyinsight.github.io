package com.easyinsight.analysis {
import com.easyinsight.pseudocontext.CustomCodeEvent;
import com.easyinsight.util.EITitleWindow;


import flash.events.MouseEvent;

import mx.containers.VBox;
import mx.controls.Button;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

public class CustomCodeWindow extends EITitleWindow {

    private var coreContent:VBox;

    public function CustomCodeWindow() {
        super();
    }

    private var detailObject:UIComponent;

    protected override function createChildren():void {
        super.createChildren();
        if (coreContent == null) {
            coreContent = new VBox();
            coreContent.setStyle("horizontalAlign", "center");
        }
        addChild(coreContent);
        coreContent.addChild(detailObject);
        var closeButton:Button = new Button();
        closeButton.label = "Close";
        closeButton.addEventListener(MouseEvent.CLICK, onClose);
        coreContent.addChild(closeButton);
    }

    private function onClose(event:MouseEvent):void {
        PopUpManager.removePopUp(this);
    }

    public static function newWindow(parent:UIComponent, event:CustomCodeEvent, dataSourceID:int):void {
        /*var window:CustomCodeWindow = new CustomCodeWindow();
        var hrWindow:HighriseNotesWindow = new HighriseNotesWindow();
        hrWindow.spec = event.data as HighriseNotesSpec;
        hrWindow.dataSourceID = dataSourceID;
        window.detailObject = hrWindow;
        PopUpManager.addPopUp(window, parent, true);
        PopUpUtil.centerPopUp(window);*/
    }
}
}