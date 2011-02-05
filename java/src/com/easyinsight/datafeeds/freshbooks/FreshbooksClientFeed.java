package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 29, 2010
 * Time: 1:46:46 PM
 */
public class FreshbooksClientFeed extends FreshbooksFeed {
    public FreshbooksClientFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey);
    }
    
    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            Map<String, Key> keys = new HashMap<String, Key>();
            for (AnalysisItem analysisItem : analysisItems) {
                keys.put(analysisItem.getKey().toKeyString(), analysisItem.createAggregateKey());
            }
            DataSet dataSet = new DataSet();

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("client.list", "<page>" + requestPage + "</page>", conn);
                Node invoicesSummaryNode = invoicesDoc.query("/response/clients").get(0);
                String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                pages = Integer.parseInt(pageString);
                currentPage = Integer.parseInt(currentPageString);
                Nodes invoices = invoicesDoc.query("/response/clients/client");
                for (int i = 0; i < invoices.size(); i++) {
                    Node invoice = invoices.get(i);
                    String firstName = queryField(invoice, "first_name/text()");
                    String lastName = queryField(invoice, "last_name/text()");
                    String name = firstName + " " + lastName;
                    String userName = queryField(invoice, "username/text()");
                    String email = queryField(invoice, "email/text()");
                    String clientID = queryField(invoice, "client_id/text()");
                    String workPhone = queryField(invoice, "work_phone/text()");
                    String address1 = queryField(invoice, "p_street1/text()");
                    String address2 = queryField(invoice, "p_street2/text()");
                    String city = queryField(invoice, "p_city/text()");
                    String state = queryField(invoice, "p_state/text()");
                    String zip = queryField(invoice, "p_code/text()");
                    String country = queryField(invoice, "p_country/text()");
                    String organization = queryField(invoice, "organization/text()");

                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(FreshbooksClientSource.FIRST_NAME), firstName);
                    row.addValue(keys.get(FreshbooksClientSource.CLIENT_ID), clientID);
                    row.addValue(keys.get(FreshbooksClientSource.LAST_NAME), lastName);
                    row.addValue(keys.get(FreshbooksClientSource.NAME), name);
                    row.addValue(keys.get(FreshbooksClientSource.USERNAME), userName);
                    row.addValue(keys.get(FreshbooksClientSource.EMAIL), email);
                    row.addValue(keys.get(FreshbooksClientSource.WORK_PHONE), workPhone);
                    row.addValue(keys.get(FreshbooksClientSource.PRIMARY_STREET1), address1);
                    row.addValue(keys.get(FreshbooksClientSource.PRIMARY_STREET2), address2);
                    row.addValue(keys.get(FreshbooksClientSource.CITY), city);
                    row.addValue(keys.get(FreshbooksClientSource.STATE), state);
                    row.addValue(keys.get(FreshbooksClientSource.POSTAL), zip);
                    row.addValue(keys.get(FreshbooksClientSource.COUNTRY), country);
                    row.addValue(keys.get(FreshbooksClientSource.ORGANIZATION), organization);
                    row.addValue(keys.get(FreshbooksClientSource.COUNT), 1);
                }
                requestPage++;
            } while (currentPage < pages);
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
