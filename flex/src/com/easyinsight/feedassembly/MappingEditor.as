/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 4/1/11
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.util.AutoCompleteManager;

import flash.events.Event;

import mx.controls.TextInput;

public class MappingEditor extends TextInput {

    private var mapping:Object;

    private var autoCompleteManager:AutoCompleteManager;

    public function MappingEditor() {
        addEventListener(Event.CHANGE, onChange);
    }

    private function onChange(event:Event):void {
        mapping[String(_dataSource.dataFeedID)] = this.text;
    }

    private var _dataSource:FeedDefinitionData;

    public function set dataSource(value:FeedDefinitionData):void {
        _dataSource = value;
    }

    override public function set data(val:Object):void {
        mapping = val;
        this.text = mapping[String(_dataSource.dataFeedID)];
        /*autoCompleteManager = new AutoCompleteManager();
        autoCompleteManager.dataProvider = _dataSource.fields;
        autoCompleteManager.minCharsForCompletion = 1;
        autoCompleteManager.labelField = "display";
        autoCompleteManager.target = this;*/

    }

    override public function get data():Object {
        return mapping;
    }
}
}
