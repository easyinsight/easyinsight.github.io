/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/14/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.EventDispatcher;

import mx.events.PropertyChangeEvent;

[Bindable]
public class MultiValueFilterOption extends EventDispatcher {
    private var _selected:Boolean;
    private var _label:String;

    [Bindable(event="selectedChanged")]
    public function get selected():Boolean {
        return _selected;
    }
    public function set selected(value:Boolean):void {
        var temp:Boolean = _selected;
        _selected = value;
        if(temp != value)
            dispatchEvent(new PropertyChangeEvent("selectedChanged"))
    }

    [Bindable(event="labelChanged")]
    public function get label():String {
        return _label;
    }

    public function set label(value:String):void {
        var temp:String = _label;
        _label = value;
        if(temp != value)
            dispatchEvent(new PropertyChangeEvent("labelChanged"))
    }

    public function MultiValueFilterOption(label:String = "", selected:Boolean = false) {
        this.label = label;
        this.selected = selected;
    }


}
}
