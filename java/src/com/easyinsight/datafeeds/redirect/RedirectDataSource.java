package com.easyinsight.datafeeds.redirect;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.users.User;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.userupload.UploadPolicy;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Aug 17, 2010
 * Time: 10:19:55 AM
 */
public class RedirectDataSource extends FeedDefinition implements IServerDataSourceDefinition {
    private long redirectDataSourceID;

    public long getRedirectDataSourceID() {
        return redirectDataSourceID;
    }

    public void setRedirectDataSourceID(long redirectDataSourceID) {
        this.redirectDataSourceID = redirectDataSourceID;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.REDIRECT;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PERSONAL;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    public long create(EIConnection conn, List<AnalysisItem> externalAnalysisItems) throws Exception {
        setOwnerName(retrieveUser(conn, SecurityUtil.getUserID()).getUserName());
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
        setUploadPolicy(uploadPolicy);
        FeedCreationResult feedCreationResult = new FeedCreation().createFeed(this, conn, new DataSet(), uploadPolicy);
        return feedCreationResult.getFeedID();
    }

    public static User retrieveUser(Connection conn, long userID) {
        try {
            User user = null;
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new RedirectFeed(redirectDataSourceID);        
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM REDIRECT_DATA_SOURCE WHERE data_source_id = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO REDIRECT_DATA_SOURCE (data_source_id, delegate_data_source_id) VALUES (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setLong(2, redirectDataSourceID);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DELEGATE_DATA_SOURCE_ID FROM REDIRECT_DATA_SOURCE WHERE " +
                "data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        redirectDataSourceID = rs.getLong(1);
    }

    public CredentialsResponse refreshData(long accountID, Date now, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime) {
        throw new UnsupportedOperationException();
    }

    public boolean refreshData(long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime) throws Exception {
        throw new UnsupportedOperationException();
    }

    public CredentialsResponse refreshData(long accountID, Date now, FeedDefinition parentDefinition, EIConnection conn, String callDataID, Date lastRefreshTime) {
        throw new UnsupportedOperationException();
    }
}
