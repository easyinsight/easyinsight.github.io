package com.easyinsight.listing {
import mx.controls.Label;
public class SizeRenderer extends Label{

    private var _data:Object;

    public function SizeRenderer() {
        super();
    }

    override public function set data(value:Object):void {
        _data = value;
        if (_data is DataFeedDescriptor) {
            var descriptor:DataFeedDescriptor = _data as DataFeedDescriptor;
            if (descriptor.feedType == DataFeedDescriptor.ANALYSIS || descriptor.feedType == DataFeedDescriptor.COMPOSITE) {
                this.text = "( Derived )";
            } else {
                this.text = String(descriptor.size);
            }
        } else {
            this.text = "";
        }
    }

    override public function get data():Object {
        return _data;
    }
}
}