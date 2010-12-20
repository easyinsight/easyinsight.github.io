package com.easyinsight.datafeeds.twitter;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;

import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.ws.security.util.XmlSchemaDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Dec 10, 2009
 * Time: 11:37:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class TwitterFeed extends Feed {

    private static XPathContext namespaces;

    private DataSet dataSet;

    static {
        namespaces = new XPathContext();
        namespaces.addNamespace("atom", "http://www.w3.org/2005/Atom");
        namespaces.addNamespace("twitter", "http://api.twitter.com/");
    }

    public TwitterFeed(ArrayList<String> searches) {
        this.searches = searches;
    }

    private ArrayList<String> searches;


    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        if(dataSet == null)
            return null;
        if(Arrays.asList(TwitterDataSource.PUBLISHED, TwitterDataSource.UPDATED).contains(analysisItem.getKey().toKeyString())) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -13);
            AnalysisDateDimensionResultMetadata dateMeta = new AnalysisDateDimensionResultMetadata();
            dateMeta.setEarliestDate(cal.getTime());
            dateMeta.setLatestDate(new Date());
            return dateMeta;
        }
        else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
            for(IRow row : dataSet.getRows()) {
                metadata.addValue(analysisItem, row.getValue(analysisItem.getKey()), insightRequestMetadata);
            }
            return metadata;
        }
        return null;
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            DateFormat df = new XmlSchemaDateFormat();
            DataSet dataSet = new DataSet();
            for(String search:searches) {
                Builder b = new Builder();
                Document doc = runRestRequest("/search.atom?q=" + URLEncoder.encode(search, "UTF-8"), new HttpClient(), b, "http://search.twitter.com", null);
                Nodes statuses = doc.query("atom:feed/atom:entry", namespaces);

                for(int i = 0;i < statuses.size();i++) {
                    IRow row = dataSet.createRow();
                    Node n = statuses.get(i);
                    for(AnalysisItem item : allAnalysisItems) {
                        Key k = item.getKey();
                        if(k.toKeyString().equals(TwitterDataSource.AUTHOR_NAME))
                            row.addValue(k, queryField(n, "atom:author/atom:name/text()"));
                        else if(k.toKeyString().equals(TwitterDataSource.AUTHOR_URL))
                            row.addValue(k, queryField(n, "atom:author/atom:uri/text()"));
                        else if(k.toKeyString().equals(TwitterDataSource.CONTENT))
                            row.addValue(k, queryField(n, "atom:content/text()"));
                        else if(k.toKeyString().equals(TwitterDataSource.COUNT))
                            row.addValue(k, 1.0);
                        else if(k.toKeyString().equals(TwitterDataSource.IMAGE_LOCATION))
                            row.addValue(k, queryField(n, "atom:link[@rel='image']/@href"));
                        else if(k.toKeyString().equals(TwitterDataSource.LANGUAGE))
                            row.addValue(k, queryField(n, "twitter:lang/text()"));
                        else if(k.toKeyString().equals(TwitterDataSource.PUBLISHED))
                            row.addValue(k, queryDateField(n, "atom:published/text()", df));
                        else if(k.toKeyString().equals(TwitterDataSource.SOURCE))
                            row.addValue(k, queryField(n, "twitter:source/text()"));
                        else if(k.toKeyString().equals(TwitterDataSource.STATUS_LINK))
                            row.addValue(k, queryField(n, "atom:link[@rel='alternate']/@href"));
                        else if(k.toKeyString().equals(TwitterDataSource.TITLE))
                            row.addValue(k, queryField(n, "atom:title/text()"));
                        else if(k.toKeyString().equals(TwitterDataSource.TWEET_ID))
                            row.addValue(k, queryField(n, "atom:id/text()"));
                        else if(k.toKeyString().equals(TwitterDataSource.UPDATED))
                            row.addValue(k, queryDateField(n, "atom:updated/text()", df));
                    }
                }
            }
            this.dataSet = dataSet;
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ArrayList<String> getSearches() {
        return searches;
    }

    public void setSearches(ArrayList<String> searches) {
        this.searches = searches;
    }

    private Document runRestRequest(String path, HttpClient client, Builder builder, String url, EIPageInfo info) {
        HttpMethod restMethod = new GetMethod(url + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;
        try {
            client.executeMethod(restMethod);
            doc = builder.build(restMethod.getResponseBodyAsStream());
        }
        catch (nu.xom.ParsingException e) {
                throw new RuntimeException("Invalid username/password.");
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    private static Date queryDateField(Node n, String xpath, DateFormat df) throws ParseException {
        String val = queryField(n, xpath);
        Date date = null;
        if(val != null )
            return df.parse(val);
        else
            return null;
    }

    private static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath, namespaces);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    private class EIPageInfo {
        public int MaxPages;
        public int currentPage;
    }
}
