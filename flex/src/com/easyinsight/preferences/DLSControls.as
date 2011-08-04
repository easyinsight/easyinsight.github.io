/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.preferences {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class DLSControls extends HBox {

    private var dls:DataSourceDLS;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    private var editButton:Button;
    private var deleteButton:Button;

    public function DLSControls() {
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        editButton = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.toolTip = "Edit...";
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Delete";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }

    private function onEdit(event:MouseEvent):void {
        var window:DLSConfigWindow = new DLSConfigWindow();
        window.dls = dls;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new DLSEvent(dls));
    }

    override public function set data(val:Object):void {
        dls = val as DataSourceDLS;
    }

    override public function get data():Object {
        return dls;
    }
}
}
