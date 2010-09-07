package com.easyinsight.etl {
import mx.collections.ArrayCollection;
import mx.containers.VBox;

public class LookupTablePage extends VBox {

    private var _pairs:ArrayCollection = new ArrayCollection();

    private var _rowType:String;

    public function LookupTablePage() {
        super();
        setStyle("paddingTop", 5);
        setStyle("paddingBottom", 5);
    }

    public function set rowType(value:String):void {
        _rowType = value;
    }

    public function get pairs():ArrayCollection {
        return _pairs;
    }

    protected override function createChildren():void {
        super.createChildren();
        for each (var pair:LookupPair in _pairs) {
            var row:LookupTableRow = new LookupTableRow();
            row.pair = pair;
            row.mode = _rowType;
            addChild(row);
        }
    }

    public function save(masterLookupTable:Object, masterPairs:ArrayCollection):void {
        for each (var pair:LookupPair in masterLookupTable) {
            for each (var row:LookupTableRow in getChildren()) {
                var savePair:LookupPair = row.save();
                if (String(savePair.sourceValue.getValue()) == String(pair.sourceValue.getValue())) {
                    pair.targetValue = savePair.targetValue;
                }
            }
            masterPairs.addItem(pair);
        }
    }
}
}