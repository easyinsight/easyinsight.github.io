package com.easyinsight.groups {
import com.easyinsight.goals.GoalTreeDescriptor;
import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;
import mx.containers.HBox;
import mx.controls.Label;
public class GoalTreeNameRenderer extends HBox{

    private var goalTree:GoalTreeDescriptor;
    private var _admin:Boolean;

    private var nameLabel:Label;

    public function GoalTreeNameRenderer() {
        super();
        nameLabel = new Label();
        setStyle("horizontalAlign", "left");
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        contextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        if (_admin) {
            var deleteContextItem:ContextMenuItem = new ContextMenuItem("Remove Goal Tree From Group", true);
            deleteContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, deleteGoalTree);
            contextMenu.customItems = [ deleteContextItem ];
        }
        addChild(nameLabel);
    }

    private function deleteGoalTree(event:ContextMenuEvent):void {
        dispatchEvent(new RemoveItemFromGroupEvent(RemoveItemFromGroupEvent.REMOVE_GOAL_TREE_FROM_GROUP, goalTree.id));
    }

    public function set admin(val:Boolean):void {
        _admin = val;
    }

    override public function set data(value:Object):void {
        this.goalTree = value as GoalTreeDescriptor;
        nameLabel.text = goalTree.name;
    }

    override public function get data():Object {
        return this.goalTree;
    }
}
}