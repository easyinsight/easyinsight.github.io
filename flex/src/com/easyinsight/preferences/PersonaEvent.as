package com.easyinsight.preferences {
import flash.events.Event;

public class PersonaEvent extends Event {

    public static const NEW_PERSONA:String = "newPersona";
    public static const DELETE_PERSONA:String = "deletePersona";

    public var persona:Persona;

    public function PersonaEvent(type:String, persona:Persona) {
        super(type);
        this.persona = persona;
    }

    override public function clone():Event {
        return new PersonaEvent(type, persona);
    }
}
}