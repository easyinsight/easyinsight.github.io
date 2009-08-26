package com.easyinsight.solutions;

import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.goals.GoalUtil;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: Aug 25, 2009
 * Time: 5:24:30 PM
 */
public class SolutionVisitor {
    public Set<FeedType> getFeedTypes(Solution solution) throws SQLException {
        Set<FeedType> feedTypeSet = new HashSet<FeedType>();
        // get the data sources associated to solution
        // get the goal tree if there is one
        // get the data sources off that goal tree
        Set<Long> dataSourceIDs = new HashSet<Long>();
        if (solution.getGoalTreeID() > 0) {
            dataSourceIDs.addAll(GoalUtil.getDataSourceIDs(solution.getGoalTreeID(), false));
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement getSourcesStmt = conn.prepareStatement("SELECT FEED_ID FROM SOLUTION_TO_FEED WHERE SOLUTION_ID = ?");
            getSourcesStmt.setLong(1, solution.getSolutionID());
            ResultSet sourceRS = getSourcesStmt.executeQuery();
            while (sourceRS.next()) {
                long dataSourceID = sourceRS.getLong(1);
                dataSourceIDs.add(dataSourceID);
            }
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT DISTINCT FEED_TYPE FROM DATA_FEED WHERE DATA_FEED_ID IN (");
            Iterator<Long> dsIter = dataSourceIDs.iterator();
            while (dsIter.hasNext()) {
                dsIter.next();
                queryBuilder.append("?");
                if (dsIter.hasNext()) {
                    queryBuilder.append(",");
                }
            }
            queryBuilder.append(")");
            PreparedStatement queryStmt = conn.prepareStatement(queryBuilder.toString());
            dsIter = dataSourceIDs.iterator();
            int i = 1;
            while (dsIter.hasNext()) {
                long id = dsIter.next();
                queryStmt.setLong(i++, id);
            }
            ResultSet typeRS = queryStmt.executeQuery();
            while (typeRS.next()) {
                int type = typeRS.getInt(1);
                feedTypeSet.add(new FeedType(type));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return feedTypeSet;
    }
}
