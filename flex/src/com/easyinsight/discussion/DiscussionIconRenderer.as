package com.easyinsight.discussion {
import com.easyinsight.groups.AuditMessage;
import mx.controls.Image;
import mx.containers.HBox;
public class DiscussionIconRenderer extends HBox {
    [Bindable]
    [Embed(source="../../../../assets/eye.png")]
    private var auditIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/messagex16.png")]
    private var messageIcon:Class;

    private var iconImage:Image;

    private var auditMessage:AuditMessage;

    public function DiscussionIconRenderer() {
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        iconImage = new Image();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(iconImage);
    }

    override public function set data(val:Object):void {
        this.auditMessage = val as AuditMessage;
        if (auditMessage.audit) {
            iconImage.source = auditIcon;
        } else {
            iconImage.source = messageIcon;
        }
    }

    override public function get data():Object {
        return this.auditMessage;
    }
}
}