package com.easyinsight.collaboration
{
	import mx.controls.Button;

	public class JoinCollaborationSessionButton extends Button
	{
		private var _data:CollaborationSession;
		
		public function JoinCollaborationSessionButton()
		{
			super();
			label = "Join";
		}
		
		override public function set data(value:Object):void {
			_data = value as CollaborationSession;
		}
		
		override public function get data():Object {
			return _data;
		}
	}
}