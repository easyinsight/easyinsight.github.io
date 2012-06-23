/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/20/12
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.basecampnext.BasecampNextAccount")]
public class BasecampNextAccount {

    public var id:String;
    public var name:String;

    public function BasecampNextAccount() {
    }
}
}
