package com.easyinsight.pseudocontext {
public class PseudoContextItem {

    private var _label:String;
    private var _click:Function;

    public function PseudoContextItem(label:String, click:Function) {
        this._label = label;
        this._click = click;
    }

    public function get label():String {
        return _label;
    }

    public function get click():Function {
        return _click;
    }
}
}