package com.easyinsight.goals;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.solutions.Solution;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.util.RandomTextGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * User: James Boe
 * Date: Jul 2, 2009
 * Time: 2:55:01 PM
 */
public class InstallationSystem {

    private EIConnection conn;
    private FeedStorage feedStorage = new FeedStorage();

    public InstallationSystem(EIConnection conn) {
        this.conn = conn;
    }

    public FeedDefinition installConnection(Solution solution) throws Exception {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_TYPE FROM SOLUTION WHERE SOLUTION_ID = ?");
        queryStmt.setLong(1, solution.getSolutionID());

        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        int type = rs.getInt(1);
        return installConnection(type);
    }

    public FeedDefinition installConnection(int dataSourceType) throws Exception {
        FeedDefinition dataSource = new DataSourceTypeRegistry().createDataSource(FeedType.valueOf(dataSourceType));
        dataSource.setApiKey(RandomTextGenerator.generateText(12));
        dataSource.setDateCreated(new Date());
        dataSource.setDateUpdated(new Date());
        dataSource.setVisible(false);
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
        dataSource.setUploadPolicy(uploadPolicy);
        feedStorage.addFeedDefinitionData(dataSource, conn);
        return dataSource;
    }
}
