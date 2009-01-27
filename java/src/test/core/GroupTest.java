package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.groups.GroupService;
import com.easyinsight.groups.Group;
import com.easyinsight.groups.GroupToUserBinding;
import com.easyinsight.analysis.TagCloud;
import com.easyinsight.analysis.Tag;
import com.easyinsight.users.InternalUserService;
import test.util.TestUtil;

import java.util.Arrays;
import java.util.List;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 11:20:10 AM
 */
public class GroupTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
        new DataRetrieval();
    }

    public void testGroupStorage() {
        /*long userID = TestUtil.getTestUser();
        GroupService groupService = new GroupService();
        Group group = new Group();
        group.setName("Test Group");
        group.setDescription("Test Description");
        group.setPubliclyJoinable(false);
        group.setPubliclyVisible(false);
        TagCloud tagCloud = new TagCloud();
        List<Tag> tagList = Arrays.asList(new Tag("Test"), new Tag("Group"));
        tagCloud.setTags(tagList);
        group.setTagCloud(tagCloud);
        GroupToUserBinding groupToUserBinding = new GroupToUserBinding();
        groupToUserBinding.setUser(new InternalUserService().retrieveUser(userID));
        groupToUserBinding.setGroup(group);
        List<GroupToUserBinding> userList = Arrays.asList( groupToUserBinding );
        group.setUsers(userList);
        List<Long> feeds = Arrays.asList(5L, 10L, 15L);
        group.setFeeds(feeds);
        long groupID = groupService.addGroup(group);
        Group retrievedGroup = groupService.getGroup(groupID);
        assertEquals(group.getName(), retrievedGroup.getName());
        assertEquals(group.getDescription(), retrievedGroup.getDescription());
        assertEquals(group.isPubliclyJoinable(), retrievedGroup.isPubliclyJoinable());
        assertEquals(group.isPubliclyVisible(), retrievedGroup.isPubliclyVisible());
        assertNotNull(retrievedGroup.getTagCloud());
        assertNotNull(retrievedGroup.getTagCloud().getTags());
        assertEquals(tagList, retrievedGroup.getTagCloud().getTags());
        assertEquals(userList.size(), retrievedGroup.getUsers().size());
        assertEquals(feeds, retrievedGroup.getFeeds());*/
    }
}
