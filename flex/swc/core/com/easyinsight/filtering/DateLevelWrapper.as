/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/15/11
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DateLevelWrapper")]
public class DateLevelWrapper {

    public var dateLevel:int;
    public var display:String;
    public var shortDisplay:String;

    public function DateLevelWrapper() {
    }
}
}
