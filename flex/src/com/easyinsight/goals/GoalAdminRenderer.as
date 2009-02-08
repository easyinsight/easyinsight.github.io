package com.easyinsight.goals
{
	import flash.events.MouseEvent;
	
	import ilog.orgchart.OrgChartItem;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.controls.Label;

	public class GoalAdminRenderer extends VBox
	{
		private var orgChartItem:OrgChartItem;
		private var deleteButton:Button;
		
		[Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        private var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../assets/add.png")]
        private var addIcon:Class;
		
		public function GoalAdminRenderer()
		{
			super();
			this.width = 100;
			this.height = 80;
			var label:Label = new Label();
			label.text = "Blah";
			label.setStyle("fontSize", 10);
			addChild(label);
			var box:HBox = new HBox();
			box.setStyle("horizontalAlign", "center");
			box.percentWidth = 100;
			addChild(box);
			var addButton:Button = new Button();
			addButton.toolTip = "Add Child";			
			addButton.setStyle("icon", addIcon);
			addButton.addEventListener(MouseEvent.CLICK, addChildNode);
			box.addChild(addButton);
			deleteButton = new Button();
			deleteButton.toolTip = "Delete";
			deleteButton.setStyle("icon", deleteIcon);
			deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
			box.addChild(deleteButton);			
		}
		
		private function deleteSelf(event:MouseEvent):void {
			var goalTreeNode:GoalTreeNode = orgChartItem.data as GoalTreeNode;
			var parent:GoalTreeNode = goalTreeNode.parent;
			parent.children.removeItemAt(parent.children.getItemIndex(goalTreeNode));
			if (goalTreeNode.children != null) {
				for each (var childNode:GoalTreeNode in goalTreeNode.children) {
					childNode.parent = parent;
					parent.children.addItem(childNode);
				}				
			}
			dispatchEvent(new OrganizationChartRefreshEvent());
		}
		
		private function addChildNode(event:MouseEvent):void {
			var goalTreeNode:GoalTreeNode = orgChartItem.data as GoalTreeNode;
			if (goalTreeNode.children == null) {
				goalTreeNode.children = new ArrayCollection();
			}
			var newChild:GoalTreeNode = new GoalTreeNode();
			goalTreeNode.children.addItem(newChild);
			newChild.parent = goalTreeNode;
			dispatchEvent(new OrganizationChartRefreshEvent(newChild));
		}
		
		override public function set data(value:Object):void {
			this.orgChartItem = value as OrgChartItem;
			var goalTreeNode:GoalTreeNode = orgChartItem.data as GoalTreeNode;
			deleteButton.enabled = goalTreeNode.parent != null;				
		}
		
		override public function get data():Object {
			return orgChartItem;
		}
	}
}