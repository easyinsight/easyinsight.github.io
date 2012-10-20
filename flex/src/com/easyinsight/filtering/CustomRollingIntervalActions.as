/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/18/12
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class CustomRollingIntervalActions extends HBox {

    private var editButton:Button;
    private var deleteButton:Button;

    private var interval:CustomRollingInterval;

    public function CustomRollingIntervalActions() {
        editButton = new Button();
        editButton.setStyle("icon", ImageConstants.EDIT_ICON);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        editButton.toolTip = "Edit...";

        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        deleteButton.toolTip = "Delete";
        setStyle("horizontalAlign", "center");
    }

    private function onEdit(event:MouseEvent):void {
        var window:CustomRollingIntervalWindow = new CustomRollingIntervalWindow();
        window.interval = interval;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new CustomRollingIntervalEvent(CustomRollingIntervalEvent.FILTER_DELETED, interval));
    }

    override public function set data(val:Object):void {
        interval = val as CustomRollingInterval;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }
}
}
