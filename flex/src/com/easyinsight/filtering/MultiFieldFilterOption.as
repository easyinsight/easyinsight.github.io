/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/14/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;

import flash.events.Event;

import flash.events.EventDispatcher;

import mx.events.PropertyChangeEvent;

[Bindable]
public class MultiFieldFilterOption extends EventDispatcher {
    private var _selected:Boolean;
    private var _analysisItem:AnalysisItem;
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

    [Bindable(event="analysisItemChanged")]
    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function set analysisItem(value:AnalysisItem):void {
        if (_analysisItem == value) return;
        _analysisItem = value;
        dispatchEvent(new Event("analysisItemChanged"));
    }

    public function MultiFieldFilterOption(analysisItem:AnalysisItemSelection, selected:Boolean = false) {
        this.analysisItem = analysisItem.analysisItem;
        _label = analysisItem.analysisItem.display;
        this.selected = selected;
    }


}
}
