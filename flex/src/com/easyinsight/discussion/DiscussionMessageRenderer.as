package com.easyinsight.discussion {
import com.easyinsight.groups.AuditMessage;
import mx.controls.Label;
public class DiscussionMessageRenderer extends Label {
    private var auditMessage:AuditMessage;

    public function DiscussionMessageRenderer() {
        //this.maxWidth = 290;
    }

    override public function set data(value:Object):void {
        auditMessage = value as AuditMessage;
        this.text = auditMessage.message;
    }

    override public function get data():Object {
        return auditMessage;
    }
}
}