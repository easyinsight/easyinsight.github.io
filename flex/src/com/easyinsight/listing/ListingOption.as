package com.easyinsight.listing
{
import mx.containers.VBox;
import flash.events.MouseEvent;
import mx.controls.Image;
	import mx.collections.ArrayCollection;

[Event(name="listingChange", type="com.easyinsight.listing.ListingChangeEvent")]
	public class ListingOption extends VBox
	{
		public static const MY_ANALYSES:String = "MyAnalyses";
		public static const MY_SUBSCRIPTIONS:String = "MySubscriptions";
		public static const CATALOG:String = "Catalog";
		public static const UPLOADS:String = "MyUploads";

        private var image:Image;
		
		public var _displayName:String;
		public var _perspective:IPerspective;
		public var _enableAnalysis:Boolean;
		public var _iconClass:Class;
		[Bindable]
		public var children:ArrayCollection;
		
		public function ListingOption()
		{
            setStyle("horizontalAlign", "center");
            setStyle("verticalAlign", "middle");
            addEventListener(MouseEvent.CLICK, listingChange);
            addEventListener(MouseEvent.MOUSE_OVER, onMouseOver);
            addEventListener(MouseEvent.MOUSE_OUT, onMouseOut);
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
        public function set perspective(val:IPerspective):void {
            _perspective = val;
        }
        public function set enableAnalysis(val:Boolean):void {
            _enableAnalysis = val;
        }
        public function set iconClass(val:Class):void {
            _iconClass = val;
        }

        public function get displayName():String {
            return _displayName;
        }
        public function get perspective():IPerspective {
            return _perspective;
        }
        public function get enableAnalysis():Boolean {
            return _enableAnalysis;
        }
        public function get iconClass():Class {
            return _iconClass;
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