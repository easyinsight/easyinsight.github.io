package com.easyinsight.analysis.range {
import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.events.CollectionEvent;
import mx.events.CollectionEventKind;

[Event(name="deleteRange", type="com.easyinsight.analysis.range.RangeEvent")]
public class RangeBox extends VBox {

    private var _dataProvider:ArrayCollection;

    public function RangeBox() {
        super();
    }

    [Bindable(event="dataProviderChanged")]
    public function get dataProvider():ArrayCollection {
        return _dataProvider;
    }

    public function set dataProvider(value:ArrayCollection):void {
        if (_dataProvider == value) return;
        if (_dataProvider != null) {
            _dataProvider.removeEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionChange);
        }
        _dataProvider = value;
        _dataProvider.addEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionChange);
        dispatchEvent(new Event("dataProviderChanged"));
        invalidateProperties();
    }

    private function onCollectionChange(event:CollectionEvent):void {
        if (event.kind == CollectionEventKind.ADD) {            
            var newItem:RangeOption = _dataProvider.getItemAt(event.location) as RangeOption;
            var rangeEditor:RangeRowEditor = new RangeRowEditor();
            rangeEditor.data = newItem;
            addChildAt(rangeEditor, event.location);
            invalidateSize();
        } else if (event.kind == CollectionEventKind.REMOVE) {
            var child:RangeRowEditor = removeChildAt(event.location) as RangeRowEditor;
            child.removeEventListener(RangeEvent.DELETE_RANGE, onDelete);
            invalidateSize();
        }
    }

    public function validate():Array {
        var results:Array = [];
        for each (var rangeEditor:RangeRowEditor in getChildren()) {
            var editorResults:Array = rangeEditor.validate();
            for each (var obj:Object in editorResults) {
                results.push(obj);
            }            
        }
        return results;
    }

    public function save():void {
        for each (var rangeEditor:RangeRowEditor in getChildren()) {
            rangeEditor.save();
        }
    }

    protected override function commitProperties():void {
        super.commitProperties();
        removeAllChildren();
        if (_dataProvider != null) {
            for each (var rangeOption:RangeOption in _dataProvider) {
                var rangeEditor:RangeRowEditor = new RangeRowEditor();
                rangeEditor.addEventListener(RangeEvent.DELETE_RANGE, onDelete);
                rangeEditor.data = rangeOption;
                addChild(rangeEditor);
            }
        }
    }

    private function onDelete(event:RangeEvent):void {
        dispatchEvent(event);
    }
}
}