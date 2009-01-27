package com.easyinsight.groups {
import mx.controls.Label;
public class GroupDescriptionRenderer extends Label {
    private var groupDescriptor:GroupDescriptor;

    public function GroupDescriptionRenderer() {
        this.maxWidth = 290;
    }

    override public function set data(value:Object):void {
        this.groupDescriptor = value as GroupDescriptor;
        this.text = groupDescriptor.description;
    }

    override public function get data():Object {
        return this.groupDescriptor;
    }
}
}