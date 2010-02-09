package com.easyinsight.datafeeds.wesabe;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
import com.easyinsight.logging.LogClass;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.*;
import java.sql.Connection;

/**
 * User: jboe
 * Date: Jul 16, 2009
 * Time: 9:48:00 AM
 */
public class WesabeTransactionDataSource extends WesabeBaseSource {

    public static final String DATE = "Date";
    public static final String AMOUNT = "Amount";
    public static final String MERCHANT = "Merchant";
    public static final String DISPLAYNAME = "Transaction Display Name";
    public static final String RAWNAME = "Transaction Raw Name";
    public static final String RAWTXN = "Transaction Raw Type";
    public static final String TAGS = "Tags";

    public WesabeTransactionDataSource() {
        setFeedName("Transactions");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(ACCOUNT_ID, DATE, AMOUNT, MERCHANT, TAGS, DISPLAYNAME, RAWNAME, RAWTXN);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(MERCHANT), true));
        analysisItems.add(new AnalysisDimension(keys.get(DISPLAYNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(RAWNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(RAWTXN), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), false, ","));
        AnalysisDimension accountDimension = new AnalysisDimension(keys.get(ACCOUNT_ID), true);
        accountDimension.setHidden(true);
        analysisItems.add(accountDimension);
        AnalysisMeasure amountMeasure = new AnalysisMeasure(keys.get(AMOUNT), AggregationTypes.SUM);
        FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
        formattingConfiguration.setFormattingType(FormattingConfiguration.CURRENCY);
        amountMeasure.setFormattingConfiguration(formattingConfiguration);
        analysisItems.add(amountMeasure);
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage) {
        DataSet dataSet = new DataSet();
        try {
            NodeList transactions = getNodes(credentials, "transactions", "txaction");
            for (int i = 0; i < transactions.getLength(); i++) {
                IRow row = dataSet.createRow();
                Node transaction = transactions.item(i);
                NodeList transactionChildren = transaction.getChildNodes();
                for (int j = 0; j < transactionChildren.getLength(); j++) {
                    Node transactionChildNode = transactionChildren.item(j);
                    if ("account-id".equals(transactionChildNode.getNodeName())) {
                        row.addValue(keys.get(ACCOUNT_ID), new StringValue(transactionChildNode.getTextContent()));
                    } else if ("date".equals(transactionChildNode.getNodeName())) {
                        row.addValue(keys.get(DATE), new StringValue(transactionChildNode.getTextContent()));
                    } else if ("amount".equals(transactionChildNode.getNodeName())) {
                        row.addValue(keys.get(AMOUNT), new NumericValue(Double.parseDouble(transactionChildNode.getTextContent())));
                    } else if ("merchant".equals(transactionChildNode.getNodeName())) {
                        NodeList merchantChildren = transactionChildNode.getChildNodes();
                        for (int k = 0; k < merchantChildren.getLength(); k++) {
                            Node merchantItem = merchantChildren.item(k);
                            if ("name".equals(merchantItem.getNodeName())) {
                                row.addValue(keys.get(MERCHANT), merchantItem.getTextContent());
                            }
                        }
                    } else if ("tags".equals(transactionChildNode.getNodeName())) {
                        NodeList tagChildren = transactionChildNode.getChildNodes();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int k = 0; k < tagChildren.getLength(); k++) {
                            Node tagItem = tagChildren.item(k);
                            if ("tag".equals(tagItem.getNodeName())) {
                                stringBuilder.append(tagItem.getChildNodes().item(1).getTextContent());
                                /*if ("name".equals(tagItem.getNodeName())) {
                                    stringBuilder.append(tagItem.getTextContent());
                                }*/
                                stringBuilder.append(",");
                            }
                        }
                        if (stringBuilder.length() > 0) {
                            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                        }
                        row.addValue(keys.get(TAGS), stringBuilder.toString());
                    } else if ("display-name".equals(transactionChildNode.getNodeName())) {
                        row.addValue(keys.get(DISPLAYNAME), transactionChildNode.getTextContent());
                    } else if ("raw-name".equals(transactionChildNode.getNodeName())) {
                        row.addValue(keys.get(RAWNAME), transactionChildNode.getTextContent());
                    } else if ("raw-txntype".equals(transactionChildNode.getNodeName())) {
                        row.addValue(keys.get(RAWTXN), transactionChildNode.getTextContent());
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
        return FeedType.WESABE_TRANSACTIONS;
    }
}
