package com.easyinsight.groups {
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.framework.User;
import flash.events.MouseEvent;
import mx.controls.Button;
import mx.containers.HBox;
public class MyGroupsControls extends HBox {

    private var groupDescriptor:GroupDescriptor;

    [Bindable]
    [Embed(source="../../../../assets/media_play_green.png")]
    private var goIcon:Class;

    private var playButton:Button;

    public function MyGroupsControls() {
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        playButton = new Button();
        playButton.setStyle("icon", goIcon);
        playButton.toolTip = "Enter Group";
        playButton.addEventListener(MouseEvent.CLICK, goToGroup);
    }

    private function goToGroup(event:MouseEvent):void {
        var groupDetail:GroupDetail = new GroupDetail();
        groupDetail.groupID = groupDescriptor.groupID;
        User.getEventNotifier().dispatchEvent(new NavigationEvent(null, groupDetail));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(playButton);
    }

    override public function set data(value:Object):void {
        this.groupDescriptor = value as GroupDescriptor;
    }

    override public function get data():Object {
        return this.groupDescriptor;
    }
}
}