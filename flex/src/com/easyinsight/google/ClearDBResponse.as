/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/18/11
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.cleardb.ClearDBResponse")]
public class ClearDBResponse {

    public var successful:Boolean;
    public var eiDescriptor:EIDescriptor;
    public var errorMessage:String;

    public function ClearDBResponse() {
    }
}
}
