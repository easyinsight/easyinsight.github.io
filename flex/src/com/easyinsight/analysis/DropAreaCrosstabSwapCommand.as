package com.easyinsight.analysis
{
import com.easyinsight.analysis.crosstab.CrosstabControlBar;
import com.easyinsight.commands.ICommand;
import com.easyinsight.commands.IReportEditorCommand;

public class DropAreaCrosstabSwapCommand implements ICommand, IReportEditorCommand
	{
		
		public function DropAreaCrosstabSwapCommand() {
		}

		public function execute():void {
		}
		
		public function undo():Boolean {
			return true;
		}

    public function executeWithViewFactory(viewFactory:DataViewFactory):void {
        var bar:CrosstabControlBar = viewFactory.dropAreaControlBar as CrosstabControlBar;
        bar.swapGroupings();
        viewFactory.refresh();
    }
}
}