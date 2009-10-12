package com.easyinsight.eventing;

import com.easyinsight.logging.LogClass;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 4, 2009
 * Time: 11:27:36 AM
 */
public class EventDispatcher extends Thread {
    private boolean running;
    private LinkedBlockingQueue<EIEvent> queue = new LinkedBlockingQueue<EIEvent>();
    // TODO: thread safety on these
    private Map<String, List<EIEventListener>> listeners = new HashMap<String, List<EIEventListener>>();

    private static EventDispatcher _instance = new EventDispatcher();

    public static EventDispatcher instance() {
        return _instance;
    }

    @Override
    public void run() {
        running = true;
        try {
            while (running) {
                EIEvent e = queue.take();
                List<EIEventListener> listenerSet = listeners.get(e.getEventType());
                if (listenerSet != null) {
                    for (EIEventListener l : listenerSet)
                        try {
                            l.execute(e);
                        }
                        catch (Exception ex) {
                            LogClass.error(ex);
                        }
                }

            }
        } catch (InterruptedException e) {
            // interrupted!
        }
    }


    public void dispatch(EIEvent e) {
        queue.add(e);
    }

    public void registerListener(String s, EIEventListener l) {
        List<EIEventListener> listenerSet = listeners.get(s);
        if (listenerSet == null) {
            listenerSet = new LinkedList<EIEventListener>();
            listeners.put(s, listenerSet);
        }
        listenerSet.add(l);

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        this.interrupt();
    }
}
