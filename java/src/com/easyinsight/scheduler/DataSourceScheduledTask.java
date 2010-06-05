package com.easyinsight.scheduler;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMutex;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.email.UserStub;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Date;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 5:35:53 PM
 */
@Entity
@Table(name="data_source_query")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class DataSourceScheduledTask extends ScheduledTask {

    @Column(name="data_source_id")
    private long dataSourceID;
    private static FeedStorage feedStorage = new FeedStorage();

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    protected void execute(Date now, EIConnection conn) throws Exception {
        try {
            IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedStorage.getFeedDefinitionData(dataSourceID);
            UserStub dataSourceUser = null;
            List<FeedConsumer> owners = dataSource.getUploadPolicy().getOwners();
            for (FeedConsumer owner : owners){
                if (owner.type() == FeedConsumer.USER) {
                    dataSourceUser = (UserStub) owner;
                }
            }
            if (dataSourceUser == null) {
                throw new RuntimeException("No user found for data source.");
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, USER_ID, USER.ACCOUNT_ID, ACCOUNT.ACCOUNT_TYPE, USER.account_admin FROM USER, ACCOUNT " +
                    "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
            queryStmt.setInt(1, Account.ACTIVE);
            queryStmt.setInt(2, Account.TRIAL);
            queryStmt.setLong(3, dataSourceUser.getUserID());
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String userName = rs.getString(1);
                long userID = rs.getLong(2);
                long accountID = rs.getLong(3);
                int accountType = rs.getInt(4);
                boolean accountAdmin = rs.getBoolean(5);
                SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin);
                if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {
                    try {
                        dataSource.refreshData(null, dataSourceUser.getAccountID(), now, conn, null);
                    } finally {
                        DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error("Data source " + dataSourceID + " had error " + e.getMessage() + " in trying to refresh data.");
            throw e;
        }
    }
}
