package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.NamedKey;
import com.easyinsight.IRow;
import com.easyinsight.logging.LogClass;
import com.sforce.soap.enterprise.Soap;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Opportunity;

import java.util.List;

/**
 * User: James Boe
 * Date: Jul 3, 2008
 * Time: 10:08:52 PM
 */
public class OpportunitiesQueryNode implements ISalesforceData {

    public static final NamedKey ACCOUNT_ID = new NamedKey("Account Id");
    public static final NamedKey AMOUNT = new NamedKey("Amount");
    public static final NamedKey EXPECTED_REVENUE = new NamedKey("Expected Revenue");
    public static final NamedKey FISCAL = new NamedKey("Fiscal");
    public static final NamedKey LEAD_SOURCE = new NamedKey("Lead Source");
    public static final NamedKey NAME = new NamedKey("Opportunity Name");
    public static final NamedKey PROBABILITY = new NamedKey("Probability");
    public static final NamedKey STAGE_NAME = new NamedKey("Stage");
    public static final NamedKey TYPE = new NamedKey("Opportunity Type");

    public DataSet getDataSet(Soap port) {
        String query2 = "select AccountId, Amount, ExpectedRevenue, Fiscal, LeadSource, Name, Probability, " +
                "StageName, Type from Opportunity";
        QueryResult opportunities = null;
        try {
            opportunities = port.query(query2);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }

        DataSet opportunityDataSet = new DataSet();

        if (opportunities != null) {
            List<SObject> records = opportunities.getRecords();
            if (!records.isEmpty()) {
                for (SObject sobj : records) {
                    IRow row = opportunityDataSet.createRow();
                    Opportunity opportunity = (Opportunity) sobj;
                    if (opportunity.getAccountId() != null) row.addValue(ACCOUNT_ID, new StringValue(opportunity.getAccountId().getValue()));
                    if (opportunity.getAmount() != null) row.addValue(AMOUNT, new NumericValue(opportunity.getAmount().getValue()));
                    if (opportunity.getExpectedRevenue() != null) row.addValue(EXPECTED_REVENUE, new NumericValue(opportunity.getExpectedRevenue().getValue()));
                    if (opportunity.getFiscal() != null) row.addValue(FISCAL, new StringValue(opportunity.getFiscal().getValue()));
                    if (opportunity.getLeadSource() != null) row.addValue(LEAD_SOURCE, new StringValue(opportunity.getLeadSource().getValue()));
                    if (opportunity.getName() != null) row.addValue(NAME, new StringValue(opportunity.getName().getValue()));
                    if (opportunity.getProbability() != null) row.addValue(PROBABILITY, new NumericValue(opportunity.getProbability().getValue()));
                    if (opportunity.getStageName() != null) row.addValue(STAGE_NAME, new StringValue(opportunity.getStageName().getValue()));
                    if (opportunity.getType() != null) row.addValue(TYPE, new StringValue(opportunity.getType().getValue()));
                }
            }
        }

        return opportunityDataSet;
    }
}