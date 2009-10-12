package com.easyinsight.eventing;

import org.hibernate.Session;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.outboundnotifications.ConfigureDataFeedTodo;
import com.easyinsight.outboundnotifications.ConfigureDataFeedInfo;
import com.easyinsight.outboundnotifications.TodoEventInfo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 4, 2009
 * Time: 4:04:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoCompletedListener extends EIEventListener {
    public void execute(EIEvent e) {
        TodoCompletedEvent event = (TodoCompletedEvent) e;
        FeedDefinition f = event.getFeedDefinition();
        if(f.isConfigured()) {

            Session s = Database.instance().createSession();
            try {
                s.getTransaction().begin();

                List results = s.createQuery("from ConfigureDataFeedTodo where feedID = ?").setLong(0, f.getDataFeedID()).list();
                for(Object o:results) {
                    ConfigureDataFeedTodo todo = (ConfigureDataFeedTodo) o;
                    ConfigureDataFeedInfo info = new ConfigureDataFeedInfo();
                    info.setAction(TodoEventInfo.COMPLETE);
                    info.setFeedID(f.getDataFeedID());
                    info.setFeedName(f.getFeedName());
                    info.setTodoID(todo.getId());
                    info.setUserId(todo.getUserID());
                    MessageUtils.sendMessage("generalNotifications", todo);
                    
                    s.delete(todo);
                }
                s.getTransaction().commit();
            } catch(Exception ex) {
                s.getTransaction().rollback();
            }
            finally {
                s.close();
            }
        }
    }
}
