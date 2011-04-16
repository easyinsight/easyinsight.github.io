/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/5/11
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.composite.FederationSource")]
public class FederationSource {

    public var dataSourceID:int;
    public var value:String;
    public var name:String;
    public var dataSourceType:int;
    public var fieldMappings:ArrayCollection;

    public function FederationSource() {
    }
}
}
