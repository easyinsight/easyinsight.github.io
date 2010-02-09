package com.easyinsight.analysis.range {
import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.TextInput;
import mx.events.CollectionEvent;

public class ExplicitRangeRenderer extends TextInput {

    private var range:RangeOption;
    
    public function ExplicitRangeRenderer() {
        super();
    }

    [Bindable]
    override public function set data(val:Object):void {
        range = val as RangeOption;
    }

    override public function get data():Object {
        return range;
    }
}
}