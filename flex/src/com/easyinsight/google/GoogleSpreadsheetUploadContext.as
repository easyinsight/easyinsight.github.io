package com.easyinsight.google {
import com.easyinsight.customupload.wizard.UploadContext;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.google.GoogleSpreadsheetUploadContext")]
public class GoogleSpreadsheetUploadContext extends UploadContext {

    public var worksheetURL:String;

    public function GoogleSpreadsheetUploadContext() {
        super();
    }
}
}