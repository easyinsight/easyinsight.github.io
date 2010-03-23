package com.easyinsight.preferences {
import mx.binding.utils.BindingUtils;
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

    override public function set data(val:Object):void {
        option = val as UIOption;
        if (option != null) {
            checkbox.visible = option.parent != null;
            BindingUtils.bindProperty(checkbox, "selected", option, "selected");
            BindingUtils.bindProperty(option, "selected", checkbox, "selected");
            if (option.parent != null) {
                BindingUtils.bindProperty(checkbox, "enabled", option.parent, "selected");                
            }
        }
    }

    override public function get data():Object {
        return option;
    }
}
}
