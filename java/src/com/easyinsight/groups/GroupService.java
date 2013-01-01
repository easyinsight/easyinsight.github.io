package com.easyinsight.groups;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.audit.AuditMessage;
import com.easyinsight.users.Account;

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

    public void deleteGroups(List<Integer> groupIDs) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM COMMUNITY_GROUP WHERE COMMUNITY_GROUP_ID = ?");
            for (Integer groupID : groupIDs) {
                SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
                deleteStmt.setLong(1, groupID);
                deleteStmt.executeUpdate();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void deleteGroup(long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM COMMUNITY_GROUP WHERE COMMUNITY_GROUP_ID = ?");
            deleteStmt.setLong(1, groupID);
            deleteStmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
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

    public List<DataSourceDescriptor> getGroupDataSources(long groupID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            return new FeedStorage().getDataSourcesForGroup(SecurityUtil.getUserID(), groupID, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
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

    public void removeDashboardFromGroup(long dashboardID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        try {
            groupStorage.removeDashboardFromGroup(dashboardID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void removeScorecardFromGroup(long scorecardID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.OWNER);
        try {
            groupStorage.removeScorecardFromGroup(scorecardID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
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

    public void addDashboardToGroup(long dashboardID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SHARER);
        SecurityUtil.authorizeDashboard(dashboardID);
        try {
            groupStorage.addDashboardToGroup(dashboardID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addScorecardToGroup(long scorecardID, long groupID) {
        SecurityUtil.authorizeGroup(groupID, Roles.SHARER);
        SecurityUtil.authorizeScorecard(scorecardID);
        try {
            groupStorage.addScorecardToGroup(scorecardID, groupID);
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
