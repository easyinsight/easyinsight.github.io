package com.easyinsight.customupload {
[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.CsvFileUploadFormat")]
public class CsvFileUploadFormat extends UploadFormat{
    public function CsvFileUploadFormat() {
    }

    override public function getFormatType():int {
        return UploadFormat.CSV;
    }

}
}