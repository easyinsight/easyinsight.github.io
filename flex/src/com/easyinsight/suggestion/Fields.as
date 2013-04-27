/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/23/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.util.SmartComboBox;

import mx.collections.ArrayCollection;

public class Fields extends SmartComboBox {

    public var typeRestrictor:int;
    private var _fields:ArrayCollection;
    private var fieldsChanged:Boolean = false;

    public function Fields() {
        labelField = "display";
        selectedProperty = "display";
    }

    public function set fields(value:ArrayCollection):void {
        if (_fields != value) {
            _fields = value;
            fieldsChanged = true;
        }
    }

    override protected function commitProperties():void {
        super.commitProperties();
        if (fieldsChanged && _fields != null) {
            fieldsChanged = false;
            var targetFields:ArrayCollection = new ArrayCollection();
            for each (var field:AnalysisItem in _fields) {
                if (field.hasType(typeRestrictor)) {
                    targetFields.addItem(field);
                }
            }
            dataProvider = targetFields;
        }
    }

    public function selectedAnalysisItem():AnalysisItem {
        return AnalysisItem(selectedItem);
    }
}
}
