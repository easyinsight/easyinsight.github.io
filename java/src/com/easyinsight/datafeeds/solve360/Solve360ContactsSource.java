package com.easyinsight.datafeeds.solve360;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.kashoo.KashooBaseSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:43 PM
 */
public class Solve360ContactsSource extends Solve360BaseSource {

    public static final String CONTACT_ID = "Contact ID";
    public static final String CONTACT_NAME = "Contact Name";

    public Solve360ContactsSource() {
        setFeedName("Contacts");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SOLVE360_CONTACTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CONTACT_ID, CONTACT_NAME);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_ID)));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_NAME)));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Solve360CompositeSource solve360CompositeSource = (Solve360CompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(solve360CompositeSource.getUserEmail(), solve360CompositeSource.getAuthKey());
        try {
            Document doc = runRestRequest("https://secure.solve360.com/contacts?layout=1", httpClient, new Builder(), solve360CompositeSource);
            System.out.println(doc.toXML());
            DataSet dataSet = new DataSet();
            Nodes responseNode = doc.query("/response");
            Node response = responseNode.get(0);
            for (int i = 0; i < response.getChildCount(); i++) {
                Node contactNode = response.getChild(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(CONTACT_ID), queryField(contactNode, "id/text()"));
                row.addValue(keys.get(CONTACT_NAME), queryField(contactNode, "name/text()"));
            }

            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
