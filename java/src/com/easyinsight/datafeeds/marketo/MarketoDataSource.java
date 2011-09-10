package com.easyinsight.datafeeds.marketo;

import com.easyinsight.analysis.*;
import com.easyinsight.client.marketo.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Mar 29, 2010
 * Time: 9:27:22 PM
 */
public class MarketoDataSource extends ServerDataSourceDefinition {

    public static final String LEAD_EMAIL = "Lead Email";
    public static final String LEAD_SOURCE = "Lead Source";
    public static final String LEAD_SCORE = "Lead Score";
    public static final String TITLE = "Title";
    public static final String COMPANY = "Company";
    public static final String INDUSTRY = "Industry";
    public static final String ANNUAL_REVENUE = "Annual Revenue";
    public static final String LEAD_STATUS = "Lead Status";
    public static final String LEAD_TYPE = "Lead Type";
    public static final String FIRST_NAME = "First Name";
    public static final String LAST_NAME = "Last Name";
    public static final String POSTAL_CODE = "Postal Code";
    public static final String COUNT = "Count";

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    public static void main(String[] args) {
        MktMktowsApiService service = new MktMktowsApiService();
        MktowsPort port = service.getMktowsApiSoapPort();
        ParamsGetMultipleLeads params = new ParamsGetMultipleLeads();
        SuccessGetMultipleLeads leads = port.getMultipleLeads(params);
        JAXBElement<ArrayOfLeadRecord> elements = leads.getResult().getLeadRecordList();
        List<LeadRecord> records = elements.getValue().getLeadRecord();
        for (LeadRecord leadRecord : records) {
            JAXBElement<String> element = leadRecord.getEmail();
            String email = element.getValue();
            System.out.println(email);
            JAXBElement<ArrayOfAttribute> attributes = leadRecord.getLeadAttributeList();
            ArrayOfAttribute array = attributes.getValue();
            List<Attribute> attributeList = array.getAttribute();
            for (Attribute attribute : attributeList) {
                System.out.println("\t" + attribute.getAttrName() + "\t" + attribute.getAttrValue());
            }
        }
        
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PROFESSIONAL;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.MARKETO;
    }

    public static String getHexString(byte[] b) {
          String result = "";
          for (int i=0; i < b.length; i++) {
            result +=
                  Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
          }
          return result;
        }
    
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        DataSet dataSet = new DataSet();
        try {
            DatatypeFactory fac = DatatypeFactory.newInstance();
            MktMktowsApiService service = new MktMktowsApiService();
            MktowsPort port = service.getMktowsApiSoapPort();
            List<Header> headers = (List<Header>) ((BindingProvider)port).getRequestContext().get(Header.HEADER_LIST);
            AuthenticationHeaderInfo header = new AuthenticationHeaderInfo();
            String userID = "impactstreet1_014261584BA9744F1173B4";
            String key = "675731691623111344BBAA99774455011112CD322F47";
            header.setMktowsUserId(userID);
            GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
            cal.setTime(new Date());
            XMLGregorianCalendar timestamp = fac.newXMLGregorianCalendar(cal);
            header.setRequestTimestamp(timestamp.toXMLFormat());
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal((timestamp.toXMLFormat() + userID).getBytes("UTF-8"));

            // base64-encode the hmac

            header.setRequestSignature(getHexString(rawHmac).toLowerCase());
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
            timestamp = fac.newXMLGregorianCalendar(cal);
            params.setLastUpdatedAt(timestamp);
            SuccessGetMultipleLeads leads = port.getMultipleLeads(params);
            JAXBElement<ArrayOfLeadRecord> elements = leads.getResult().getLeadRecordList();
            List<LeadRecord> records = elements.getValue().getLeadRecord();
            for (LeadRecord leadRecord : records) {
                JAXBElement<String> element = leadRecord.getEmail();
                String email = element.getValue();
                System.out.println(email);
                JAXBElement<ArrayOfAttribute> attributes = leadRecord.getLeadAttributeList();
                ArrayOfAttribute array = attributes.getValue();
                IRow row = dataSet.createRow();
                row.addValue(keys.get(LEAD_EMAIL), email);
                row.addValue(keys.get(COUNT), new NumericValue(1));
                List<Attribute> attributeList = array.getAttribute();
                row.addValue(keys.get(LEAD_SCORE), fromAttribute("LeadScore", attributeList, Value.NUMBER));
                row.addValue(keys.get(TITLE), fromAttribute("Title", attributeList, Value.STRING));
                row.addValue(keys.get(ANNUAL_REVENUE), fromAttribute("AnnualRevenue", attributeList, Value.NUMBER));
                row.addValue(keys.get(COMPANY), fromAttribute("Company", attributeList, Value.STRING));
                row.addValue(keys.get(INDUSTRY), fromAttribute("Industry", attributeList, Value.STRING));
                row.addValue(keys.get(LEAD_STATUS), fromAttribute("LeadStatus", attributeList, Value.STRING));
                row.addValue(keys.get(LEAD_TYPE), fromAttribute("Type__c", attributeList, Value.STRING));
                row.addValue(keys.get(FIRST_NAME), fromAttribute("FirstName", attributeList, Value.STRING));
                row.addValue(keys.get(LAST_NAME), fromAttribute("LastName", attributeList, Value.STRING));
                row.addValue(keys.get(POSTAL_CODE), fromAttribute("PostalCode", attributeList, Value.STRING));
                /*for (Attribute attribute : attributeList) {
                    System.out.println("\t" + attribute.getAttrName() + "\t" + attribute.getAttrValue());
                }*/
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    private Value fromAttribute(String key, List<Attribute> attributeList, int type) {
        Value value = null;
        for (Attribute attribute : attributeList) {
            if (key.equals(attribute.getAttrName())) {
                if (type == Value.NUMBER) {
                    value = new NumericValue(Double.parseDouble(attribute.getAttrValue()));
                } else if (type == Value.DATE) {

                } else if (type == Value.STRING) {
                    value = new StringValue(attribute.getAttrValue());
                }
            }
        }
        return value;
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(LEAD_EMAIL, COUNT, LEAD_SCORE, TITLE, COMPANY, INDUSTRY, ANNUAL_REVENUE, LEAD_STATUS, LEAD_TYPE,
                FIRST_NAME, LAST_NAME, POSTAL_CODE, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(LEAD_EMAIL), true));
        items.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.COUNT));
        items.add(new AnalysisMeasure(keys.get(LEAD_SCORE), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(ANNUAL_REVENUE), AggregationTypes.SUM));
        items.add(new AnalysisDimension(keys.get(TITLE), true));
        items.add(new AnalysisDimension(keys.get(COMPANY), true));
        items.add(new AnalysisDimension(keys.get(INDUSTRY), true));
        items.add(new AnalysisDimension(keys.get(LEAD_STATUS), true));
        items.add(new AnalysisDimension(keys.get(LEAD_TYPE), true));
        items.add(new AnalysisDimension(keys.get(FIRST_NAME), true));
        items.add(new AnalysisDimension(keys.get(LAST_NAME), true));
        items.add(new AnalysisZipCode(keys.get(POSTAL_CODE), true));
        return items;
    }

    @Override
    public boolean isLongRefresh() {
        return true;
    }
}
