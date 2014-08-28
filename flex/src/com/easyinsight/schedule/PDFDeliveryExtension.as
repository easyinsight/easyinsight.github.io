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

    public static const FLASH:int = 0;
    public static const HTML:int = 1;


    public var showHeader:Boolean = true;
    public var width:int;
    public var height:int;
    public var orientation:String = "Landscape";
    public var generateByHTML:int = HTML;

    public function PDFDeliveryExtension() {
    }
}
}
