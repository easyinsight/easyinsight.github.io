/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/27/13
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.PDFDeliveryExtension")]
public class PDFDeliveryExtension extends DeliveryExtension {

    public var showHeader:Boolean;
    public var width:int;
    public var height:int;
    public var orientation:String;

    public function PDFDeliveryExtension() {
    }
}
}
