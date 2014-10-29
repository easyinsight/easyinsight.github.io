/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/17/12
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.listing.ArghButton;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
import mx.events.MenuEvent;
import mx.managers.PopUpManager;

public class DashboardEditButton extends HBox {
    
    /*private var stackButton:Button;
    private var deleteButton:Button;*/

    public var dashboardStack:DashboardStack;

    private var argh:ArghButton = new ArghButton();

    private var _dashboardBox:DashboardBox;

    private var _selected:Boolean;

    public function set dashboardBox(value:DashboardBox):void {
        _dashboardBox = value;
        if (_dashboardBox.element is DashboardStack || _dashboardBox.element is DashboardGrid) {
            var options:ArrayCollection = new ArrayCollection([{label: "Switch to", data: "switchToTab"},
                {label: "Remove", data: "removeStackEntry"},
                {label: "Rename", data: "rename"}]);
            argh.dataProvider = options;
        }
    }

    public function DashboardEditButton() {

        argh.addEventListener(MenuEvent.ITEM_CLICK, onItemClick);
        argh.labelField = "label";
        argh.openAlways = true;
        argh.styleName = "dashboardOpenTabButton";
        argh.setStyle("popUpStyleName", "dropAreaPopup");

        var options:ArrayCollection = new ArrayCollection([{label: "Switch to", data: "switchToTab"},
            {label: "Remove", data: "removeStackEntry"}]);



        argh.dataProvider = options;

        /*stackButton = new Button();
        stackButton.addEventListener(MouseEvent.CLICK, onClick);
        stackButton.styleName = "grayButton";

        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);*/

        setStyle("paddingLeft", 2);
        setStyle("paddingRight", 2);
        setStyle("paddingTop", 2);
        setStyle("paddingBottom", 2);


    }

    public function set selected(value:Boolean):void {
        _selected = value;
        if (_selected) {
            argh.setStyle("fillColors", [0xBEBEBE, 0xBEBEBE, 0xBEBEBE, 0xBEBEBE]);
        } else {
            argh.setStyle("fillColors", [0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF]);
        }
    }

    private function onChange(event:Event):void {
        argh.label = _dashboardBox.element.label;
    }

    private function onItemClick(event:MenuEvent):void {
        var target:String = event.item.data;
        if (target == "removeStackEntry") {
            dispatchEvent(new DashboardStackEvent(DashboardStackEvent.DELETE_PAGE));
        } else if (target == "switchToTab") {
            dispatchEvent(new DashboardStackEvent(DashboardStackEvent.CLICK));
        } else if (target == "rename") {
            var window:ElementLabelWindow = new ElementLabelWindow();
            window.dashboardElement = _dashboardBox.element;
            window.addEventListener(Event.CHANGE, onChange, false, 0, true);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        }
    }

    override protected function createChildren():void {
        super.createChildren();
        BindingUtils.bindProperty(argh, "label", this, "label");
        addChild(argh);
        /*addChild(stackButton);
        addChild(deleteButton);*/
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
