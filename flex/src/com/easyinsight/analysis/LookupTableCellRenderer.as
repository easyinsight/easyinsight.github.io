package com.easyinsight.analysis {
import com.easyinsight.analysis.list.LookupTableChangeEvent;
import com.easyinsight.etl.LookupTable;

import flash.events.Event;
import mx.controls.TextInput;
import mx.events.FlexEvent;
import mx.formatters.Formatter;

public class LookupTableCellRenderer extends TextInput {
    private var _data:Object;
    private var _analysisItem:AnalysisItem;
    private var _lookupTable:LookupTable;
    private var _pairMap:Object;

    public function LookupTableCellRenderer() {
        super();
        addEventListener(Event.CHANGE, onChange);
    }

    private function onChange(event:Event):void {
        var srcString:String;
        if (_data[_lookupTable.sourceField.qualifiedName()] is Value) {
            srcString = String(Value(_data[_lookupTable.sourceField.qualifiedName()]).getValue());
        } else {
            srcString = String(_data[_lookupTable.sourceField.qualifiedName()]);
        }
        var dstObj:Object;
        if (_lookupTable.targetField.hasType(AnalysisItemTypes.MEASURE)) {
            dstObj = Number(this.text);
            if (isNaN(Number(dstObj))) {
                dstObj = 0;
            }
        } else {
            dstObj = this.text;
        }
        _data[_analysisItem.qualifiedName()] = dstObj;
        _pairMap[srcString] = dstObj;
        dispatchEvent(new LookupTableChangeEvent());
    }

    public function set lookupTable(value:LookupTable):void {
        _lookupTable = value;
    }

    public function set pairMap(value:Object):void {
        _pairMap = value;
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
    }

    override public function set data(value:Object):void {
        _data = value;
        var text:String;
        if (value != null) {
            var field:String = analysisItem.qualifiedName();
            var formatter:Formatter = analysisItem.getFormatter();
            if (value[field] is Value) {
                var objVal:Value = value[field];
                if (objVal == null) {
                    text = "";
                } else {
                    text = formatter.format(objVal.getValue());
                }
            } else {
                if (value[field] != null) {
                    text = formatter.format(value[field]);
                } else {
                    text = "";
                }

            }
        } else {
            text = "";
        }

        this.text = text;
        invalidateProperties();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    override public function get data():Object {
        return _data;
    }
}
}