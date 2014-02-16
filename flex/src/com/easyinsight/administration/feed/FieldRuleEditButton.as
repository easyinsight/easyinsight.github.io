/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/5/14
 * Time: 9:14 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.LinkButton;

public class FieldRuleEditButton extends HBox {

    public var editButton:LinkButton;

    public function FieldRuleEditButton() {
        editButton = new LinkButton();
        editButton.label = "Edit...";
        editButton.addEventListener(MouseEvent.CLICK, onClick);
    }

    private var fieldRule:FieldRule;

    override public function set data(val:Object):void {
        fieldRule = val as FieldRule;
    }

    override public function get data():Object {
        return fieldRule;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(editButton);
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new FieldRuleEvent(FieldRuleEvent.EDIT_RULE, fieldRule));
    }
}
}
