package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.CompositeFeed;
import com.easyinsight.datafeeds.CompositeFeedNode;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 4:00:05 PM
 */
public class SalesforceFeed extends CompositeFeed {

    public static final String SALESFORCE_CREDENTIAL = "salesforce";

    public SalesforceFeed(CompositeFeedNode parentNode) {
        //super(parentNode);
    }

    public FeedType getDataFeedType() {
        return FeedType.SALESFORCE;
    }

   /* private Soap getPort(Map<String, Credentials> credentials) {
        Credentials salesforceCredentials = credentials.get(SALESFORCE_CREDENTIAL);

        Soap port;
        if (salesforceCredentials == null) {
            throw new CredentialFailureException(new CredentialsDefinition(SALESFORCE_CREDENTIAL));
        }
        try {
            port = SalesforceConnection.instance().getPort(salesforceCredentials.getUserName(),
                    salesforceCredentials.getPassword());
        } catch (InvalidIdFault invalidIdFault) {
            throw new CredentialFailureException(new CredentialsDefinition(SALESFORCE_CREDENTIAL));
        } catch (LoginFault loginFault) {
            throw new CredentialFailureException(new CredentialsDefinition(SALESFORCE_CREDENTIAL));
        }
        return port;
    }    */

   /* protected DataSet getUncachedDataSet(Map<String, Credentials> credentials, List<Key> columns, Integer maxRows) {
        Soap port = getPort(credentials);
        return getData(port, columns);
    }     */

   /* private DataSet getData(Soap port, List<Key> columns) {
        // accounts have opportunities
        // accounts have contacts
        // accounts have cases
        // opportunities have products
        // it looks like opportunities also have partners and competitors
        // cases have solutions
        return rootNode.getDataSet(port, columns);
    }   */

    /*public List<AnalysisItem> getFields() {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.ACCOUNT_NUMBER, true));
        analysisItems.add(new AnalysisMeasure(AccountsQueryNode.ANNUAL_REVENUE, AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.ACCOUNT_ID, true));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.INDUSTRY, true));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.ACCOUNT_NAME, true));
        analysisItems.add(new AnalysisMeasure(AccountsQueryNode.NUMBER_EMPLOYEES, AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.RATING, true));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.SIC, true));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.SITE, true));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.TICKER_SYMBOL, true));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.TYPE, true));
        analysisItems.add(new AnalysisDimension(AccountsQueryNode.WEBSITE, true));
        return analysisItems;
    } */

    /*protected DataSet createDataSet(long accountID, String[] columns, Credentials credentials) {

        // get the web service client to salesforce

        Soap port = getSalesforceClient(accountID);

        // resolve each column internally to build the salesforce query we need

        // get the query tree
        

        // return the data sets, merge them appropriately

        String query = "select AccountNumber, AnnualRevenue, Id, Industry, Name, NumberOfEmployees, Rating," +
                "Sic, Site, TickerSymbol, Type, Website from Account";

        QueryResult qr;
        try {
            qr = port.query(query);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }


        DataSet accountDataSet = new DataSet();
        if (qr != null) {
            List<SObject> records = qr.getRecords();
            if (!records.isEmpty()) {
                for (SObject sobj : records) {
                    IRow row = accountDataSet.createRow();
                    Account account = (Account) sobj;
                    if (account.getAccountNumber() != null) row.addValue("AccountNumber", account.getAccountNumber().getValue());
                    if (account.getAnnualRevenue() != null) row.addValue("AnnualRevenue", String.valueOf(account.getAnnualRevenue().getValue()));
                    if (account.getId() != null) row.addValue("Id", account.getId().getValue());
                    if (account.getIndustry() != null) row.addValue("Industry", account.getIndustry().getValue());
                    if (account.getName() != null) row.addValue("Name", account.getName().getValue());
                    if (account.getNumberOfEmployees() != null) row.addValue("NumberOfEmployees", String.valueOf(account.getNumberOfEmployees().getValue()));
                    if (account.getRating() != null) row.addValue("Rating", account.getRating().getValue());
                    if (account.getSic() != null) row.addValue("Sic", account.getSic().getValue());
                    if (account.getSite() != null) row.addValue("Site", account.getSite().getValue());
                    if (account.getTickerSymbol() != null) row.addValue("TickerSymbol", account.getTickerSymbol().getValue());
                    if (account.type() != null) row.addValue("Type", account.type().getValue());
                    if (account.getWebsite() != null) row.addValue("Website", account.getWebsite().getValue());
                }
            }
        }
        String query2 = "select AccountId, Amount, ExpectedRevenue, Fiscal, LeadSource, Name, Probability, " +
                "StageName, Type from Opportunity";
        QueryResult qr2 = null;
        try {
            qr2 = port.query(query2);
        } catch (Exception e) {
            LogClass.error(e);
            LogClass.debug("\n\n");
        }

        if (qr2 != null) {
            List<SObject> records = qr2.getRecords();
            if (!records.isEmpty()) {
                for (SObject sobj : records) {
                    IRow row = accountDataSet.createRow();
                    Opportunity opportunity = (Opportunity) sobj;
                    if (opportunity.getAccountId() != null) row.addValue("AccountId", opportunity.getAccountId().getValue());
                    if (opportunity.getAmount() != null) row.addValue("Amount", String.valueOf(opportunity.getAmount().getValue()));
                    if (opportunity.getExpectedRevenue() != null) row.addValue("ExpectedRevenue", String.valueOf(opportunity.getExpectedRevenue().getValue()));
                    if (opportunity.getFiscal() != null) row.addValue("Fiscal", opportunity.getFiscal().getValue());
                    if (opportunity.getLeadSource() != null) row.addValue("LeadSource", opportunity.getLeadSource().getValue());
                    if (opportunity.getName() != null) row.addValue("Name", opportunity.getName().getValue());
                    if (opportunity.getProbability() != null) row.addValue("Probability", String.valueOf(opportunity.getProbability().getValue()));
                    if (opportunity.getStageName() != null) row.addValue("StageName", opportunity.getStageName().getValue());
                    if (opportunity.type() != null) row.addValue("Type", opportunity.type().getValue());
                }
            }
        }
        return accountDataSet;
    } */
}
