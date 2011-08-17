/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import mx.collections.ArrayCollection;

public class MultiFlatDateFilterOptionWindow extends MultiFilterOptionWindow {
    public function MultiFlatDateFilterOptionWindow() {
    }


    override protected function createAvailableItems():ArrayCollection {
        var filter:MultiFlatDateFilterDefinition = filterDefinition as MultiFlatDateFilterDefinition;
        var obj:Object = new Object();
        for each (var level:DateLevelWrapper in filter.levels) {
            obj[String(level.dateLevel)] = true;
        }
        var availableItems:ArrayCollection = new ArrayCollection();
        availableItems.addItem(new MultiFilterOption("January", 0, obj["0"]));
        availableItems.addItem(new MultiFilterOption("February", 1, obj["1"]));
        availableItems.addItem(new MultiFilterOption("March", 2, obj["2"]));
        availableItems.addItem(new MultiFilterOption("April", 3, obj["3"]));
        availableItems.addItem(new MultiFilterOption("May", 4, obj["4"]));
        availableItems.addItem(new MultiFilterOption("June", 5, obj["5"]));
        availableItems.addItem(new MultiFilterOption("July", 6, obj["6"]));
        availableItems.addItem(new MultiFilterOption("August", 7, obj["7"]));
        availableItems.addItem(new MultiFilterOption("September", 8, obj["8"]));
        availableItems.addItem(new MultiFilterOption("October", 9, obj["9"]));
        availableItems.addItem(new MultiFilterOption("November", 10, obj["10"]));
        availableItems.addItem(new MultiFilterOption("December", 11, obj["11"]));
        return availableItems;
    }

    override protected function saveFilter(availableItems:ArrayCollection):void {
        var filter:MultiFlatDateFilterDefinition = filterDefinition as MultiFlatDateFilterDefinition;
        var levels:ArrayCollection = new ArrayCollection();
        for each (var multiFilterOption:MultiFilterOption in availableItems) {
            if (multiFilterOption.selected) {
                var wrapper:DateLevelWrapper = new DateLevelWrapper();
                wrapper.dateLevel = multiFilterOption.value as int;
                levels.addItem(wrapper);
            }
        }
        filter.levels = levels;
    }
}
}
