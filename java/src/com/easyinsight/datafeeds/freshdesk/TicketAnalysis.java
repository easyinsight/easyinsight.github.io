package com.easyinsight.datafeeds.freshdesk;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/4/14
 * Time: 1:59 PM
 */
public class TicketAnalysis {

    public static final int UNASSIGNED = 0;
    public static final int AGENT = 1;
    public static final int CUSTOMER = 2;
    public static final int SOLVED = 3;



    private int agentHandles;
    private int customerHandles;
    private int waitState;
    private long elapsedAgentTime;
    private long elapsedCustomerTime;
    private long elapsedInitialWaitTime;
    private ZonedDateTime creationTime;

    public TicketAnalysis(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public long getElapsedInitialWaitTime() {
        return elapsedInitialWaitTime;
    }

    public long getElapsedCustomerTime() {
        return elapsedCustomerTime;
    }

    public long getElapsedAgentTime() {
        return elapsedAgentTime;
    }

    public int getWaitState() {
        return waitState;
    }

    public int getCustomerHandles() {
        return customerHandles;
    }

    public int getAgentHandles() {
        return agentHandles;
    }

    private List<ResponsibilityChange> changes = new ArrayList<>();

    public void calculate() {
        int responsibleParty = UNASSIGNED;
        ZonedDateTime lastTime = null;
        for (ResponsibilityChange change : changes) {
            if (change.assignee == AGENT && responsibleParty != AGENT) {
                agentHandles++;
                if (responsibleParty == UNASSIGNED) {
                    elapsedInitialWaitTime += Duration.between(creationTime, change.time).toMillis();
                } else {
                    elapsedCustomerTime += Duration.between(lastTime, change.time).toMillis();
                }
            } else if (change.assignee == CUSTOMER && responsibleParty != CUSTOMER) {
                customerHandles++;
                if (responsibleParty == UNASSIGNED) {
                    elapsedInitialWaitTime += Duration.between(creationTime, change.time).toMillis();
                } else {
                    elapsedAgentTime += Duration.between(lastTime, change.time).toMillis();
                }
            } else if (change.assignee == SOLVED) {
                if (responsibleParty == CUSTOMER) {
                    elapsedCustomerTime += Duration.between(lastTime, change.time).toMillis();
                } else if (responsibleParty == AGENT) {
                    elapsedAgentTime += Duration.between(lastTime, change.time).toMillis();
                }
            }
            responsibleParty = change.assignee;
            lastTime = change.time;
            waitState = change.assignee;
        }
        if (waitState != SOLVED) {
            if (responsibleParty == AGENT) {
                elapsedAgentTime += Duration.between(lastTime, ZonedDateTime.now()).toMillis();
            } else if (responsibleParty == CUSTOMER) {
                elapsedCustomerTime += Duration.between(lastTime, ZonedDateTime.now()).toMillis();
            }
        }
    }

    private class ResponsibilityChange {
        private int assignee;
        private ZonedDateTime time;

        private ResponsibilityChange(int assignee, ZonedDateTime time) {
            this.assignee = assignee;
            this.time = time;
        }
    }

    public void addResponsibility(int assignee, Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        changes.add(new ResponsibilityChange(assignee, zdt));
    }

}
