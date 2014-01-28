package com.easyinsight.datafeeds.kashoo;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import nu.xom.ParsingException;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:43 PM
 */
public class KashooRecordSource extends KashooBaseSource {

    public static final String RECORD_ID = "Record ID";
    public static final String BUSINESS_ID = "Business ID";
    public static final String TYPE = "Record Type";
    public static final String KEEP_IN_ORIGINAL_CURRENCY = "Keep in Original Currency";
    public static final String EXCHANGE_PAYMENT = "Record - Exchange Payment";
    public static final String CURRENCY = "Currency";
    public static final String DATE = "Date";
    public static final String COLLABERATION_CONTEXT = "Collaberation Context";
    public static final String TERMS = "Terms";
    public static final String DEPOSIT_CURRENCY = "Deposit Currency";
    public static final String REMOVED = "Removed";
    public static final String BALANCE_DUE = "Balance Due";
    public static final String DEPOSIT_ACCOUNT = "Deposit Account";
    public static final String NUMBER = "Number";
    public static final String AMOUNT = "Amount";
    public static final String TOTAL_DUE = "Total Due";
    public static final String CONTACT_NAME = "Contact Name";
    public static final String ACCOUNT = "Account";
    public static final String DUE_DATE = "Due Date";
    public static final String CONTACT = "Contact ID";
    public static final String MEMO = "Memo";
    public static final String READ_ONLY = "Read Only";
    public static final String PO_NUMBER = "PO Number";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public KashooRecordSource() {
        setFeedName("Records");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(RECORD_ID, BUSINESS_ID, TYPE, KEEP_IN_ORIGINAL_CURRENCY, EXCHANGE_PAYMENT, CURRENCY, DATE, COLLABERATION_CONTEXT, TERMS, DEPOSIT_CURRENCY, REMOVED, BALANCE_DUE,
                DUE_DATE, DEPOSIT_ACCOUNT, NUMBER, AMOUNT, TOTAL_DUE, CONTACT_NAME, CONTACT, ACCOUNT, MEMO, READ_ONLY, PO_NUMBER);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.KASHOO_RECORDS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(RECORD_ID)));
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_ID)));
        analysisItems.add(new AnalysisDimension(keys.get(TYPE)));
        analysisItems.add(new AnalysisDimension(keys.get(KEEP_IN_ORIGINAL_CURRENCY)));
        analysisItems.add(new AnalysisDimension(keys.get(EXCHANGE_PAYMENT)));
        analysisItems.add(new AnalysisDimension(keys.get(CURRENCY)));
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE), true));
        analysisItems.add(new AnalysisDimension(keys.get(COLLABERATION_CONTEXT)));
        analysisItems.add(new AnalysisDimension(keys.get(TERMS)));
        analysisItems.add(new AnalysisDimension(keys.get(DEPOSIT_CURRENCY)));
        analysisItems.add(new AnalysisDimension(keys.get(REMOVED)));
        analysisItems.add(new AnalysisMeasure(keys.get(BALANCE_DUE), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(keys.get(DEPOSIT_ACCOUNT)));
        analysisItems.add(new AnalysisDimension(keys.get(NUMBER)));
        analysisItems.add(new AnalysisMeasure(keys.get(AMOUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(TOTAL_DUE), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_NAME)));
        analysisItems.add(new AnalysisDimension(keys.get(ACCOUNT)));
        analysisItems.add(new AnalysisDateDimension(keys.get(DUE_DATE), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT)));
        analysisItems.add(new AnalysisDimension(keys.get(MEMO)));
        analysisItems.add(new AnalysisDimension(keys.get(READ_ONLY)));
        analysisItems.add(new AnalysisDimension(keys.get(PO_NUMBER)));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Token tokenObj = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.KASHOO, parentDefinition.getDataFeedID(), false, conn);
        // TODO: change to json encoding for auth key, per https://www.kashoo.com/api-docs/
        HttpClient httpClient = getHttpClient();
        DataSet dataSet = new DataSet();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            JSONArray resp = (JSONArray) runRestRequest("/api/users/me/businesses", httpClient, parentDefinition, tokenObj.getTokenValue());
            for(Object o : resp) {
                JSONObject curObject = (JSONObject) o;

                String businessID = String.valueOf(curObject.get("id"));
                int page = 0;
                JSONArray responseObject;
                do {
                    responseObject = (JSONArray) runRestRequest("/api/businesses/" + businessID + "/records?limit=" + PAGE_LIMIT + "&offset=" + (page * PAGE_LIMIT), httpClient, parentDefinition, tokenObj.getTokenValue());
                    for(Object oo : responseObject) {
                        try {
                            JSONObject jj = (JSONObject) oo;
                            IRow row = dataSet.createRow();
                            row.addValue(keys.get(RECORD_ID), String.valueOf(jj.get("id")));
                            row.addValue(keys.get(BUSINESS_ID), businessID);
                            row.addValue(keys.get(TYPE), String.valueOf(jj.get("type")));
                            row.addValue(keys.get(KEEP_IN_ORIGINAL_CURRENCY), String.valueOf(jj.get("keepInOriginalCurrency")));
                            row.addValue(keys.get(EXCHANGE_PAYMENT), String.valueOf(jj.get("exchangePayment")));
                            row.addValue(keys.get(CURRENCY), String.valueOf(jj.get("currency")));
                            row.addValue(keys.get(DATE), jj.get("date") != null ? new DateValue(dateFormat.parse(String.valueOf(jj.get("date")))) : new EmptyValue());
                            row.addValue(keys.get(COLLABERATION_CONTEXT), String.valueOf(jj.get("collaberationContext")));
                            row.addValue(keys.get(TERMS), String.valueOf(jj.get("terms")));
                            row.addValue(keys.get(DEPOSIT_CURRENCY), String.valueOf(jj.get("depositCurrency")));
                            row.addValue(keys.get(REMOVED), String.valueOf(jj.get("removed")));
                            row.addValue(keys.get(BALANCE_DUE), jj.get("balanceDue") != null ? new NumericValue(Double.valueOf((Integer) jj.get("balanceDue")) / 100.0) : new EmptyValue());
                            row.addValue(keys.get(DEPOSIT_ACCOUNT), String.valueOf(jj.get("depositAccount")));
                            row.addValue(keys.get(NUMBER), String.valueOf(jj.get("number")));
                            row.addValue(keys.get(AMOUNT), jj.get("amount") != null ? new NumericValue(Double.valueOf((Integer) jj.get("amount")) / 100.0) : new EmptyValue());
                            row.addValue(keys.get(TOTAL_DUE), jj.get("totalDue") != null ? new NumericValue(Double.valueOf((Integer) jj.get("totalDue")) / 100.0) : new EmptyValue());
                            row.addValue(keys.get(CONTACT_NAME), String.valueOf(jj.get("contactName")));
                            row.addValue(keys.get(ACCOUNT), String.valueOf(jj.get("account")));
                            row.addValue(keys.get(DUE_DATE), jj.get("dueDate") != null ? new DateValue(dateFormat.parse(String.valueOf(jj.get("dueDate")))) : new EmptyValue());
                            row.addValue(keys.get(CONTACT), String.valueOf(jj.get("contact")));
                            row.addValue(keys.get(MEMO), String.valueOf(jj.get("memo")));
                            row.addValue(keys.get(READ_ONLY), String.valueOf(jj.get("readOnly")));
                            row.addValue(keys.get(PO_NUMBER), String.valueOf(jj.get("poNumber")));

                        } catch (ParseException e) {
                            LogClass.error(e);
                        }
                    }

                    page = page + 1;
                } while(responseObject.size() != 0);
            }
        } catch (ParsingException e) {
            throw new RuntimeException(e);
        }

        return dataSet;
    }
}
