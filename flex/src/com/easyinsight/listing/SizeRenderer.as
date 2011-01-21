package com.easyinsight.listing {
import com.easyinsight.datasources.DataSourceBehavior;

import com.easyinsight.solutions.DataSourceDescriptor;

import mx.controls.Label;
public class SizeRenderer extends Label{



    public function SizeRenderer() {
        super();
    }

    private var _data:Object;

    override public function set data(value:Object):void {
        _data = value;
        if (_data is DataSourceDescriptor) {
            var descriptor:DataSourceDescriptor = _data as DataSourceDescriptor;
            this.text = DataSourceBehavior.sizeLabel(descriptor.dataSourceType, descriptor.size);
        } else {
            this.text = "";
        }
    }

    override public function get data():Object {
        return _data;
    }
}
}