/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/14/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.listing.Tag;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.events.PropertyChangeEvent;

[Bindable]
public class BatchTagOption extends EventDispatcher {
    private var _selected:Boolean;
    private var _tag:Tag;
    private var _label:String;

    [Bindable(event="selectedChanged")]
    public function get selected():Boolean {
        return _selected;
    }
    public function set selected(value:Boolean):void {
        var temp:Boolean = _selected;
        _selected = value;
        if(temp != value)
            dispatchEvent(new PropertyChangeEvent("selectedChanged", false, false, null, null, temp,  value));
    }

    public function get label():String {
        return _label;
    }


    [Bindable(event="tagChanged")]
    public function get tag():Tag {
        return _tag;
    }

    public function set tag(value:Tag):void {
        if (_tag == value) return;
        _tag = value;
        dispatchEvent(new Event("tagChanged"));
    }

    public function BatchTagOption(tag:Tag, selected:Boolean = false) {
        this.tag = tag;
        _label = tag.name;
        this.selected = selected;
    }


}
}
