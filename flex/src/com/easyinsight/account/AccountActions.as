package com.easyinsight.account {
import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;
public class AccountActions extends HBox{

    private var editAccountButton:Button;

    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var editButton:Button;
    private var deleteButton:Button;

    private var account:AccountAdminTO;

    public function AccountActions() {
        super();
        editButton = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        this.setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }


    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }

    private function onEdit(event:MouseEvent):void {
        var window:AdminEditAccountWindow = new AdminEditAccountWindow();
        window.accountAdminTO = account;
        window.addEventListener(RefreshAccountsListEvent.REFRESH_ACCOUNTS_LIST, onChange);
        PopUpManager.addPopUp(window, this.parent.parent, true);
        PopUpManager.centerPopUp(window);
    }

    private function onChange(event:RefreshAccountsListEvent):void {
        dispatchEvent(event);
    }

    private function onDelete(event:MouseEvent):void {
        
    }

    override public function set data(val:Object):void {
        this.account = val as AccountAdminTO;
    }

    override public function get data():Object {
        return this.account;
    }
}
}