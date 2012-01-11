/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/9/12
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.Page")]
public class Page {
    
    public var bytes:ByteArray;
    public var width:int;
    public var height:int;
    
    public function Page() {
    }
}
}
