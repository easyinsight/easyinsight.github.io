package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.groups.Group;
import com.easyinsight.groups.GroupService;
import com.easyinsight.groups.GroupUser;
import com.easyinsight.analysis.Tag;
import com.easyinsight.security.Roles;

import java.util.Arrays;
import java.util.List;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 11:20:10 AM
 */
public class GroupTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testGroupStorage() {
        long userID = TestUtil.getIndividualTestUser();
        GroupService groupService = new GroupService();
        Group group = new Group();
        group.setName("Test Group");
        group.setDescription("Test Description");
        group.setPubliclyJoinable(false);
        group.setPubliclyVisible(false);
        List<Tag> tagList = Arrays.asList(new Tag("Test"), new Tag("Group"));
        group.setTags(tagList);
        GroupUser groupUser = new GroupUser(userID, null, null, null, Roles.OWNER, null);
        group.setGroupUsers(Arrays.asList(groupUser));
        Group retrieved = groupService.addGroup(group);
        long groupID = retrieved.getGroupID();
        /*groupService.addFeedToGroup(0, groupID);
        groupService.addGoalToGroup(0, groupID);
        groupService.addGoalTreeToGroup(0, groupID);
        groupService.addGroupComment(null);
        groupService.addMemberToGroup(groupID);
        groupService.addReportToGroup(0, groupID);*/
        groupService.getFeeds(groupID);
        groupService.getGoals(groupID);
        groupService.getGoalTrees(groupID);
        groupService.getGroupMessages(groupID, null, null);
        groupService.getInsights(groupID);
        groupService.getMemberGroups();
        groupService.getPublicGroups();
        groupService.getUsers(groupID);
        Group retrievedGroup = groupService.getGroup(groupID);
        assertEquals(group.getName(), retrievedGroup.getName());
        assertEquals(group.getDescription(), retrievedGroup.getDescription());
        assertEquals(group.isPubliclyJoinable(), retrievedGroup.isPubliclyJoinable());
        assertEquals(group.isPubliclyVisible(), retrievedGroup.isPubliclyVisible());
    }
}
