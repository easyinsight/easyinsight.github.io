package com.easyinsight.customupload.api {
import flash.events.MouseEvent;
import mx.controls.Button;
import mx.containers.HBox;
public class DynamicServiceEditControls extends HBox {

    private var editButton:Button;
    private var deleteButton:Button;
    private var configuredMethod:ConfiguredMethod;

    [Bindable]
    [Embed(source="../../../../../assets/navigate_cross.png")]
    public var closeIcon:Class;

    [Bindable]
    [Embed(source="../../../../../assets/pencil.png")]
    public var editIcon:Class;

    public function DynamicServiceEditControls() {
        super();
        editButton = new Button();
        editButton.toolTip = "Edit...";
        editButton.setStyle("icon", editIcon);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        addChild(editButton);
        deleteButton = new Button();
        deleteButton.toolTip = "Delete";
        deleteButton.setStyle("icon", closeIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        addChild(deleteButton);
        setStyle("percentWidth", 100);
        setStyle("horizontalAlign", "center");
    }

    private function onEdit(event:MouseEvent):void {
        parent.dispatchEvent(new EditMethodEvent(configuredMethod));
    }

    private function onDelete(event:MouseEvent):void {
        parent.dispatchEvent(new MethodDefinitionEvent(MethodDefinitionEvent.METHOD_REMOVED, configuredMethod));
    }

    override public function set data(object:Object):void {
        this.configuredMethod = object as ConfiguredMethod;
    }

    override public function get data():Object {
        return this.configuredMethod;
    }
}
}