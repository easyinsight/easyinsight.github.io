package com.easyinsight.goals {
import com.easyinsight.analysis.PopupMenuFactory;
import com.easyinsight.util.PrefixManager;

import flash.events.ContextMenuEvent;
import flash.ui.ContextMenuItem;

import mx.containers.HBox;
import mx.controls.Image;
public class GoalIconRenderer extends HBox{

    private var image:Image;

    public function GoalIconRenderer() {
        super();
        image = new Image();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
        var removeItem:ContextMenuItem = new ContextMenuItem("Remove Goal");
        removeItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onRemove);
        PopupMenuFactory.assignMenu(this, [ removeItem]);
    }

    private function onRemove(event:ContextMenuEvent):void {
        dispatchEvent(new GoalSubscriptionEvent(GoalSubscriptionEvent.GOAL_REMOVE, _goalTreeNodeData.goalTreeNodeID));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    private var _goalTreeNodeData:GoalTreeNodeData;

    override public function set data(val:Object):void {
        this._goalTreeNodeData = val as GoalTreeNodeData;
        if (this._goalTreeNodeData.iconImage != null) {
            image.load(PrefixManager.prefix + "/app/assets/icons/16x16/" + this._goalTreeNodeData.iconImage);
        }
    }

    override public function get data():Object {
        return this._goalTreeNodeData;
    }
}
}