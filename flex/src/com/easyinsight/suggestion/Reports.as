/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/23/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.util.TextInputWithArrow;

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;

public class Reports extends TextInputWithArrow {

    private var _fields:ArrayCollection;
    private var fieldsChanged:Boolean = false;

    public function Reports() {
        labelField = "name";
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
            var targetFields:ArrayCollection = new ArrayCollection(_fields.toArray());
            var sort:Sort = new Sort();
            sort.fields = [ new SortField("name", true) ];
            targetFields.sort = sort;
            targetFields.refresh();
            dataProvider = targetFields;
        }
    }

    public function selected():EIDescriptor {
        return EIDescriptor(selectedItem);
    }
}
}
