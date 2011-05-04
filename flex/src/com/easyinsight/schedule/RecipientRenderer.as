package com.easyinsight.schedule {
import com.easyinsight.administration.sharing.UserStub;

import mx.controls.Label;

public class RecipientRenderer extends Label {

    private var obj:Object;

    public function RecipientRenderer() {
        super();
    }

    override public function set data(val:Object):void {
        obj = val;
        if (obj is UserStub) {
            text = UserStub(obj).displayName;
        } else {
            text = String(obj);
        }
    }

    override public function get data():Object {
        return obj;
    }
}
}