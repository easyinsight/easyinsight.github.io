/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/11/13
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.customupload {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.userupload.AnalyzeUploadResponse")]
public class AnalyzeUploadResponse {

    public var fieldUploadInfos:ArrayCollection;
    public var error:String;

    public function AnalyzeUploadResponse() {
    }
}
}
