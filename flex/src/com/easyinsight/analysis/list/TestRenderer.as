package com.easyinsight.analysis.list {
import mx.controls.Label;
import mx.core.IFactory;
import mx.events.FlexEvent;
public class TestRenderer extends Label implements IFactory {
    public function TestRenderer() {
        super();
    }

    override public function set data(value:Object):void {
        this.text = "blah";
        invalidateProperties();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function newInstance():* {
        return new TestRenderer();
    }
}
}