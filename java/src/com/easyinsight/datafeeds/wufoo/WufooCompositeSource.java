package com.easyinsight.datafeeds.wufoo;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.batchbook2.Batchbook2CustomFieldSource;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/16/12
 * Time: 4:22 PM
 */
public class WufooCompositeSource extends CompositeServerDataSource {

    private String ytUserName;
    private String ytPassword;

    private String wfApiKey;
    private String url;

    public WufooCompositeSource() {
        setFeedName("Wufoo");
    }

    public String getUrl() {
        if (url == null || "".equals(url)) {
            return url;
        }
        String wufooURL = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "https://") + url;
        if(wufooURL.endsWith("/")) {
            wufooURL = wufooURL.substring(0, wufooURL.length() - 1);
        }
        if(!wufooURL.endsWith(".wufoo.com"))
            wufooURL = wufooURL + ".wufoo.com";
        return wufooURL;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWfApiKey() {
        return wfApiKey;
    }

    public void setWfApiKey(String wfApiKey) {
        this.wfApiKey = wfApiKey;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> defaultChildren = super.childDataSources(conn);
        Map<String, WufooForm> forms = new WufooForms().getForms(this);
        for (CompositeFeedNode existing : getCompositeFeedNodes()) {
            if (existing.getDataSourceType() == FeedType.WUFOO_FORM.getType()) {
                FeedDefinition existingSource = new FeedStorage().getFeedDefinitionData(existing.getDataFeedID(), conn);
                WufooFormSource wufooSource = (WufooFormSource) existingSource;
                forms.remove(wufooSource.getFormID());
            }
        }
        for (WufooForm form : forms.values()) {
            WufooFormSource source = new WufooFormSource();
            source.setFeedName(form.getName());
            source.setFormID(form.getId());
            newDefinition(source, conn, "", getUploadPolicy());
            CompositeFeedNode node = new CompositeFeedNode();
            node.setDataFeedID(source.getDataFeedID());
            node.setDataSourceType(source.getFeedType().getType());
            getCompositeFeedNodes().add(node);
            defaultChildren.add(source);
        }
        return defaultChildren;
    }



    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM WUFOO_COMPOSITE_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO WUFOO_COMPOSITE_SOURCE (URL, API_KEY, DATA_SOURCE_ID) VALUES (?, ?, ?)");
        insertStmt.setString(1, url);
        insertStmt.setString(2, wfApiKey);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT URL, API_KEY FROM WUFOO_COMPOSITE_SOURCE WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            url = rs.getString(1);
            wfApiKey = rs.getString(2);
        }
        getStmt.close();
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.WUFOO_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }
}
