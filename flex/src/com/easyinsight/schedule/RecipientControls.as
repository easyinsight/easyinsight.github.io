package com.easyinsight.schedule {
import com.easyinsight.administration.sharing.UserStub;

import flash.events.MouseEvent;

import mx.controls.Button;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class RecipientControls extends UIComponent implements IListItemRenderer {

    private var obj:Object;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var deleteButton:Button;
    
    public function RecipientControls() {
        super();
    }

    override protected function createChildren():void {
        super.createChildren();
        if (deleteButton == null) {
            deleteButton = new Button();
            deleteButton.setStyle("icon", deleteIcon);
            deleteButton.toolTip = "Delete scheduled activity";
            deleteButton.addEventListener(MouseEvent.CLICK, deleteActivity);
        }
        addChild(deleteButton);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var buttonWidth:int = 40;
        var buttonHeight:int = 22;
        var padding:int = 5;
        deleteButton.move((padding),0);
        deleteButton.setActualSize(buttonWidth, buttonHeight);
    }

    private function deleteActivity(event:MouseEvent):void {
        if (obj is UserStub) dispatchEvent(new RecipientEvent(RecipientEvent.DELETE_RECIPIENT, UserStub(obj)));
        else dispatchEvent(new RecipientEvent(RecipientEvent.DELETE_RECIPIENT, null, String(obj)));
    }

    [Bindable("dataChange")]
    public function set data(value:Object):void {
        obj = value;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return obj;
    }
}
}