/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/9/11
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class FederatedControls extends HBox {

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var deleteButton:Button;

    private var source:FederationSource;

    public function FederatedControls() {
        super();
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        deleteButton.toolTip = "Delete";
        setStyle("horizontalAlign", "center");
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new DeleteFederatedEvent(source));
    }

    override protected function createChildren():void {
        super.createChildren();
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
