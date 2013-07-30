package com.easyinsight.datafeeds.json;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 2/8/13
 * Time: 1:29 PM
 */
public class JSONFeed extends Feed {
    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {

        DataSet dataSet = new DataSet();
        try {
            JSONDataSource source = (JSONDataSource) new FeedStorage().getFeedDefinitionData(getFeedID(), conn);
            String userName = source.getUserName();
            int httpMethod = source.getHttpMethod();
            String jsonPath = source.getJsonPath();
            String password = source.getPassword();
            String url = source.getUrl();
            String nextPageString = source.getNextPageString();

            /*Map<String, List<Key>> keys = new HashMap<String, List<Key>>();
            for (AnalysisItem analysisItem : analysisItems) {
                List<Key> keyList = keys.get(analysisItem.getKey().toKeyString());
                if (keyList == null) {
                    keyList = new ArrayList<Key>();
                    keys.put(analysisItem.getKey().toKeyString(), keyList);
                }
                keyList.add(analysisItem.createAggregateKey());
            }*/

            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            HttpClient client = new HttpClient();
            if (userName != null || "".equals(userName)) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            int page = 1;
            boolean hasMorePages;
            do {
                HttpMethod restMethod;
                if (httpMethod == JSONDataSource.GET) {
                    if (page == 1) {
                        restMethod = new GetMethod(url);
                    } else {
                        restMethod = new GetMethod(nextPageString.replace("{n}", String.valueOf(page)));
                    }
                } else if (httpMethod == JSONDataSource.POST) {
                    if (page == 1) {
                        restMethod = new PostMethod(url);
                    } else {
                        restMethod = new PostMethod(nextPageString.replace("{n}", String.valueOf(page)));
                    }
                } else {
                    throw new RuntimeException("Unknown http method " + httpMethod);
                }

                restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
                client.executeMethod(restMethod);
                List<Map> coreArray = null;
                String jsonString = restMethod.getResponseBodyAsString();
                if (jsonPath == null || "".equals(jsonPath.trim())) {
                    Object obj = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(jsonString);
                    if (obj instanceof List) {
                        List list = (List) obj;
                        coreArray = new ArrayList<Map>();
                        for (Object o : list) {
                            if (o instanceof Map) {
                                coreArray.add((Map) o);
                            }
                        }
                    }
                } else {
                    coreArray = JsonPath.read(jsonString, jsonPath);
                }

                if (coreArray == null) {
                    if (page == 1) {
                        throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.", source));
                    } else {
                        break;
                    }
                } else if (coreArray.size() == 0) {
                    if (page == 1) {
                        throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.", source));
                    } else {
                        break;
                    }
                }
                int pageResults = 0;
                for (Map object : coreArray) {
                    IRow row = dataSet.createRow();
                    pageResults++;
                    for (AnalysisItem item : analysisItems) {
                        String keyName = item.getKey().toKeyString();
                        Object value = object.get(keyName);
                        if (value == null) {
                            row.addValue(item.createAggregateKey(), new EmptyValue());
                        } else if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            AnalysisDateDimension date = (AnalysisDateDimension) item;
                            DateFormat df = new SimpleDateFormat(date.getCustomDateFormat());
                            try {
                                row.addValue(item.createAggregateKey(), df.parse(value.toString()));
                            } catch (ParseException e) {
                                row.addValue(item.createAggregateKey(), new EmptyValue());
                            }
                        } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                            if (value instanceof Number) {
                                row.addValue(item.createAggregateKey(), (Number) value);
                            } else {
                                row.addValue(item.createAggregateKey(), Double.parseDouble(value.toString()));
                            }
                        } else {
                            row.addValue(item.createAggregateKey(), value.toString());
                        }
                    }
                }

                page++;
                if (page > 500) {
                    break;
                }
                hasMorePages = pageResults > 0 && nextPageString != null && !"".equals(nextPageString);
            } while (hasMorePages);
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
