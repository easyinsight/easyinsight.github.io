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

    public function MultiFlatDateFilterDefinition() {
    }

    override public function getType():int {
        return FilterDefinition.MULTI_FLAT_DATE;
    }
}
}
