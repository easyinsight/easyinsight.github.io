package test.apithrottling;

import junit.framework.TestCase;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.GregorianCalendar;


/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 1, 2009
 * Time: 10:17:07 AM
 */
public class testApiThrottle extends TestCase{
    public void testQuickApiThrottle() {
        BasicAuthValidatingPublishServiceService service = new BasicAuthValidatingPublishServiceService();
        BasicAuthValidatedPublish port = service.getBasicAuthValidatingPublishServicePort();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "testuser");
        ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "password");
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/app/services/ValidatedPublishBasic");
        Row r = new Row();
        NumberPair pair = new NumberPair();
        pair.setKey("Inventory Quantity");
        pair.setValue(173);
        r.getNumberPairs().add(pair);
        DatePair datepair = new DatePair();
        GregorianCalendar g = new GregorianCalendar();

        // wtf
        XMLGregorianCalendar xgc = null;
        try {
            xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(g);
        } catch (DatatypeConfigurationException e) {
        }

        datepair.setKey("Month");
        datepair.setValue(xgc);
        r.getDatePairs().add(datepair);

        StringPair sp1 = new StringPair();
        sp1.setKey("Product Brand");
        sp1.setValue("Kotex");
        r.getStringPairs().add(sp1);
        StringPair sp2 = new StringPair();
        sp2.setKey("Product Category");
        sp2.setValue("Tampons");
        r.getStringPairs().add(sp2);
        StringPair sp3 = new StringPair();
        sp3.setKey("Store");
        sp3.setValue("Safeway");
        r.getStringPairs().add(sp3);

        NumberPair np2 = new NumberPair();
        np2.setKey("Unit Profit");
        np2.setValue(.5);
        r.getNumberPairs().add(np2);
        NumberPair np3 = new NumberPair();
        np3.setKey("Unit Revenue");
        np3.setValue(2.5);
        r.getNumberPairs().add(np3);
        try {
            port.addRow("dsiKwTceJzcJ", r);
            assertTrue(false); // we should be expecting an exception, if not, blow up.
        }
        catch(SOAPFaultException sf) {
            assertEquals("Exceeded your limit.", sf.getMessage());
        }
    }
}
