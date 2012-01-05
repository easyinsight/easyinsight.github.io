package com.easyinsight.preferences {
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class PersonaControls extends HBox {


    private var editButton:Button;
    private var deleteButton:Button;

    private var persona:Persona;

    public function PersonaControls() {
        super();
        editButton = new Button();
        editButton.setStyle("icon", ImageConstants.EDIT_ICON);
        editButton.toolTip = "Edit Persona...";
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.toolTip = "Delete Persona";
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    override public function set data(val:Object):void {
        persona = val as Persona;
    }

    override public function get data():Object {
        return persona;
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(editButton);
        addChild(deleteButton);
    }

    private function onEdit(event:MouseEvent):void {
        var window:PersonaConfigurationWindow = new PersonaConfigurationWindow();
        window.persona = persona;
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onDelete(event:MouseEvent):void {
        
    }
}
}