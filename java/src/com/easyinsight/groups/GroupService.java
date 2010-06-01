package com.easyinsight.groups;

import com.easyinsight.analysis.AnalysisStorage;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.goals.GoalTreeNode;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.audit.AuditMessage;
import com.easyinsight.users.Account;
import com.easyinsight.goals.GoalTreeDescriptor;
import com.easyinsight.goals.GoalStorage;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.userupload.MyDataTree;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:18:11 AM
 */
public class GroupService {

    private GroupStorage groupStorage = new GroupStorage();

    public boolean canCreateGroup() {
        EIConnection conn = Database.instance().getConnection();
        try {
            if (SecurityUtil.getAccountTier() == Account.BASIC) {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP_ID FROM COMMUNITY_GROUP, GROUP_TO_USER_JOIN, USER " +
                        "WHERE USER.ACCOUNT_ID = ? AND USER.USER_ID = GROUP_TO_USER_JOIN.USER_ID AND " +
                        "GROUP_TO_USER_JOIN.group_id = COMMUNITY_GROUP.COMMUNITY_GROUP_ID");
                queryStmt.setLong(1, SecurityUtil.getAccountID());
                ResultSet rs = queryStmt.executeQuery();
                return !rs.next();
            } else if (SecurityUtil.getAccountTier() >= Account.PLUS && SecurityUtil.isAccountAdmin()) {
                return true;
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return false;
    }

    public GroupResponse openGroupIfPossible(String groupKey) {
        GroupResponse groupResponse;
        try {
            try {
                long groupID = SecurityUtil.authorizeGroupByKey(groupKey, Roles.SUBSCRIBER);
                groupResponse = new GroupResponse(GroupResponse.SUCCESS, groupID);
            } catch (com.easyinsight.security.SecurityException e) {
                if (e.getReason() == SecurityException.LOGIN_REQUIRED)
                    groupResponse = new GroupResponse(GroupResponse.NEED_LOGIN, 0);
                else
                    groupResponse = new GroupResponse(GroupResponse.REJECTED, 0);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return groupResponse;
    }

    public Group addGroup(Group group) {
        SecurityUtil.authorizeAccountTier(Account.BASIC);
        long userID = SecurityUtil.getUserID();
        try {
            return groupStorage.addGroup(group, userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long addGroupComment(GroupComment groupComment) {
        SecurityUtil.authorizeGroup(groupComment.getGroupID(), Roles.SUBSCRIBER);
        try {
            groupComment.setUserID(SecurityUtil.getUserID());
            return groupStorage.addGroupComment(groupComment);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<AuditMessage> getGroupMessagesForUser(Date startDate, Date endDate) {
        List<AuditMessage> messages = new ArrayList<AuditMessage>();
        try {
            List<GroupDescriptor> groups = getMemberGroups();
            for (GroupDescriptor groupDescriptor : groups) {
                messages.addAll(getGroupMessages(groupDescriptor.getGroupID(), startDate, endDate));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return messages;        
    }

    public List<AuditMessage> getGroupMessages(long groupID, Date startDate, Date endDate) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        try {
            if (startDate == null) {
                startDate = new Date(0);
            }
            if (endDate == null) {
                endDate = new Date();
            }
            return groupStorage.getGroupMessages(groupID, startDate, endDate, 0);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupUser> getUsers(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        try {
            return groupStorage.getUsersForGroup(groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateGroup(Group group) {
        SecurityUtil.authorizeGroup(group.getGroupID(), Roles.OWNER);
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            groupStorage.updateGroup(group, conn);
            groupStorage.addGroupAudit(new GroupAuditMessage(SecurityUtil.getUserID(), new Date(), "Group updated", group.getGroupID(), null), conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.closeConnection(conn);
        }
    }

    public void updateGroupUsers(long groupID, List<GroupUser> users) {
        SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<GroupUser> existingUsers = getUsers(groupID);
            existingUsers.removeAll(users);
            for (GroupUser user : existingUsers) {
                groupStorage.removeUserFromGroup(user.getUserID(), groupID, conn);
            }
            for (GroupUser user : users) {
                groupStorage.addUserToGroup(user.getUserID(), groupID, user.getRole(), conn);
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.closeConnection(conn);
        }
    }

    public Group getGroup(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        try {
            Group group = groupStorage.getGroup(groupID);
            List<GroupUser> users = groupStorage.getUsersForGroup(groupID);
            group.setGroupUsers(users);
            return group;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupDescriptor> getPublicGroups() {
        try {
            return groupStorage.getAllPublicGroups();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    /*public void addUserToGroup(long userID, long groupID, int userRole) {
        try {
            groupStorage.addUserToGroup(userID, groupID, userRole);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void removeUserFromGroup(long userID, long groupID) {
        try {
            groupStorage.removeUserFromGroup(userID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    } */

    public void addMemberToGroup(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        long userID = SecurityUtil.getUserID();
        try {
            Group group = getGroup(groupID);
            int role;
            if (group.isPubliclyVisible()) {
                role = Roles.SUBSCRIBER;
            } else if (group.isPubliclyJoinable()) {
                role = Roles.SHARER;
            } else {
                throw new RuntimeException();
            }
            groupStorage.addUserToGroup(userID, groupID, role);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDescriptor> getGroupDataSources(long groupID) {
        try {
            return new FeedStorage().getDataSourcesForGroup(groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public MyDataTree getFeedAnalysisTree(long groupID) {
        try {
            FeedStorage feedStorage = new FeedStorage();
            List<Object> objects = new ArrayList<Object>();

            Map<Long, FeedDescriptor> descriptorMap = new HashMap<Long, FeedDescriptor>();

            List<FeedDescriptor> groupDataSources = feedStorage.getDataSourcesForGroup(groupID);
            for (FeedDescriptor groupDescriptor : groupDataSources) {
                if (!descriptorMap.containsKey(groupDescriptor.getDataFeedID())) {
                    descriptorMap.put(groupDescriptor.getDataFeedID(), groupDescriptor);
                }
            }

            objects.addAll(descriptorMap.values());
            AnalysisStorage analysisStorage = new AnalysisStorage();
            Map<Long, Set<EIDescriptor>> analysisDefinitions = new HashMap<Long, Set<EIDescriptor>>();



            for (InsightDescriptor analysisDefinition : analysisStorage.getReportsForGroup(groupID)) {
                Set<EIDescriptor> defList = analysisDefinitions.get(analysisDefinition.getDataFeedID());
                if (defList == null) {
                    defList = new HashSet<EIDescriptor>();
                    analysisDefinitions.put(analysisDefinition.getDataFeedID(), defList);
                }
                defList.add(analysisDefinition);
            }

            for (FeedDescriptor feedDescriptor : descriptorMap.values()) {
                Set<EIDescriptor> analysisDefList = analysisDefinitions.remove(feedDescriptor.getDataFeedID());
                if (analysisDefList == null) {
                    analysisDefList = new HashSet<EIDescriptor>();
                }
                feedDescriptor.setChildren(new ArrayList<EIDescriptor>(analysisDefList));
            }
            for (Set<EIDescriptor> defList : analysisDefinitions.values()) {
                objects.addAll(defList);
            }
            return new MyDataTree(objects, true);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
    
    public void inviteNewUserToGroup(String emailAddress, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        try {
            String activationID = groupStorage.inviteNewUserToGroup(groupID);
            
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void removeDataSourceFromGroup(long dataSourceID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        try {
            groupStorage.removeDataSourceFromGroup(dataSourceID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void removeReportFromGroup(long reportID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        try {
            groupStorage.removeReportFromGroup(reportID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupDescriptor> getGroupsForDataSource(long dataSourceID) {
        try {
            return groupStorage.getGroupsForDataSource(dataSourceID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateGroupsForDataSource(long dataSourceID, List<GroupDescriptor> groups) {
        try {
            groupStorage.updateGroupsForDataSource(dataSourceID, groups);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public List<GroupDescriptor> getGroupsForReport(long reportID) {
        try {
            return groupStorage.getGroupsForReport(reportID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateGroupsForReport(long reportID, List<GroupDescriptor> groups) {
        try {
            groupStorage.updateGroupsForReport(reportID, groups);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public void removeGoalTreeFromGroup(long goalTreeID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        try {
            groupStorage.removeGoalTreeFromGroup(goalTreeID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void removeGoalFromGroup(long goalID, long groupID) {
         SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        try {
            groupStorage.removeGoalFromGroup(goalID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addFeedToGroup(long feedID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        SecurityUtil.authorizeFeed(feedID, Roles.SHARER);
        try {
            groupStorage.addFeedToGroup(feedID, groupID, Roles.OWNER);
            FeedRegistry.instance().flushCache(feedID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addInsightToGroup(long insightID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SHARER);
        SecurityUtil.authorizeInsight(insightID);
        try {
            groupStorage.addReportToGroup(insightID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addGoalTreeToGroup(long goalTreeID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SHARER);
        SecurityUtil.authorizeGoalTree(goalTreeID, Roles.SUBSCRIBER);
        try {
            groupStorage.addGoalTreeToGroup(goalTreeID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addGoalToGroup(long goalID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SHARER);
        SecurityUtil.authorizeGoal(goalID, Roles.SUBSCRIBER);
        try {
            groupStorage.addGoalToGroup(goalID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupChange> getChanges(long groupID) {
        throw new UnsupportedOperationException();
    }

    public List<InsightDescriptor> getInsights(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        try {
            return groupStorage.getInsights(groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDescriptor> getFeeds(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        long userID = SecurityUtil.getUserID();
        try {
            return groupStorage.getFeeds(groupID, userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GoalTreeDescriptor> getGoalTrees(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        try {
            return groupStorage.getGoalTrees(groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GoalTreeNode> getGoals(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
        try {
            Calendar cal = Calendar.getInstance();
            Date endDate = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, -7);
            Date startDate = cal.getTime();
            return new GoalStorage().getGoalsForGroup(groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupDescriptor> getMemberGroups() {
        long userID = SecurityUtil.getUserID();
        try {
            return groupStorage.getGroupsForUser(userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupDescriptor> getAccountGroups() {
        long accountID = SecurityUtil.getAccountID();
        try {
            return groupStorage.getGroupsForAccount(accountID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
