package com.easyinsight.util {


import flash.events.Event;

import flash.events.FocusEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.TextInput;
import mx.validators.Validator;
[Event(name="itemChanged", type="com.easyinsight.util.CompletionItemEvent")]
public class AnalysisItemCompletionInput extends TextInput{

    private var _items:ArrayCollection;
    private var _selectedItem:Object;

    private var manager:CustomCompletionManager;

    private var focused:Boolean = false;

    private var _labelField:String;

    private var validator:AnalysisItemValidator;

    public function AnalysisItemCompletionInput() {
        super();
        manager = new CustomCompletionManager();
        manager.target = this;        
        manager.minCharsForCompletion = 0;
        manager.maxRowCount = 12;
        manager.addEventListener(Event.CLOSE, onCompletionClose);
        validator = new AnalysisItemValidator();
        validator.source = this;
        validator.property = "selectedItem";
        addEventListener(FocusEvent.FOCUS_IN, onFocus);
        addEventListener(Event.CHANGE, onChange);
    }

    private function onChange(event:Event):void {
        var matchedItem:Object;
        for each (var item:Object in _items) {
            if (item[_labelField] == this.text) {
                matchedItem = item;
            }
        }
        if (matchedItem == null) {
            selectedItem = null;
        } else {
            selectedItem = matchedItem;
        }
        var results:Array = Validator.validateAll([ validator ]);
        if (results.length == 0) {
            dispatchEvent(new CompletionItemEvent(CompletionItemEvent.ITEM_CHANGED, selectedItem));
        }
    }

    public function set labelField(value:String):void {
        _labelField = value;
        manager.labelField = value;
    }

    private function onFocus(event:FocusEvent):void {
        manager.forceOpen();

    }

    private function onCompletionClose(event:Event):void {
        var matchedItem:Object;
        for each (var item:Object in _items) {
            if (item[_labelField] == this.text) {
                matchedItem = item;
            }
        }
        if (matchedItem == null) {
            selectedItem = null;
        } else {
            selectedItem = matchedItem;
        }
        var results:Array = Validator.validateAll([ validator ]);
        if (results.length == 0) {
            dispatchEvent(new CompletionItemEvent(CompletionItemEvent.ITEM_CHANGED, selectedItem));
        }
    }

    [Bindable(event="selectedItemChanged")]
    public function get selectedItem():Object {
        return _selectedItem;
    }

    public function set selectedItem(value:Object):void {
        if (_selectedItem == value) return;
        _selectedItem = value;
        if (_selectedItem == null) {
            this.text = "";
        } else {
            this.text = _selectedItem[_labelField];
        }
        dispatchEvent(new Event("selectedItemChanged"));
    }

    [Bindable(event="itemsChanged")]
    public function get items():ArrayCollection {
        return _items;
    }

    public function set items(value:ArrayCollection):void {
        if (_items == value) return;
        _items = value;
        dispatchEvent(new Event("itemsChanged"));
    }

    override protected function commitProperties():void {
        super.commitProperties();
        BindingUtils.bindProperty(manager, "dataProvider", this, "items");
        
    }
}
}