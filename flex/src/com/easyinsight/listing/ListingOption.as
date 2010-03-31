package com.easyinsight.listing
{

import mx.containers.VBox;
import flash.events.MouseEvent;
	import mx.collections.ArrayCollection;

[Event(name="listingChange", type="com.easyinsight.listing.ListingChangeEvent")]
	public class ListingOption extends VBox
	{
		public var _displayName:String;
        public var _perspectiveClass:Class;
		public var _perspective:IPerspective;
		[Bindable]
		public var children:ArrayCollection;
		
		public function ListingOption()
		{
            setStyle("horizontalAlign", "center");
            setStyle("verticalAlign", "middle");
            addEventListener(MouseEvent.CLICK, listingChange);
            addEventListener(MouseEvent.MOUSE_OVER, onMouseOver);
            addEventListener(MouseEvent.MOUSE_OUT, onMouseOut);
            useHandCursor = true;
            buttonMode = true;
		}


    protected override function commitProperties():void {
        super.commitProperties();
        if (_perspective == null && _perspectiveClass != null) {
            _perspective = new _perspectiveClass();
        }
    }

    private function listingChange(event:MouseEvent):void {
            dispatchEvent(new ListingChangeEvent(perspective));
        }

        private function onMouseOver(event:MouseEvent):void {
            this.setStyle("backgroundColor", 0xC8C8C8);
        }

        private function onMouseOut(event:MouseEvent):void {
            this.setStyle("backgroundColor", 0xF0F0F0);
        }

        public function set displayName(val:String):void {
            _displayName = val;
        }


    public function set perspectiveClass(value:Class):void {
        _perspectiveClass = value;
        invalidateProperties();
    }

    public function get displayName():String {
            return _displayName;
        }
        public function get perspective():IPerspective {
            return _perspective;
        }
        /*override protected function createChildren():void {
            super.createChildren();
            if (image == null) {
                image = new Image();
                image.source = _iconClass;
                image.percentHeight = 100;
                image.percentWidth = 100;
            }
            addChild(image);
        } */
    }
}