/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/31/12
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.controls.Alert;

import mx.controls.Menu;

import mx.core.mx_internal;
import mx.managers.DragManager;

use namespace mx_internal;
import mx.controls.PopUpMenuButton;

public class DashboardArghButton extends PopUpMenuButton {
    public function DashboardArghButton() {
    }

    /*mx_internal override function overArrowButton(event:MouseEvent):Boolean {
        return true;
    }*/

    override protected function clickHandler(event:MouseEvent):void {
        if (!DragManager.isDragging) {
            super.clickHandler(event);
        }
        if (!overArrowButton(event)) {
            dispatchEvent(new Event("stackClick"));
        }
    }

    public function invalidateMenuDataProvider(data:ArrayCollection):void {
        Menu(getPopUp()).dataProvider = data;
    }
}
}
