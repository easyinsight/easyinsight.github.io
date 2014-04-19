package com.easyinsight.analysis;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.export.ExportMetadata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/5/14
 * Time: 2:12 PM
 */
public class Usage {
    private List<InsightDescriptor> reportsUsingAsAddon;
    private List<DataSourceDescriptor> dataSourcesBasedOn;
    private List<DashboardDescriptor> dashboardsUsing;

    public Usage(List<InsightDescriptor> reportsUsingAsAddon, List<DataSourceDescriptor> dataSourcesBasedOn, List<DashboardDescriptor> dashboardsUsing) {
        this.reportsUsingAsAddon = reportsUsingAsAddon;
        this.dataSourcesBasedOn = dataSourcesBasedOn;
        this.dashboardsUsing = dashboardsUsing;
    }

    public List<InsightDescriptor> getReportsUsingAsAddon() {
        return reportsUsingAsAddon;
    }

    public List<DataSourceDescriptor> getDataSourcesBasedOn() {
        return dataSourcesBasedOn;
    }

    public List<DashboardDescriptor> getDashboardsUsing() {
        return dashboardsUsing;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        JSONArray arr = new JSONArray();
        for(InsightDescriptor id : reportsUsingAsAddon) {
            arr.put(id.toJSON(md));
        }
        jo.put("addons", arr);
        arr = new JSONArray();
        for(DataSourceDescriptor ds : dataSourcesBasedOn) {
            arr.put(ds.toJSON(md));
        }
        jo.put("data_sources", arr);
        arr = new JSONArray();
        for(DashboardDescriptor db : dashboardsUsing) {
            arr.put(db.toJSON(md));
        }
        jo.put("dashboards", arr);
        return jo;
    }
}
