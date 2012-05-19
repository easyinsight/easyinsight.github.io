package com.easyinsight.api.v3;


import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportCalculation;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.*;
import nu.xom.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class UpdateRowsPKServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        DataStorage dataStorage = null;
        try {
            Nodes baseNodes = document.query("/update");
            if (baseNodes.size() == 0) {
                throw new ServiceRuntimeException("The root element of the request needs to be update.");
            }
            Node baseNode = baseNodes.get(0);
            Nodes baseRows = baseNode.query("rows");
            if (baseRows.size() == 0) {
                throw new ServiceRuntimeException("You need to specify a rows node with the update call.");
            }
            Element rootNode = (Element) baseRows.get(0);
            Attribute dataSourceAttribute = rootNode.getAttribute("dataSourceName");
            if (dataSourceAttribute == null) {
                throw new ServiceRuntimeException("You need to specify a data source via the dataSourceName attribute on the root element of the call.");
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
            Nodes rowNodes = document.query("/update/rows/row");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DataSet dataSet = new DataSet();
            dataStorage = DataStorage.writeConnection(dataSource, conn, SecurityUtil.getAccountID());
            for (int i = 0; i < rowNodes.size(); i++) {
                System.out.println("Inspecting row...");
                Element rowNode = (Element) rowNodes.get(i);
                Long rowID = Long.parseLong(rowNode.getAttribute("rowID").getValue());
                IRow row = dataSet.createRow();
                for (int j = 0; j < rowNode.getChildCount(); j++) {
                    Node node = rowNode.getChild(j);
                    if (node instanceof Element) {
                        Element columnNode = (Element) node;
                        String nodeName = columnNode.getLocalName().toLowerCase();
                        AnalysisItem analysisItem = fieldMap.get(nodeName);
                        if (analysisItem == null) {
                            throw new ServiceRuntimeException("No field was found in the data source definition matching the key of " + nodeName + ".");
                        }
                        String value = columnNode.getValue().trim();
                        if ("".equals(value)) {
                            row.addValue(analysisItem.getKey(), new EmptyValue());
                        } else {
                            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                                try {
                                    row.addValue(analysisItem.getKey(), dateFormat.parse(value));
                                } catch (ParseException e) {
                                    throw new ServiceRuntimeException("We couldn't parse the date value of " + value + " that you passed in with " + nodeName + ". Date values should match the pattern of yyyy-MM-dd'T'HH:mm:ss.");
                                }
                            } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                                try {
                                    row.addValue(analysisItem.getKey(), Double.parseDouble(value));
                                } catch (NumberFormatException e) {
                                    throw new ServiceRuntimeException("We couldn't parse the numeric value of " + value + " that you passed in with " + nodeName + ".");
                                }
                            } else {
                                row.addValue(analysisItem.getKey(), value);
                            }
                        }
                    }
                }
                List<IDataTransform> transforms = new ArrayList<IDataTransform>();
                if (dataSource.getMarmotScript() != null && !"".equals(dataSource.getMarmotScript())) {
                    StringTokenizer toker = new StringTokenizer(dataSource.getMarmotScript(), "\r\n");
                    while (toker.hasMoreTokens()) {
                        String line = toker.nextToken();
                        transforms.addAll(new ReportCalculation(line).apply(dataSource));
                    }
                }
                dataStorage.updateRow(row, dataSource.getFields(), transforms, rowID);
            }
            dataStorage.commit();
            return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }
    }
}
