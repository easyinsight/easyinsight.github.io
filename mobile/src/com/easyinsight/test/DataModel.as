/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/10/11
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.test {
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

public class DataModel extends EventDispatcher {

    private var _descriptors:ArrayCollection;

    private var _localList:ArrayCollection;

    public function DataModel() {
    }

    public function get localList():ArrayCollection {
        return _localList;
    }

    public function set localList(value:ArrayCollection):void {
        _localList = value;
    }

    public function get descriptors():ArrayCollection {
        return _descriptors;
    }

    public function set descriptors(value:ArrayCollection):void {
        _descriptors = value;
    }
}
}
