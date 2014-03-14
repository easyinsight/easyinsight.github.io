/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/19/14
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.administration.feed {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.composite.CustomFieldTag")]
public class CustomFieldTag {

    public var type:int;
    public var name:String;
    public var tagID:int;

    public function CustomFieldTag() {
    }
}
}
