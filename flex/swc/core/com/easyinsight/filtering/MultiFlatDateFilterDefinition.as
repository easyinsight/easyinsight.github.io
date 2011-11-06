/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/16/11
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.MultiFlatDateFilter")]
public class MultiFlatDateFilterDefinition extends FilterDefinition {

    public var levels:ArrayCollection = new ArrayCollection();
    public var endDateProperty:String;

    public function MultiFlatDateFilterDefinition() {
    }

    override public function getType():int {
        return FilterDefinition.MULTI_FLAT_DATE;
    }

    public function createLabel():String {
        var label:String;
        var firstValue:int = 11;
        var lastValue:int = 0;
        for each (var wrapper:DateLevelWrapper in levels) {
            firstValue = Math.min(wrapper.dateLevel, firstValue);
            lastValue = Math.max(wrapper.dateLevel, lastValue);
            var firstLabel:String = toMonthLabel(firstValue);
            var secondLabel:String = toMonthLabel(lastValue);
            if (firstLabel == secondLabel) {
                label = firstLabel;
            } else {
                label = firstLabel + " to " + secondLabel;
            }
        }
        return label;
    }

    private function toMonthLabel(value:int):String {
            var label:String;
            switch (value) {
                case 0:
                    label = "Jan";
                    break;
                case 1:
                    label = "Feb";
                    break;
                case 2:
                    label = "Mar";
                    break;
                case 3:
                    label = "Apr";
                    break;
                case 4:
                    label = "May";
                    break;
                case 5:
                    label = "Jun";
                    break;
                case 6:
                    label = "Jul";
                    break;
                case 7:
                    label = "Aug";
                    break;
                case 8:
                    label = "Sep";
                    break;
                case 9:
                    label = "Oct";
                    break;
                case 10:
                    label = "Nov";
                    break;
                case 11:
                    label = "Dec";
                    break;
            }
            return label;
        }
}
}
