/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/28/11
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {

import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class ConnectionControls extends HBox {





    private var deleteButton:Button;
    private var editButton:Button;

    private var connection:CompositeFeedConnection;

    public function ConnectionControls() {
        editButton = new Button();
        editButton.setStyle("icon", ImageConstants.EDIT_ICON);
        editButton.toolTip = "Edit this connection...";
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.toolTip = "Remove this connection";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        setStyle("horizontalAlign", "center");
    }

    private function onEdit(event:MouseEvent):void {
        var window:DSEditJoinWindow = new DSEditJoinWindow();
        window.metadataFields = _fields;
        window.existingConnection = connection;
        PopUpManager.addPopUp(window,  this, true);
        PopUpUtil.centerPopUp(window);
    }

    private var _fields:ArrayCollection;

    public function set fields(value:ArrayCollection):void {
        _fields = value;
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new ConnectionDeleteEvent(connection));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        connection = val as CompositeFeedConnection;
    }

    override public function get data():Object {
        return connection;
    }
}
}
