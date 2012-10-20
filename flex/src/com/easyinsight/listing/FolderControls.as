/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/15/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.solutions.CustomFolder;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class FolderControls extends HBox {

    private var editButton:Button;
    private var deleteButton:Button;

    public function FolderControls() {
        editButton = new Button();
        editButton.setStyle("icon", ImageConstants.EDIT_ICON);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        setStyle("paddingLeft", 5);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }

    private var folder:CustomFolder;

    override public function set data(val:Object):void {
        folder = val as CustomFolder;
    }

    private function onEdit(event:MouseEvent):void {
        dispatchEvent(new FolderEvent(FolderEvent.RENAME_FOLDER, folder));
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new FolderEvent(FolderEvent.DELETE_FOLDER, folder));
    }
}
}
