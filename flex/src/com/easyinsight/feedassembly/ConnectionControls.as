/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/28/11
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class ConnectionControls extends HBox {

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var deleteButton:Button;

    private var connection:CompositeFeedConnection;

    public function ConnectionControls() {
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Remove this data source";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        setStyle("horizontalAlign", "center");
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new ConnectionDeleteEvent(connection));
    }

    override protected function createChildren():void {
        super.createChildren();
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
