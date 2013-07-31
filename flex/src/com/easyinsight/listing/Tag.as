/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/25/13
 * Time: 8:30 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {

[Bindable]
[RemoteClass(alias="com.easyinsight.tag.Tag")]
public class Tag {

    public var name:String;
    public var id:int;
    public var selected:Boolean;

    public function Tag() {
    }
}
}
