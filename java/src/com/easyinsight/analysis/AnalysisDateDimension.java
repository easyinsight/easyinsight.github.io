package com.easyinsight.analysis;

import com.easyinsight.core.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * User: James Boe
 * Date: Feb 29, 2008
 * Time: 10:49:36 AM
 */
@Entity
@Table(name="analysis_date")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisDateDimension extends AnalysisDimension {
    @Column(name="date_level")
    private int dateLevel;
    @Column(name="custom_date_format")
    private String customDateFormat;
    private transient DateFormat cachedDateFormat;
    private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static final int YEAR_LEVEL = 1;
    public static final int MONTH_LEVEL = 2;
    public static final int DAY_LEVEL = 3;
    public static final int HOUR_LEVEL = 4;
    public static final int MINUTE_LEVEL = 5;
    public static final int WEEK_LEVEL = 7;
    public static final int MONTH_FLAT = 8;
    public static final int DAY_OF_YEAR_FLAT = 9;
    public static final int DAY_OF_WEEK_FLAT = 10;
    public static final int WEEK_OF_YEAR_FLAT = 11;

    public AnalysisDateDimension(Key key, boolean group, int dateLevel) {
        super(key, group);
        this.dateLevel = dateLevel;
    }

    public AnalysisDateDimension(Key key, String displayName, int dateLevel) {
        super(key, displayName);
        this.dateLevel = dateLevel;
    }

    public AnalysisDateDimension(Key key, boolean group, int dateLevel, String customDateFormat) {
        super(key, group);
        this.dateLevel = dateLevel;
        this.customDateFormat = customDateFormat;
    }

    public AnalysisDateDimension(String key, boolean group, int dateLevel) {
        super(key, group);
        this.dateLevel = dateLevel;
    }

    public AnalysisDateDimension() {
    }

    public String getCustomDateFormat() {
        return customDateFormat;
    }

    public void setCustomDateFormat(String customDateFormat) {
        this.customDateFormat = customDateFormat;
    }

    public int getDateLevel() {
        return dateLevel;
    }

    public void setDateLevel(int dateLevel) {
        this.dateLevel = dateLevel;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.DATE_DIMENSION;
    }

    public Value renameMeLater(Value value) {
        if (cachedDateFormat == null) {
            if (customDateFormat == null) {
                cachedDateFormat = defaultDateFormat;
            } else {
                cachedDateFormat = new SimpleDateFormat(customDateFormat);
            }
        }
        Date tempDate = null;
        try {
            if (value.type() == Value.STRING) {
                StringValue stringValue = (StringValue) value;
                String rawString = stringValue.getValue();
                tempDate = cachedDateFormat.parse(rawString);
            } else if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                tempDate = dateValue.getDate();
            }
        } catch (ParseException e) {
        }
        if (tempDate != null)
            return new DateValue(tempDate);
        else
            return new EmptyValue();
    }

    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata) {
        if (cachedDateFormat == null) {
            if (customDateFormat == null) {
                cachedDateFormat = defaultDateFormat;
            } else {
                cachedDateFormat = new SimpleDateFormat(customDateFormat);
            }
        }
        Date tempDate = null;
        Date finalDate;
        try {
            if (value.type() == Value.STRING) {
                StringValue stringValue = (StringValue) value;
                String rawString = stringValue.getValue();
                tempDate = cachedDateFormat.parse(rawString);
            } else if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                tempDate = dateValue.getDate();
            }
        } catch (ParseException e) {
        }
        Value resultValue;
        if (tempDate != null) {
            Calendar calendar = Calendar.getInstance();
            if (insightRequestMetadata.getUtcOffset() != 0) {
                int hours = insightRequestMetadata.getUtcOffset() / 60;
                // int minutes = Math.abs(insightRequestMetadata.getUtcOffset() % 60);
                String timeZoneString;
                if (hours > 0) {
                    timeZoneString = "GMT-" + String.valueOf(hours);
                } else {
                    timeZoneString = "GMT+" + String.valueOf(hours);
                }
                TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
                calendar.setTimeZone(timeZone);
            }

            calendar.setTime(tempDate);
            if (dateLevel < WEEK_LEVEL) {
                switch (dateLevel) {
                    case YEAR_LEVEL:
                        calendar.set(Calendar.MONTH, 0);
                        calendar.set(Calendar.DAY_OF_YEAR, 1);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case MONTH_LEVEL:
                        calendar.set(Calendar.DAY_OF_MONTH, 0);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case DAY_LEVEL:
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case HOUR_LEVEL:
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case MINUTE_LEVEL:
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    default:
                        throw new RuntimeException();
                }
                finalDate = calendar.getTime();
                resultValue = new DateValue(finalDate);
            } else if (dateLevel == WEEK_LEVEL) {
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                resultValue = new StringValue(weekOfYear + " - " + calendar.get(Calendar.YEAR));
            } else {
                switch (dateLevel) {
                    case MONTH_FLAT:
                        resultValue = new NumericValue(calendar.get(Calendar.MONTH));
                        break;
                    case DAY_OF_YEAR_FLAT:
                        resultValue = new NumericValue(calendar.get(Calendar.DAY_OF_YEAR));
                        break;
                    case DAY_OF_WEEK_FLAT:
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        switch (dayOfWeek) {
                            case Calendar.MONDAY:
                                resultValue = new StringValue("Monday");
                                break;
                            case Calendar.TUESDAY:
                                resultValue = new StringValue("Tuesday");
                                break;
                            case Calendar.WEDNESDAY:
                                resultValue = new StringValue("Wednesday");
                                break;
                            case Calendar.THURSDAY:
                                resultValue = new StringValue("Thursday");
                                break;
                            case Calendar.FRIDAY:
                                resultValue = new StringValue("Friday");
                                break;
                            case Calendar.SATURDAY:
                                resultValue = new StringValue("Saturday");
                                break;
                            case Calendar.SUNDAY:
                                resultValue = new StringValue("Sunday");
                                break;
                            default:
                                throw new RuntimeException();
                        }

                        break;
                    case WEEK_OF_YEAR_FLAT:
                        resultValue = new NumericValue(calendar.get(Calendar.WEEK_OF_YEAR));
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
        } else {
            resultValue = new EmptyValue();
        }
        return resultValue;
    }

    public AnalysisItemResultMetadata createResultMetadata() {
        return new AnalysisDateDimensionResultMetadata();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisDateDimension)) return false;
        if (!super.equals(o)) return false;

        AnalysisDateDimension that = (AnalysisDateDimension) o;

        return dateLevel == that.dateLevel;

    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + dateLevel;
        return result;
    }

    /*@Override
    public AggregateKey createAggregateKey() {
        return new AggregateDateKey(getKey(), getType(), getDateLevel());
    }*/
}
