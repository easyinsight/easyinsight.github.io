package com.easyinsight.discussion {
import com.easyinsight.groups.AuditMessage;
import com.easyinsight.groups.GroupAuditMessage;
import com.easyinsight.groups.GroupComment;
import mx.containers.HBox;
import mx.controls.Label;
public class GroupRenderer extends HBox{

    private var _auditMessage:AuditMessage;

    private var groupLabel:Label;

    public function GroupRenderer() {
        super();
        groupLabel = new Label();
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(groupLabel);
    }

    override public function set data(val:Object):void {
        this._auditMessage = val as AuditMessage;
        if (_auditMessage is GroupAuditMessage) {
            var groupAuditMessage:GroupAuditMessage = _auditMessage as GroupAuditMessage;
            groupLabel.text = groupAuditMessage.groupName;
        } else if (_auditMessage is GroupComment) {
            var groupComment:GroupComment = _auditMessage as GroupComment;
            groupLabel.text = groupComment.groupName;
        }
    }

    override public function get data():Object {
        return this._auditMessage;
    }
}
}