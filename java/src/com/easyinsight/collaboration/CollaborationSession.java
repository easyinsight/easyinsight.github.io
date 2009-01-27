package com.easyinsight.collaboration;

import com.easyinsight.users.User;
import com.easyinsight.users.InternalUserService;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 29, 2008
 * Time: 2:11:36 PM
 */
public class CollaborationSession {
    private String sessionName;
    private String host;
    private List<Participant> participants = new ArrayList<Participant>();

    public CollaborationSession() {
    }

    public CollaborationSession(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void addOwner(long accountID) {
        User user = new InternalUserService().retrieveUser(accountID);
        host = user.getName();
        participants.add(new Participant(accountID, ParticipantRole.OWNER, user.getName()));
    }

    public void addAttendee(long accountID) {
        User user = new InternalUserService().retrieveUser(accountID);
        participants.add(new Participant(accountID, ParticipantRole.VIEWER, user.getName()));
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
