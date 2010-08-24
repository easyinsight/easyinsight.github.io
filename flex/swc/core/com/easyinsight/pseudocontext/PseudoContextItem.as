package com.easyinsight.pseudocontext {
public class PseudoContextItem {

    private var _label:String;
    private var _click:Function;
    private var _data:Object;

    public function PseudoContextItem(label:String, click:Function, data:Object = null) {
        this._label = label;
        this._click = click;
        this._data = data;
    }


    public function get data():Object {
        return _data;
    }

    public function get label():String {
        return _label;
    }

    public function get click():Function {
        return _click;
    }
}
}