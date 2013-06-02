/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/29/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.customupload {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.database.SchemaTable")]
public class SchemaTable {

    public var name:String;
    public var schemaColumns:ArrayCollection;

    public function SchemaTable() {
    }
}
}
