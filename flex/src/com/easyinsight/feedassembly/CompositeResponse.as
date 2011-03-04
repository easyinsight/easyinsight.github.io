/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/2/11
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.CompositeResponse")]
public class CompositeResponse {

    public var firstFields:ArrayCollection;
    public var secondFields:ArrayCollection;
    public var firstName:String;
    public var secondName:String;
    public var firstID:int;
    public var secondID:int;

    public function CompositeResponse() {
    }
}
}
