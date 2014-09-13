package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dataset.DataSet;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/12/14
 * Time: 2:01 PM
 */
public class InfusionsoftSavedFilterSource extends InfusionsoftTableSource {
    public List<InfusionsoftReport> getReports(InfusionsoftCompositeSource parent) throws MalformedURLException, XmlRpcException {
        AnalysisItem filterName = new AnalysisDimension(new NamedKey("FilterName"));
        AnalysisItem id = new AnalysisDimension(new NamedKey("Id"));
        AnalysisItem reportStoredName = new AnalysisDimension(new NamedKey("ReportStoredName"));
        AnalysisItem userID = new AnalysisDimension(new NamedKey("UserId"));
        DataSet savedFilters = query("SavedFilter", Arrays.asList(filterName, id, reportStoredName, userID), parent);
        List<InfusionsoftReport> reports = new ArrayList<>();
        for (IRow row : savedFilters.getRows()) {
            String name = row.getValue(filterName).toString();
            String reportID = row.getValue(id).toString();
            String userIDValue = "1";
            reports.add(new InfusionsoftReport(name, reportID, userIDValue));
        }
        return reports;
    }
}
