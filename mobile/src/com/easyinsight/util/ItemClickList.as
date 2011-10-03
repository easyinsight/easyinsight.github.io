/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/3/11
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import com.easyinsight.reportviews.FilterListEntry;

import mx.core.ClassFactory;

import spark.components.List;

[Event(name="itemClick", type="mx.events.ItemClickEvent")]
public class ItemClickList extends List {
    public function ItemClickList() {
        super();
        itemRenderer = new ClassFactory(FilterListEntry);
    }
}
}
