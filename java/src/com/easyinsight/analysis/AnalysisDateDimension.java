package com.easyinsight.analysis;

import com.easyinsight.core.*;
import com.easyinsight.security.SecurityUtil;

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
    private String customDateFormat = "yyyy-MM-dd";

    @Column(name="output_date_format")
    private String outputDateFormat;

    private transient DateFormat cachedDateFormat;

    private transient boolean timeshift = true;

    private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static final int YEAR_LEVEL = 1;
    public static final int MONTH_LEVEL = 2;
    public static final int DAY_LEVEL = 3;
    public static final int HOUR_LEVEL = 4;
    public static final int MINUTE_LEVEL = 5;
    public static final int WEEK_LEVEL = 6;
    public static final int MONTH_FLAT = 7;
    public static final int DAY_OF_YEAR_FLAT = 8;
    public static final int DAY_OF_WEEK_FLAT = 9;
    public static final int WEEK_OF_YEAR_FLAT = 10;
    public static final int QUARTER_OF_YEAR_LEVEL = 11;
    public static final int QUARTER_OF_YEAR_FLAT = 12;

    public AnalysisDateDimension(Key key, boolean group, int dateLevel) {
        super(key, group);
        this.dateLevel = dateLevel;
    }

    @Override
    protected String getQualifiedSuffix() {
        return getType() + ":" + dateLevel + ":" + toDisplay();
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

    public String getOutputDateFormat() {
        return outputDateFormat;
    }

    public void setOutputDateFormat(String outputDateFormat) {
        this.outputDateFormat = outputDateFormat;
    }

    public boolean isTimeshift() {
        return timeshift;
    }

    public void setTimeshift(boolean timeshift) {
        this.timeshift = timeshift;
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

    private transient Calendar calendar;

    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata, boolean timezoneShift) {
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
            } else if (value.type() == Value.NUMBER) {
                NumericValue numberValue = (NumericValue) value;
                tempDate = cachedDateFormat.parse(Long.toString(numberValue.toDouble().longValue()));
            }
        } catch (ParseException e) {
        }
        Value resultValue;
        if (tempDate != null) {
            if (timezoneShift) {
                int time = insightRequestMetadata.getUtcOffset() / 60;
                String string;
                if (time > 0) {
                    string = "GMT-"+Math.abs(time);
                } else if (time < 0) {
                    string = "GMT+"+Math.abs(time);
                } else {
                    string = "GMT";
                }
                TimeZone timeZone = TimeZone.getTimeZone(string);
                if (calendar == null) {
                    calendar = Calendar.getInstance();
                }
                calendar.setTimeZone(timeZone);
            }
            if (calendar == null) {
                calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(SecurityUtil.getFirstDayOfWeek());
            }
            calendar.setTimeInMillis(tempDate.getTime());
            if (dateLevel <= WEEK_LEVEL || dateLevel == QUARTER_OF_YEAR_LEVEL) {
                switch (dateLevel) {
                    case YEAR_LEVEL:
                        calendar.set(Calendar.MONTH, 0);
                        calendar.set(Calendar.DAY_OF_YEAR, 2);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case QUARTER_OF_YEAR_LEVEL:
                        calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH)) / 3 * 3);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                    case MONTH_LEVEL:
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        break;
                    case WEEK_LEVEL:
                        calendar.set(Calendar.DAY_OF_WEEK, SecurityUtil.getFirstDayOfWeek());
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
                if (outputDateFormat != null && outputDateFormat.length() > 0) {
                    resultValue = new StringValue(new SimpleDateFormat(outputDateFormat).format(calendar.getTime()), new DateValue(finalDate));
                } else {
                    resultValue = new DateValue(finalDate);
                }
            } else {
                switch (dateLevel) {
                    case QUARTER_OF_YEAR_FLAT:
                        int quarter = (calendar.get(Calendar.MONTH)) / 3 + 1;
                        resultValue = new StringValue("Q" + quarter);
                        break;
                    case MONTH_FLAT:
                        if (outputDateFormat != null && outputDateFormat.length() > 0) {
                            resultValue = new StringValue(new SimpleDateFormat(outputDateFormat).format(calendar.getTime()), new NumericValue(calendar.get(Calendar.MONTH)));
                            break;
                        }
                        int month = calendar.get(Calendar.MONTH);
                        switch (month) {
                            case Calendar.JANUARY:
                                resultValue = new StringValue("January", new NumericValue(0));
                                break;
                            case Calendar.FEBRUARY:
                                resultValue = new StringValue("February", new NumericValue(1));
                                break;
                            case Calendar.MARCH:
                                resultValue = new StringValue("March", new NumericValue(2));
                                break;
                            case Calendar.APRIL:
                                resultValue = new StringValue("April", new NumericValue(3));
                                break;
                            case Calendar.MAY:
                                resultValue = new StringValue("May", new NumericValue(4));
                                break;
                            case Calendar.JUNE:
                                resultValue = new StringValue("June", new NumericValue(5));
                                break;
                            case Calendar.JULY:
                                resultValue = new StringValue("July", new NumericValue(6));
                                break;
                            case Calendar.AUGUST:
                                resultValue = new StringValue("August", new NumericValue(7));
                                break;
                            case Calendar.SEPTEMBER:
                                resultValue = new StringValue("September", new NumericValue(8));
                                break;
                            case Calendar.OCTOBER:
                                resultValue = new StringValue("October", new NumericValue(9));
                                break;
                            case Calendar.NOVEMBER:
                                resultValue = new StringValue("November", new NumericValue(10));
                                break;
                            case Calendar.DECEMBER:
                                resultValue = new StringValue("December", new NumericValue(11));
                                break;
                            default:
                                throw new RuntimeException();
                        }
                        break;
                    case DAY_OF_YEAR_FLAT:
                        resultValue = new NumericValue(calendar.get(Calendar.DAY_OF_YEAR));
                        break;
                    case DAY_OF_WEEK_FLAT:
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        switch (dayOfWeek) {
                            case Calendar.MONDAY:
                                resultValue = new StringValue("Monday", new NumericValue(1));
                                break;
                            case Calendar.TUESDAY:
                                resultValue = new StringValue("Tuesday", new NumericValue(2));
                                break;
                            case Calendar.WEDNESDAY:
                                resultValue = new StringValue("Wednesday", new NumericValue(3));
                                break;
                            case Calendar.THURSDAY:
                                resultValue = new StringValue("Thursday", new NumericValue(4));
                                break;
                            case Calendar.FRIDAY:
                                resultValue = new StringValue("Friday", new NumericValue(5));
                                break;
                            case Calendar.SATURDAY:
                                resultValue = new StringValue("Saturday", new NumericValue(6));
                                break;
                            case Calendar.SUNDAY:
                                resultValue = new StringValue("Sunday", new NumericValue(0));
                                break;
                            default:
                                throw new RuntimeException();
                        }

                        break;
                    case WEEK_OF_YEAR_FLAT:
                        if (outputDateFormat != null && outputDateFormat.length() > 0) {
                            resultValue = new StringValue(new SimpleDateFormat(outputDateFormat).format(calendar.getTime()), new NumericValue(calendar.get(Calendar.WEEK_OF_YEAR)));
                            break;
                        }
                        resultValue = new NumericValue(calendar.get(Calendar.WEEK_OF_YEAR));
                        break;
                    default:
                        throw new RuntimeException();
                }
                resultValue.setOriginalValue(new DateValue(tempDate));
            }
        } else {
            resultValue = new EmptyValue();
        }
        return resultValue;
    }

    private transient AggregateDateKey cachedDateKey;

    @Override
    public AggregateKey createAggregateKey() {
        // in case of filters, how do we do this...
        if (cachedDateKey == null) {
            cachedDateKey = new AggregateDateKey(getKey(), getType(), dateLevel, getFilters());
        }
        return cachedDateKey;
    }

    @Override
    public AnalysisItem clone() throws CloneNotSupportedException {
        AnalysisDateDimension measure = (AnalysisDateDimension) super.clone();
        measure.cachedDateKey = null;
        return measure;
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
}
