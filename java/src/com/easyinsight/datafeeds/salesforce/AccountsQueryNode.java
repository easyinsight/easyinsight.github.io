package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.NamedKey;
import com.easyinsight.analysis.IRow;
import com.easyinsight.logging.LogClass;
import com.sforce.soap.enterprise.Soap;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.Account;

import java.util.List;

/**
 * User: James Boe
 * Date: Jul 3, 2008
 * Time: 10:08:52 PM
 */
public class AccountsQueryNode implements ISalesforceData {

    public static final NamedKey ACCOUNT_NUMBER = new NamedKey("Account Number");
    public static final NamedKey ANNUAL_REVENUE = new NamedKey("Annual Revenue");
    public static final NamedKey ACCOUNT_ID = new NamedKey("Account Id");
    public static final NamedKey INDUSTRY = new NamedKey("Industry");
    public static final NamedKey ACCOUNT_NAME = new NamedKey("Account Name");
    public static final NamedKey NUMBER_EMPLOYEES = new NamedKey("Number of Employees");
    public static final NamedKey RATING = new NamedKey("Rating");
    public static final NamedKey SIC = new NamedKey("Sic");
    public static final NamedKey SITE = new NamedKey("Site");
    public static final NamedKey TICKER_SYMBOL = new NamedKey("Ticker Symbol");
    public static final NamedKey TYPE = new NamedKey("Type");
    public static final NamedKey WEBSITE = new NamedKey("Website");

    public DataSet getDataSet(Soap port) {
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
                    if (account.getAccountNumber() != null) row.addValue(ACCOUNT_NUMBER,
                            new StringValue(account.getAccountNumber().getValue()));
                    if (account.getAnnualRevenue() != null) row.addValue(ANNUAL_REVENUE,
                            new NumericValue(account.getAnnualRevenue().getValue()));
                    if (account.getId() != null) row.addValue(ACCOUNT_ID,
                            new StringValue(account.getId().getValue()));
                    if (account.getIndustry() != null) row.addValue(INDUSTRY,
                            new StringValue(account.getIndustry().getValue()));
                    if (account.getName() != null) row.addValue(ACCOUNT_NAME,
                            new StringValue(account.getName().getValue()));
                    if (account.getNumberOfEmployees() != null) row.addValue(NUMBER_EMPLOYEES,
                            new NumericValue(account.getNumberOfEmployees().getValue()));
                    if (account.getRating() != null) row.addValue(RATING,
                            new StringValue(account.getRating().getValue()));
                    if (account.getSic() != null) row.addValue(SIC,
                            new StringValue(account.getSic().getValue()));
                    if (account.getSite() != null) row.addValue(SITE,
                            new StringValue(account.getSite().getValue()));
                    if (account.getTickerSymbol() != null) row.addValue(TICKER_SYMBOL,
                            new StringValue(account.getTickerSymbol().getValue()));
                    if (account.getType() != null) row.addValue(TYPE,
                            new StringValue(account.getType().getValue()));
                    if (account.getWebsite() != null) row.addValue(WEBSITE,
                            new StringValue(account.getWebsite().getValue()));
                }
            }
        }
        return accountDataSet;
    }
}
