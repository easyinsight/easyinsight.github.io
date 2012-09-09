package com.easyinsight.api.v3;


import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class DefineDataSourceServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        // "<defineDataSource><dataSourceName></dataSourceName><fields><field type=\"\"><key></key><name></name></field></fields></defineDataSource>";

        DataStorage dataStorage = null;
        try {
            Nodes dataSourceNameNodes = document.query("/defineDataSource/dataSourceName/text()");
            String dataSourceName;
            if (dataSourceNameNodes.size() == 0) {
                return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>You need to specify a data source name.</message>");
            }

            dataSourceName = dataSourceNameNodes.get(0).getValue();

            Nodes refreshKeyNodes = document.query("/defineDataSource/refreshKey/text()");
            String refreshKey = null;
            if(refreshKeyNodes.size() > 0) {
                refreshKey = refreshKeyNodes.get(0).getValue();
            }

            Nodes refreshUrlNodes = document.query("/defineDataSource/refreshUrl/text()");
            String refreshUrl = null;
            if(refreshUrlNodes.size() > 0) {
                refreshUrl = refreshUrlNodes.get(0).getValue();
            }

            if((refreshKey == null) != (refreshUrl == null)) {
                return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>You must enter both refreshKey and refreshUrl if you enter either of them.</message>");
            }

            Nodes fields = document.query("/defineDataSource/fields/field");
            List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
            for (int i = 0; i < fields.size(); i++) {
                Element fieldNode = (Element) fields.get(i);
                Attribute type = fieldNode.getAttribute("dataType");
                if (type == null) {
                    return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>One of your fields was missing a dataType attribute.</message>");
                }

                Nodes keyNodes = fieldNode.query("key/text()");
                String key;
                if (keyNodes.size() == 0) {
                    return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>One of your fields was missing a key node.</message>");
                }
                key = keyNodes.get(0).getValue();
                Nodes nameNodes = fieldNode.query("name/text()");
                String displayName;
                if (nameNodes.size() == 0) {
                    displayName = key;
                } else {
                    displayName = nameNodes.get(0).getValue();
                }
                AnalysisItem analysisItem;
                String typeString = type.getValue().toLowerCase().trim();
                if ("grouping".equals(typeString)) {
                    analysisItem = new AnalysisDimension(new NamedKey(key), displayName);
                } else if ("measure".equals(typeString)) {
                    analysisItem = new AnalysisMeasure(new NamedKey(key), displayName, AggregationTypes.SUM);
                } else if ("tags".equals(typeString)) {
                    analysisItem = new AnalysisList(new NamedKey(key), displayName, true, ",");
                } else if ("postal".equals(typeString)) {
                    analysisItem = new AnalysisZipCode(new NamedKey(key), displayName);
                } else if ("longitude".equals(typeString)) {
                    analysisItem = new AnalysisLongitude(new NamedKey(key), true, displayName);
                } else if ("latitude".equals(typeString)) {
                    analysisItem = new AnalysisLatitude(new NamedKey(key), true, displayName);
                } else if ("date".equals(typeString)) {
                    analysisItem = new AnalysisDateDimension(new NamedKey(key), displayName, AnalysisDateDimension.DAY_LEVEL);
                } else {
                    return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>Unrecognized dataType of " + typeString + " passed in with field " + key + ". Valid options are grouping, measure, tags, postal, longitude, latitude, and date.</message>");
                }
                analysisItems.add(analysisItem);
            }
            CallData callData = convertData(dataSourceName, analysisItems, conn, true, refreshKey, refreshUrl);
            dataStorage = callData.dataStorage;
            dataStorage.commit();
            return new ResponseInfo(ResponseInfo.ALL_GOOD, "<dataSourceKey>" + callData.apiKey + "</dataSourceKey>");
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }
    }
}
