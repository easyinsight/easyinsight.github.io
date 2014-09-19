package com.easyinsight.commands
{
import com.easyinsight.analysis.DataViewFactory;

public interface IReportEditorCommand
	{
		function executeWithViewFactory(viewFactory:DataViewFactory):void;
	}
}