/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/17/11
 * Time: 10:02 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.OrFilter")]
public class OrFilter extends FilterDefinition {

    public var filters:ArrayCollection = new ArrayCollection();

    public function OrFilter() {
    }

    override public function getType():int {
        return FilterDefinition.OR;
    }
}
}
