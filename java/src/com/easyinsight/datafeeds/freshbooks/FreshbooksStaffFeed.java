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
 * Time: 3:26:33 PM
 */
public class FreshbooksStaffFeed extends FreshbooksFeed {
    protected FreshbooksStaffFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            FreshbooksCompositeSource parent = (FreshbooksCompositeSource) getParentSource(conn);
            if (!parent.isLiveDataSource()) {
                return super.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
            }
            Map<String, Collection<Key>> keys = new HashMap<String, Collection<Key>>();
            for (AnalysisItem analysisItem : analysisItems) {
                Collection<Key> keyColl = keys.get(analysisItem.getKey().toKeyString());
                if (keyColl == null) {
                    keyColl = new ArrayList<Key>();
                    keys.put(analysisItem.getKey().toKeyString(), keyColl);
                }
                keyColl.add(analysisItem.createAggregateKey());
            }
            DataSet dataSet = new DataSet();


                Document invoicesDoc = query("staff.list", "", conn);
                Nodes invoices = invoicesDoc.query("/response/staff_members/member");
                for (int i = 0; i < invoices.size(); i++) {
                    Node invoice = invoices.get(i);
                    String firstName = queryField(invoice, "first_name/text()");
                    String lastName = queryField(invoice, "last_name/text()");
                    String name = firstName + " " + lastName;
                    String userName = queryField(invoice, "username/text()");
                    String email = queryField(invoice, "email/text()");
                    String staffID = queryField(invoice, "staff_id/text()");

                    IRow row = dataSet.createRow();
                    addValue(row, FreshbooksStaffSource.FIRST_NAME, firstName, keys);
                    addValue(row, FreshbooksStaffSource.STAFF_ID, staffID, keys);
                    addValue(row, FreshbooksStaffSource.LAST_NAME, lastName, keys);
                    addValue(row, FreshbooksStaffSource.NAME, name, keys);
                    addValue(row, FreshbooksStaffSource.USERNAME, userName, keys);
                    addValue(row, FreshbooksStaffSource.EMAIL, email, keys);
                }

            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
