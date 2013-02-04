/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/23/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.MultiColor")]
public class MultiColor {

    public var color1Start:int;
    public var color1StartEnabled:Boolean;
    public var color1End:int;
    public var color1EndEnabled:Boolean;

    public function MultiColor(color1Start:int = 0, color1StartEnabled:Boolean = false, color1End:int = 0, color1EndEnabled:Boolean = false) {
        this.color1Start = color1Start;
        this.color1StartEnabled = color1StartEnabled;
        this.color1End = color1End;
        this.color1EndEnabled = color1EndEnabled;
    }
}
}
