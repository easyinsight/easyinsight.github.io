package com.anotherflexdev.diagrammer {
	import flash.events.Event;

	public class LabelLinkEvent extends Event {
		public static const LABEL_LINK:String = "labelLink";
		public var value:String;
        public var link:Link;
		
		public function LabelLinkEvent(value:String, link:Link = null) {
			super(LABEL_LINK);
			this.value = value;
            this.link = link;
		}

        override public function clone():Event {
            return new LabelLinkEvent(value, link);
        }
    }
}