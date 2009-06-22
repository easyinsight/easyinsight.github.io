package com.easyinsight.goals
{
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.analysis.AnalysisMeasure;
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.goals.GoalTreeNode")]
	public class GoalTreeNode
	{
        public var goalTreeNodeID:int;
		public var children:ArrayCollection = new ArrayCollection();
		public var parent:GoalTreeNode;
        public var subTreeID:int;
        public var subTreeName:String;

		public var coreFeedID:int;
		public var coreFeedName:String;

        public var analysisMeasure:AnalysisMeasure;
        public var goalValue:Number;
        public var filterDefinition:FilterDefinition;
        public var highIsGood:Boolean = true;

		public var associatedFeeds:ArrayCollection = new ArrayCollection();
		public var associatedInsights:ArrayCollection = new ArrayCollection();
		public var associatedSolutions:ArrayCollection = new ArrayCollection();

		public var tags:ArrayCollection = new ArrayCollection();
		public var name:String;
		public var tempName:String;
		public var description:String;
        public var iconImage:String;

        private var _blah:String;

        public var users:ArrayCollection = new ArrayCollection();
        public var newSubTree:SolutionGoalTreeDescriptor;
        public var milestone:GoalTreeMilestone;

        private var _renderer:IGoalRenderer;
		
		public function GoalTreeNode()
		{
		}

        public function setBlah(blah:String):void {
            _blah = blah;
        }

        public function updateName():void {
            if (_blah != null) {
                name = _blah;
            }
        }

        public function getRenderer():IGoalRenderer {
            return _renderer;
        }

        public function setRenderer(val:IGoalRenderer):void {
            _renderer = val;
        }

        public function clone():GoalTreeNode {
            var clonedNode:GoalTreeNode = new GoalTreeNode();
            // recurse children?
            clonedNode.analysisMeasure = this.analysisMeasure;
            clonedNode.coreFeedID = this.coreFeedID;
            clonedNode.coreFeedName = this.coreFeedName;
            clonedNode.goalValue = this.goalValue;
            clonedNode.filterDefinition = this.filterDefinition;
            clonedNode.highIsGood = this.highIsGood;
            clonedNode.name = this.name;
            clonedNode.description = this.description;
            clonedNode.iconImage = this.iconImage;
            clonedNode.associatedFeeds = new ArrayCollection(associatedFeeds.toArray());
            clonedNode.associatedInsights = new ArrayCollection(associatedInsights.toArray());
            var newChildren:ArrayCollection = new ArrayCollection();
            for each (var childNode:GoalTreeNode in children) {
                var clonedChild:GoalTreeNode = childNode.clone();
                clonedChild.parent = clonedNode;
                newChildren.addItem(clonedChild);
            }
            clonedNode.children = newChildren;
            return clonedNode;
        }
	}
}