package com.easyinsight.collaboration;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 29, 2008
 * Time: 6:43:02 PM
 */
public class SessionManager {
    private static SessionManager sessionManager;
    private Map<String, CollaborationSession> sessionMap = new HashMap<String, CollaborationSession>();
    private Map<String, String> passwords = new HashMap<String, String>();

    public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public void addSession(CollaborationSession collaborationSession, String password) {
        sessionMap.put(collaborationSession.getSessionName(), collaborationSession);
        passwords.put(collaborationSession.getSessionName(), password);
    }

    public String getPassword(String sessionName) {
        return passwords.get(sessionName);
    }

    public List<CollaborationSession> getSessions() {
        return new ArrayList<CollaborationSession>(sessionMap.values());
    }

    public CollaborationSession getSession(String name) {
        return sessionMap.get(name);
    }

    public void deleteSession(String name) {
        sessionMap.remove(name);
        passwords.remove(name);
    }
}
