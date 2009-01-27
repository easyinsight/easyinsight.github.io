package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.ExcelUploadFormat")]
	public class ExcelUploadFormat extends UploadFormat
	{
		public function ExcelUploadFormat()
		{
		}

		override public function getFormatType():int {
			return UploadFormat.EXCEL;
		}
	}
}