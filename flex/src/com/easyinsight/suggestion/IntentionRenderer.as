/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/17/11
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.util.AutoSizeTextArea;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.TextArea;

public class IntentionRenderer extends HBox {

    private var suggestion:IntentionSuggestion;

    private var titleLabel:Label;
    private var descriptionArea:TextArea;
    private var button:Button;

    public function IntentionRenderer() {
        titleLabel = new Label();
        descriptionArea = new AutoSizeTextArea();
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new IntentionEvent(IntentionEvent.SUGGESTION_CHOICE, suggestion));
    }

    override protected function createChildren():void {
        super.createChildren();
        setStyle("horizontalGap", 20);
        titleLabel.width = 200;
        descriptionArea.editable = false;
        descriptionArea.selectable = false;
        descriptionArea.verticalScrollPolicy = "off";
        descriptionArea.horizontalScrollPolicy = "off";
        descriptionArea.width = 300;
        button = new Button();
        button.styleName = "grayButton";
        button.label = "Apply Suggestion";
        button.addEventListener(MouseEvent.CLICK, onClick);
        setStyle("paddingRight", 10);
        setStyle("verticalAlign", "middle");
        addChild(titleLabel);
        addChild(descriptionArea);
        addChild(button);
    }

    override public function set data(val:Object):void {
        suggestion = val as IntentionSuggestion;
        if (suggestion != null) {
            titleLabel.text = suggestion.headline;
            descriptionArea.text = suggestion.description;
        }
    }

    override public function get data():Object {
        return suggestion;
    }
}
}
