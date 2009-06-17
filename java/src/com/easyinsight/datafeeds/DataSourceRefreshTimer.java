package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.email.UserStub;

import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Apr 10, 2009
 * Time: 9:24:31 AM
 */
public class DataSourceRefreshTimer extends TimerTask {
    public void run() {
        try {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            List<Long> dataSourceIDs = new ArrayList<Long>();
            Connection conn = Database.instance().getConnection();
            try {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID, REFRESH_INTERVAL FROM DATA_FEED WHERE REFRESH_INTERVAL > 0");
                ResultSet dataSources = queryStmt.executeQuery();
                while (dataSources.next()) {
                    long dataSourceID = dataSources.getLong(1);
                    long refreshInterval = dataSources.getLong(2);
                    if (refreshInterval < 24 && hour % refreshInterval == 0) {
                        dataSourceIDs.add(dataSourceID);                        
                    } else if (hour == 0) {
                        dataSourceIDs.add(dataSourceID);
                    }
                }
            } finally {
                Database.instance().closeConnection(conn);
            }
            for (Long dataSourceID : dataSourceIDs) {
                // TODO: add credentials to refresh
                IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) new FeedStorage().getFeedDefinitionData(dataSourceID);
                UserStub dataSourceUser = null;
                List<FeedConsumer> owners = dataSource.getUploadPolicy().getOwners();
                for (FeedConsumer owner : owners){
                    if (owner.type() == FeedConsumer.USER) {
                        dataSourceUser = (UserStub) owner;
                    }
                }
                dataSource.refreshData(null, dataSourceUser.getAccountID(), null, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
