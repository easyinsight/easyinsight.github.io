package com.easyinsight.groups {
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.framework.User;
import com.easyinsight.util.AutoSizeTextArea;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.VBox;
import mx.controls.Label;
import mx.controls.TextArea;

public class GroupView extends VBox{

    private var group:GroupDescriptor;
    private var _titleText:String;
    private var titleLabel:TextArea;
    private var box:VBox;

    private var backgroundColor:uint = 0xFFFFFF;

    public function GroupView() {
        super();
        titleLabel = new TextArea();
        titleLabel.setStyle("fontSize", 16);
        titleLabel.setStyle("backgroundAlpha", 0);
        titleLabel.editable = false;
        titleLabel.selectable = false;
        titleLabel.setStyle("borderStyle", "none");
        BindingUtils.bindProperty(titleLabel, "text", this, "titleText");
        setStyle("paddingLeft", 10);
        setStyle("paddingRight", 10);
        setStyle("paddingTop", 10);
        setStyle("paddingBottom", 10);
        setStyle("backgroundAlpha", 0);
        setStyle("horizontalAlign", "center");
        mouseChildren = false;
        buttonMode = true;
        useHandCursor = true;
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        var groupDetail:GroupDetail = new GroupDetail();
        groupDetail.groupID = group.groupID;
        User.getEventNotifier().dispatchEvent(new NavigationEvent(null, groupDetail));
    }

    [Bindable(event="titleTextChanged")]
    public function get titleText():String {
        return _titleText;
    }

    public function set titleText(value:String):void {
        if (_titleText == value) return;
        _titleText = value;
        dispatchEvent(new Event("titleTextChanged"));
    }

    override protected function createChildren():void {
        super.createChildren();
        box = new VBox();
        box.setStyle("borderStyle", "solid");
        box.setStyle("cornerRadius", 15);
        box.setStyle("dropShadowEnabled", "true");
        box.setStyle("backgroundAlpha", 1);
        box.setStyle("backgroundColor", backgroundColor);
        box.setStyle("horizontalAlign", "center");
        box.setStyle("verticalAlign", "middle");
        box.width = 190;
        box.height = 130;
        box.addChild(titleLabel);
        addChild(box);
    }

    override public function set data(val:Object):void {
        group = val as GroupDescriptor;
        titleText = group.name;
        toolTip = "Click to View this Group";        
        if (box != null) box.setStyle("backgroundColor", backgroundColor);

    }

    override public function get data():Object {
        return group;
    }
}
}