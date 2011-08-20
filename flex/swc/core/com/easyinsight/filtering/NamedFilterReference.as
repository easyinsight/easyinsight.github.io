/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 4/4/11
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.NamedFilterReference")]
public class NamedFilterReference extends FilterDefinition {

    public var referenceName:String = "";

    public function NamedFilterReference() {
    }

    override public function getType():int {
        return FilterDefinition.NAMED_REF;
    }
}
}
