package com.easyinsight.account
{
import com.easyinsight.framework.UserTransferObject;
import com.easyinsight.util.PersonaItemComboBox;

import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.ComboBox;
	import mx.events.ListEvent;

	public class UserPersonaEditor extends HBox
	{
		private var user:UserTransferObject;
		private var comboBox:PersonaItemComboBox;

        private var _personas:ArrayCollection;
		
		public function UserPersonaEditor()
		{
			super();
			setStyle("horizontalAlign", "center");
			this.percentWidth = 100;
            comboBox = new PersonaItemComboBox();
            comboBox.labelField = "name";
            BindingUtils.bindProperty(comboBox, "dataProvider", this, "personas");
            BindingUtils.bindProperty(comboBox, "selectedValue", this, "personaID");
            comboBox.selectedProperty = "personaID";
            comboBox.addEventListener(ListEvent.CHANGE, change);
		}

        [Bindable(event="personasChanged")]
        public function get personas():ArrayCollection {
            return _personas;
        }

        public function set personas(value:ArrayCollection):void {
            if (_personas == value) return;
            _personas = value;
            dispatchEvent(new Event("personasChanged"));
        }

        private var _personaID:int;


        [Bindable(event="personaIDChanged")]
        public function get personaID():int {
            return _personaID;
        }

        public function set personaID(value:int):void {
            if (_personaID == value) return;
            _personaID = value;
            dispatchEvent(new Event("personaIDChanged"));
        }

        override protected function createChildren():void {

			addChild(comboBox);
		}
		
		private function change(event:ListEvent):void {
            user.personaID = comboBox.selectedItem.personaID;
			dispatchEvent(new Event("userChange", true));
		}
		
		[Bindable]
		override public function set data(object:Object):void {
			this.user = object as UserTransferObject;
            personaID = user.personaID;
		}
		
		override public function get data():Object {
			return this.user;
		}
		
	}
}