package com.easyinsight.groups
{
	import com.easyinsight.genredata.AnalyzableItemPanel;

	public class GroupItemPanel extends AnalyzableItemPanel
	{
		private var _groupID:int;
		
		public function GroupItemPanel()
		{
			super();
		}
		
		public function set groupID(groupID:int):void {
			this._groupID = groupID;
		}
		
		override public function refreshData():void {
            this.operation.send(_groupID);
        }
        
        override protected function titleClick():void {
            this.fullListOperation.send(_groupID);
        }
	}
}