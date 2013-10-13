package com.easyinsight.api.v3;

import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceInternalService;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.storage.DataStorage;
import nu.xom.Document;
import nu.xom.Nodes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/12/13
 * Time: 11:58 AM
 */
public class AddFieldServlet extends APIServlet {
    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        // apply via alter
        Nodes dataSourceNameNodes = document.query("/addField/dataSourceName/text()");
        String dataSourceName = dataSourceNameNodes.get(0).getValue();
        Map<Long, Boolean> dataSources = findDataSourceIDsByName(dataSourceName, conn);
        if (dataSources.size() == 0) {
            throw new ServiceRuntimeException("We couldn't find a data source by the name or key of " + dataSourceName + ". Create the data source by posting to defineDataSource before you use this call.");
        } else if (dataSources.size() == 2) {
            throw new ServiceRuntimeException("We found more than one data source by the name of " + dataSourceName + ". Use a data source key or delete all but one of the data sources by this name from your Easy Insight account.");
        }
        FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSources.keySet().iterator().next(), conn);
        String fieldToAddName = document.query("/addField/fieldToAdd/text()").get(0).getValue();
        NamedKey key = new NamedKey(fieldToAddName);
        AnalysisItem fieldToAdd = new AnalysisMeasure(key, AggregationTypes.SUM);
        fieldToAdd.setDisplayName(fieldToAddName);
        dataSource.getFields().add(fieldToAdd);
        new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
        DataStorage storage = DataStorage.writeConnection(dataSource, conn);
        storage.alter(fieldToAdd.getKey());
        storage.commit();
        return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
    }
}
