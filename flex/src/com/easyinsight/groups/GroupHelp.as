package com.easyinsight.groups
{
	import com.easyinsight.help.MyDataHelp;
	import com.easyinsight.help.Screencast;

	public class GroupHelp extends MyDataHelp
	{
		public function GroupHelp()
		{
			super();
		}
		
		override protected function getSummary():String {
			return "The My Data page is your location for data you've uploaded and insights you've created. From this page, you can:\n" + 
					"\tUpload Excel spreadsheets and delimited text files\n" + 
					"\tPull in data from Google Spreadsheets, Wesabe, and Salesforce.com\n" + 
					"\tJoin feeds together into a new single feed\n" + 
					"\tManage the APIs to your feeds\n";
		}
		
		override protected function createScreencastArray():Array {
			return [ new Screencast("Uploading an Excel spreadsheet", "assets/screencasts/demo.swf") ];
		}
		
		override protected function getDetail():String {
			return "";
		}
	}
}