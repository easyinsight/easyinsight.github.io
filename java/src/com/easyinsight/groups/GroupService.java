package com.easyinsight.groups;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.audit.AuditMessage;

import java.util.List;
import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:18:11 AM
 */
public class GroupService {

    private GroupStorage groupStorage = new GroupStorage();

    public long addGroup(Group group) {
        long userID = SecurityUtil.getUserID();
        try {
            return groupStorage.addGroup(group, userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long addGroupComment(GroupComment groupComment) {
        try {
            groupComment.setUserID(SecurityUtil.getUserID());
            return groupStorage.addGroupComment(groupComment);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<AuditMessage> getGroupMessages(long groupID) {
        try {
            return groupStorage.getGroupMessages(groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupUser> getUsers(long groupID) {
        try {
            return groupStorage.getUsersForGroup(groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateGroup(Group group, List<GroupUser> users) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            groupStorage.updateGroup(group, conn);
            groupStorage.addGroupAudit(new GroupAuditMessage(SecurityUtil.getUserID(), new Date(), "Group updated", group.getGroupID()), conn);
            List<GroupUser> existingUsers = getUsers(group.getGroupID());
            existingUsers.removeAll(users);
            for (GroupUser user : existingUsers) {
                groupStorage.removeUserFromGroup(user.getUserID(), group.getGroupID(), conn);
            }
            for (GroupUser user : users) {
                groupStorage.addUserToGroup(user.getUserID(), group.getGroupID(), user.getRole(), conn);
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
            Database.instance().closeConnection(conn);
        }
    }

    public Group getGroup(long groupID) {
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
    
    public void inviteNewUserToGroup(String emailAddress, long groupID) {
        try {
            String activationID = groupStorage.inviteNewUserToGroup(groupID);
            
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addFeedToGroup(long feedID, long groupID) {
        try {
            groupStorage.addFeedToGroup(feedID, groupID, Roles.SUBSCRIBER);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addInsightToGroup(long insightID, long groupID) {
        try {
            groupStorage.addInsightToGroup(insightID, groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupChange> getChanges(long groupID) {
        throw new UnsupportedOperationException();
    }

    public List<WSAnalysisDefinition> getInsights(long groupID) {
        try {
            return groupStorage.getInsights(groupID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDescriptor> getFeeds(long groupID) {
        long userID = SecurityUtil.getUserID();
        try {
            return groupStorage.getFeeds(groupID, userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupDescriptor> getMemberGroupsNotIncludingFeed(long feedID) {
        long userID = SecurityUtil.getUserID();
        try {
            return groupStorage.getGroupsForUserExcludingFeed(userID, feedID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<GroupDescriptor> getMemberGroupsNotIncludingInsight(long insightID) {
        long userID = SecurityUtil.getUserID();
        try {
            return groupStorage.getGroupsForUserExcludingInsight(userID, insightID);
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
}
