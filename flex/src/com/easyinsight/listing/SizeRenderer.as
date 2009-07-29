package com.easyinsight.listing {
import mx.controls.Label;
public class SizeRenderer extends Label{



    public function SizeRenderer() {
        super();
    }

    private var _data:Object;

    override public function set data(value:Object):void {
        _data = value;
        if (_data is DataFeedDescriptor) {
            var descriptor:DataFeedDescriptor = _data as DataFeedDescriptor;
            if (descriptor.feedType == DataFeedDescriptor.ANALYSIS) {
                this.text = "( Derived )";
            } else if (descriptor.size == 0) {
                this.text = "( Live )";
            } else {
                this.text = String(descriptor.size);
            }
            if (descriptor.solutionTemplate) {
                setStyle("textDecoration", "underline");
                toolTip = "This data source is the template for a solution.";
            } else {
                setStyle("textDecoration", "");
                toolTip = "";
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