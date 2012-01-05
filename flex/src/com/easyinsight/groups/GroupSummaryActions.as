/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/27/11
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.groups {
import com.easyinsight.skin.ImageConstants;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class GroupSummaryActions extends HBox {

    private var editButton:Button;
    private var deleteButton:Button;

    private var group:GroupDescriptor;

    public function GroupSummaryActions() {
        editButton = new Button();
        editButton.setStyle("icon", ImageConstants.EDIT_ICON);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        setStyle("horizontalAlign", "center");
    }

    private function onEdit(event:MouseEvent):void {
        dispatchEvent(new GroupSelectedEvent(GroupSelectedEvent.GROUP_SELECTED, group.groupID));
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new GroupSelectedEvent(GroupSelectedEvent.GROUP_DELETE, group.groupID));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        this.group = val as GroupDescriptor;
    }

    override public function get data():Object {
        return this.group;
    }
}
}
