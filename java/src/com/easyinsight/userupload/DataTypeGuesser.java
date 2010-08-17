package com.easyinsight.userupload;

import com.easyinsight.core.*;
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
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"),
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("MM-dd-yy"),
            new SimpleDateFormat("MM-dd-yyyy"),
            new SimpleDateFormat("MM/dd/yy"),
            new SimpleDateFormat("yyyy/MM/dd"),
            new SimpleDateFormat("MM/dd/yyyy")
    };

    private Map<Key, AnalysisItem> dataTypeMap = new HashMap<Key, AnalysisItem>();

    private Map<Key, Set<String>> guessesMap = new HashMap<Key, Set<String>>();

    public Map<Key, Set<String>> getGuessesMap() {
        return guessesMap;
    }

    public void addValue(Key tag, Value value) {
        if (value == null) {
            value = new EmptyValue();
        }
            AnalysisItem newGuess = null;
        Set<String> strings = guessesMap.get(tag);
        if (strings == null) {
            strings = new HashSet<String>();
            guessesMap.put(tag, strings);
        }
        if (strings.size() < 3) {
            if (value.toString() != null && !"".equals(value.toString().trim())) {
                strings.add(value.toString());
            }
        }
        if ("".equals(value.toString().trim()) || value.type() == Value.EMPTY) {
            if (dataTypeMap.get(tag) == null) {
                dataTypeMap.put(tag, new AnalysisDimension(tag, true));
            }
            return;
        }
        if (value.type() == Value.STRING) {
            AnalysisItem existingGuess = dataTypeMap.get(tag);
            if (existingGuess == null) {
                StringValue stringValue = (StringValue) value;
                String string = stringValue.getValue();
                if (string.length() == 4) {
                    try {
                        int intValue = Integer.parseInt(string);
                        if (intValue >= 1980 && intValue <= 2014) {
                            newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.YEAR_LEVEL, "yyyy");
                        }
                    } catch (NumberFormatException e) {
                    }
                }
                if (newGuess == null) {
                    try {
                        Double.parseDouble(stringValue.getValue());
                        newGuess = new AnalysisMeasure(tag, AggregationTypes.SUM);
                    } catch (NumberFormatException e) {
                        SimpleDateFormat dateFormat = guessDate(stringValue.getValue());
                        if (dateFormat != null) {
                            newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.DAY_LEVEL, dateFormat.toPattern());
                        } else {
                            newGuess = new AnalysisDimension(tag, true);
                        }
                    }
                }
            } else {
                newGuess = existingGuess;
            }
        } else {
            switch (value.type()) {
                case Value.DATE:
                    newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.DAY_LEVEL);
                    break;
                case Value.NUMBER:
                    NumericValue numericValue = (NumericValue) value;
                    double doubleValue = numericValue.getValue();
                    long intValue = (long) doubleValue;
                    if (doubleValue == intValue && intValue >= 1980 && intValue <= 2014) {
                        newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.YEAR_LEVEL, "yyyy");
                    } else {
                        newGuess = new AnalysisMeasure(tag, AggregationTypes.SUM);
                    }
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
