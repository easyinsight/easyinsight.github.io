package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.TextDataResults")]
	public class TextDataResults extends DataResults
	{
		public var text:String;

		public function TextDataResults()
		{			
		}

	}
}