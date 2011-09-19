package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.pipeline.FilterComponent;
import com.easyinsight.pipeline.IComponent;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * User: James Boe
 * Date: Jan 12, 2008
 * Time: 9:47:18 PM
 */
@Entity
@Table(name="filter")
@Inheritance(strategy= InheritanceType.JOINED)
public class FilterDefinition implements Serializable, Cloneable {
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem field;
    @Column(name="apply_before_aggregation")
    private boolean applyBeforeAggregation = true;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="filter_id")
    private long filterID;
    @Column(name="intrinsic")
    private boolean intrinsic;
    @Column(name="enabled")
    private boolean enabled = true;
    @Column(name="show_on_report_view")
    private boolean showOnReportView = true;
    @Column(name="filter_name")
    private String filterName;
    @Column(name="column_level_template")
    private boolean templateFilter;
    @Column(name="toggle_enabled")
    private boolean toggleEnabled;
    @Column(name="minimum_role")
    private int minimumRole = 4;

    @Column(name="marmotscript")
    private String marmotScript;
    /*@Column(name="show_if_owner")
    private boolean showIfOwner;*/

    public FilterDefinition() {
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public int getMinimumRole() {
        return minimumRole;
    }

    public void setMinimumRole(int minimumRole) {
        this.minimumRole = minimumRole;
    }

    public boolean isToggleEnabled() {
        return toggleEnabled;
    }

    public void setToggleEnabled(boolean toggleEnabled) {
        this.toggleEnabled = toggleEnabled;
    }

    public boolean isTemplateFilter() {
        return templateFilter;
    }

    public void setTemplateFilter(boolean templateFilter) {
        this.templateFilter = templateFilter;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public boolean isShowOnReportView() {
        return showOnReportView;
    }

    public void setShowOnReportView(boolean showOnReportView) {
        this.showOnReportView = showOnReportView;
    }

    public boolean isSingleSource() {
        return true;
    }

    public FilterDefinition(AnalysisItem field) {
        this.field = field;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isIntrinsic() {
        return intrinsic;
    }

    public void setIntrinsic(boolean intrinsic) {
        this.intrinsic = intrinsic;
    }

    public long getFilterID() {
        return filterID;
    }

    public void setFilterID(long filterID) {
        this.filterID = filterID;
    }

    public AnalysisItem getField() {
        return field;
    }

    public void setField(AnalysisItem field) {
        this.field = field;
    }

    public boolean isApplyBeforeAggregation() {
        return applyBeforeAggregation;
    }

    public void setApplyBeforeAggregation(boolean applyBeforeAggregation) {
        this.applyBeforeAggregation = applyBeforeAggregation;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, int criteria) {
        return getField().getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria);
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        throw new UnsupportedOperationException();
    }

    public String toQuerySQL(String tableName) {
        throw new UnsupportedOperationException();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean validForQuery() {
        if (getField() != null) {
            if (getField().hasType(AnalysisItemTypes.STEP) || getField().hasType(AnalysisItemTypes.RANGE_DIMENSION)) {
                return false;
            }
            if (getField().getLookupTableID() != null && getField().getLookupTableID() > 0) {
                return false;
            }
            if (isTemplateFilter()) {
                return false;
            }
        }
        return enabled;
    }

    public FilterDefinition clone() throws CloneNotSupportedException {
        FilterDefinition filter = (FilterDefinition) super.clone();
        filter.setFilterID(0);
        return filter;
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        if (field != null) {
            setField(replacementMap.get(field.getAnalysisItemID()));
        }
    }

    public void beforeSave(Session session) {
        if (getField() != null) {
            getField().reportSave(session);
            if (getField().getKey().getKeyID() == 0) {
                session.save(getField().getKey());
            }
            if (getField().getAnalysisItemID() == 0) {
                session.save(getField());
            } else {
                session.update(getField());
            }
        }
    }

    public void afterLoad() {
        if (getField() != null) {
            setField((AnalysisItem) Database.deproxy(getField()));
            getField().afterLoad();
        }
    }

    public List<IComponent> createComponents(boolean beforeAggregation, IFilterProcessor filterProcessor, AnalysisItem sourceItem, boolean columnLevel) {
        List<IComponent> components = new ArrayList<IComponent>();
        if (isEnabled() && beforeAggregation == isApplyBeforeAggregation()) {
            if (!isTemplateFilter() || columnLevel) {
                components.add(new FilterComponent(this, filterProcessor));
            }
        }
        /*if (beforeAggregation == isApplyBeforeAggregation()) {
            components.add(new FilterPipelineCleanupComponent(this));
        }*/
        return components;
    }

    public String toXML() {
        throw new UnsupportedOperationException();
    }

    public void timeshift(Feed dataSource, Collection<FilterDefinition> filters) {
        if (getField() != null) {
            if (getField().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension dateDim = (AnalysisDateDimension) getField();
                boolean dateTime = dataSource.getDataSource().checkDateTime(getField().toDisplay(), getField().getKey());
                dateDim.setTimeshift(dateTime);
            }
        }
    }
}
