package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataService;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.WSListDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 12/2/14
 * Time: 7:34 AM
 */
public class LastDate extends Function {
    @Override
    public Value evaluate() {
        EIConnection conn = Database.instance().getConnection();
        try {
            String dateName = minusQuotes(0);
            Feed feed = FeedRegistry.instance().getFeed(calculationMetadata.getInsightRequestMetadata().getDataSourceID(), conn);
            AnalysisItem dateField = null;
            for (AnalysisItem item : feed.getFields()) {
                if (dateName.equals(item.toDisplay()) || dateName.equals(item.toUnqualifiedDisplay())) {
                    dateField = item;
                    break;
                }
            }

            if (dateField == null) {
                throw new FunctionException("Could not find the specified field " + dateName);
            }
            WSListDefinition list = new WSListDefinition();
            list.setColumns(Arrays.asList(dateField));
            list.setFilterDefinitions(new ArrayList<>());
            list.setDataFeedID(calculationMetadata.getInsightRequestMetadata().getDataSourceID());
            DataSet dataSet = DataService.listDataSet(list, calculationMetadata.getInsightRequestMetadata(), conn);

            Date maxDate = null;
            for (IRow row : dataSet.getRows()) {
                Value value = row.getValue(dateField);
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    Date date = dateValue.getDate();
                    if (maxDate == null || date.after(maxDate)) {
                        maxDate = date;
                    }
                }
            }

            if (maxDate == null) {
                return new EmptyValue();
            } else {
                return new DateValue(maxDate);
            }
        } finally {
            Database.closeConnection(conn);
        }
    }

    @Override
    public int getParameterCount() {
        return 1;
    }
}
