package com.easyinsight.framework {
public class DetailLinkParser {

    protected var id:int;

    public function DetailLinkParser() {
    }

    public function matches(o:Object):Boolean {
        var idString:String = o[getIDName()];
        if (idString != null) {
            this.id = int(idString);
            execute();
            return true;
        } else {
            return false;
        }
    }

    protected function execute():void {

    }

    protected function getIDName():String {
        return null;
    }
}
}