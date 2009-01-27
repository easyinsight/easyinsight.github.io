package com.easyinsight.collaboration;

import flex.messaging.MessageBroker;
import flex.messaging.util.UUIDUtils;
import flex.messaging.messages.AsyncMessage;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 29, 2008
 * Time: 2:10:47 PM
 */
public class CollaborationService {


    public void openCollaborationSession(long starterAccountID, String sessionName, String password) {
        CollaborationSession session = new CollaborationSession(sessionName);

        session.addOwner(starterAccountID);
        SessionManager.getInstance().addSession(session, password);
    }

    public boolean authenticateSession(String sessionName, String password) {
        String sessionPassword = SessionManager.getInstance().getPassword(sessionName);
        return sessionPassword == null || "".equals(sessionPassword) || sessionPassword.equals(password);
    }

    public void joinSession(long accountID, String sessionName) {
        SessionManager.getInstance().getSession(sessionName).addAttendee(accountID);
        /*MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
        String clientID = UUIDUtils.createUUID();
        AsyncMessage msg = new AsyncMessage();
        msg.setDestination();
        msg.setHeader("DSSubtopic", stock.getSymbol());
        msg.setClientId(clientID);
        msg.setMessageId(UUIDUtils.createUUID());
        msg.setTimestamp(System.currentTimeMillis());
        msg.setBody(stock);*/
    }

    public List<CollaborationSession> getCollaborationSessions(long accountID) {
        return SessionManager.getInstance().getSessions();
    }

    public void endCollaborationSession(String sessionName) {
        SessionManager.getInstance().deleteSession(sessionName);
    }
}
