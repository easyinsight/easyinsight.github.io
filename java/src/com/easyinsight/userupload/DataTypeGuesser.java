package com.easyinsight.userupload;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;
import com.easyinsight.analysis.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * User: jboe
* Date: Jan 3, 2008
* Time: 1:44:25 PM
*/
public class DataTypeGuesser implements IDataTypeGuesser {

    private static SimpleDateFormat[] dateFormats = new SimpleDateFormat[] {
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("MM-dd-yy"),
            new SimpleDateFormat("MM-dd-yyyy"),
            new SimpleDateFormat("MM/dd/yy"),
            new SimpleDateFormat("yyyy/MM/dd"),
            new SimpleDateFormat("MM/dd/yyyy")
    };

    private Map<Key, AnalysisItem> dataTypeMap = new HashMap<Key, AnalysisItem>();

    public void addValue(Key tag, Value value) {
        if (value != null) {
            AnalysisItem newGuess;
            if (value.type() == Value.STRING) {
                AnalysisItem existingGuess = dataTypeMap.get(tag);
                if (existingGuess == null) {
                    StringValue stringValue = (StringValue) value;
                    try {
                        Double.parseDouble(stringValue.getValue());
                        newGuess = new AnalysisMeasure(tag, AggregationTypes.SUM);
                    } catch (NumberFormatException e) {
                        SimpleDateFormat dateFormat = guessDate(stringValue.getValue());
                        if (dateFormat != null) {
                            newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.YEAR_LEVEL, dateFormat.toPattern());
                        } else {
                            newGuess = new AnalysisDimension(tag, true);
                        }
                    }
                } else {
                    newGuess = existingGuess;
                }
            } else {
                switch (value.type()) {
                    case Value.DATE:
                        newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.YEAR_LEVEL);
                        break;
                    case Value.NUMBER:
                        newGuess = new AnalysisMeasure(tag, AggregationTypes.SUM);
                        break;
                    case Value.STRING:
                    case Value.EMPTY:
                    default:
                        newGuess = new AnalysisDimension(tag, true);
                        break;
                }
            }
            dataTypeMap.put(tag, newGuess);
        }
    }

    private SimpleDateFormat guessDate(String value) {
        SimpleDateFormat matchedFormat = null;
        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                dateFormat.parse(value);
                matchedFormat = dateFormat;
                break;
            } catch (ParseException e) {
                // didn't work...
            }
        }
        return matchedFormat;
    }

    public List<AnalysisItem> createFeedItems() {
        return new ArrayList<AnalysisItem>(dataTypeMap.values());
    }
}
