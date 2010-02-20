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
            switch (descriptor.feedType) {
                case DataFeedDescriptor.ANALYSIS:
                    this.text = "( Derived )";
                    break;
                case DataFeedDescriptor.SALESFORCE:
                case DataFeedDescriptor.GOOGLE_ANALYTICS:
                case DataFeedDescriptor.GOOGLE:
                case DataFeedDescriptor.TWITTER:
                case DataFeedDescriptor.GNIP:
                case DataFeedDescriptor.CLOUD_WATCH:
                    this.text = "( Live )";
                    break;
                case DataFeedDescriptor.COMPOSITE:
                    this.text = "( Composite )";
                    break;
                default:
                    this.text = String(descriptor.size);
                    break;
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