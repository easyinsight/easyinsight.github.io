/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/1/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.customupload {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.json.JSONSetup")]
public class JSONSetup {

    public var result:String;
    public var fields:ArrayCollection;
    public var generatedFields:ArrayCollection;
    public var fieldLine:String;
    public var results:int;
    public var suggestedJSONPath:String;

    public function JSONSetup() {
    }
}
}

