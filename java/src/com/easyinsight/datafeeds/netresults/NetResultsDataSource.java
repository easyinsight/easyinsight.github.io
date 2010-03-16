package com.easyinsight.datafeeds.netresults;

import com.easyinsight.client.netresults.*;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;

import java.math.BigInteger;

/**
 * User: jamesboe
 * Date: Mar 9, 2010
 * Time: 3:16:02 PM
 */
public class NetResultsDataSource {
    public static void main(String[] args) throws Exception {
        NRAPILocator locator = new NRAPILocator();
        //locator.setNRAPIPortEndpointAddress("https://apps.net-results.com/soap/v1");

        NRAPIPortType port = locator.getNRAPIPort();
        ((Stub) port)._setProperty(Call.USERNAME_PROPERTY, "jboe@easy-insight.com");
        ((Stub) port)._setProperty(Call.PASSWORD_PROPERTY, "Ichai1g");

        Contact contact = port.getContactByContactId(BigInteger.valueOf(8418636));
        System.out.println("blah");
    }
}
