package com.easyinsight.api.v3;


import com.easyinsight.analysis.*;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class AddRowsServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn) throws Exception {
        DataStorage dataStorage = null;
        try {
            Nodes baseRows = document.query("/rows");
            if (baseRows.size() == 0) {
                throw new ServiceRuntimeException("The root element of the request needs to be rows.");
            }
            Element rootNode = (Element) baseRows.get(0);
            Attribute dataSourceAttribute = rootNode.getAttribute("dataSourceName");
            if (dataSourceAttribute == null) {
                throw new ServiceRuntimeException("You need to specify a data source via the dataSourceName attribute on the root element of the request.");
            }
            String dataSourceName = dataSourceAttribute.getValue();
            Map<Long, Boolean> dataSources = findDataSourceIDsByName(dataSourceName, conn);
            if (dataSources.size() == 0) {
                throw new ServiceRuntimeException("We couldn't find a data source by the name or key of " + dataSourceName + ". Create the data source by posting to defineDataSource before you use this call.");
            } else if (dataSources.size() == 2) {
                throw new ServiceRuntimeException("We found more than one data source by the name of " + dataSourceName + ". Use a data source key or delete all but one of the data sources by this name from your Easy Insight account.");
            }
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSources.keySet().iterator().next(), conn);
            Map<String, AnalysisItem> fieldMap = new HashMap<String, AnalysisItem>();
            for (AnalysisItem field : dataSource.getFields()) {
                fieldMap.put(field.getKey().toKeyString().toLowerCase(), field);
            }
            Nodes rowNodes = document.query("/rows/row");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DataSet dataSet = new DataSet();
            for (int i = 0; i < rowNodes.size(); i++) {
                Node rowNode = rowNodes.get(i);
                IRow row = dataSet.createRow();
                for (int j = 0; j < rowNode.getChildCount(); j++) {
                    Element columnNode = (Element) rowNode.getChild(j);
                    String nodeName = columnNode.getLocalName().toLowerCase();
                    AnalysisItem analysisItem = fieldMap.get(nodeName);
                    if (analysisItem == null) {
                        throw new ServiceRuntimeException("No field was found in the data source definition matching the key of " + nodeName + ".");
                    }
                    String value = columnNode.getValue().trim();
                    if ("".equals(value)) {
                        row.addValue(analysisItem.createAggregateKey(), new EmptyValue());
                    } else {
                        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            try {
                                row.addValue(analysisItem.createAggregateKey(), dateFormat.parse(value));
                            } catch (ParseException e) {
                                throw new ServiceRuntimeException("We couldn't parse the date value of " + value + " that you passed in with " + nodeName + ". Date values should match the pattern of yyyy-MM-dd'T'HH:mm:ss.");
                            }
                        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                            try {
                                row.addValue(analysisItem.createAggregateKey(), Double.parseDouble(value));
                            } catch (NumberFormatException e) {
                                throw new ServiceRuntimeException("We couldn't parse the numeric value of " + value + " that you passed in with " + nodeName + ".");
                            }
                        } else {
                            row.addValue(analysisItem.createAggregateKey(), value);
                        }
                    }
                }
            }

            dataStorage = DataStorage.writeConnection(dataSource, conn, SecurityUtil.getAccountID());
            dataStorage.insertData(dataSet);
            dataStorage.commit();
            return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }
    }
}
