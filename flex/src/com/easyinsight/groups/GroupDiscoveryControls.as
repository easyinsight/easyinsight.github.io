package com.easyinsight.groups {
import flash.events.MouseEvent;
import mx.controls.Button;
import mx.containers.HBox;
public class GroupDiscoveryControls extends HBox {
    private var groupDescriptor:GroupDescriptor;
    private var enterGroupButton:Button;

    [Bindable]
    [Embed(source="../../../../assets/media_play_green.png")]
    private var goIcon:Class;

    public function GroupDiscoveryControls() {
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        enterGroupButton = new Button();
        enterGroupButton.setStyle("icon", goIcon);
        enterGroupButton.addEventListener(MouseEvent.CLICK, selectGroup);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(enterGroupButton);
    }

    private function selectGroup(event:MouseEvent):void {
        dispatchEvent(new GroupSelectedEvent(groupDescriptor.groupID));
    }

    override public function set data(val:Object):void {
        this.groupDescriptor = val as GroupDescriptor;
    }

    override public function get data():Object {
        return this.groupDescriptor;
    }
}
}