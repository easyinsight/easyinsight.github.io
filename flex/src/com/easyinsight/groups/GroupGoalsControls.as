package com.easyinsight.groups
{
    import com.easyinsight.goals.*;

    import mx.rpc.events.ResultEvent;
    import mx.rpc.remoting.RemoteObject;
	import com.easyinsight.genredata.AnalyzeEvent;
	
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;

	public class GroupGoalsControls extends HBox
	{
        [Embed(source="../../../../assets/media_play_green.png")]
        public var playIcon:Class;
        
        private var goalTree:GoalTreeDescriptor;

        private var goalService:RemoteObject;
        
		public function GroupGoalsControls()
		{
			super();
            this.percentWidth = 100;
            setStyle("horizontalAlign", "center");
			var viewButton:Button = new Button();
			viewButton.setStyle("icon", playIcon);
			viewButton.toolTip = "View";
			viewButton.addEventListener(MouseEvent.CLICK, viewGoalTree);
			addChild(viewButton);
		}

        override public function set data(value:Object):void {
			this.goalTree = value as GoalTreeDescriptor;
		}
		
		override public function get data():Object {
			return this.goalTree;
		}
		
		private function viewGoalTree(event:MouseEvent):void {
			goalService = new RemoteObject();
            goalService.destination = "goalService";
            goalService.createDataTree.addEventListener(ResultEvent.RESULT, gotDataTree);
            goalService.createDataTree.send(goalTree.goalTreeID, null, null);
		}

        private function gotDataTree(event:ResultEvent):void {
            dispatchEvent(new AnalyzeEvent(new GoalDataAnalyzeSource(goalService.createDataTree.lastResult as GoalTree))); 
        }
	}
}