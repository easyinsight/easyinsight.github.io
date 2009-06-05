package com.easyinsight.eventing;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 4, 2009
 * Time: 3:00:17 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EIEventListener {
    public abstract void execute(EIEvent e);
}
