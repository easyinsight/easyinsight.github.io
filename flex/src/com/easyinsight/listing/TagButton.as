/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/25/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.events.MouseEvent;

import mx.controls.LinkButton;

public class TagButton extends LinkButton {

    public var tag:Tag;
    private var tagSelected:Boolean;

    public function TagButton() {
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        if (tagSelected) {
            tagSelected = false;
            dispatchEvent(new TagEvent(TagEvent.TAG_UNSELECTED, tag));
        } else {
            tagSelected = true;
            dispatchEvent(new TagEvent(TagEvent.TAG_SELECTED, tag));
        }
    }

    override public function set data(val:Object):void {
        tag = val as Tag;
        label = tag.name;
    }

    override public function get data():Object {
        return tag;
    }


}
}
