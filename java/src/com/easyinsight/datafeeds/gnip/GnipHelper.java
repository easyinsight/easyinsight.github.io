package com.easyinsight.datafeeds.gnip;

import com.gnipcentral.client.Config;
import com.gnipcentral.client.GnipConnection;
import com.gnipcentral.client.GnipException;
import com.gnipcentral.client.resource.*;

import java.util.List;
import java.util.LinkedList;

import org.joda.time.DateTime;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 15, 2009
 * Time: 5:45:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class GnipHelper {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void createFilter(Publisher p, String filterName, List<Rule> rules) throws GnipException {
        Config c = new Config(username, password);
        Filter f = new Filter(filterName);
        f.setRules(rules);
        GnipConnection g = new GnipConnection(c);
        g.create(p, f);
    }
    
    public void createFilter(Publisher p, String filterName, RuleType type, String rule) throws GnipException {
        Rule r = new Rule(type, rule);
        List<Rule> rules = new LinkedList<Rule>();
        rules.add(r);
        createFilter(p, filterName, rules);
    }

    public Activities getActivities(Publisher p, String filterName, DateTime bucketId) throws GnipException {
        Config c = new Config(username, password);
        GnipConnection g = new GnipConnection(c);
        return g.getActivities(p, new Filter(filterName), bucketId);
    }

    
}
