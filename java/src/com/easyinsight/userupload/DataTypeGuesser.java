package com.easyinsight.userupload;

import com.easyinsight.core.*;
import com.easyinsight.analysis.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 1:44:25 PM
 */
public class DataTypeGuesser implements IDataTypeGuesser {

    private static String[] dateFormatStrings = new String[]{
            "yyyy-MM-dd",
            "MM-dd-yy",
            "MM-dd-yyyy",
            "MM/dd/yy",
            "yyyy/MM/dd",
            "MM/dd/yyyy"
    };



    private static SimpleDateFormat[] dateFormats = new SimpleDateFormat[dateFormatStrings.length];

    static {
        for (int i = 0; i < dateFormatStrings.length; i++) {
            dateFormats[i] = new SimpleDateFormat(dateFormatStrings[i]);
        }
    }

    private static String[] dateTimeFormatStrings = new String[]{
            "yyyy-MM-dd'T'HH:mm:ssz",
            "EEE, dd MMM yyyy HH:mm:ss z"
    };

    private static SimpleDateFormat[] dateTimeFormats = new SimpleDateFormat[dateTimeFormatStrings.length];

    static {
        for (int i = 0; i < dateTimeFormatStrings.length; i++) {
            dateTimeFormats[i] = new SimpleDateFormat(dateTimeFormatStrings[i]);
        }
    }

    private Map<Key, List<AnalysisItem>> dataTypeMap = new HashMap<>();

    private Map<Key, Set<String>> guessesMap = new HashMap<>();
    private Map<Key, Set<String>> rawDataMap = new HashMap<>();

    public Map<Key, Set<String>> getGuessesMap() {
        return guessesMap;
    }

    public Map<Key, Set<String>> getRawDataMap() {
        return rawDataMap;
    }

    public void addValue(Key tag, Value value) {
        if (value == null) {
            value = new EmptyValue();
        }
        AnalysisItem newGuess = null;
        Set<String> strings = guessesMap.get(tag);
        if (strings == null) {
            strings = new HashSet<>();
            guessesMap.put(tag, strings);
        }
        if (strings.size() < 3) {
            if (value.toString() != null && !"".equals(value.toString().trim())) {
                strings.add(value.toString());
            }
        }

        if (!dataTypeMap.containsKey(tag)) {
            dataTypeMap.put(tag, new ArrayList<>(50));

        }

        List<AnalysisItem> guesses = dataTypeMap.get(tag);

        if (guesses.size() > 50) {
            return;
        }

        if ("".equals(value.toString().trim()) || value.type() == Value.EMPTY) {
            if (guesses.size() == 0) {
                guesses.add(new AnalysisDimension(tag, true));
            }
            return;
        }
        if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            String string = stringValue.getValue();
            if (string.length() == 4) {
                try {
                    int intValue = Integer.parseInt(string);
                    if (intValue >= 1960 && intValue <= 2050) {
                        newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.YEAR_LEVEL, "yyyy");
                    }
                } catch (NumberFormatException e) {
                }
            }
            if (newGuess == null) {
                try {
                    String keyName = tag.toDisplayName();
                    if (keyName.toLowerCase().endsWith("id") || keyName.toLowerCase().endsWith("key") || keyName.toLowerCase().endsWith("fk")) {
                        newGuess = new AnalysisDimension(tag, true);
                    } else {
                        double numericValue = NumericValue.produceDoubleValueStrict(stringValue.getValue());
                        if (numericValue == 0) {
                            String dateTimeFormat = guessDateTime(stringValue.getValue());
                            if (dateTimeFormat != null) {
                                newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.DAY_LEVEL, dateTimeFormat, false);
                            } else {
                                String dateFormat = guessDate(stringValue.getValue());
                                if (dateFormat != null) {
                                    newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.DAY_LEVEL, dateFormat, true);
                                } else {
                                    newGuess = new AnalysisDimension(tag, true);
                                }
                            }
                        } else {
                            newGuess = new AnalysisMeasure(tag, AggregationTypes.SUM);
                            if (stringValue.getValue().startsWith("$")) {
                                newGuess.setFormattingType(FormattingConfiguration.CURRENCY);
                            } else if (stringValue.getValue().endsWith("%")) {
                                newGuess.setFormattingType(FormattingConfiguration.PERCENTAGE);
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    String dateTimeFormat = guessDateTime(stringValue.getValue());
                    if (dateTimeFormat != null) {
                        newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.DAY_LEVEL, dateTimeFormat, false);
                    } else {
                        String dateFormat = guessDate(stringValue.getValue());
                        if (dateFormat != null) {
                            newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.DAY_LEVEL, dateFormat);
                        } else {
                            newGuess = new AnalysisDimension(tag, true);
                        }
                    }
                }
            }
        } else {
            switch (value.type()) {
                case Value.DATE:
                    newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.DAY_LEVEL);
                    break;
                case Value.NUMBER:
                    String keyName = tag.toDisplayName();
                    if (keyName.toLowerCase().endsWith("id") || keyName.toLowerCase().endsWith("key") || keyName.toLowerCase().endsWith("fk")) {
                        newGuess = new AnalysisDimension(tag, true);
                    } else {
                        NumericValue numericValue = (NumericValue) value;
                        double doubleValue = numericValue.getValue();
                        long intValue = (long) doubleValue;
                        if (doubleValue == intValue && intValue >= 1960 && intValue <= 2050) {
                            newGuess = new AnalysisDateDimension(tag, true, AnalysisDateDimension.YEAR_LEVEL, "yyyy");
                        } else {
                            newGuess = new AnalysisMeasure(tag, AggregationTypes.SUM);
                        }
                    }
                    break;
                case Value.STRING:
                case Value.EMPTY:
                default:
                    newGuess = new AnalysisDimension(tag, true);
                    break;
            }
        }
        guesses.add(newGuess);

    }

    private String guessDateTime(String value) {
        for (int i = 0; i < dateTimeFormats.length; i++) {
            SimpleDateFormat dateFormat = dateTimeFormats[i];
            try {
                dateFormat.parse(value);
                return dateTimeFormatStrings[i];
            } catch (ParseException e) {
                // didn't work...
            }
        }
        return null;
    }

    private String guessDate(String value) {
        for (int i = 0; i < dateFormats.length; i++) {
            SimpleDateFormat dateFormat = dateFormats[i];
            try {
                dateFormat.parse(value);
                return dateFormatStrings[i];
            } catch (ParseException e) {
                // didn't work...
            }
        }
        return null;
    }

    public List<AnalysisItem> createFeedItems() {
        List<AnalysisItem> dataSourceItems = new ArrayList<>();
        for (List<AnalysisItem> guesses : dataTypeMap.values()) {
            Map<AnalysisItem, Integer> map = new HashMap<>();
            for (AnalysisItem guess : guesses) {
                Integer count = map.get(guess);
                if (count == null) {
                    map.put(guess, 1);
                } else {
                    map.put(guess, count + 1);
                }
            }
            int max = Integer.MIN_VALUE;
            AnalysisItem top = null;
            for (Map.Entry<AnalysisItem, Integer> entry : map.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    top = entry.getKey();
                }
            }
            dataSourceItems.add(top);
        }
        return dataSourceItems;
    }
}
