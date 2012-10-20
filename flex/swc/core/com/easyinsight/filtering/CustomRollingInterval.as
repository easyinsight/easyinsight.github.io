/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/18/12
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.CustomRollingInterval")]
public class CustomRollingInterval {

    public var customRollingIntervalID:int;
    public var intervalNumber:int;
    public var filterLabel:String;
    public var endDefined:Boolean;
    public var startDefined:Boolean;
    public var endScript:String;
    public var startScript:String;

    public function CustomRollingInterval() {
    }
}
}
