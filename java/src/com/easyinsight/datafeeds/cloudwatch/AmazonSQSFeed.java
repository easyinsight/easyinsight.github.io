package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.QueueAttribute;
import com.xerox.amazonws.sqs2.QueueService;
import com.xerox.amazonws.sqs2.SQSUtils;

import java.util.*;

/**
 * User: jamesboe
 * Date: 4/7/11
 * Time: 5:38 PM
 */
public class AmazonSQSFeed extends AmazonBaseFeed {

    @Override
    protected DataSet retrieve(Collection<AnalysisDimension> dimensions, Collection<AnalysisMeasure> measures, Date startDate, Date endDate, int period, AnalysisDateDimension analysisDateDimension, String key, String secretKey) throws Exception {
        DataSet dataSet = new DataSet();
        QueueService queueService = new QueueService(key, secretKey);
        List<MessageQueue> queues = queueService.listMessageQueues(null);
        for (MessageQueue queue : queues) {
            IRow row = dataSet.createRow();
            Map<String, String> attributes = queue.getQueueAttributes(QueueAttribute.ALL);
            for (AnalysisItem analysisItem : dimensions) {
                if (analysisItem.toDisplay().equals(AmazonSQSSource.QUEUE_CREATION_DATE)) {
                    row.addValue(analysisItem.createAggregateKey(), attributes.get(QueueAttribute.CREATED_TIMESTAMP.name()));
                } else if (analysisItem.toDisplay().equals(AmazonSQSSource.LAST_MODIFIED)) {
                    row.addValue(analysisItem.createAggregateKey(), attributes.get(QueueAttribute.LAST_MODIFIED_TIMESTAMP.name()));
                } else if (analysisItem.toDisplay().equals(AmazonSQSSource.QUEUE_NAME)) {
                    String[] tokens = attributes.get("QueueArn").split(":");
                    String name = tokens[tokens.length - 1];
                    row.addValue(analysisItem.createAggregateKey(), name);
                }
            }
            for (AnalysisItem analysisItem : measures) {
                if (analysisItem.toDisplay().equals(AmazonSQSSource.APPROXIMATE_MESSAGES)) {
                    row.addValue(analysisItem.createAggregateKey(), attributes.get(QueueAttribute.APPROXIMATE_NUMBER_OF_MESSAGES.name()));
                } else if (analysisItem.toDisplay().equals(AmazonSQSSource.COUNT)) {
                    row.addValue(analysisItem.createAggregateKey(), 1);
                }
            }
            DateValue dummyDateValue = new DateValue(endDate);
            row.addValue(analysisDateDimension.createAggregateKey(), dummyDateValue);
        }
        return dataSet;
    }
}
