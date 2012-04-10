package com.easyinsight.api.v3;


import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.DayWhere;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import nu.xom.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class UpdateRowsServlet extends APIServlet {

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
            Nodes whereRows = baseNode.query("wheres/where");
            if (whereRows.size() == 0) {
                throw new ServiceRuntimeException("You need to specify at least one where node with the update call.");
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
            Nodes rowNodes = document.query("/rows/row");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DataSet dataSet = new DataSet();
            for (int i = 0; i < rowNodes.size(); i++) {
                Node rowNode = rowNodes.get(i);
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
            }
            List<IWhere> wheres = new ArrayList<IWhere>();
            for (int i = 0; i < whereRows.size(); i++) {
                Node node = whereRows.get(i);
                if (node instanceof Element) {
                    Element whereNode = (Element) node;
                    Nodes keyNodes = whereNode.query("key/text()");
                    if (keyNodes.size() == 0) {
                        throw new ServiceRuntimeException("You need to specify a key node for each where in your update call.");
                    }
                    Node keyNode = keyNodes.get(0);
                    String key = keyNode.getValue().toLowerCase();
                    AnalysisItem analysisItem = fieldMap.get(key);
                    if (analysisItem == null) {
                        throw new ServiceRuntimeException("We couldn't find " + key + " in the data source.");
                    }
                    Attribute typeAttribute = whereNode.getAttribute("whereType");
                    if (typeAttribute == null) {
                        throw new ServiceRuntimeException("You need to specify a whereType attribute for each where in your update call.");
                    }
                    String whereType = typeAttribute.getValue().toLowerCase().trim();
                    IWhere where;
                    if ("day".equals(whereType)) {
                        Nodes yearNodes = whereNode.query("year/text()");
                        if (yearNodes.size() == 0) {
                            throw new ServiceRuntimeException("You need to specify a year node with a type of day for where clauses.");
                        }
                        int year;
                        try {
                            year = Integer.parseInt(yearNodes.get(0).getValue());
                        } catch (NumberFormatException e) {
                            throw new ServiceRuntimeException(yearNodes.get(0).getValue() + " in your where clause is an invalid value for a year.");
                        }
                        int dayOfYear;
                        Nodes dayNodes = whereNode.query("day/text()");
                        if (dayNodes.size() == 0) {
                            throw new ServiceRuntimeException("You need to specify a day node with a type of day for where clauses.");
                        }
                        try {
                            dayOfYear = Integer.parseInt(dayNodes.get(0).getValue());
                        } catch (NumberFormatException e) {
                            throw new ServiceRuntimeException(dayNodes.get(0).getValue() + " in your where clause is an invalid value for a day of the year.");
                        }
                        where = new DayWhere(analysisItem.getKey(), year, dayOfYear);
                    } else if ("string".equals(whereType)) {
                        Nodes valueNodes = whereNode.query("value/text()");
                        if (valueNodes.size() == 0) {
                            throw new ServiceRuntimeException("You must specify a value node with a type of string for where clauses.");
                        }
                        String value = valueNodes.get(0).getValue();
                        where = new StringWhere(analysisItem.getKey(), value);
                    } else {
                        throw new ServiceRuntimeException(whereType + " is an unrecognized where type. Valid options are day and string.");
                    }
                    wheres.add(where);
                }

            }

            System.out.println("Updating with a data set of size = " + dataSet.getRows().size());
            dataStorage = DataStorage.writeConnection(dataSource, conn, SecurityUtil.getAccountID());
            dataStorage.updateData(dataSet, wheres);
            dataStorage.commit();
            return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }
    }
}
