package com.easyinsight.account {
import flash.display.DisplayObject;
import mx.core.IFactory;
public class EditUserButtonFactory implements IFactory {

    private var parent:DisplayObject;


    public function EditUserButtonFactory(parent:DisplayObject) {
        this.parent = parent;
    }
    public function newInstance():* {
        var editable:Boolean = true;
        return new EditUserButton(parent, editable);
    }
}
}