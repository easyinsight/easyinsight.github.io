/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/13/11
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountAreaInfo")]
public class AccountAreaInfo {
    
    public var addressLine1:String;
    public var addressLine2:String;
    public var city:String;
    public var state:String;
    public var postalCode:String;
    public var country:String;
    public var vat:String;
    public var companyName:String;
    
    public function AccountAreaInfo() {
    }
}
}
