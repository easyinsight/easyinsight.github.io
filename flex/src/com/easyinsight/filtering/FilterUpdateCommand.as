package com.easyinsight.filtering
{
    import com.easyinsight.commands.ICommand;
	
	import mx.collections.ArrayCollection;

	public class FilterUpdateCommand implements ICommand
	{
		private var previousFilters:ArrayCollection;
		private var newFilters:ArrayCollection;
		
		public function FilterUpdateCommand()
		{
		}

		public function execute():void
		{
		}
		
		public function undo():Boolean
		{
			return false;
		}
		
	}
}