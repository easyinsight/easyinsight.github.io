package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/15/11
 * Time: 11:03 PM
 */
@Entity
@Table(name = "multi_date_filter")
@PrimaryKeyJoinColumn(name = "filter_id")
public class MultiFlatDateFilter extends FilterDefinition {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "multi_date_filter_to_date_level_wrapper",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "date_level_wrapper_id", nullable = false))
    private List<DateLevelWrapper> levels = new ArrayList<DateLevelWrapper>();

    @Column(name = "end_date_property")
    private String endDateProperty;

    @Transient
    private List<DateLevelWrapper> cachedValues;

    @Column(name="units_back")
    private int unitsBack;
    @Column(name="units_forward")
    private int unitsForward;
    @Column(name="include_relative")
    private boolean includeRelative;
    @Column(name="all_option")
    private boolean allOption;

    @Column(name = "level")
    private int level;

    public List<DateLevelWrapper> getCachedValues() {
        return cachedValues;
    }

    public void setCachedValues(List<DateLevelWrapper> cachedValues) {
        this.cachedValues = cachedValues;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int type() {
        return FilterDefinition.MULTI_FLAT_DATE;
    }

    public boolean isAllOption() {
        return allOption;
    }

    public void setAllOption(boolean allOption) {
        this.allOption = allOption;
    }

    public int getUnitsBack() {
        return unitsBack;
    }

    public void setUnitsBack(int unitsBack) {
        this.unitsBack = unitsBack;
    }

    public int getUnitsForward() {
        return unitsForward;
    }

    public void setUnitsForward(int unitsForward) {
        this.unitsForward = unitsForward;
    }

    public boolean isIncludeRelative() {
        return includeRelative;
    }

    public void setIncludeRelative(boolean includeRelative) {
        this.includeRelative = includeRelative;
    }

    public void customFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        super.customFromXML(element, xmlImportMetadata);
        Nodes levels = element.query("/levels/level");
        for (int i = 0; i < levels.size(); i++) {
            Node level = levels.get(i);
            String value = level.query("text()").get(0).getValue();
            DateLevelWrapper wrapper = new DateLevelWrapper();
            wrapper.setDateLevel(Integer.parseInt(value));
            this.levels.add(wrapper);
        }
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        Element levels = new Element("levels");
        element.appendChild(levels);
        for (DateLevelWrapper levelWrapper : this.levels) {
            Element wrapperElement = new Element("level");
            wrapperElement.appendChild(String.valueOf(levelWrapper.getDateLevel()));
            levels.appendChild(wrapperElement);
        }
        return element;
    }

    public String getEndDateProperty() {
        return endDateProperty;
    }

    public void setEndDateProperty(String endDateProperty) {
        this.endDateProperty = endDateProperty;
    }

    public List<DateLevelWrapper> getLevels() {
        return levels;
    }

    public void setLevels(List<DateLevelWrapper> dateLevels) {
        this.levels = dateLevels;
    }

    @Override
    public FilterDefinition clone() throws CloneNotSupportedException {
        MultiFlatDateFilter filter = (MultiFlatDateFilter) super.clone();
        List<DateLevelWrapper> wrappers = new ArrayList<DateLevelWrapper>();
        for (DateLevelWrapper wrapper : getLevels()) {
            wrappers.add(wrapper.clone());
        }
        filter.setLevels(wrappers);
        return filter;
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedMultiFlatDateFilter(getField(), levels, level, insightRequestMetadata);
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        List<DateLevelWrapper> wrappers = new ArrayList<DateLevelWrapper>();
        for (DateLevelWrapper wrapper : getLevels()) {
            wrappers.add(wrapper);
        }
        setLevels(wrappers);
    }

    public boolean dateTime() {
        AnalysisDateDimension date = (AnalysisDateDimension) getField();
        return (date.isTimeshift());
    }

    @Override
    public String toQuerySQL(String tableName, Database database, InsightRequestMetadata insightRequestMetadata) {
        //if (!dateTime()) {
            StringBuilder sb = new StringBuilder();
            if (level == AnalysisDateDimension.MONTH_FLAT) {
                if (database.getDialect() == Database.MYSQL) {
                    sb.append("month(" + getField().toKeySQL() + ") IN (");
                } else {
                    sb.append("extract(month from " + getField().toKeySQL() + ") IN (");
                }
                for (DateLevelWrapper wrapper : levels) {
                    sb.append("?,");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                return sb.toString();
            } else if (level == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {

            } else if (level == AnalysisDateDimension.YEAR_LEVEL) {
                if (database.getDialect() == Database.MYSQL) {
                    sb.append("year(" + getField().toKeySQL() + ") IN (");
                } else {
                    sb.append("extract(year from " + getField().toKeySQL() + ") IN (");
                }
                for (DateLevelWrapper wrapper : levels) {
                    sb.append("?,");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                return sb.toString();
            } else if (level == AnalysisDateDimension.WEEK_LEVEL) {

            } else if (level == AnalysisDateDimension.MONTH_LEVEL) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Date earliestDate = null;
                    Date latestDate = null;
                    for (DateLevelWrapper wrapper : levels) {
                        Date date = sdf.parse(wrapper.getShortDisplay());
                        if (earliestDate == null || date.before(earliestDate)) {
                            earliestDate = date;
                        }
                        if (latestDate == null || date.after(latestDate)) {
                            latestDate = date;
                        }
                    }
                    if (earliestDate != null && latestDate != null) {
                        sb.append(getField().toKeySQL() + " >= ? AND " + getField().toKeySQL() + " <= ?");
                    }
                    return sb.toString();
                } catch (ParseException e) {
                    LogClass.error(e);
                }
            }
        //}
        return null;
    }

    @Override
    public boolean validForQuery() {
        return super.validForQuery() && (level == AnalysisDateDimension.MONTH_FLAT || level == AnalysisDateDimension.YEAR_LEVEL || level == AnalysisDateDimension.MONTH_LEVEL) &&
                levels != null && levels.size() > 0;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        //if (!dateTime()) {
            if (level == AnalysisDateDimension.MONTH_FLAT) {
                for (DateLevelWrapper wrapper : levels) {
                    preparedStatement.setInt(start++, wrapper.getDateLevel() + 1);
                }
            } else if (level == AnalysisDateDimension.YEAR_LEVEL) {
                for (DateLevelWrapper wrapper : levels) {
                    preparedStatement.setInt(start++, Integer.parseInt(wrapper.getShortDisplay()));
                }
            } else if (level == AnalysisDateDimension.MONTH_LEVEL) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Date earliestDate = null;
                    Date latestDate = null;
                    for (DateLevelWrapper wrapper : levels) {
                        Date date = sdf.parse(wrapper.getShortDisplay());
                        if (earliestDate == null || date.before(earliestDate)) {
                            earliestDate = date;
                        }
                        if (latestDate == null || date.after(latestDate)) {
                            latestDate = date;
                        }
                    }
                    if (earliestDate != null && latestDate != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(earliestDate);
                        c.add(Calendar.MONTH, -1);
                        Date startDate = c.getTime();
                        c.setTime(latestDate);
                        c.add(Calendar.MONTH, 1);
                        Date endDate = c.getTime();
                        preparedStatement.setTimestamp(start++, new Timestamp(startDate.getTime()));
                        preparedStatement.setTimestamp(start++, new Timestamp(endDate.getTime()));
                    }
                } catch (ParseException e) {
                    LogClass.error(e);
                }
            }
        //}
        /*preparedStatement.setInt(start++, value);
        return start;*/
        return start;
    }

    @Override
    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata) throws JSONException {
        JSONObject jo = super.toJSON(filterHTMLMetadata);
        jo.put("type", "multi_date");

        List<DateLevelWrapper> wrappers = new DataService().getMultiDateOptions(this);

        JSONArray values = new JSONArray();
        JSONObject map = new JSONObject();
        JSONObject displayMap = new JSONObject();
        for (DateLevelWrapper dateLevelWrapper : wrappers) {
            JSONObject o = new JSONObject();
            o.put("display", dateLevelWrapper.getDisplay());
            o.put("dateLevel", dateLevelWrapper.getDateLevel());
            map.put(String.valueOf(dateLevelWrapper.getDateLevel()), dateLevelWrapper.getShortDisplay());
            displayMap.put(String.valueOf(dateLevelWrapper.getDateLevel()), dateLevelWrapper.getDisplay());
            values.put(o);
        }

        int minLevel = Integer.MAX_VALUE;
        int maxLevel = Integer.MIN_VALUE;
        DateLevelWrapper startWrapper = null;
        DateLevelWrapper endWrapper = null;
        for (DateLevelWrapper wrapper : getLevels()) {
            if (wrapper.getDateLevel() < minLevel) {
                minLevel = wrapper.getDateLevel();
                startWrapper = wrapper;
            }
            if (wrapper.getDateLevel() > maxLevel) {
                maxLevel = wrapper.getDateLevel();
                endWrapper = wrapper;
            }

        }

        jo.put("lookup", map);
        jo.put("displayLookup", map);
        jo.put("values", values);
        jo.put("startLevel", minLevel);
        if (startWrapper != null) {
            jo.put("startLabel", startWrapper.getShortDisplay());
        }
        jo.put("endLevel", maxLevel);
        if (endWrapper != null) {
            jo.put("endLabel", endWrapper.getShortDisplay());
        }

        return jo;
    }

    @Override
    public void override(FilterDefinition overrideFilter) {
        MultiFlatDateFilter f = (MultiFlatDateFilter) overrideFilter;
        this.setLevels(f.getLevels());
    }

    @Override
    public String asString(InsightRequestMetadata insightRequestMetadata) {
        int minLevel = Calendar.DECEMBER;
        int maxLevel = Calendar.JANUARY;
        for (DateLevelWrapper wrapper : getLevels()) {
            minLevel = Math.min(minLevel, wrapper.getDateLevel());
            maxLevel = Math.max(maxLevel, wrapper.getDateLevel());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, minLevel);
        String firstMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        calendar.set(Calendar.MONTH, maxLevel);
        String endMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        return firstMonth + " to " + endMonth;
    }
}
