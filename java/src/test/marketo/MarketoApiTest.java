package test.marketo;

import junit.framework.TestCase;
import com.easyinsight.client.marketo.*;

import javax.xml.ws.BindingProvider;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;

import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.commons.codec.binary.Base64;

import java.util.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Mar 29, 2010
 * Time: 9:51:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarketoApiTest extends TestCase {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    
    public void testMarketo() {
        try {
            MktMktowsApiService service = new MktMktowsApiService();
            MktowsPort port = service.getMktowsApiSoapPort();
            List<Header> headers = (List<Header>) ((BindingProvider)port).getRequestContext().get(Header.HEADER_LIST);
            AuthenticationHeaderInfo header = new AuthenticationHeaderInfo();
            String userID = "";
            String key = "";
            header.setMktowsUserId(userID);
            GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
            cal.setTime(new Date());
            XMLGregorianCalendar timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            header.setRequestTimestamp(timestamp.toXMLFormat());
            SecretKeySpec signingKey = new SecretKeySpec(new BigInteger(key, 16).toByteArray(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal((timestamp.toXMLFormat() + userID).getBytes());

            // base64-encode the hmac

            header.setRequestSignature(new String(Base64.encodeBase64(rawHmac)).toLowerCase());
            QName name = new QName("http://www.marketo.com/mktows/", "AuthenticationHeader");
            Header authHeader = new Header(name, header, new JAXBDataBinding(AuthenticationHeaderInfo.class));
            if(headers == null)
                headers = new ArrayList<Header>();
            headers.add(authHeader);
            ((BindingProvider)port).getRequestContext().put(Header.HEADER_LIST, headers);
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://na-d.marketo.com/soap/mktows/1_2");
            ParamsGetMultipleLeads params = new ParamsGetMultipleLeads();
            cal = (GregorianCalendar) GregorianCalendar.getInstance();
            cal.set(2010, 0, 1);
            timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            params.setLastUpdatedAt(timestamp);
            SuccessGetMultipleLeads result = port.getMultipleLeads(params);
            } catch (DatatypeConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
