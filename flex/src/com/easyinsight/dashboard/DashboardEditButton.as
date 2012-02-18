/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/17/12
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.skin.ImageConstants;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;

public class DashboardEditButton extends HBox {
    
    private var stackButton:Button;
    private var deleteButton:Button;

    public var dashboardStack:DashboardStack;
    
    public function DashboardEditButton() {
        stackButton = new Button();
        stackButton.addEventListener(MouseEvent.CLICK, onClick);
        stackButton.styleName = "grayButton";

        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        
        setStyle("borderStyle", "solid");
        setStyle("borderThickness", 1);
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("paddingLeft", 2);
        setStyle("paddingRight", 2);
        setStyle("paddingTop", 2);
        setStyle("paddingBottom", 2);
    }

    override protected function createChildren():void {
        super.createChildren();
        BindingUtils.bindProperty(stackButton, "label", this, "label");
        addChild(stackButton);
        addChild(deleteButton);
    }
    
    private function onDelete(event:MouseEvent):void {
        if (dashboardStack.count == 1) {
            Alert.show("You can't delete all pages of a stack.");
        } else {
            dispatchEvent(new DashboardStackEvent(DashboardStackEvent.DELETE_PAGE));
        }
    }
    
    private function onClick(event:MouseEvent):void {
        dispatchEvent(new DashboardStackEvent(DashboardStackEvent.CLICK));
    }
}
}
