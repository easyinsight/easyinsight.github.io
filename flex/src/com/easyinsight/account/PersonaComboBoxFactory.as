package com.easyinsight.account {
import flash.events.Event;
import flash.events.EventDispatcher;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.core.IFactory;

public class PersonaComboBoxFactory extends EventDispatcher implements IFactory {

    private var _personas:ArrayCollection;

    public function PersonaComboBoxFactory() {
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

    public function newInstance():* {
        var editor:UserPersonaEditor = new UserPersonaEditor();
        BindingUtils.bindProperty(editor, "personas", this, "personas");
        return editor;
    }
}
}