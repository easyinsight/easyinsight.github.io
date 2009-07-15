package test.goals;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.goals.*;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UploadResponse;
import com.easyinsight.analysis.*;
import com.easyinsight.email.UserStub;
import test.util.TestUtil;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 3:53:33 PM
 */
public class GoalTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public static long createDefaultTestDataSource(long userID) {
        String csvText = "Revenue,When\n500,2009-01-25\n600,2009-01-26";
        UserUploadService userUploadService = new UserUploadService();
        long uploadID = userUploadService.addRawUploadData(userID, "blah.csv", csvText.getBytes());
        UploadResponse uploadResponse = userUploadService.create(uploadID, "Default Test Data Source");
        if (!uploadResponse.isSuccessful()) {
            throw new RuntimeException(uploadResponse.getFailureMessage());
        }
        return uploadResponse.getFeedID();
    }

    private AnalysisItem findAnalysisItem(long dataSourceID, String name) {
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
        for (AnalysisItem field : feedDefinition.getFields()) {
            if (field.getKey().toKeyString().equals(name)) {
                return field;
            }
        }
        throw new RuntimeException();
    }

    public void testGoalCreation() throws SQLException {
        long userID = TestUtil.getProUser();
        long dataSourceID = createDefaultTestDataSource(userID);
        GoalService goalService = new GoalService();
        GoalTree goalTree = new GoalTree();
        goalTree.setName("Goal Tree");
        GoalTreeNode rootNode = new GoalTreeNode();
        rootNode.setName("Root Node");
        GoalTreeNode dataNode = new GoalTreeNode();
        dataNode.setParent(rootNode);
        dataNode.setCoreFeedID(dataSourceID);
        dataNode.setAnalysisMeasure((AnalysisMeasure) findAnalysisItem(dataSourceID, "Revenue"));
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setField(findAnalysisItem(dataSourceID, "When"));
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.DAY);
        dataNode.setFilters(Arrays.asList((FilterDefinition) rollingFilterDefinition));
        dataNode.setGoalValue(1000);
        dataNode.setHighIsGood(true);
        dataNode.setName("Data Node");
        rootNode.setChildren(Arrays.asList(dataNode));
        goalTree.setRootNode(rootNode);
        UserStub user = new UserStub(userID, null, null, null);
        List<FeedConsumer> admins = new ArrayList<FeedConsumer>();
        admins.add(user);
        goalTree.setAdministrators(admins);
        GoalTree savedTree = goalService.createGoalTree(goalTree).getGoalTree();
        long goalTreeID = savedTree.getGoalTreeID();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 23);
        Date startDate = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 30);
        Date endDate = cal.getTime();
        GoalTreeMilestone milestone = new GoalTreeMilestone();
        milestone.setMilestoneName("Milestone");
        milestone.setMilestoneDate(cal.getTime());
        goalService.saveMilestone(milestone);
        dataNode.setMilestone(milestone);
        goalService.updateGoalTree(goalTree);
        GoalTree dataTree = goalService.createDataTree(goalTreeID, startDate, endDate);
        GoalTreeNodeData data = (GoalTreeNodeData) dataTree.getRootNode().getChildren().get(0);
        goalService.subscribeToGoal(data.getGoalTreeNodeID());
        List<GoalTreeNodeData> datas = goalService.getGoals();
        new GoalService().deleteGoalTree(goalTreeID);
        assertEquals(goalService.getGoals().size(), 0);        
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT * FROM GOAL_TREE_NODE WHERE GOAL_TREE_ID = ?");
            queryStmt.setLong(1, goalTreeID);
            ResultSet rs = queryStmt.executeQuery();
            assertFalse(rs.next());
        } finally {
            Database.instance().closeConnection(conn);
        }
    }
}
