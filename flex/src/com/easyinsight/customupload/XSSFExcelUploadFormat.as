package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.XSSFExcelUploadFormat")]
	public class XSSFExcelUploadFormat extends UploadFormat
	{
		public function XSSFExcelUploadFormat()
		{
		}

		override public function getFormatType():int {
			return UploadFormat.EXCEL;
		}
	}
}