/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/12/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.skin.ImageConstants;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class DashboardStackEditButton extends HBox {

    private var deleteButton:Button;

    public function DashboardStackEditButton() {
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:Event):void {

    }
}
}
