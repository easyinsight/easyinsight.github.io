package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        return new MaterializedMultiFlatDateFilter(getField(), levels, level);
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

    @Override
    public String toQuerySQL(String tableName, Database database) {
        /*return "year(" + getField().toKeySQL() + ") = ?";*/
        return null;
    }

    @Override
    public boolean validForQuery() {
        return false;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
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
        for (DateLevelWrapper dateLevelWrapper : wrappers) {
            JSONObject o = new JSONObject();
            o.put("display", dateLevelWrapper.getDisplay());
            o.put("dateLevel", dateLevelWrapper.getDateLevel());
            map.put(String.valueOf(dateLevelWrapper.getDateLevel()), dateLevelWrapper.getShortDisplay());
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
