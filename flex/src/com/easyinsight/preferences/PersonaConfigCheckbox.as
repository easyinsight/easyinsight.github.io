package com.easyinsight.preferences {
import mx.binding.utils.BindingUtils;
import mx.binding.utils.ChangeWatcher;
import mx.containers.HBox;
import mx.controls.CheckBox;

public class PersonaConfigCheckbox extends HBox {

    private var option:UIOption;

    private var checkbox:CheckBox;

    public function PersonaConfigCheckbox() {
        super();
        checkbox = new CheckBox();
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(checkbox);
    }

    private var watcher1:ChangeWatcher;
    private var watcher2:ChangeWatcher;
    private var watcher3:ChangeWatcher;

    override public function set data(val:Object):void {
        if (watcher1 != null) {
            watcher1.unwatch();
            watcher1 = null;
        }
        if (watcher2 != null) {
            watcher2.unwatch();
            watcher2 = null;
        }
        if (watcher3 != null) {
            watcher3.unwatch();
            watcher3 = null;
        }
        option = val as UIOption;
        if (option != null) {
            checkbox.visible = option.parent != null;
            watcher1 = BindingUtils.bindProperty(checkbox, "selected", option, "selected");
            watcher2 = BindingUtils.bindProperty(option, "selected", checkbox, "selected");
            if (option.parent != null) {
                watcher3 = BindingUtils.bindProperty(checkbox, "enabled", option.parent, "selected");
            }
        } else {
            checkbox.visible = false;
        }
    }

    override public function get data():Object {
        return option;
    }
}
}
