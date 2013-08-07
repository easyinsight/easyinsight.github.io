/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/25/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.events.MouseEvent;

import mx.containers.Canvas;
import mx.controls.Text;

public class TagButton extends Canvas {

    public var tag:Tag;
    private var tagSelected:Boolean;

    private var tagLabel:Text;

    public function TagButton() {
        addEventListener(MouseEvent.CLICK, onClick);
        tagLabel = new Text();
        addChild(tagLabel);
        mouseChildren = false;
        mouseEnabled = true;
        buttonMode = true;
        useHandCursor = true;
        styleName = "defaultLinkButton";
    }

    private function onClick(event:MouseEvent):void {
        if (tagSelected) {
            tagSelected = false;
            styleName = "defaultLinkButton";
            dispatchEvent(new TagEvent(TagEvent.TAG_UNSELECTED, tag));
        } else {
            tagSelected = true;
            styleName = "toggledLinkButton";
            dispatchEvent(new TagEvent(TagEvent.TAG_SELECTED, tag));
        }
    }

    public function set selectionState(selected:Boolean):void {
        tagSelected = selected;
        if (tagSelected) {
            styleName = "toggledLinkButton";
        } else {
            styleName = "defaultLinkButton";
        }
    }

    override public function set data(val:Object):void {
        tag = val as Tag;
        tagLabel.text = tag.name + " - "+ tag.id;
    }

    override public function get data():Object {
        return tag;
    }


}
}
