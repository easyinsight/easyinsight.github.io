/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/9/11
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class FederatedControls extends HBox {



    private var editButton:Button;
    private var deleteButton:Button;

    private var source:FederationSource;

    public function FederatedControls() {
        super();
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        deleteButton.toolTip = "Delete";

        editButton = new Button();
        editButton.setStyle("icon", ImageConstants.EDIT_ICON);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        editButton.toolTip = "Edit";
        setStyle("horizontalAlign", "center");
    }

    private function onEdit(event:MouseEvent):void {
        var window:FedEditWindow = new FedEditWindow();
        window.fedSource = source;
        PopUpManager.addPopUp(window,  this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new DeleteFederatedEvent(source));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        source = val as FederationSource;
    }

    override public function get data():Object {
        return source;
    }
}
}
