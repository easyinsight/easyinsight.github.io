/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/21/12
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.account {
import mx.formatters.DateFormatter;

[Bindable]
[RemoteClass(alias="com.easyinsight.admin.NewsEntry")]
public class NewsEntry {

    public var news:String;
    public var title:String;
    public var date:Date;
    public var id:int;

    public function NewsEntry() {
    }

    public function get label():String {
        var df:DateFormatter  = new DateFormatter();
        df.format("yyyy-MM-dd");
        return df.format(date);
    }
}
}
