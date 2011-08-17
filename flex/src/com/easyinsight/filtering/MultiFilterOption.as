/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
public class MultiFilterOption {

    public var label:String;
    public var value:Object;
    public var selected:Boolean;

    public function MultiFilterOption(label:String, value:Object, selected:Boolean) {
        this.label = label;
        this.value = value;
        this.selected = selected;
    }
}
}
