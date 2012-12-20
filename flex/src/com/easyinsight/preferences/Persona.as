package com.easyinsight.preferences {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.Persona")]
public class Persona {

    public static const EMPTY_PERSONA:Object = { name: "[ No Persona ]" };

    public var name:String;
    public var uiSettings:UISettings;
    public var personaID:int;
    public var selected:Boolean;
    public var dataSourceDLS:ArrayCollection = new ArrayCollection();

    public function Persona() {
    }
}
}
