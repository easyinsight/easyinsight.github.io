package com.easyinsight.framework {
public class FragmentTester {

    public var fragment:String;
    public var onMatch:Function;

    public function FragmentTester(fragment:String, onMatch:Function) {
        this.fragment = fragment;
        this.onMatch = onMatch;
    }

    public function test(obj:Object, workspace:PrimaryWorkspace):Boolean {
        var property:String = obj[fragment];
        if (property != null) {
            onMatch.call(null, property, workspace, obj);
            return true;
        }
        return false;
    }
}
}