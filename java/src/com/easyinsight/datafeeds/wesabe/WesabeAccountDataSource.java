package com.easyinsight.datafeeds.wesabe;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.StringValue;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.sql.Connection;

/**
 * User: jboe
 * Date: Jul 16, 2009
 * Time: 9:47:54 AM
 */
public class WesabeAccountDataSource extends WesabeBaseSource {

    public static final String ACCOUNT_NAME = "Account Name";
    public static final String ACCOUNT_BALANCE = "Account Balance";

    public WesabeAccountDataSource() {
        setFeedName("Accounts");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(ACCOUNT_NAME, ACCOUNT_ID, ACCOUNT_BALANCE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(ACCOUNT_NAME), true));
        AnalysisDimension accountDimension = new AnalysisDimension(keys.get(ACCOUNT_ID), true);
        accountDimension.setHidden(true);
        analysisItems.add(accountDimension);
        AnalysisMeasure amountMeasure = new AnalysisMeasure(keys.get(ACCOUNT_BALANCE), AggregationTypes.SUM);
        FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
        formattingConfiguration.setFormattingType(FormattingConfiguration.CURRENCY);
        amountMeasure.setFormattingConfiguration(formattingConfiguration);
        analysisItems.add(amountMeasure);
        return analysisItems;
    }

    public DataSet getDataSet(com.easyinsight.users.Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition) {
        DataSet dataSet = new DataSet();        
        try {
            NodeList accounts = getNodes(credentials, "accounts", "account");
            for (int i = 0; i < accounts.getLength(); i++) {
                IRow row = dataSet.createRow();
                Node account = accounts.item(i);
                NodeList tags = account.getChildNodes();

                for (int j = 0; j < tags.getLength(); j++) {
                    Node tag = tags.item(j);
                    if ("name".equals(tag.getNodeName())) {
                        row.addValue(keys.get(ACCOUNT_NAME), new StringValue(tag.getTextContent()));
                    } else if ("current-balance".equals(tag.getNodeName())) {
                        row.addValue(keys.get(ACCOUNT_BALANCE), new StringValue(tag.getTextContent()));
                    } else if ("id".equals(tag.getNodeName())) {
                        row.addValue(keys.get(ACCOUNT_ID), new StringValue(tag.getTextContent()));
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.WESABE_ACCOUNTS;
    }
}
