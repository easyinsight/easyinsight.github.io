package com.easyinsight.google {
import com.easyinsight.administration.feed.GoogleFeedDefinition;
import com.easyinsight.customupload.wizard.UploadContext;

[Bindable]
[RemoteClass(alias="com.easyinsight.userupload.GoogleSpreadsheetUploadContext")]
public class GoogleSpreadsheetUploadContext extends UploadContext {

    public var feedDefinition:GoogleFeedDefinition;

    public function GoogleSpreadsheetUploadContext() {
        super();
    }
}
}