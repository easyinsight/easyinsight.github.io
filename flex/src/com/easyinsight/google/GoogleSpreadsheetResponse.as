/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/14/10
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.google.GoogleSpreadsheetResponse")]
public class GoogleSpreadsheetResponse {

    public var spreadsheets:ArrayCollection;
    public var validOAuth:Boolean;

    public function GoogleSpreadsheetResponse() {
    }
}
}
