/**
 * Created by jamesboe on 6/30/14.
 */
package com.easyinsight.analysis {
import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.PDFImageData")]
public class PDFImageData {

    public var bytes:ByteArray;
    public var width:int;
    public var height:int;

    public function PDFImageData() {
    }
}
}
