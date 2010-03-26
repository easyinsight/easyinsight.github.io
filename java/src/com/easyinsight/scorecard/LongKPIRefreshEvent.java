package com.easyinsight.scorecard;

import com.easyinsight.eventing.EIEvent;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Mar 22, 2010
 * Time: 7:42:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class LongKPIRefreshEvent extends EIEvent {
    public static final String LONG_KPI_REFRESH_EVENT = "longKPIRefreshEvent";
    private ScorecardRefreshEvent event;
    private Date sendDate;

    public LongKPIRefreshEvent() {
        super(LONG_KPI_REFRESH_EVENT);
    }

    public ScorecardRefreshEvent getEvent() {
        return event;
    }

    public void setEvent(ScorecardRefreshEvent event) {
        this.event = event;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}