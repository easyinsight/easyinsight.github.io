package com.easyinsight.scorecard;

import com.easyinsight.eventing.EIEventListener;
import com.easyinsight.eventing.EIEvent;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Mar 22, 2010
 * Time: 9:20:52 PM

 A very poor implementation.
 TODO: MAKE THIS NOT SUCK EVENTUALLY.
 */
public class LongKPIRefreshListener extends EIEventListener {
    private static LongKPIRefreshListener _instance = new LongKPIRefreshListener();
    private List<LongKPIRefreshEvent> eventList = Collections.synchronizedList(new LinkedList<LongKPIRefreshEvent>());

    private LongKPIRefreshListener() { }

    public static LongKPIRefreshListener instance() {
        return _instance;
    }

    public List<LongKPIRefreshEvent> getEventsForUser(long userID) {
        List<LongKPIRefreshEvent> events = new ArrayList<LongKPIRefreshEvent>();
        List<LongKPIRefreshEvent> removedEvents = new ArrayList<LongKPIRefreshEvent>();
        for(LongKPIRefreshEvent event : eventList) {
            if(event.getSendDate().getTime() - new Date().getTime() > (1000 * 60 * 5)) {
                removedEvents.add(event);
            }
            else if(event.getEvent().getUserId() == userID) {
                events.add(event);
                removedEvents.add(event);
            }
        }
        eventList.removeAll(removedEvents);
        return events;
    }

    public void execute(EIEvent e) {
        LongKPIRefreshEvent event = (LongKPIRefreshEvent) e;
        eventList.add(event);
    }
}
